package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;

import java.util.Vector;

public class OnlineGameMapBuilder extends MapBuilder {
    private String delimiterAboutDestroyed = "d";
    private String delimiterCombo = delimiterAboutDestroyed + delimiter;
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
        if (codeElementsArray.length != 2)
            for (int i = 2; i < codeElementsArray.length; i += 2) {
                codeOfTurnResult += determineStatusCellAfterShoot(codeElementsArray[i], codeElementsArray[i + 1]);
            }
        return editCodeForSending(codeOfTurnResult);
    }

    private String determineStatusCellAfterShoot(int x, int y) {
        String resultOfTurn = delimiter + x + delimiter + y;
        if (map[x][y].getStatus() == 6) {
            map[x][y].setImage(2);
            Vector<Cell> cellsOfShip = getCellsOfShip(x, y, 6, 2);
            int countOfDamagedCells = 0;
            for (Cell cell : cellsOfShip) if (cell.getStatus() == 2) countOfDamagedCells++;
            if (countOfDamagedCells == cellsOfShip.size()) {
                increaseTurnsForRecharge(cellsOfShip.size());
                resultOfTurn = delimiterAboutDestroyed;
                for (int i = 0; i < cellsOfShip.size(); i++) {
                    cellsOfShip.get(i).setImage(3);
                    int[] coordinates = cellsOfShip.get(i).getCoordinates();
                    resultOfTurn += delimiter + coordinates[0] + delimiter + coordinates[1] + delimiter + 3;
                }
                resultOfTurn += delimiterAboutDestroyed;
            } else {
                resultOfTurn += delimiter + 2;
            }
        } else {
            if (map[x][y].getStatus() != 3 && map[x][y].getStatus() != 2)
                resultOfTurn += delimiter + map[x][y].getStatus();
            else resultOfTurn = "";
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
        messageForLog=onlineGameWindow.getMessageForLog();
        boolean isDamageOrDestroy = false;
        int[] lastUsingCellForActionCoordinates = onlineGameWindow.getPastUsingCellForActionCoordinates();

        String[] codeBlocks = codeOfResultTurn.split(delimiterCombo);
        String[][] codeElements = new String[codeBlocks.length][];
        for (int i = 0; i < codeBlocks.length; i++)
            codeElements[i] = codeBlocks[i].split(delimiter);
        //convert to integer
        int[][] codeIntegerElements = new int[codeElements.length][];
        for (int i = 0; i < codeElements.length; i++) {
            codeIntegerElements[i] = new int[codeElements[i].length];
            for (int j = 0; j < codeElements[i].length; j++) {
                codeIntegerElements[i][j] = Integer.parseInt(codeElements[i][j]);
            }
        }
        //set status for cells
        boolean isDecisionShipWasNotMade;
        for (int i = 0; i < codeIntegerElements.length; i++) {
            isDecisionShipWasNotMade = true;
            for (int j = 0; j < codeIntegerElements[i].length; j += 3) {
                //decision effect of shooting
                if (lastUsingCellForActionCoordinates[0] == codeIntegerElements[i][j] && lastUsingCellForActionCoordinates[1] == codeIntegerElements[i][j + 1] && (codeIntegerElements[i][j + 2] == 2 || codeIntegerElements[i][j + 2] == 3))
                    isDamageOrDestroy = true;
                map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].setImage(codeIntegerElements[i][j + 2]);
                map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].setNotActive();
                //decision "is it destroyed ship or something else"
                if (isDecisionShipWasNotMade && isDestroyedShip(codeIntegerElements[i])) {
                    messageForLog += createMessageAboutDestroyedShip(codeIntegerElements[i].length / 3);
                    //there must be method for images****************************************************************************
                    showCellsAroundShip(codeIntegerElements[i]);
                    isDecisionShipWasNotMade = false;
                }
            }
        }
        //decision of next game turn
        if (isDamageOrDestroy && onlineGameWindow.getPastMode() == 0) {
            onlineGameWindow.changeTurn();
            onlineGameWindow.appendIntoLog(messageForLog, true);
        } else {
            onlineGameWindow.sendMessageAboutChangeTurn();
            onlineGameWindow.appendIntoLog(messageForLog, false);
        }
    }

    private void showCellsAroundShip(int[] codeOfShip) {
        switch (directionOfShip(codeOfShip[0], codeOfShip[1], 3, 3)) {
            case 0:
                showCellsAroundOneDeckShip(codeOfShip[0], codeOfShip[1]);
                break;
            case 1:
                
                break;
            case -1:

                break;
        }
    }

    private void showCellsAroundOneDeckShip(int x, int y) {
        for (int i = 0; i < 2; i++) {
            if (x - i >= 0) {
                if (y - 1 >= 0) {
                    map[x - i][y - 1].setImage(5);
                    map[x - i][y - 1].setNotActive();
                }
                if (y + 1 < map.length) {
                    map[x - i][y + 1].setImage(5);
                    map[x - i][y + 1].setNotActive();
                }
            }
            if(y - i >= 0){
                if (x - 1 >= 0) {
                    map[x - 1][y - i].setImage(5);
                    map[x - 1][y - i].setNotActive();
                }
                if (x + 1 < map.length) {
                    map[x + 1][y - i].setImage(5);
                    map[x + 1][y - i].setNotActive();
                }
            }
        }
        if (x + 1 < map.length && y + 1 < map.length) {
            map[x + 1][y + 1].setImage(5);
            map[x + 1][y + 1].setNotActive();
        }
    }

    private String createMessageAboutDestroyedShip(int lengthOfShip) {
        switch (lengthOfShip) {
            case 4:
                return "*Four-deck ship was destroyed\n";
            case 3:
                return "*Three-deck ship was destroyed\n";
            case 2:
                return "*Two-deck ship was destroyed\n";
            case 1:
                return "*One-deck ship was destroyed\n";
            default:
                return "";
        }
    }

    private boolean isDestroyedShip(int[] codeOfCells) {
        for (int i = 0; i < codeOfCells.length; i += 3) {
            if (codeOfCells[i + 2] != 3) return false;
        }
        return true;
    }

    private void increaseTurnsForRecharge(int lengthOfShip) {
        switch (lengthOfShip) {
            case 4:
                count4Ship--;
                onlineGameWindow.blockTurnsForVolley();
                break;
            case 3:
                count3Ship--;
                if (count3Ship == 0) onlineGameWindow.blockTurnsForExploration();
                else onlineGameWindow.changeTurnsForExploration();
                break;
            case 2:
                count2Ship--;
                if (count2Ship == 0) onlineGameWindow.blockTurnsForShootingOnStraight();
                else onlineGameWindow.changeTurnsForShootingOnStraight();
                break;
            case 1:
                count1Ship--;
                onlineGameWindow.changeTurnsForAllShootingModes();
                break;
        }
    }

    private String editCodeForSending(String codeOfTurnResult) {
        StringBuffer stringBuffer = new StringBuffer(codeOfTurnResult);
        if (codeOfTurnResult.charAt(codeOfTurnResult.length() - 1) == delimiterAboutDestroyed.charAt(0))
            stringBuffer.deleteCharAt(codeOfTurnResult.length() - 1);
        stringBuffer.deleteCharAt(0);
        if (stringBuffer.charAt(0) == delimiter.charAt(0))
            stringBuffer.deleteCharAt(0);
        for (int i = 0; i < 3; i++) {
            int index = stringBuffer.indexOf(delimiterAboutDestroyed + delimiterAboutDestroyed);
            if (index != -1) stringBuffer.deleteCharAt(index);
        }
        codeOfTurnResult = String.valueOf(stringBuffer);
        return codeOfTurnResult;
    }

    public void createCodeCellsOfAction(int x, int y, int mode, int actionType) {
        messageForLog = "";
        String code = "";
        /*mode:
         *0-simple shoot
         * 1-volley shoot
         * 2-exploration of the map
         * 3-shooting on straight*/
        //create message for log about actioned cells and first part of code
        switch (mode) {
            case 0:
                code=x + delimiter + y;
                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
        //create and send code of cells
        onlineGameWindow.setMessageForLog(messageForLog);
        onlineGameWindow.sendCodeOfGameTurn(code+delimiter+actionType);
    }
}
