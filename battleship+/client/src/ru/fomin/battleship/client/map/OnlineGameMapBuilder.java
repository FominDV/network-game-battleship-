package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;

import java.util.Vector;

public class OnlineGameMapBuilder extends MapBuilder {
    private OnlineGameWindow onlineGameWindow;

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
        int ResultOfTurnForCell = determineStatusCellAfterShoot(codeElementsArray[0], codeElementsArray[1]);
        String codeOfTurnResult = codeElementsArray[0] + delimiter + codeElementsArray[1] + delimiter + ResultOfTurnForCell;
        if (codeElementsArray.length == 2) return codeOfTurnResult;
        for (int i = 2; i < codeElementsArray.length; i += 2) {
            ResultOfTurnForCell = determineStatusCellAfterShoot(codeElementsArray[i], codeElementsArray[i + 1]);
            codeOfTurnResult += delimiter + codeElementsArray[i] + delimiter + codeElementsArray[i + 1] + ResultOfTurnForCell;
        }
        return codeOfTurnResult;
    }

    private int determineStatusCellAfterShoot(int x, int y) {
        int ResultOfTurnForCell = -1;
        if (map[x][y].getStatus() == 6) {
           Vector<Cell> cellsOfShip=getCellsOfShip(x,y,6);

        } else {
            return 5;
        }
    }

    private String processExplorationDataOfOpponentTurn(int[] codeElementsArray) {
        String codeOfTurnResult = codeElementsArray[0] + delimiter + codeElementsArray[1] + delimiter + map[codeElementsArray[0]][codeElementsArray[1]].getStatus();
        for (int i = 2; i < codeElementsArray.length; i += 2) {
            codeOfTurnResult += delimiter + codeElementsArray[i] + delimiter + codeElementsArray[i + 1] + delimiter + map[codeElementsArray[i]][codeElementsArray[i + 1]].getStatus();
        }
        return codeOfTurnResult;
    }

    public void processDataOfResultTurn(String codeOfResultTurn) {
        boolean isDamageOrDestroy = false;

    }
}
