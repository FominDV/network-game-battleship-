package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

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
        if (isPostMode == true && map[x][y].getStatus() == 5 && validSpaces(x, y) && validShipLength(x, y)) return true;
        else return false;
    }

    private boolean validShipLength(int x, int y) {
        int maxLengthOfShip = maxLengthOfShip();
        int statusOfCell;
        int direction = directionOfShip(x, y);
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
        if (x - 1 >= 0 && map[x - 1][y].getStatus() == 4) {
            for (int i = 2; i < 5; i++) {
                if (x - i < 0 || map[x - i][y].getStatus() != 4) return i - 1;
            }
        }
        if (map[x + 1][y].getStatus() == 4) {
            for (int i = 2; i < 5; i++) {
                if (map[x + i][y].getStatus() != 4) return i - 1;
            }
        }
        if (y - 1 >= 0 && map[x][y - 1].getStatus() == 4) {
            for (int i = 2; i < 5; i++) {
                if (y - i < 0 || map[x][y - i].getStatus() != 4) return i - 1;
            }
        }
        if (map[x][y + 1].getStatus() == 4) {
            for (int i = 2; i < 5; i++) {
                if (map[x][y + i].getStatus() != 4) return i - 1;
            }
        }
        return 0;
    }

    private int maxLengthOfShip() {
        if (count4Ship < MAX_OF_SHIP4) return 4;
        if (count3Ship < MAX_OF_SHIP3) return 3;
        if (count2Ship < MAX_OF_SHIP2) return 2;
        if (count1Ship < MAX_OF_SHIP1) return 1;
        return 0;
    }

    private boolean validSpaces(int x, int y) {
        int direction = directionOfShip(x, y);
        if (y - 1 >= 0 && x - 1 >= 0) {
            if (direction == -1 && map[x][y + 1].getStatus() != 5 && map[x][y - 1].getStatus() != 5 && (map[x + 1][y].getStatus() != 5 || map[x - 1][y].getStatus() != 5) && map[x - 1][y - 1].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5 && map[x + 1][y - 1].getStatus() != 5 && map[x - 1][y + 1].getStatus() != 5)
                return false;
            if (direction == 1 && map[x+1][y].getStatus() != 5 && map[x-1][y].getStatus() != 5 && (map[x][y+1].getStatus() != 5 || map[x][y-1].getStatus() != 5) && map[x - 1][y - 1].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5 && map[x + 1][y - 1].getStatus() != 5 && map[x - 1][y + 1].getStatus() != 5)
                return false;
            if (direction == 0 && map[x][y + 1].getStatus() != 5 && map[x][y - 1].getStatus() != 5 && map[x + 1][y].getStatus() != 5 && map[x - 1][y].getStatus() != 5 && map[x - 1][y - 1].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5 && map[x + 1][y - 1].getStatus() != 5 && map[x - 1][y + 1].getStatus() != 5)
                return false;}
            else {
            if (y - 1 >= 0 && x - 1 < 0) {
                if (direction == -1 && map[x][y + 1].getStatus() != 5 && map[x][y - 1].getStatus() != 5 && map[x + 1][y].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5 && map[x + 1][y - 1].getStatus() != 5)
                    return false;
                if (direction == 1 && map[x + 1][y].getStatus() != 5 && (map[x][y + 1].getStatus() != 5 || map[x][y - 1].getStatus() != 5) && map[x + 1][y + 1].getStatus() != 5 && map[x + 1][y - 1].getStatus() != 5)
                    return false;
                if (direction == 0 && map[x][y + 1].getStatus() != 5 && map[x][y - 1].getStatus() != 5 && map[x + 1][y].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5 && map[x + 1][y - 1].getStatus() != 5)
                    return false;
            } else {
                if (y - 1 < 0 && x - 1 < 0) {
                    if (direction == -1 && map[x][y + 1].getStatus() != 5 && map[x + 1][y].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5)
                        return false;
                    if (direction == 1 && map[x + 1][y].getStatus() != 5 && map[x][y + 1].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5)
                        return false;
                    if (direction == 0 && map[x][y + 1].getStatus() != 5 && map[x + 1][y].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5)
                        return false;
                }else {
                    if (y - 1 < 0 && x - 1 >= 0){
                        if (direction == -1 && map[x][y + 1].getStatus() != 5&& (map[x + 1][y].getStatus() != 5 || map[x - 1][y].getStatus() != 5) && map[x + 1][y + 1].getStatus() != 5  && map[x - 1][y + 1].getStatus() != 5)
                            return false;
                        if (direction == 1 && map[x+1][y].getStatus() != 5 && map[x-1][y].getStatus() != 5 && map[x][y+1].getStatus() != 5  && map[x + 1][y + 1].getStatus() != 5  && map[x - 1][y + 1].getStatus() != 5)
                            return false;
                        if (direction == 0 && map[x][y + 1].getStatus() != 5  && map[x + 1][y].getStatus() != 5 && map[x - 1][y].getStatus() != 5 && map[x + 1][y + 1].getStatus() != 5&& map[x - 1][y + 1].getStatus() != 5)
                            return false;
                    }
                }
            }
        }
            return true;
        }


    /*-1: horizontal
     * 0: no direction
     * 1-vertical*/
    private int directionOfShip(int x, int y) {
        if(x-1>=0){
            if ( (map[x - 1][y].getStatus() == 4 || map[x + 1][y].getStatus() == 4)) return -1;
        }else{
            if (  map[x + 1][y].getStatus() == 4) return -1;
        }
        if(y-1>=0){
            if (  (map[x][y - 1].getStatus() == 4 || map[x][y + 1].getStatus() == 4)) return 1;
        }else{
            if ( map[x][y + 1].getStatus() == 4) return 1;
        }
        return 0;
    }
}
