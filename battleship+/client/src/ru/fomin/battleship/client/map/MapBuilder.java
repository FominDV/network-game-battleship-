package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

import java.util.Vector;

public class MapBuilder {
    protected Cell[][] map;
    private PreparingForGameFrame preparingForGameFrame;
    protected final int MAX_OF_SHIP4 = 1;
    protected final int MAX_OF_SHIP3 = 2;
    protected final int MAX_OF_SHIP2 = 3;
    protected final int MAX_OF_SHIP1 = 4;
    protected int count1Ship = 0;
    protected int count2Ship = 0;
    protected int count3Ship = 0;
    protected int count4Ship = 0;
    static String delimiter = "x";
    protected final int[] IMAGES_OF_SHIP1 = {11};
    protected final int[] IMAGES_OF_SHIP3 = {311, 312, 313, 321, 322, 323};
    protected final int[] IMAGES_OF_SHIP2 = {211, 212, 221, 222};
    protected final int[] IMAGES_OF_SHIP4 = {411, 412, 413, 414, 421, 422, 423, 424};

    public MapBuilder(Cell[][] map, PreparingForGameFrame preparingForGameFrame) {
        this.map = map;
        this.preparingForGameFrame = preparingForGameFrame;
    }

    public MapBuilder() {
    }

    public void setCountOfShips() {
        if (preparingForGameFrame != null)
            preparingForGameFrame.setCountOfShips(count1Ship, count2Ship, count3Ship, count4Ship);
    }


    public boolean validCellForPreparingPost(boolean isPostMode, int x, int y) {
        if (isPostMode && map[x][y].getStatus() == 5 && validSpaces(x, y) && validShipLength(x, y)) return true;
        else return false;
    }

