package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.client.map.Cell;
import ru.fomin.battleship.client.map.MapBuilder;
import ru.fomin.battleship.common.LibraryOfPrefixes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnlineGameWindow extends JFrame implements ActionListener {
    private final String opponentNickname;
    private final String NICK_NAME;
    private String mapCodeOfUser;
    private String mapCodeOfOpponent="";
    private final int SIZE_OF_MAP;
    private WorkingWithNetwork listener;
    private MapBuilder mapBuilderOfUser;
    private MapBuilder mapBuilderOfOpponent;
    private final int WIDTH=1200;
    private final int HEIGHT=750;

    private final JPanel WRAPPER_FOR_MAP_OF_USER = new JPanel(new GridBagLayout());
    private final JPanel WRAPPER_FOR_MAP_OF_OPPONENT = new JPanel(new GridBagLayout());
    private JPanel PANEL_MAP_OF_USER;
    private JPanel PANEL_MAP_OF_OPPONENT;

    JButton BUTTON_SEND=new JButton("SEND MESSAGE");

    public OnlineGameWindow(String opponentNickname, String nickName, String mapCodeOfUser, WorkingWithNetwork listener, int sizeOfMap) {
        this.opponentNickname = opponentNickname;
        NICK_NAME = nickName;
        this.mapCodeOfUser = mapCodeOfUser;
        this.listener=listener;
        SIZE_OF_MAP=sizeOfMap;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        listener.sendMessageToServer(LibraryOfPrefixes.getMapCodeMessage(mapCodeOfUser));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(NICK_NAME + " VS " + opponentNickname);
        setResizable(false);
        int wrapperSize=SIZE_OF_MAP * 50;
        WRAPPER_FOR_MAP_OF_USER.setSize(wrapperSize, wrapperSize);
        WRAPPER_FOR_MAP_OF_OPPONENT.setSize(wrapperSize, wrapperSize);
        PANEL_MAP_OF_USER = new JPanel(new GridLayout(SIZE_OF_MAP + 1, SIZE_OF_MAP + 1));
        PANEL_MAP_OF_OPPONENT = new JPanel(new GridLayout(SIZE_OF_MAP + 1, SIZE_OF_MAP + 1));
        fillMap(PANEL_MAP_OF_USER, mapBuilderOfUser);
        fillMap(PANEL_MAP_OF_OPPONENT, mapBuilderOfOpponent);

        BUTTON_SEND.addActionListener(this);

        setVisible(true);
    }

    private void fillMap(JPanel panelMap, MapBuilder mapBuilder) {
        panelMap.add(new JLabel());
        for (int i = 1; i <= SIZE_OF_MAP; i++) {
            JLabel number = new JLabel((String.valueOf(i)));
            number.setHorizontalAlignment(SwingConstants.CENTER);
            panelMap.add(number);
        }
        Cell[][] map = new Cell[SIZE_OF_MAP][SIZE_OF_MAP];
        for (int i = 0; i < map.length; i++) {
            panelMap.add(new JLabel(String.valueOf(i + 1)));
            for (int j = 0; j < map.length; j++) {
                map[i][j] = new Cell(5, this, i, j);
                panelMap.add(map[i][j]);
            }
        }
        mapBuilder = new MapBuilder(map, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BUTTON_SEND)) {
            listener.sendMessageToServer(LibraryOfPrefixes.getChatMessage(""));
            return;
        }

        throw new RuntimeException("Unknown source: " + source);
    }

    public void setChatMessage(String message){

    }
    public void setMapCodeOfOpponent(String mapCodeOfOpponent){
        this.mapCodeOfOpponent=mapCodeOfOpponent;
    }
}
