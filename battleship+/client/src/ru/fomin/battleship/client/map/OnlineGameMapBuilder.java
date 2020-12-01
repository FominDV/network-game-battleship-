package ru.fomin.battleship.client.map;

import ru.fomin.battleship.client.gui.OnlineGameWindow;

public class OnlineGameMapBuilder extends MapBuilder{
    private OnlineGameWindow onlineGameWindow;

    public OnlineGameMapBuilder(Cell[][] map, OnlineGameWindow onlineGameWindow) {
        this.map = map;
        this.onlineGameWindow=onlineGameWindow;
    }
}
