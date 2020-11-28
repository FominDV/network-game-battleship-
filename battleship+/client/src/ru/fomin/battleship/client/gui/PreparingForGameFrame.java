package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.map.Cell;
import ru.fomin.battleship.client.client_core.SearchOpponentThread;
import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.client.map.MapBuilder;
import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PreparingForGameFrame extends JFrame implements ActionListener {
    private final String INFO_ABOUT_GAME = "<html>1)Select the \"REMOVE\" or \"POST\" mode\"<br>2)Click on the field and click \"POST\" to place the ship" +
            "<br>3)Click on the ship for removing in the \"REMOVE\" mode" +
            "<br>4)If you want to cancel the selected cells, click to \"CANCEL ACTION\"<br>5)For exit to registration menu click to \"EXIT\"" +
            "<br>6)If you have saved maps, you can download the map by clicking \"LOAD THE MAP\"<br>7)You can only start the game by placing all the ships. To start the game, click \"START\"" +
            "<br>8)There should be an empty space around each ship<br>9)The game has 4 single-deck, 3 double-deck, 2 three-deck and 1 four-deck ships</html>";
    private final String INFO_ABOUT_DEVELOPER = "<html>Developer: Dmitriy Fomin<br>GitHub: https://github.com/FominDV <br> Email: 79067773397@yandex.ru</html>";
    private final String START = "START";
    private final String STOP = "<html>STOP SEARCHING<br>OPPONENT</html>";
    private final String REMOVE_MODE = "  REMOVE MODE";
    private final String POST_MODE = "     POST MODE";
    private final int SIZE_OF_MAP = 10;
    private final int WIDTH = 700;
    private final int HEIGHT = 600;
    private final String WINDOW_TITLE = "Map-Maker by ";
    private final SocketThread SOCKET_THREAD;
    private final String NICK_NAME;
    private final WorkingWithNetwork listener;
    private String opponentNickname = "empty";
    private SearchOpponentThread searchOpponentThread = null;
    private MapBuilder mapBuilder;
    private final Color COLOR_OF_BACKGROUND = new Color(99, 234, 234);
    private Color COLOR_OF_START = new Color(215, 99, 99);

    private final Font FONT_FOR_BUTTONS = new Font(Font.SERIF, Font.BOLD, 16);
    private final Font FONT_FOR_LABEL_SHIPS = new Font(Font.SERIF, Font.BOLD, 24);
    private final Font FONT_FOR_MODE = new Font(Font.SERIF, Font.BOLD, 30);

    private final JPanel PANEL_MAP = new JPanel(new GridLayout(SIZE_OF_MAP, SIZE_OF_MAP));
    private final JPanel WRAPPER_FOR_MAP = new JPanel(new GridBagLayout());
    private final JPanel PANEL_BOTTOM = new JPanel(new GridLayout(2, 3));
    private final JPanel PANEL_TOP = new JPanel(new GridLayout(1, 2));
    private final JPanel PANEL_LEFT_MAIN = new JPanel(new GridLayout(5, 1));
    private final JPanel PANEL_LEFT_TOP = new JPanel(new GridLayout(1, 1));
    private final JPanel PANEL_LEFT_BOTTOM = new JPanel(new GridLayout(4, 2));

    private final JButton BUTTON_POST = new JButton("POST");
    private final JButton BUTTON_REMOVE = new JButton("REMOVE");
    private final JButton BUTTON_CANCEL = new JButton("<html>CANCEL<br>ACTION</html>");
    private final JButton BUTTON_LOAD = new JButton("<html><p align='center'>LOAD<br>THE MAP</p></html>");
    private final JButton BUTTON_START = new JButton(START);
    private final JButton BUTTON_EXIT = new JButton("EXIT");

    private final JButton BUTTON_INFO = new JButton("INSTRUCTION MANUAL");
    private final JButton BUTTON_DEVELOPER_INFO = new JButton("DEVELOPER");

    private JLabel labelMode = new JLabel(POST_MODE);
    private final JLabel LABEL_SHIP_4_IMAGE = new JLabel();
    private final JLabel LABEL_SHIP_3_IMAGE = new JLabel();
    private final JLabel LABEL_SHIP_2_IMAGE = new JLabel();
    private final JLabel LABEL_SHIP_1_IMAGE = new JLabel();
    private JLabel labelCount4Ship = new JLabel();
    private JLabel labelCount3Ship = new JLabel();
    private JLabel labelCount2Ship = new JLabel();
    private JLabel labelCount1Ship = new JLabel();


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
        /*width and height of imager equal 40 and border of cell equal 1*/
        WRAPPER_FOR_MAP.setSize(SIZE_OF_MAP * 41, SIZE_OF_MAP * 41);
        fillMap();
        WRAPPER_FOR_MAP.add(PANEL_MAP);

        labelCount1Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelCount2Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelCount3Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelCount4Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelMode.setFont(FONT_FOR_MODE);
        mapBuilder.setCountOfShips();


        BUTTON_POST.setFont(FONT_FOR_BUTTONS);
        BUTTON_POST.setBackground(COLOR_OF_BACKGROUND);
        BUTTON_REMOVE.setFont(FONT_FOR_BUTTONS);
        BUTTON_REMOVE.setBackground(COLOR_OF_BACKGROUND);
        BUTTON_CANCEL.setFont(FONT_FOR_BUTTONS);
        BUTTON_CANCEL.setBackground(COLOR_OF_BACKGROUND);
        BUTTON_LOAD.setFont(FONT_FOR_BUTTONS);
        BUTTON_LOAD.setBackground(COLOR_OF_BACKGROUND);
        BUTTON_START.setFont(FONT_FOR_BUTTONS);
        BUTTON_START.setBackground(COLOR_OF_START);
        BUTTON_EXIT.setFont(FONT_FOR_BUTTONS);
        BUTTON_EXIT.setBackground(COLOR_OF_BACKGROUND);
        BUTTON_INFO.setFont(FONT_FOR_BUTTONS);
        BUTTON_DEVELOPER_INFO.setFont(FONT_FOR_BUTTONS);

        BUTTON_DEVELOPER_INFO.addActionListener(this);
        BUTTON_INFO.addActionListener(this);
        BUTTON_POST.addActionListener(this);
        BUTTON_REMOVE.addActionListener(this);
        BUTTON_CANCEL.addActionListener(this);
        BUTTON_LOAD.addActionListener(this);
        BUTTON_START.addActionListener(this);
        BUTTON_EXIT.addActionListener(this);

        PANEL_LEFT_TOP.add(labelMode);
        PANEL_LEFT_BOTTOM.add(LABEL_SHIP_4_IMAGE);
        PANEL_LEFT_BOTTOM.add(labelCount4Ship);
        PANEL_LEFT_BOTTOM.add(LABEL_SHIP_3_IMAGE);
        PANEL_LEFT_BOTTOM.add(labelCount3Ship);
        PANEL_LEFT_BOTTOM.add(LABEL_SHIP_2_IMAGE);
        PANEL_LEFT_BOTTOM.add(labelCount2Ship);
        PANEL_LEFT_BOTTOM.add(LABEL_SHIP_1_IMAGE);
        PANEL_LEFT_BOTTOM.add(labelCount1Ship);

        PANEL_LEFT_MAIN.add(PANEL_LEFT_TOP);
        PANEL_LEFT_MAIN.add(PANEL_LEFT_BOTTOM);

        PANEL_TOP.add(BUTTON_INFO);
        PANEL_TOP.add(BUTTON_DEVELOPER_INFO);
        PANEL_BOTTOM.add(BUTTON_POST);
        PANEL_BOTTOM.add(BUTTON_REMOVE);
        PANEL_BOTTOM.add(BUTTON_CANCEL);
        PANEL_BOTTOM.add(BUTTON_LOAD);
        PANEL_BOTTOM.add(BUTTON_START);
        PANEL_BOTTOM.add(BUTTON_EXIT);
        add(WRAPPER_FOR_MAP, BorderLayout.EAST);
        add(PANEL_BOTTOM, BorderLayout.SOUTH);
        add(PANEL_TOP, BorderLayout.NORTH);
        add(PANEL_LEFT_MAIN, BorderLayout.WEST);

        setVisible(true);
    }

    private void fillMap() {
        Cell[][] map = new Cell[SIZE_OF_MAP][SIZE_OF_MAP];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = new Cell(5);
                PANEL_MAP.add(map[i][j]);
            }
        }
        mapBuilder = new MapBuilder(map, this);
    }

    private void searchOpponent() {
        if (searchOpponentThread == null || !(searchOpponentThread.isAlive())) {
            listener.sendMessageToServer(LibraryOfPrefixes.MESSAGE_ABOUT_START_SEARCHING);
            searchOpponentThread = new SearchOpponentThread(this);
            BUTTON_START.setText(STOP);
        }
    }


    public void setOpponentNickname(String opponentNickname) {
        this.opponentNickname = opponentNickname;
    }

    public void setTitleAboutOpponent() {
        if (!opponentNickname.equals("empty"))
            setTitle(NICK_NAME + " VS " + opponentNickname);
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

    public void sendMessageForSearching() {
        listener.sendMessageToServer(LibraryOfPrefixes.getSearchOpponent(NICK_NAME));
    }

    public void stopSearching() {
        searchOpponentThread.interrupt();
        searchOpponentThread.stop();
        listener.sendMessageToServer(LibraryOfPrefixes.STOP_SEARCHING);
        BUTTON_START.setText(START);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BUTTON_DEVELOPER_INFO)) {
            showDeveloperInfo();
            return;
        }
        if (source.equals(BUTTON_INFO)) {
            showInstruction();
            return;
        }
        if (source.equals(BUTTON_EXIT)) {
            SOCKET_THREAD.close();
            return;
        }
    }

    private void showInstruction() {
        JOptionPane.showMessageDialog(null, INFO_ABOUT_GAME, "INSTRUCTION MANUAL", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDeveloperInfo() {
        JOptionPane.showMessageDialog(null, INFO_ABOUT_DEVELOPER, "INFORMATION ABOUT DEVELOPER", JOptionPane.INFORMATION_MESSAGE);
    }
    public void setCountOfShips(int ship1,int ship2,int ship3,int ship4){
        labelCount1Ship.setText("- "+ship1+"/4");
        labelCount2Ship.setText("- "+ship2+"/4");
        labelCount3Ship.setText("- "+ship3+"/4");
        labelCount4Ship.setText("- "+ship4+"/4");
    }
}