    private boolean validShipLength(int x, int y) {
        int maxLengthOfShip = maxLengthOfShip();
        int statusOfCell;
        int direction = getDirectionOfShip(x, y, 4, 4);
        int nowLengthOfShip = nowLengthOfShip(x, y, 4);
        if (nowLengthOfShip >= maxLengthOfShip) return false;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                statusOfCell = map[i][j].getStatus();
                if (direction == -1 && statusOfCell == 4 && (i != x - 1 || i != x + 1) && j != y) return false;
                if (direction == 1 && statusOfCell == 4 && (i != y - 1 || i != y + 1) && i != x) return false;
                if (direction == 0 && statusOfCell == 4) return false;
            }
        }
        return true;
    }

    protected int nowLengthOfShip(int x, int y, int status) {
        int direction = getDirectionOfShip(x, y, status, status);
        if (direction == 0) return 0;
        int countCellOfShip = 0;
        if (direction == -1) {
            for (int i = 1; i <= 4; i++) {
                if (x - i >= 0 && map[x - i][y].getStatus() == status) countCellOfShip++;
                else break;
            }
            for (int i = 1; i <= 4; i++) {
                if (x + i < map.length && map[x + i][y].getStatus() == status) countCellOfShip++;
                else break;
            }
        } else {
            for (int i = 1; i <= 4; i++) {
                if (y - i >= 0 && map[x][y - i].getStatus() == status) countCellOfShip++;
                else break;
            }
            for (int i = 1; i <= 4; i++) {
                if (y + i < map.length && map[x][y + i].getStatus() == status) countCellOfShip++;
                else break;
            }
        }
        return countCellOfShip;
    }

    private int maxLengthOfShip() {
        if (count4Ship < MAX_OF_SHIP4) return 4;
        if (count3Ship < MAX_OF_SHIP3) return 3;
        if (count2Ship < MAX_OF_SHIP2) return 2;
        if (count1Ship < MAX_OF_SHIP1) return 1;
        return 0;
    }

    private boolean validSpaces(int x, int y) {
        int direction = getDirectionOfShip(x, y, 4, 4);
        if (y - 1 >= 0 && x - 1 >= 0) {
            if (direction == -1 && !((y + 1 >= map.length || (map[x][y + 1].getStatus() == 5 && map[x - 1][y + 1].getStatus() == 5)) && map[x][y - 1].getStatus() == 5 && ((x + 1 >= map.length || map[x + 1][y].getStatus() == 5 || map[x + 1][y].getStatus() == 4) || (map[x - 1][y].getStatus() == 5 || map[x - 1][y].getStatus() == 4)) && map[x - 1][y - 1].getStatus() == 5 && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5) && (x + 1 >= map.length || map[x + 1][y - 1].getStatus() == 5)))
                return false;
            if (direction == 1 && !((x + 1 >= map.length || (map[x + 1][y].getStatus() == 5 && map[x + 1][y - 1].getStatus() == 5)) && map[x - 1][y].getStatus() == 5 && ((y + 1 >= map.length || ((map[x][y + 1].getStatus() == 5 || map[x][y + 1].getStatus() == 4) && map[x - 1][y + 1].getStatus() == 5))) && (map[x][y - 1].getStatus() == 5 || map[x][y - 1].getStatus() == 4) && map[x - 1][y - 1].getStatus() == 5 && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                return false;
            if (direction == 0 && !((y + 1 >= map.length || (map[x][y + 1].getStatus() == 5 && map[x - 1][y + 1].getStatus() == 5)) && map[x][y - 1].getStatus() == 5 && (x + 1 >= map.length || (map[x + 1][y].getStatus() == 5 && map[x + 1][y - 1].getStatus() == 5)) && map[x - 1][y].getStatus() == 5 && map[x - 1][y - 1].getStatus() == 5 && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                return false;
        } else {
            if (y - 1 >= 0 && x - 1 < 0) {
                if (direction == -1 && !((y + 1 >= map.length || map[x][y + 1].getStatus() == 5) && map[x][y - 1].getStatus() == 5 && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5) && (x + 1 >= map.length || (map[x + 1][y - 1].getStatus() == 5 && (map[x + 1][y].getStatus() == 5 || map[x + 1][y].getStatus() == 4)))))
                    return false;
                if (direction == 1 && !(((y + 1 >= map.length || map[x][y + 1].getStatus() == 5 || map[x][y + 1].getStatus() == 4) || (map[x][y - 1].getStatus() == 5 || map[x][y - 1].getStatus() == 4)) && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5) && (x + 1 >= map.length || (map[x + 1][y - 1].getStatus() == 5 && map[x + 1][y].getStatus() == 5))))
                    return false;
                if (direction == 0 && !((y + 1 >= map.length || map[x][y + 1].getStatus() == 5) && map[x][y - 1].getStatus() == 5 && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5) && (x + 1 >= map.length || (map[x + 1][y - 1].getStatus() == 5 && map[x + 1][y].getStatus() == 5))))
                    return false;
            } else {
                if (y - 1 < 0 && x - 1 < 0) {
                    if (direction == -1 && !((y + 1 >= map.length || map[x][y + 1].getStatus() == 5) && (x + 1 >= map.length || map[x + 1][y].getStatus() == 5 || map[x + 1][y].getStatus() == 4) && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                        return false;
                    if (direction == 1 && !((x + 1 >= map.length || map[x + 1][y].getStatus() == 5) && (y + 1 >= map.length || map[x][y + 1].getStatus() == 5 || map[x][y + 1].getStatus() == 4) && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                        return false;
                    if (direction == 0 && !((y + 1 >= map.length || map[x][y + 1].getStatus() == 5) && (x + 1 >= map.length || map[x + 1][y].getStatus() == 5) && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                        return false;
                } else {
                    if (y - 1 < 0 && x - 1 >= 0) {
                        if (direction == -1 && !((y + 1 >= map.length || (map[x][y + 1].getStatus() == 5 && map[x - 1][y + 1].getStatus() == 5)) && ((x + 1 >= map.length || map[x + 1][y].getStatus() == 5 || map[x + 1][y].getStatus() == 4) || (map[x - 1][y].getStatus() == 5 || map[x - 1][y].getStatus() == 4)) && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                            return false;
                        if (direction == 1 && !((x + 1 >= map.length || map[x + 1][y].getStatus() == 5) && map[x - 1][y].getStatus() == 5 && (y + 1 >= map.length || ((map[x][y + 1].getStatus() == 5 || map[x][y + 1].getStatus() == 4) && map[x - 1][y + 1].getStatus() == 5)) && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                            return false;
                        if (direction == 0 && !((y + 1 >= map.length || (map[x][y + 1].getStatus() == 5 && map[x - 1][y + 1].getStatus() == 5)) && (x + 1 >= map.length || map[x + 1][y].getStatus() == 5) && map[x - 1][y].getStatus() == 5 && (x + 1 >= map.length || y + 1 >= map.length || map[x + 1][y + 1].getStatus() == 5)))
                            return false;
                    }
                }
            }
        }
        return true;
    }


    /*1: horizontal
     * 0: no direction
     * -1: vertical*/
    protected int getDirectionOfShip(int x, int y, int status1, int status2) {
        if (x - 1 >= 0) {
            if (x + 1 < map.length) {
                if (map[x - 1][y].getStatus() == status1 || map[x + 1][y].getStatus() == status1 || map[x - 1][y].getStatus() == status2 || map[x + 1][y].getStatus() == status2)
                    return -1;
            } else {
                if (map[x - 1][y].getStatus() == status1 || map[x - 1][y].getStatus() == status2) return -1;
            }
        } else {
            if (x + 1 < map.length) {
                if (map[x + 1][y].getStatus() == status1 || map[x + 1][y].getStatus() == status2) return -1;
            }
        }

        if (y - 1 >= 0) {
            if (y + 1 < map.length) {
                if (map[x][y - 1].getStatus() == status1 || map[x][y + 1].getStatus() == status1 || map[x][y - 1].getStatus() == status2 || map[x][y + 1].getStatus() == status2)
                    return 1;
            } else {
                if (map[x][y - 1].getStatus() == status1 || map[x][y - 1].getStatus() == status2) return 1;
            }
        } else {
            if (y + 1 < map.length) {
                if (map[x][y + 1].getStatus() == status1 || map[x][y + 1].getStatus() == status2) return 1;
            }
        }
        return 0;
    }

    public void cancelStatus4() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getStatus() == 4) map[i][j].setImage(5);
            }
        }
    }

    public void post() {
        int x = -1, y = -1, direction = -10;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getStatus() == 4) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if (x == -1) return;
        direction = getDirectionOfShip(x, y, 4, 4);
        Vector<Cell> cellsForBuildingShip = getCellsOfShip(x, y, 4, 4);
        int lengthOfShip = cellsForBuildingShip.size();
        postTheShip(lengthOfShip, cellsForBuildingShip, direction);
    }

    public void postTheShip(int lengthOfShip, Vector<Cell> cellsOfShip, int direction) {
        if (!isSetNewCountOfShips(lengthOfShip)) return;
        for (Cell cell : cellsOfShip) {
            cell.setImage(6);
        }
        setImageForShip(cellsOfShip, direction, getImagesForShip(cellsOfShip.size()));
    }


    protected void setImageForShip(Vector<Cell> cellsOfShip, int direction, int[] imagesForShip) {
        if (direction == 0) {
            cellsOfShip.get(0).setImage(imagesForShip[0]);
            return;
        }
        for (int i = 0; i < cellsOfShip.size(); i++) {
            if (direction == 1) {
                cellsOfShip.get(i).setImage(imagesForShip[i]);
            } else {
                cellsOfShip.get(i).setImage(imagesForShip[i + cellsOfShip.size()]);
            }
        }
    }

    private int[] getImagesForShip(int lengthOfShip) {
        switch (lengthOfShip) {
            case 2:
                return IMAGES_OF_SHIP2;
            case 3:
                return IMAGES_OF_SHIP3;
            case 4:
                return IMAGES_OF_SHIP4;
            default:
                return IMAGES_OF_SHIP1;
        }
    }


    private boolean isSetNewCountOfShips(int lengthOfShip) {
        switch (lengthOfShip) {
            case 4:
                if (count4Ship == MAX_OF_SHIP4) return false;
                count4Ship++;
                break;
            case 3:
                if (count3Ship == MAX_OF_SHIP3) return false;
                count3Ship++;
                break;
            case 2:
                if (count2Ship == MAX_OF_SHIP2) return false;
                count2Ship++;
                break;
            case 1:
                if (count1Ship == MAX_OF_SHIP1) return false;
                count1Ship++;
                break;
        }
        setCountOfShips();
        return true;
    }

    protected Vector<Cell> getCellsOfShip(int x, int y, int status1, int status2) {
        int direction = getDirectionOfShip(x, y, status1, status2);
        Vector<Cell> cellsOfShip = new Vector<>();
        int bufferNumberOfCells;
        if (direction == 0) cellsOfShip.add(map[x][y]);
        //vertical
        if (direction == -1) {
            bufferNumberOfCells = 0;
            for (int i = 1; i <= map.length; i++) {
                if (x + i < map.length && (map[x + i][y].getStatus() == status1 || map[x + i][y].getStatus() == status2))
                    bufferNumberOfCells++;
                else break;
            }
            for (int i = bufferNumberOfCells; i > 0; i--) cellsOfShip.add(map[x + i][y]);
            cellsOfShip.add(map[x][y]);
            bufferNumberOfCells = 0;
            for (int i = 1; i <= map.length; i++) {
                if (x - i >= 0 && (map[x - i][y].getStatus() == status1 || map[x - i][y].getStatus() == status2))
                    bufferNumberOfCells++;
                else break;
            }
            for (int i = 1; i <= bufferNumberOfCells; i++) cellsOfShip.add(map[x - i][y]);
        }
        //horizontal
        if (direction == 1) {
            bufferNumberOfCells = 0;
            for (int i = 1; i <= map.length; i++) {
                if (y - i >= 0 && (map[x][y - i].getStatus() == status1 || map[x][y - i].getStatus() == status2))
                    bufferNumberOfCells++;
                else break;
            }
            for (int i = bufferNumberOfCells; i > 0; i--) cellsOfShip.add(map[x][y - i]);
            cellsOfShip.add(map[x][y]);
            bufferNumberOfCells = 0;
            for (int i = 1; i <= map.length; i++) {
                if (y + i < map.length && (map[x][y + i].getStatus() == status1 || map[x][y + i].getStatus() == status2))
                    bufferNumberOfCells++;
                else break;
            }
            for (int i = 1; i <= bufferNumberOfCells; i++) cellsOfShip.add(map[x][y + i]);
        }
        return cellsOfShip;
    }

    public void remove(int x, int y) {
        if (map[x][y].getStatus() != 6) return;
        Vector<Cell> cellsOfShip = getCellsOfShip(x, y, 6, 6);
        for (Cell cell : cellsOfShip) {
            cell.setImage(5);
        }
        reduceCountOfShips(cellsOfShip.size());
        setCountOfShips();
    }

    private void reduceCountOfShips(int lengthOfShip) {
        switch (lengthOfShip) {
            case 4:
                count4Ship--;
                break;
            case 3:
                count3Ship--;
                break;
            case 2:
                count2Ship--;
                break;
            case 1:
                count1Ship--;
                break;
        }
    }

    public String getDataSaving() {
        String dataSaving = "";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getStatus() == 6) if (dataSaving.equals("")) dataSaving += i + delimiter + j;
                else dataSaving += delimiter + i + delimiter + j;
            }
        }
        return dataSaving;
    }

    public void loadMap(String dataMap) {
        int x, y;
        String[] coordinates = dataMap.split(delimiter);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j].setImage(5);
            }
        }
        for (int i = 0; i < coordinates.length; i += 2) {
            x = Integer.parseInt(coordinates[i]);
            y = Integer.parseInt(coordinates[i + 1]);
            map[x][y].setImage(4);
        }
        count1Ship = 0;
        count2Ship = 0;
        count3Ship = 0;
        count4Ship = 0;
        for (int i = 0; i < 10; i++) post();
        setCountOfShips();
    }


}
