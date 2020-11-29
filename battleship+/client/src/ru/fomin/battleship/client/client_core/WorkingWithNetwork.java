package ru.fomin.battleship.client.client_core;

import java.util.Vector;

public interface WorkingWithNetwork {
    void sendMessageToServer(String message);
    Vector<String[]> getDataMap();
}
