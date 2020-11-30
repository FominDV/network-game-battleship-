package ru.fomin.battleship.client.gui;

import javax.swing.*;

public class OnlineGameWindow extends JFrame {
    private final String opponentNickname;
    private final String NICK_NAME;
    private final int WIDTH=800;
    private final int HEIGHT=400;

    public OnlineGameWindow(String opponentNickname, String nickName) {
        this.opponentNickname = opponentNickname;
        NICK_NAME = nickName;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(NICK_NAME + " VS " + opponentNickname);
        setResizable(false);

        setVisible(true);
    }


}
