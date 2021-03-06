package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.OnlineGameWindow;
import ru.fomin.battleship.client.gui.PreparingForGameFrame;
import ru.fomin.battleship.client.gui.SavingMapWindow;
import ru.fomin.battleship.client.gui.SearchingOpponent;
import ru.fomin.network.SocketThread;

import java.util.Vector;

public interface WorkingWithHandler {
    void sendMessageToServer(String message);
    Vector<String[]> getDataMap();
    void setSavingMapWindow(SavingMapWindow savingMapWindow);
    void setSearchingOpponent(SearchingOpponent searchingOpponent);
    void setOnlineGameWindow(OnlineGameWindow onlineGameWindow);
    void setPreparingForGameWindow(PreparingForGameFrame preparingForGameFrame);
    void exitToMapBuilder();
    SocketThread getSocket();
    String getLogin();
}
