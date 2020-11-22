package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.map.Cell;
import ru.fomin.battleship.client.client_core.SearchOpponentThread;
import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.client.map.MapBuilder;
import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;

import javax.swing.*;
import java.awt.*;

public class PreparingForGameFrame extends JFrame {
    private final int SIZE_OF_MAP=10;
    private final int WIDTH = 800;
    private final int HEIGHT = 500;
    private final String WINDOW_TITLE = "Map-Maker by ";
    private final SocketThread SOCKET_THREAD;
    private final String NICK_NAME;
    private final WorkingWithNetwork listener;
    private String opponentNickname = "empty";
    private SearchOpponentThread searchOpponentThread=null;
    private MapBuilder mapBuilder;
    private final Color COLOR_OF_BACKGROUND=new Color(0,128,128);
    
    private final JPanel PANEL_MAP=new JPanel(new GridLayout(SIZE_OF_MAP, SIZE_OF_MAP));
    private final JPanel WRAPPER_FOR_MAP=new JPanel(new GridBagLayout());
    private final JPanel PANEL_BOTTOM=new JPanel(new GridLayout(2,3));



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
        WRAPPER_FOR_MAP.setSize(400,400);
        fillMap();
        WRAPPER_FOR_MAP.add(PANEL_MAP);
        add(WRAPPER_FOR_MAP, BorderLayout.EAST);
        add(PANEL_BOTTOM, BorderLayout.NORTH);

        setVisible(true);
    }

    private void fillMap() {
        Cell[][] map=new Cell[SIZE_OF_MAP][SIZE_OF_MAP];
        for(int i=0; i<map.length;i++){
            for(int j=0;j<map.length;j++){
                map[i][j]=new Cell(5);
                PANEL_MAP.add(map[i][j]);
            }
        }
        mapBuilder=new MapBuilder(map, this);
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
