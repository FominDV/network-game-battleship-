package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.PreparingForGameFrame;
import ru.fomin.battleship.client.gui.SearchingOpponent;

public class SearchOpponentThread extends Thread {
    private final long TIME_OUT = 1000L;
    private PreparingForGameFrame preparingGameFrame;
    private SearchingOpponent searchingOpponent;
    public SearchOpponentThread(PreparingForGameFrame preparingGameFrame, SearchingOpponent searchingOpponent) {
        this.preparingGameFrame = preparingGameFrame;
        this.searchingOpponent=searchingOpponent;
        start();
    }

    @Override
    public void run() {
        int allDots=0;
        while (!isInterrupted() && preparingGameFrame.isOpponentNicknameEmpty()) {
            preparingGameFrame.sendMessageForSearching();
            if(allDots>16983) allDots=0;
            allDots++;
            searchingOpponent.showProcess(allDots);
            try {
                sleep(TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            }
        }
    }

}

