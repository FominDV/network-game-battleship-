package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;

public class MapBuilder {
    private Cell[][] map;
    private PreparingForGameFrame preparingForGameFrame;
    public MapBuilder(Cell[][] map, PreparingForGameFrame preparingForGameFrame){
        this.map=map;
        this.preparingForGameFrame=preparingForGameFrame;
    }
}
