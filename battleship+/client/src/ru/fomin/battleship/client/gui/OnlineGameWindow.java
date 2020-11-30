package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.client.map.MapBuilder;

import javax.swing.*;

public class OnlineGameWindow extends JFrame {
    private final String opponentNickname;
    private final String NICK_NAME;
    private WorkingWithNetwork listener;
    private MapBuilder mapBuilder;
    private String mapCodeOfUser;
    private String mapCodeOfOpponent;
    private final int WIDTH=800;
    private final int HEIGHT=400;

    public OnlineGameWindow(String opponentNickname, String nickName, MapBuilder mapBuilder, WorkingWithNetwork listener, String mapCodeOfUser, String mapCodeOfOpponent) {
        this.opponentNickname = opponentNickname;
        NICK_NAME = nickName;
        this.mapBuilder=mapBuilder;
        this.listener=listener;
        this.mapCodeOfUser=mapCodeOfUser;
        this.mapCodeOfOpponent=mapCodeOfOpponent;
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
