package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.client.map.Cell;
import ru.fomin.battleship.client.map.OnlineGameMapBuilder;
import ru.fomin.battleship.common.LibraryOfPrefixes;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.sleep;

public class OnlineGameWindow extends JFrame implements ActionListener {
    private final String TEXT_TURN_OF_USER = "YOUR TURN";
    private final String TEXT_TURN_OF_OPPONENT = "TURN OF OPPONENT";

    private boolean isTurnOfUser = false;
    private final String opponentNickname;
    private final String NICK_NAME;
    private String mapCodeOfUser;
    private final int SIZE_OF_MAP;
    private WorkingWithNetwork listener;
    private OnlineGameMapBuilder mapBuilderOfUser;
    private OnlineGameMapBuilder mapBuilderOfOpponent;
    private final int WIDTH = 1440;
    private final int HEIGHT = 800;
    private final Color  COLOR_FOR_BORDER=new Color(162, 191, 234, 52);
    private final Border BORDER_FOR_LOG_AND_CHAT=BorderFactory.createLineBorder(COLOR_FOR_BORDER, 20);


    private final JPanel WRAPPER_FOR_MAP_OF_USER = new JPanel(new GridBagLayout());
    private final JPanel WRAPPER_FOR_MAP_OF_OPPONENT = new JPanel(new GridBagLayout());
    private JPanel panelMapOfUser;
    private JPanel panelMapOfOpponent;
    private final JPanel PANEL_MAIN_CENTER=new JPanel(new BorderLayout());
    private final JPanel PANEL_CENTER_OF_CENTER=new JPanel(new BorderLayout());
    private final JPanel PANEL_LOG_AND_CHAT=new JPanel(new GridLayout(2,1));


    private final JTextArea LOG = new JTextArea();
    private final JTextArea CHAT = new JTextArea();

    private final JLabel LABEL_TURN = new JLabel(TEXT_TURN_OF_OPPONENT);

    private final Font FONT_OF_TURN = new Font(Font.SERIF, Font.BOLD, 24);

    JButton BUTTON_SEND = new JButton("SEND MESSAGE");


    public OnlineGameWindow(String opponentNickname, String nickName, String mapCodeOfUser, WorkingWithNetwork listener, int sizeOfMap) {
        this.opponentNickname = opponentNickname;
        NICK_NAME = nickName;
        this.mapCodeOfUser = mapCodeOfUser;
        this.listener = listener;
        SIZE_OF_MAP = sizeOfMap;
        SwingUtilities.invokeLater(() -> initialization());
    }


    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(NICK_NAME + " VS " + opponentNickname);

        int wrapperSize = SIZE_OF_MAP * 50;
        WRAPPER_FOR_MAP_OF_USER.setSize(wrapperSize, wrapperSize);
        WRAPPER_FOR_MAP_OF_OPPONENT.setSize(wrapperSize, wrapperSize);
        int sizeOfPanelMap = SIZE_OF_MAP + 1;
        panelMapOfUser = new JPanel(new GridLayout(sizeOfPanelMap, sizeOfPanelMap));
        panelMapOfOpponent = new JPanel(new GridLayout(sizeOfPanelMap, sizeOfPanelMap));
        mapBuilderOfUser = new OnlineGameMapBuilder(fillMap(panelMapOfUser), this);
        mapBuilderOfOpponent = new OnlineGameMapBuilder(fillMap(panelMapOfOpponent), this);
        mapBuilderOfUser.loadMap(mapCodeOfUser);
        WRAPPER_FOR_MAP_OF_USER.add(panelMapOfUser);
        WRAPPER_FOR_MAP_OF_OPPONENT.add(panelMapOfOpponent);

        LABEL_TURN.setFont(FONT_OF_TURN);
        LABEL_TURN.setHorizontalAlignment(SwingConstants.CENTER);
        LOG.setEditable(false);
        LOG.setLineWrap(true);
        LOG.setWrapStyleWord(true);
        CHAT.setEditable(false);
        CHAT.setLineWrap(true);
        CHAT.setWrapStyleWord(true);
        LOG.setBorder(BORDER_FOR_LOG_AND_CHAT);
        CHAT.setBorder(BORDER_FOR_LOG_AND_CHAT);
        JScrollPane scrollLog = new JScrollPane(LOG);
        JScrollPane scrollChat = new JScrollPane(CHAT);
        PANEL_LOG_AND_CHAT.add(scrollLog);
        PANEL_LOG_AND_CHAT.add(scrollChat);
        PANEL_CENTER_OF_CENTER.add(LABEL_TURN,BorderLayout.NORTH);
        PANEL_CENTER_OF_CENTER.add(PANEL_LOG_AND_CHAT,BorderLayout.CENTER);
        PANEL_CENTER_OF_CENTER.setMaximumSize(new Dimension(wrapperSize,wrapperSize-50));

        PANEL_MAIN_CENTER.add(WRAPPER_FOR_MAP_OF_USER,BorderLayout.WEST);
        PANEL_MAIN_CENTER.add(PANEL_CENTER_OF_CENTER,BorderLayout.CENTER);
        PANEL_MAIN_CENTER.add(WRAPPER_FOR_MAP_OF_OPPONENT,BorderLayout.EAST);



        BUTTON_SEND.addActionListener(this);


        add(PANEL_MAIN_CENTER,BorderLayout.CENTER);
        setVisible(true);

    }


    private Cell[][] fillMap(JPanel panelMap) {
        panelMap.add(new JLabel());
        for (int i = 1; i <= SIZE_OF_MAP; i++) {
            JLabel number = new JLabel((String.valueOf(i)));
            number.setHorizontalAlignment(SwingConstants.CENTER);
            panelMap.add(number);
        }
        Cell[][] map = new Cell[SIZE_OF_MAP][SIZE_OF_MAP];
        for (int i = 0; i < map.length; i++) {
            JLabel number = new JLabel((String.valueOf(i + 1)));
            number.setHorizontalAlignment(SwingConstants.CENTER);
            panelMap.add(number);
            for (int j = 0; j < map.length; j++) {
                if (panelMap == panelMapOfUser) map[i][j] = new Cell(5, this, i, j, false);
                else map[i][j] = new Cell(1, this, i, j, true);

                panelMap.add(map[i][j]);
            }
        }
        return map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == BUTTON_SEND) {
            listener.sendMessageToServer(LibraryOfPrefixes.getChatMessage(""));
            return;
        }

        throw new RuntimeException("Unknown source: " + source);
    }

    public void setChatMessage(String message) {

    }


    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public boolean getTurnOfUser() {
        return isTurnOfUser;
    }

    public void setTurnOfUser(String codeNumberOfTurn) {
        if (codeNumberOfTurn.equals("1")) {
            isTurnOfUser = true;
            LABEL_TURN.setText(TEXT_TURN_OF_USER);
        } else {
            isTurnOfUser = false;
            LABEL_TURN.setText(TEXT_TURN_OF_OPPONENT);
        }
    }

    public void changeTurn() {
        if (isTurnOfUser) {
            isTurnOfUser = false;
            LABEL_TURN.setText(TEXT_TURN_OF_OPPONENT);
            listener.sendMessageToServer(LibraryOfPrefixes.CHANGE_TURN);
        } else {
            isTurnOfUser = true;
            LABEL_TURN.setText(TEXT_TURN_OF_USER);
        }
    }
}
