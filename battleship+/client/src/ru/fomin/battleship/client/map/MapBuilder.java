package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

public class MapBuilder {
    private Cell[][] map;
    private PreparingForGameFrame preparingForGameFrame;
    private int count1Ship=0;
    private int count2Ship=0;
    private int count3Ship=0;
    private int count4Ship=0;
    public MapBuilder(Cell[][] map, PreparingForGameFrame preparingForGameFrame){
        this.map=map;
        this.preparingForGameFrame=preparingForGameFrame;
    }
    public void setCountOfShips(){
        preparingForGameFrame.setCountOfShips(count1Ship,count2Ship,count3Ship,count4Ship);
    }
}
