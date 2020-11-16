package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.SearchOpponentThread;
import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreparingForGameFrame extends JFrame {
    private final int WIDTH = 600;
    private final int HEIGHT = 500;
    private final String WINDOW_TITLE = "Map-Maker by ";
    private final SocketThread SOCKET_THREAD;
    private final String NICK_NAME;
    private final WorkingWithNetwork listener;
    private String opponentNickname = "empty";
    private SearchOpponentThread searchOpponentThread=null;

    private final JPanel PANEL_MAP=new JPanel(new GridLayout(10, 10));


    public PreparingForGameFrame(SocketThread socketThread, String nickname, WorkingWithNetwork listener) {
        NICK_NAME = nickname;
        SOCKET_THREAD = socketThread;
        this.listener = listener;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE + NICK_NAME);
        setResizable(false);
        fillMap();
        setVisible(true);

    }

    private void fillMap() {
        
    }

    private void searchOpponent() {
        if(searchOpponentThread==null|| !(searchOpponentThread.isAlive())) {
            listener.sendMessageToServer(LibraryOfPrefixes.MESSAGE_ABOUT_START_SEARCHING);
            searchOpponentThread = new SearchOpponentThread(this);
        }
            }


    public void setOpponentNickname(String opponentNickname) {
        this.opponentNickname = opponentNickname;
    }

    public void setTitleAboutOpponent(){
        if(!opponentNickname.equals("empty"))
            setTitle(NICK_NAME+" VS "+ opponentNickname);
    }
    public void reconnect() {
        this.opponentNickname = "empty";
        setTitle(WINDOW_TITLE + NICK_NAME);
        stopSearching();
    }


    public boolean isOpponentNicknameEmpty() {
        if (opponentNickname.equals("empty")) return true;
        else return false;
    }
    public void sendMessageForSearching(){
        listener.sendMessageToServer(LibraryOfPrefixes.getSearchOpponent(NICK_NAME));
    }
    public void stopSearching(){
        searchOpponentThread.interrupt();
        searchOpponentThread.stop();
        listener.sendMessageToServer(LibraryOfPrefixes.STOP_SEARCHING);
    }

}
