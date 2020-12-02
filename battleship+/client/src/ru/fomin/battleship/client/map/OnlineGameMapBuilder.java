package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;

import java.util.Vector;

public class OnlineGameMapBuilder extends MapBuilder {
    private OnlineGameWindow onlineGameWindow;
    private String messageForLog;
    public OnlineGameMapBuilder(Cell[][] map, OnlineGameWindow onlineGameWindow) {
        this.map = map;
        this.onlineGameWindow = onlineGameWindow;
        count4Ship = MAX_OF_SHIP4;
        count3Ship = MAX_OF_SHIP3;
        count2Ship = MAX_OF_SHIP2;
        count1Ship = MAX_OF_SHIP1;
    }

    public void processDataOfOpponentTurn(String codeOfGameTurn) {
        String[] codeElementsArray = codeOfGameTurn.split(delimiter);
        int[] codeElementsIntegerArray = new int[codeElementsArray.length - 1];
        for (int i = 0; i < codeElementsArray.length - 1; i++)
            codeElementsIntegerArray[i] = Integer.parseInt(codeElementsArray[i]);
        if (codeElementsArray[codeElementsArray.length - 1].equals("0"))
            onlineGameWindow.sendCodeResultOfGameTurn(processShootingDataOfOpponentTurn(codeElementsIntegerArray));
        else onlineGameWindow.sendCodeResultOfGameTurn(processExplorationDataOfOpponentTurn(codeElementsIntegerArray));
    }


    private String processShootingDataOfOpponentTurn(int[] codeElementsArray) {
        String codeOfTurnResult = determineStatusCellAfterShoot(codeElementsArray[0], codeElementsArray[1]);
        if (codeElementsArray.length == 2) return codeOfTurnResult;
        for (int i = 2; i < codeElementsArray.length; i += 2) {
            codeOfTurnResult += delimiter + determineStatusCellAfterShoot(codeElementsArray[i], codeElementsArray[i + 1]);
        }
        return codeOfTurnResult;
    }

    private String determineStatusCellAfterShoot(int x, int y) {
        String resultOfTurn = x + delimiter + y;
        if (map[x][y].getStatus() == 6) {
            map[x][y].setImage(2);
            Vector<Cell> cellsOfShip = getCellsOfShip(x, y, 6, 2);
            int countOfDamagedCells = 0;
            for (Cell cell : cellsOfShip) if (cell.getStatus() == 2) countOfDamagedCells++;
            if (countOfDamagedCells == cellsOfShip.size()) {
                resultOfTurn+=delimiter+3;
                for (Cell cell : cellsOfShip) {
                    cell.setImage(3);
                    int[] coordinates=cell.getCoordinates();
                    resultOfTurn+=delimiter+coordinates[0]+delimiter+coordinates[1]+delimiter+3;
                }
            }else{
                resultOfTurn += delimiter + 2;
            }
        } else {
            resultOfTurn += delimiter + map[x][y].getStatus();
        }
        return resultOfTurn;
    }

    private String processExplorationDataOfOpponentTurn(int[] codeElementsArray) {
        String codeOfTurnResult = codeElementsArray[0] + delimiter + codeElementsArray[1] + delimiter + map[codeElementsArray[0]][codeElementsArray[1]].getStatus();
        for (int i = 2; i < codeElementsArray.length; i += 2) {
            codeOfTurnResult += delimiter + codeElementsArray[i] + delimiter + codeElementsArray[i + 1] + delimiter + map[codeElementsArray[i]][codeElementsArray[i + 1]].getStatus();
        }
        return codeOfTurnResult;
    }

    public void processDataOfResultTurn(String codeOfResultTurn) {
        messageForLog="";
        boolean isDamageOrDestroy = false;
        int[] lastUsingCellForActionCoordinates=onlineGameWindow.getPastUsingCellForActionCoordinates();
        String[] codeElements=codeOfResultTurn.split(delimiter);
        int[] codeIntegerElements=new int[codeElements.length];
        for(int i =0;i<codeElements.length;i++){
            codeIntegerElements[i]=Integer.parseInt(codeElements[i]);
        }
        for(int i=0; i<codeIntegerElements.length;i+=3){
           if(lastUsingCellForActionCoordinates[0]==codeIntegerElements[i]&&lastUsingCellForActionCoordinates[1]==codeIntegerElements[i+1]&&(codeIntegerElements[i+2]==2||codeIntegerElements[i+2]==3))
            isDamageOrDestroy=true;
           map[codeIntegerElements[i]][codeIntegerElements[i+1]].setImage(codeIntegerElements[i+2]);
        }
        if(isDamageOrDestroy&&onlineGameWindow.getPastMode()==0){
            onlineGameWindow.changeTurn();
            onlineGameWindow.appendIntoLog(messageForLog, true);
        }else{
            onlineGameWindow.sendMessageAboutChangeTurn();
            onlineGameWindow.appendIntoLog(messageForLog, false);
        }
    }

}
