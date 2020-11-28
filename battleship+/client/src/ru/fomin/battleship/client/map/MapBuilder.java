package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

import java.util.Vector;

public class MapBuilder {
    private Cell[][] map;
    private PreparingForGameFrame preparingForGameFrame;
    private final int MAX_OF_SHIP4 = 1;
    private final int MAX_OF_SHIP3 = 2;
    private final int MAX_OF_SHIP2 = 3;
    private final int MAX_OF_SHIP1 = 4;
    private int count1Ship = 0;
    private int count2Ship = 0;
    private int count3Ship = 0;
    private int count4Ship = 0;

    public MapBuilder(Cell[][] map, PreparingForGameFrame preparingForGameFrame) {
        this.map = map;
        this.preparingForGameFrame = preparingForGameFrame;
    }

    public void setCountOfShips() {
        preparingForGameFrame.setCountOfShips(count1Ship, count2Ship, count3Ship, count4Ship);
    }

    public boolean validCellForPreparingPost(boolean isPostMode, int x, int y) {
        if (isPostMode && map[x][y].getStatus() == 5 && validSpaces(x, y) && validShipLength(x, y)) return true;
        else return false;
    }

    private boolean validShipLength(int x, int y) {
        int maxLengthOfShip = maxLengthOfShip();
        int statusOfCell;
        int direction = directionOfShip(x, y, 4);
        int nowLengthOfShip = nowLengthOfShip(x, y);
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

    private int nowLengthOfShip(int x, int y) {
        int direction = directionOfShip(x, y, 4);
        if (direction == 0) return 0;
        int countCellOfShip = 0;
        if (direction == -1) {
            for (int i = 1; i <= 4; i++) {
                if (x - i >= 0 && map[x - i][y].getStatus() == 4) countCellOfShip++;
                else break;
            }
            for (int i = 1; i <= 4; i++) {
                if (x + i < map.length && map[x + i][y].getStatus() == 4) countCellOfShip++;
                else break;
            }
        } else {
            for (int i = 1; i <= 4; i++) {
                if (y - i >= 0 && map[x][y - i].getStatus() == 4) countCellOfShip++;
                else break;
            }
            for (int i = 1; i <= 4; i++) {
                if (y + i < map.length && map[x][y + i].getStatus() == 4) countCellOfShip++;
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
        int direction = directionOfShip(x, y, 4);
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
    private int directionOfShip(int x, int y, int status) {
        if (x - 1 >= 0) {
            if (x + 1 < map.length) {
                if ((map[x - 1][y].getStatus() == status || map[x + 1][y].getStatus() == status)) return -1;
            } else {
                if (map[x - 1][y].getStatus() == status) return -1;
            }
        } else {
            if (x + 1 < map.length) {
                if (map[x + 1][y].getStatus() == status) return -1;
            }
        }

        if (y - 1 >= 0) {
            if (y + 1 < map.length) {
                if (map[x][y - 1].getStatus() == status || map[x][y + 1].getStatus() == status) return 1;
            } else {
                if (map[x][y - 1].getStatus() == status) return 1;
            }
        } else {
            if (y + 1 < map.length) {
                if (map[x][y + 1].getStatus() == status) return 1;
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
        direction = directionOfShip(x, y, 4);
        Vector<Cell> cellsForBuildingShip = getCellsOfShip(x, y, 4);
        int lengthOfShip = cellsForBuildingShip.size();
        postTheShip(lengthOfShip, cellsForBuildingShip, direction);
    }

    public void postTheShip(int lengthOfShip, Vector<Cell> cellsOfShip, int direction) {
        if (!isSetNewCountOfShips(lengthOfShip)) return;
        for (Cell cell : cellsOfShip) {
            cell.setImage(6);
        }
        setImageOfShip(cellsOfShip);
    }

    private void setImageOfShip(Vector<Cell> cellsOfShip) {
        switch (cellsOfShip.size()) {
            case 4:

                break;
            case 3:

                break;
            case 2:
               setImageForShip2(cellsOfShip);
                break;
            case 1:
                cellsOfShip.get(0).setImage(11);
                break;
        }
    }

    private void setImageForShip2(Vector<Cell> cellsOfShip) {

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

    private Vector<Cell> getCellsOfShip(int x, int y, int status) {
        int direction = directionOfShip(x, y, status);
        Vector<Cell> cellsOfShip = new Vector<>();
        if (direction == 0) cellsOfShip.add(map[x][y]);
        if (direction == -1) {
            for (int i = 1; i <= map.length; i++) {
                if (x + i < map.length && map[x + i][y].getStatus() == status) cellsOfShip.add(map[x + i][y]);
            }
            cellsOfShip.add(map[x][y]);
            for (int i = 1; i <= map.length; i++) {
                if (x - i >= 0 && map[x - i][y].getStatus() == status) cellsOfShip.add(map[x - i][y]);
            }
        }
        if (direction == 1) {
            for (int i = 1; i <= map.length; i++) {
                if (y - i >= 0 && map[x][y - i].getStatus() == status) cellsOfShip.add(map[x][y - i]);
            }
            cellsOfShip.add(map[x][y]);
            for (int i = 1; i <= map.length; i++) {
                if (y + i < map.length && map[x][y + i].getStatus() == status) cellsOfShip.add(map[x][y + i]);
            }
        }
        return cellsOfShip;
    }

    public void remove(int x, int y) {
        if (map[x][y].getStatus() != 6) return;
        Vector<Cell> cellsOfShip = getCellsOfShip(x, y, 6);
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
}
