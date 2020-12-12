package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

import java.util.Vector;

public class PreparingForGameMapBuilder extends MapBuilder {

    public PreparingForGameMapBuilder(Cell[][] map, PreparingForGameFrame preparingForGameFrame) {
        this.map = map;
        this.preparingForGameFrame = preparingForGameFrame;
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

    public void cancelStatus4() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getStatus() == 4) map[i][j].setImage(5);
            }
        }
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
}
