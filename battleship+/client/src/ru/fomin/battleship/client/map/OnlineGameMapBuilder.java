package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;

import java.util.Vector;

public class OnlineGameMapBuilder extends MapBuilder {
    private String delimiterAboutDestroyed = "d";
    private String delimiterCombo = delimiterAboutDestroyed + delimiter;
    private OnlineGameWindow onlineGameWindow;
    private String messageForLog;
    private final String TEXT_DAMAGE = "*Damage:";
    private final String TEXT_MISS = "*Miss:";
    private final int[] IMAGES_OF_DAMAGED_SHIP1 = {110};
    private final int[] IMAGES_OF_DAMAGED_SHIP3 = {3110, 3120, 3130, 3210, 3220, 3230};
    private final int[] IMAGES_OF_DAMAGED_SHIP2 = {2110, 2120, 2210, 2220};
    private final int[] IMAGES_OF_DAMAGED_SHIP4 = {4110, 4120, 4130, 4140, 4210, 4220, 4230, 4240};

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
        if (codeElementsArray[codeElementsArray.length - 1].equals("0")) {
            onlineGameWindow.sendCodeResultOfGameTurn(processShootingDataOfOpponentTurn(codeElementsIntegerArray));
            verifyEndOfTheGame();
        } else {
            onlineGameWindow.sendCodeResultOfGameTurn(processExplorationDataOfOpponentTurn(codeElementsIntegerArray));
        }
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
            int[] coordinatesOfCell = cellsOfShip.get(0).getCoordinates();
            int countOfDamagedCells = 0;
            for (Cell cell : cellsOfShip) if (cell.getStatus() == 2) countOfDamagedCells++;
            if (countOfDamagedCells == cellsOfShip.size()) {
                increaseTurnsForRecharge(cellsOfShip.size());
                resultOfTurn = delimiterAboutDestroyed;
                for (Cell cell : cellsOfShip) {
                    cell.setImage(3);
                    int[] coordinates = cell.getCoordinates();
                    resultOfTurn += delimiter + coordinates[0] + delimiter + coordinates[1] + delimiter + 3;
                }
                resultOfTurn += delimiterAboutDestroyed;
                setImageForShip(cellsOfShip, getDirectionOfShip(coordinatesOfCell[0], coordinatesOfCell[1], 3, 3), getImagesOfDamagedShip(cellsOfShip.size()));
            } else {
                resultOfTurn += delimiter + 2;
                setImageToDamagedCell(x, y, cellsOfShip, getDirectionOfShip(x, y, 2, 6));
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

    //Method for set image to damaged cell of the ship
    private void setImageToDamagedCell(int x, int y, Vector<Cell> cellsOfShip, int direction) {
        int indexOfCellByShip;
        int[] coordinatesOfFirstCell = cellsOfShip.get(0).getCoordinates();
        if (direction == 1) {
            indexOfCellByShip = y - coordinatesOfFirstCell[1];
        } else {
            indexOfCellByShip = coordinatesOfFirstCell[0] - x;
        }
        int codeOfCell = getCodeOfImageForDamagedCell(cellsOfShip.size(), indexOfCellByShip, direction);
        map[x][y].setImage(codeOfCell);
    }

    //Method which return code of damaged cell
    private int getCodeOfImageForDamagedCell(int lengthOfShip, int indexOfCellByShip, int direction) {
        int[] codesForImagesOfThisDamagedShip = getImagesOfDamagedShip(lengthOfShip);
        if (direction == 1)
            return codesForImagesOfThisDamagedShip[indexOfCellByShip];
        else return codesForImagesOfThisDamagedShip[indexOfCellByShip + lengthOfShip];
    }


    public void processDataOfResultTurn(String codeOfResultTurn) {
        String damagedCells = TEXT_DAMAGE;
        String missCells = TEXT_MISS;
        messageForLog = onlineGameWindow.getMessageForLog();
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
        for (int i = 0; i < codeIntegerElements.length; i++) {
            for (int j = 0; j < codeIntegerElements[i].length; j += 3) {
                //decision effect of shooting
                if (lastUsingCellForActionCoordinates[0] == codeIntegerElements[i][j] && lastUsingCellForActionCoordinates[1] == codeIntegerElements[i][j + 1] && (codeIntegerElements[i][j + 2] == 2 || codeIntegerElements[i][j + 2] == 3))
                    isDamageOrDestroy = true;
                map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].setImage(codeIntegerElements[i][j + 2]);
                if (onlineGameWindow.getPastMode() != 2 || map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].getStatus() == 5)
                    map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].setNotActive();
                //create message for log
                if (onlineGameWindow.getPastMode() != 2 && map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].getStatus() == 5)
                    missCells += String.format(" (%s;%s)", codeIntegerElements[i][j] + 1, codeIntegerElements[i][j + 1] + 1);
                if (onlineGameWindow.getPastMode() != 2 && map[codeIntegerElements[i][j]][codeIntegerElements[i][j + 1]].getStatus() == 2)
                    damagedCells += String.format(" (%s;%s)", codeIntegerElements[i][j] + 1, codeIntegerElements[i][j + 1] + 1);
            }
            //decision "is it destroyed ship or something else"
            if (isDestroyedShip(codeIntegerElements[i])) {
                messageForLog += createMessageAboutDestroyedShip(codeIntegerElements[i].length / 3);
                int x = codeIntegerElements[i][0];
                int y = codeIntegerElements[i][1];
                int direction = getDirectionOfShip(x, y, 3, 3);
                setImageForShip(getCellsOfShip(x, y, 3, 3), direction, getImagesOfDamagedShip(codeIntegerElements[i].length / 3));
                showCellsAroundShip(codeIntegerElements[i]);
            }
        }
        if (!missCells.equals(TEXT_MISS)) messageForLog += missCells + "\n";
        if (!damagedCells.equals(TEXT_DAMAGE)) messageForLog += damagedCells + "\n";
        //decision of next game turn
        if (isDamageOrDestroy && onlineGameWindow.getPastMode() == 0) {
            onlineGameWindow.changeTurn();
            onlineGameWindow.appendIntoLog(messageForLog, true);
        } else {
            onlineGameWindow.sendMessageAboutChangeTurn();
            onlineGameWindow.appendIntoLog(messageForLog, false);
        }
    }

    private int[] getImagesOfDamagedShip(int lengthOfShip) {
        switch (lengthOfShip) {
            case 2:
                return IMAGES_OF_DAMAGED_SHIP2;
            case 3:
                return IMAGES_OF_DAMAGED_SHIP3;
            case 4:
                return IMAGES_OF_DAMAGED_SHIP4;
            default:
                return IMAGES_OF_DAMAGED_SHIP1;
        }
    }

    private void showCellsAroundShip(int[] codeOfShip) {
        switch (getDirectionOfShip(codeOfShip[0], codeOfShip[1], 3, 2)) {
            case 0:
                showCellsAroundOneDeckShip(codeOfShip[0], codeOfShip[1]);
                break;
            case 1:
                showCellsAroundHorizontalShip(codeOfShip);
                break;
            case -1:
                showCellsAroundVerticalShip(codeOfShip);
                break;
        }
    }

    private void showCellsAroundVerticalShip(int[] codeOfShip) {
        for (int i = -1; i < 2; i++) {
            if (codeOfShip[0] + 1 < map.length && codeOfShip[1] + i >= 0 && codeOfShip[1] + i < map.length) {
                map[codeOfShip[0] + 1][codeOfShip[1] + i].setImage(5);
                map[codeOfShip[0] + 1][codeOfShip[1] + i].setNotActive();
            }
            if (codeOfShip[codeOfShip.length - 3] - 1 >= 0 && codeOfShip[codeOfShip.length - 2] + i >= 0 && codeOfShip[codeOfShip.length - 2] + i < map.length) {
                map[codeOfShip[codeOfShip.length - 3] - 1][codeOfShip[codeOfShip.length - 2] + i].setImage(5);
                map[codeOfShip[codeOfShip.length - 3] - 1][codeOfShip[codeOfShip.length - 2] + i].setNotActive();
            }
        }
        for (int i = 0; i < codeOfShip.length; i += 3) {
            if (codeOfShip[i + 1] + 1 < map.length) {
                map[codeOfShip[i]][codeOfShip[i + 1] + 1].setImage(5);
                map[codeOfShip[i]][codeOfShip[i + 1] + 1].setNotActive();
            }
            if (codeOfShip[i + 1] - 1 >= 0) {
                map[codeOfShip[i]][codeOfShip[i + 1] - 1].setImage(5);
                map[codeOfShip[i]][codeOfShip[i + 1] - 1].setNotActive();
            }
        }
    }

    private void showCellsAroundHorizontalShip(int[] codeOfShip) {
        for (int i = -1; i < 2; i++) {
            if (codeOfShip[1] - 1 >= 0 && codeOfShip[0] + i >= 0 && codeOfShip[0] + i < map.length) {
                map[codeOfShip[0] + i][codeOfShip[1] - 1].setImage(5);
                map[codeOfShip[0] + i][codeOfShip[1] - 1].setNotActive();
            }
            if (codeOfShip[codeOfShip.length - 2] + 1 < map.length && codeOfShip[codeOfShip.length - 3] + i >= 0 && codeOfShip[codeOfShip.length - 3] + i < map.length) {
                map[codeOfShip[codeOfShip.length - 3] + i][codeOfShip[codeOfShip.length - 2] + 1].setImage(5);
                map[codeOfShip[codeOfShip.length - 3] + i][codeOfShip[codeOfShip.length - 2] + 1].setNotActive();
            }
        }
        for (int i = 0; i < codeOfShip.length; i += 3) {
            if (codeOfShip[i] + 1 < map.length) {
                map[codeOfShip[i] + 1][codeOfShip[i + 1]].setImage(5);
                map[codeOfShip[i] + 1][codeOfShip[i + 1]].setNotActive();
            }
            if (codeOfShip[i] - 1 >= 0) {
                map[codeOfShip[i] - 1][codeOfShip[i + 1]].setImage(5);
                map[codeOfShip[i] - 1][codeOfShip[i + 1]].setNotActive();
            }
        }
    }

    private void showCellsAroundOneDeckShip(int x, int y) {
        for (int i = -1; i < 2; i++) {
            if (y + i < map.length && y + i >= 0) {
                if (x + 1 < map.length) {
                    map[x + 1][y + i].setImage(5);
                    map[x + 1][y + i].setNotActive();
                }
                if (x - 1 >= 0) {
                    map[x - 1][y + i].setImage(5);
                    map[x - 1][y + i].setNotActive();
                }
                if (i != 0) {
                    map[x][y + i].setImage(5);
                    map[x][y + i].setNotActive();
                }
            }
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
                code = x + delimiter + y;
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
        onlineGameWindow.sendCodeOfGameTurn(code + delimiter + actionType);
    }

    private void verifyEndOfTheGame() {
        if (count1Ship + count2Ship + count3Ship + count4Ship == 0) {
            onlineGameWindow.gameIsLost();
        }
    }

    public String getCodeOfMapAfterGame() {
        StringBuffer codeOfMap = new StringBuffer(100);
        int cellStatus;
        for (int i = 0; i < map.length; i++) {
            for (int j = 1; j < map.length; j++) {
                cellStatus = map[i][j].getStatus();
                if (cellStatus == 2 || cellStatus == 6)
                    codeOfMap.append(delimiter + i + delimiter + j + delimiter + cellStatus);
            }
        }
        codeOfMap.deleteCharAt(0);
        return String.valueOf(codeOfMap);
    }

    public void openMapOfOpponent(String codeOfMap) {
        String[] stringCodeElements = codeOfMap.split(delimiter);
        int[] integerCodeOfElements = getConvertedStringArrayToIntegerArrayCode(stringCodeElements);


        openSpaceCells();
    }

    public void openSpaceCells() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getStatus() == 1) {
                    map[i][j].setImage(5);
                    map[i][j].setNotActive();
                }
            }
        }
    }

    private int[] getConvertedStringArrayToIntegerArrayCode(String[] stringCodeElements) {
        int[] integerCodeOfElements = new int[stringCodeElements.length];
        for (int i = 0; i < stringCodeElements.length; i++)
            integerCodeOfElements[i] = Integer.parseInt(stringCodeElements[i]);
        return integerCodeOfElements;
    }
}
