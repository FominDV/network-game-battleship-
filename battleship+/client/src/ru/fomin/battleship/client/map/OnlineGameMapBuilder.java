package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;

public class OnlineGameMapBuilder extends MapBuilder{
    private OnlineGameWindow onlineGameWindow;

    public OnlineGameMapBuilder(Cell[][] map, OnlineGameWindow onlineGameWindow) {
        this.map = map;
        this.onlineGameWindow=onlineGameWindow;
        count4Ship=MAX_OF_SHIP4;
        count3Ship=MAX_OF_SHIP3;
        count2Ship=MAX_OF_SHIP2;
        count1Ship=MAX_OF_SHIP1;
    }
}
