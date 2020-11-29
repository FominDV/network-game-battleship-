package ru.fomin.battleship.client.gui;

import javax.swing.*;

public class OnlineGameWindow extends JFrame {
    private final String opponentNickname;
    private final String NICK_NAME;

    public OnlineGameWindow(String opponentNickname, String nick_name) {
        this.opponentNickname = opponentNickname;
        NICK_NAME = nick_name;
    }

    public void setTitleAboutOpponent() {
            setTitle(NICK_NAME + " VS " + opponentNickname);
    }

    public void reconnect() {

    }
}
