package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;
import ru.fomin.battleship.common.LibraryOfPrefixes;

public class SearchOpponentThread extends Thread {
    private PreparingForGameFrame preparingGameFrame;

    public SearchOpponentThread( PreparingForGameFrame preparingGameFrame) {
        this.preparingGameFrame = preparingGameFrame;
        start();
    }

    @Override
    public void run() {
        while (preparingGameFrame.isOpponentNicknameEmpty()) {
            preparingGameFrame.sendMessageForSearching();
        }
        preparingGameFrame.setTitleAboutOpponent();
        interrupt();
    }

}

