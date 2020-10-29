package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;

import javax.swing.*;

public class PreparingForGameFrame extends JFrame {
    private final int WIDTH = 600;
    private final int HEIGHT = 500;
    private final String WINDOW_TITLE = "Map-Maker by ";

    private final SocketThread SOCKET_THREAD;
    private final String NICK_NAME;
    private final WorkingWithNetwork listener;
    private final Thread searchOpponent;
    private String opponentNickname="empty";

    public PreparingForGameFrame(SocketThread socketThread, String nickname, WorkingWithNetwork listener) {
        NICK_NAME = nickname;
        SOCKET_THREAD = socketThread;
        this.listener=listener;
        searchOpponent=new Thread(()->searchOpponent());
        searchOpponent.start();
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE+ NICK_NAME);
        setResizable(false);
        setVisible(true);
    }
    private void searchOpponent(){
        while(opponentNickname.equals("empty")){
            listener.sendMessageToServer(LibraryOfPrefixes.getSearchOpponent(NICK_NAME));
        }
    }
    public void setOpponentNickname(String opponentNickname){
        this.opponentNickname=opponentNickname;
    }
}
