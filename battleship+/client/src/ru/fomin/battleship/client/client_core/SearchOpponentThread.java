package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;
import ru.fomin.battleship.common.LibraryOfPrefixes;

import java.io.Closeable;
import java.io.IOException;

public class SearchOpponentThread extends Thread {
    private final long TIME_OUT = 1000L;
    private PreparingForGameFrame preparingGameFrame;

    public SearchOpponentThread(PreparingForGameFrame preparingGameFrame) {
        this.preparingGameFrame = preparingGameFrame;
        start();
    }

    @Override
    public void run() {
        while (!isInterrupted() && preparingGameFrame.isOpponentNicknameEmpty()) {
            preparingGameFrame.sendMessageForSearching();
            try {
                sleep(TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            }
        }
        preparingGameFrame.setTitleAboutOpponent();
    }

}

