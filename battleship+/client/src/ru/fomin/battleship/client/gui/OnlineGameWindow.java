package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithHandler;
import ru.fomin.battleship.client.map.Cell;
import ru.fomin.battleship.client.map.OnlineGameMapBuilder;
import static ru.fomin.battleship.common.LibraryOfPrefixes.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static java.lang.String.format;

public class OnlineGameWindow extends JFrame implements ActionListener {
    /*0-simple shoot
     * 1-volley shoot
     * 2-exploration of the map
     * 3-shooting on straight*/
    private int modeStatus = 0;
    private int pastMode;
    private int[] pastUsingCellForActionCoordinates = new int[2];
    private int turnsForVolley = 4;
    private int turnsForExploration = 3;
    private int turnsForShootingOnStraight = 2;
    private int rechargeForVolley = 0;
    private int rechargeForExploration = 0;
    private int rechargeForStraightShooting = 0;
    private boolean isNotBlockVolleyMode = true;
    private boolean isNotBlockExplorationMode = true;
    private boolean isNotBlockShootingOnStraightMode = true;

    private String messageForLog;
    private String lastPartOfMessageForLog = "";
    private final String INSTRUCTION = "<html>1)For win you should destroy all ships of opponent<br>" +
            "2)Before making any action, you should choose the mode of action<br>" +
            "3)You can select the action mode by pressing the buttons on the top panel<br>" +
            "4)Modes of the volley shoot, exploration of the map and shooting on straight should be recharged for selecting it<br>" +
            "5)For action you should click on active cell of your opponent's map<br>" +
            "6)Mode of the volley shoot shoots in selected cell and four random cells around this cell<br>" +
            "7)Mode of the exploration of the map shows for you selected cell and all cells around it<br>" +
            "8)Mode of the shooting in straight shoots in selected cell and two " +
            "random cells on random straight where selected cell is <br>" +
            "9)Mode of the simple shoot shoots only in selected cell<br>" +
            "10)If you destroy or damage ship of opponent, you action again<br>" +
            "11)If you used any mode other than simple shoot and destroyed or damaged ship of opponent, then you don't action again anyway<br>" +
            "12)By using mode of volley shoot any random cells can be already shot before<br>" +
            "13)By using mode of shooting on straight if random straight have not two unknown cells," +
            " another straight will be selected. If two straights have not two unknown cells, only one or zero cells will be shot<br>" +
            "14)For recharge of volley shoot " + turnsForVolley + " game turns is needed<br>" +
            "15)For recharge of exploration of the map " + turnsForExploration + " game turns is needed<br>" +
            "16)For recharge of shooting on straight " + turnsForShootingOnStraight + " game turns is needed<br>" +
            "17)If you lose a four-deck ship, you can no longer use the mode of volley shoot<br>" +
            "18)If you lose a three-deck ship recharge of the mode of exploration of the map will be increased on two game turns<br>" +
            "19)If you lose all three-deck ships, you can no longer use the mode of exploration of the map<br>" +
            "20)If you lose a two-deck ship recharge of the mode of shooting on straight will be increased on one game turn<br>" +
            "21)If you lose all two-deck ships, you can no longer use the mode of shooting on straight<br>" +
            "22)If you lose a one-deck ship recharge of any special mode will be increased on one game turn<br>" +
            "23)For exit to map-builder you should click the button 'EXIT'<br>" +
            "24)For send message into chat you should print your message in the field and press 'enter' or button 'SEND'<br></html>";
    private final String TEXT_TURN_OF_USER = "YOUR TURN";
    private final String TEXT_TURN_OF_OPPONENT = "TURN OF OPPONENT";
    private final String TEXT_MODE_SIMPLE_SHOOT = "<html><p align='center'>MODE:<br>SIMPLE SHOOT</p></html>";
    private final String TEXT_MODE_VOLLEY_SHOOT = "<html><p align='center'>MODE:<br>VOLLEY SHOOT</p></html>";
    private final String TEXT_MODE_EXPLORATION = "<html><p align='center'>MODE:<br>EXPLORATION OF THE MAP</p></html>";
    private final String TEXT_MODE_STRAIGHT_SHOOTING = "<html><p align='center'>MODE:<br>SHOOTING ON STRAIGHT</p></html>";
    private final String TEXT_BLOCK_MODE = "lost";
    private final String TEXT_RECHARGE1 = "recharge in ";
    private final String TEXT_RECHARGE2 = " turns";

    private boolean isPlayAgain = false;
    private boolean isTurnOfUser = false;
    private final String opponentNickname;
    private final String NICK_NAME;
    private String mapCodeOfUser;
    private final int SIZE_OF_MAP;
    private WorkingWithHandler listener;
    private OnlineGameMapBuilder mapBuilderOfUser;
    private OnlineGameMapBuilder mapBuilderOfOpponent;
    private final int WIDTH = 1440;
    private final int HEIGHT = 750;

    private final Color COLOR_FOR_BORDER_LOG = new Color(162, 191, 234, 52);
    private final Color COLOR_FOR_ACTIVE_MODE = new Color(57, 205, 41, 173);
    private final Color COLOR_FOR_NO_ACTIVE_MODE = new Color(215, 34, 34, 233);
    private final Color COLOR_FOR_EXIT = new Color(241, 83, 83, 233);
    private final Color COLOR_FOR_LABEL_MODE = new Color(106, 4, 10, 191);
    private final Color COLOR_FOR_BORDER_MODE = new Color(1, 23, 95, 233);
    private final Color COLOR_FOR_ENDING = new Color(187, 52, 0, 233);

    private final Border BORDER_FOR_LOG_AND_CHAT = BorderFactory.createLineBorder(COLOR_FOR_BORDER_LOG, 20);
    private final Border BORDER_FOR_LABEL_MODE = BorderFactory.createLineBorder(COLOR_FOR_BORDER_MODE, 10);
    private final Border BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE = BorderFactory.createLineBorder(COLOR_FOR_NO_ACTIVE_MODE, 8);
    private final Border BORDER_FOR_LABEL_RECHARGE_ACTIVE = BorderFactory.createLineBorder(COLOR_FOR_ACTIVE_MODE, 8);

    private final JPanel WRAPPER_FOR_MAP_OF_USER = new JPanel(new GridBagLayout());
    private final JPanel WRAPPER_FOR_MAP_OF_OPPONENT = new JPanel(new GridBagLayout());
    private JPanel panelMapOfUser;
    private JPanel panelMapOfOpponent;
    private final JPanel PANEL_MAIN_CENTER = new JPanel(new FlowLayout());
    private final JPanel PANEL_CENTER_OF_CENTER = new JPanel(new BorderLayout());
    private final JPanel PANEL_LOG_AND_CHAT = new JPanel(new GridLayout(2, 1));
    private final JPanel PANEL_MAIN_TOP = new JPanel(new BorderLayout());
    private final JPanel PANEL_INFO_TOP = new JPanel(new GridLayout(1, 2));
    private final JPanel PANEL_ACTION_MENU_TOP = new JPanel(new GridLayout(1, 5));
    private final JPanel[] PANEL_MODE_TOP = new JPanel[3];
    private final JPanel PANEL_BOTTOM = new JPanel(new BorderLayout());
    private final JPanel PANEL_OF_ENDING = new JPanel(new GridLayout(2, 1));


    private final JTextArea LOG = new JTextArea();
    private final JTextArea CHAT = new JTextArea();
    private final JTextField FIELD_FOR_CHAT_MESSAGE = new JTextField();

    private final JLabel LABEL_TURN = new JLabel(TEXT_TURN_OF_OPPONENT);
    private final JLabel LABEL_MODE = new JLabel(TEXT_MODE_SIMPLE_SHOOT);
    private final JLabel LABEL_RECHARGE_VOLLEY = new JLabel(TEXT_RECHARGE1 + (turnsForVolley - rechargeForVolley) + TEXT_RECHARGE2);
    private final JLabel LABEL_RECHARGE_EXPLORATION = new JLabel(TEXT_RECHARGE1 + (turnsForExploration - rechargeForExploration) + TEXT_RECHARGE2);
    private final JLabel LABEL_RECHARGE_STRAIGHT = new JLabel(TEXT_RECHARGE1 + (turnsForShootingOnStraight - rechargeForStraightShooting) + TEXT_RECHARGE2);
    private final JLabel LABEL_OF_ENDING = new JLabel();

    private final Font FONT_OF_TURN = new Font(Font.SERIF, Font.BOLD, 24);
    private final Font FONT_OF_MODE = new Font(Font.SERIF, Font.BOLD, 26);
    private final Font FONT_OF_RECHARGE = new Font(Font.SERIF, Font.BOLD, 18);
    private final Font FONT_OF_BUTTON_MODE = new Font(Font.SERIF, Font.BOLD, 25);
    private final Font FONT_OF_BUTTON_BOTTOM = new Font(Font.SERIF, Font.BOLD, 22);
    private final Font FONT_OF_BUTTON_INFO = new Font(Font.SERIF, Font.BOLD, 19);
    private final Font FONT_OF_FIELD_FOR_CHAT_MESSAGE = new Font(Font.SERIF, Font.ITALIC, 20);
    private final Font FONT_OF_CHAT = new Font(Font.SERIF, Font.ITALIC, 16);
    private final Font FONT_OF_BUTTON_PLAY_AGAIN = new Font(Font.SERIF, Font.BOLD, 26);
    private final Font FONT_OF_LABEL_ENDING = new Font(Font.SERIF, Font.BOLD, 46);

    private final JButton BUTTON_SEND = new JButton("SEND MESSAGE");
    private final JButton BUTTON_INSTRUCTION = new JButton("INSTRUCTION MANUAL");
    private final JButton BUTTON_DEVELOPER_INFO = new JButton("DEVELOPER");
    private final JButton BUTTON_MODE_SIMPLE = new JButton("<html>SIMPLE<br>SHOOT</html>");
    private final JButton BUTTON_MODE_VOLLEY = new JButton("<html>VOLLEY<br>SHOOT</html>");
    private final JButton BUTTON_MODE_EXPLORATION = new JButton("<html>EXPLORATION<br>OF THE MAP</html>");
    private final JButton BUTTON_MODE_STRAIGHT = new JButton("<html>SHOOTING<br>ON STRAIGHT</html>");
    private final JButton BUTTON_EXIT = new JButton("EXIT");


    public OnlineGameWindow(String opponentNickname, String nickName, String mapCodeOfUser, WorkingWithHandler listener, int sizeOfMap) {
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
        setResizable(false);
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
        CHAT.setFont(FONT_OF_CHAT);
        FIELD_FOR_CHAT_MESSAGE.setFont(FONT_OF_FIELD_FOR_CHAT_MESSAGE);
        FIELD_FOR_CHAT_MESSAGE.addActionListener(this);
        JScrollPane scrollLog = new JScrollPane(LOG);
        JScrollPane scrollChat = new JScrollPane(CHAT);
        scrollLog.setBorder(BORDER_FOR_LOG_AND_CHAT);
        scrollChat.setBorder(BORDER_FOR_LOG_AND_CHAT);
        PANEL_LOG_AND_CHAT.add(scrollLog);
        PANEL_LOG_AND_CHAT.add(scrollChat);
        PANEL_CENTER_OF_CENTER.setPreferredSize(new Dimension(wrapperSize - 150, wrapperSize));
        PANEL_CENTER_OF_CENTER.add(LABEL_TURN, BorderLayout.NORTH);
        PANEL_CENTER_OF_CENTER.add(PANEL_LOG_AND_CHAT, BorderLayout.CENTER);
        PANEL_CENTER_OF_CENTER.setMaximumSize(new Dimension(wrapperSize, wrapperSize - 50));
        PANEL_MAIN_CENTER.add(WRAPPER_FOR_MAP_OF_USER);
        PANEL_MAIN_CENTER.add(PANEL_CENTER_OF_CENTER);
        PANEL_MAIN_CENTER.add(WRAPPER_FOR_MAP_OF_OPPONENT);

        LABEL_TURN.setFont(FONT_OF_TURN);
        LABEL_MODE.setFont(FONT_OF_MODE);
        LABEL_RECHARGE_VOLLEY.setFont(FONT_OF_RECHARGE);
        LABEL_RECHARGE_EXPLORATION.setFont(FONT_OF_RECHARGE);
        LABEL_RECHARGE_STRAIGHT.setFont(FONT_OF_RECHARGE);
        LABEL_TURN.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_MODE.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_RECHARGE_VOLLEY.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_RECHARGE_EXPLORATION.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_RECHARGE_STRAIGHT.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_MODE.setForeground(COLOR_FOR_LABEL_MODE);
        LABEL_MODE.setBorder(BORDER_FOR_LABEL_MODE);
        LABEL_RECHARGE_VOLLEY.setBorder(BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE);
        LABEL_RECHARGE_EXPLORATION.setBorder(BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE);
        LABEL_RECHARGE_STRAIGHT.setBorder(BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE);

        BUTTON_SEND.setFont(FONT_OF_BUTTON_BOTTOM);
        BUTTON_INSTRUCTION.setFont(FONT_OF_BUTTON_INFO);
        BUTTON_DEVELOPER_INFO.setFont(FONT_OF_BUTTON_INFO);
        BUTTON_MODE_SIMPLE.setFont(FONT_OF_BUTTON_MODE);
        BUTTON_MODE_VOLLEY.setFont(FONT_OF_BUTTON_MODE);
        BUTTON_MODE_EXPLORATION.setFont(FONT_OF_BUTTON_MODE);
        BUTTON_MODE_STRAIGHT.setFont(FONT_OF_BUTTON_MODE);
        BUTTON_EXIT.setFont(FONT_OF_BUTTON_BOTTOM);

        BUTTON_EXIT.setBackground(COLOR_FOR_EXIT);
        BUTTON_MODE_SIMPLE.setBackground(COLOR_FOR_ACTIVE_MODE);
        BUTTON_MODE_VOLLEY.setBackground(COLOR_FOR_NO_ACTIVE_MODE);
        BUTTON_MODE_EXPLORATION.setBackground(COLOR_FOR_NO_ACTIVE_MODE);
        BUTTON_MODE_STRAIGHT.setBackground(COLOR_FOR_NO_ACTIVE_MODE);

        BUTTON_SEND.addActionListener(this);
        BUTTON_INSTRUCTION.addActionListener(this);
        BUTTON_DEVELOPER_INFO.addActionListener(this);
        BUTTON_MODE_SIMPLE.addActionListener(this);
        BUTTON_MODE_VOLLEY.addActionListener(this);
        BUTTON_MODE_EXPLORATION.addActionListener(this);
        BUTTON_MODE_STRAIGHT.addActionListener(this);
        BUTTON_EXIT.addActionListener(this);

        for (int i = 0; i < PANEL_MODE_TOP.length; i++) PANEL_MODE_TOP[i] = new JPanel(new BorderLayout());
        PANEL_MODE_TOP[0].add(BUTTON_MODE_VOLLEY, BorderLayout.CENTER);
        PANEL_MODE_TOP[0].add(LABEL_RECHARGE_VOLLEY, BorderLayout.SOUTH);
        PANEL_MODE_TOP[1].add(BUTTON_MODE_EXPLORATION, BorderLayout.CENTER);
        PANEL_MODE_TOP[1].add(LABEL_RECHARGE_EXPLORATION, BorderLayout.SOUTH);
        PANEL_MODE_TOP[2].add(BUTTON_MODE_STRAIGHT, BorderLayout.CENTER);
        PANEL_MODE_TOP[2].add(LABEL_RECHARGE_STRAIGHT, BorderLayout.SOUTH);
        PANEL_INFO_TOP.add(BUTTON_INSTRUCTION);
        PANEL_INFO_TOP.add(BUTTON_DEVELOPER_INFO);
        PANEL_ACTION_MENU_TOP.add(PANEL_MODE_TOP[0]);
        PANEL_ACTION_MENU_TOP.add(PANEL_MODE_TOP[1]);
        PANEL_ACTION_MENU_TOP.add(PANEL_MODE_TOP[2]);
        PANEL_ACTION_MENU_TOP.add(BUTTON_MODE_SIMPLE);
        PANEL_ACTION_MENU_TOP.add(LABEL_MODE);
        PANEL_MAIN_TOP.add(PANEL_INFO_TOP, BorderLayout.NORTH);
        PANEL_MAIN_TOP.add(PANEL_ACTION_MENU_TOP, BorderLayout.CENTER);

        PANEL_BOTTOM.add(BUTTON_EXIT, BorderLayout.WEST);
        PANEL_BOTTOM.add(BUTTON_SEND, BorderLayout.EAST);
        PANEL_BOTTOM.add(FIELD_FOR_CHAT_MESSAGE, BorderLayout.CENTER);

        add(PANEL_MAIN_CENTER, BorderLayout.CENTER);
        add(PANEL_MAIN_TOP, BorderLayout.NORTH);
        add(PANEL_BOTTOM, BorderLayout.SOUTH);
        setVisible(true);

    }

    public void setPastUsingCellForActionCoordinates(int[] pastUsingCellForActionCoordinates) {
        this.pastUsingCellForActionCoordinates = pastUsingCellForActionCoordinates;
    }

    public int[] getPastUsingCellForActionCoordinates() {
        return pastUsingCellForActionCoordinates;
    }

    public int getModeStatus() {
        return modeStatus;
    }

    public void changePastMode() {
        pastMode = modeStatus;
    }

    public int getPastMode() {
        return pastMode;
    }

    public void appendIntoLog(String message) {
        LOG.append(message + "\n");
        LOG.setCaretPosition(LOG.getDocument().getLength());
    }

    public void appendIntoLog(String message, boolean isActionAgain) {
        message = NICK_NAME + ":\n*Used mode of " + createMessageAboutMode() + "\n" + message + lastPartOfMessageForLog + createMessageAboutNextTurn(isActionAgain);
        lastPartOfMessageForLog = "";
        LOG.append(message + "\n");
        LOG.setCaretPosition(LOG.getDocument().getLength());
        listener.sendMessageToServer(getLogMessage(message));
    }

    private String createMessageAboutNextTurn(boolean isActionAgain) {
        if (isActionAgain) return "*Can action again";
        else return "*End game turn";
    }

    public void setMessageForLog(String messageForLog) {
        this.messageForLog = messageForLog;
    }

    public String getMessageForLog() {
        return messageForLog;
    }

    private String createMessageAboutMode() {
        switch (pastMode) {
            case 0:
                return format("'simple shoot' (%s;%s)", pastUsingCellForActionCoordinates[0] + 1, pastUsingCellForActionCoordinates[1] + 1);
            case 1:
                return format("'volley shoot' (%s;%s)", pastUsingCellForActionCoordinates[0] + 1, pastUsingCellForActionCoordinates[1] + 1);
            case 2:
                return format("'exploration of the map' (%s;%s)", pastUsingCellForActionCoordinates[0] + 1, pastUsingCellForActionCoordinates[1] + 1);
            case 3:
                return format("'shooting on straight' (%s;%s)", pastUsingCellForActionCoordinates[0] + 1, pastUsingCellForActionCoordinates[1] + 1);
            default:
                return "";
        }
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
        if (source == BUTTON_SEND || source == FIELD_FOR_CHAT_MESSAGE) {
            String message = FIELD_FOR_CHAT_MESSAGE.getText();
            if (!message.equals("")) {
                sendMessageIntoChat(message);
                FIELD_FOR_CHAT_MESSAGE.setText("");
            }
            return;
        }
        if (source == BUTTON_INSTRUCTION) {
            showInfoMessage(INSTRUCTION, "INSTRUCTION MANUAL");
            return;
        }
        if (source == BUTTON_DEVELOPER_INFO) {
            PreparingForGameFrame.showDeveloperInfo();
            return;
        }
        if (source == BUTTON_EXIT) {
            if (isConfirmMessage("Are you sure to want exit to map-builder from this game?")) {
                exitToMapBuilder();
            }
            return;
        }
        if (source == BUTTON_MODE_VOLLEY) {
            if (isActiveVolleyMode()) {
                modeStatus = 1;
                LABEL_MODE.setText(TEXT_MODE_VOLLEY_SHOOT);
            }
            return;
        }
        if (source == BUTTON_MODE_EXPLORATION) {
            if (isActiveExplorationMode()) {
                modeStatus = 2;
                LABEL_MODE.setText(TEXT_MODE_EXPLORATION);
            }
            return;
        }
        if (source == BUTTON_MODE_STRAIGHT) {
            if (isActiveShootingOnStraightMode()) {
                modeStatus = 3;
                LABEL_MODE.setText(TEXT_MODE_STRAIGHT_SHOOTING);
            }
            return;
        }
        if (source == BUTTON_MODE_SIMPLE) {
            setModeSimpleShoot();
            return;
        }

        throw new RuntimeException("Unknown source: " + source);
    }

    private void setModeSimpleShoot() {
        modeStatus = 0;
        LABEL_MODE.setText(TEXT_MODE_SIMPLE_SHOOT);
    }

    private void exitToMapBuilder() {
        listener.exitToMapBuilder();
    }

    private boolean isConfirmMessage(String question) {
        if (JOptionPane.showConfirmDialog(null, question, "CONFIRM WINDOW", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
            return true;
        else return false;
    }

    public void setChatMessage(String message) {
        CHAT.append(message + "\n");
        CHAT.setCaretPosition(CHAT.getDocument().getLength());
    }

    private void sendMessageIntoChat(String message) {
        listener.sendMessageToServer(getChatMessage(message));
        Date date = new Date();
        CHAT.append(format("YOU(%tR):\n%s\n", date, message));
        CHAT.setCaretPosition(CHAT.getDocument().getLength());
        FIELD_FOR_CHAT_MESSAGE.setText("");
        FIELD_FOR_CHAT_MESSAGE.grabFocus();
    }


    private void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
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
        } else {
            isTurnOfUser = true;
            LABEL_TURN.setText(TEXT_TURN_OF_USER);
        }
    }

    public void sendMessageAboutChangeTurn() {
        listener.sendMessageToServer(CHANGE_TURN);
        increaseRechargingPoints();
    }

    public void sendCodeOfGameTurn(String codeOfGameTurn) {
        listener.sendMessageToServer(getCodeOfGameTurnMessage(codeOfGameTurn));
    }

    public void processDataOfOpponentTurn(String codeOfGameTurn) {
        mapBuilderOfUser.processDataOfOpponentTurn(codeOfGameTurn);
    }

    public void sendCodeResultOfGameTurn(String codeOfTurnResult) {
        listener.sendMessageToServer(getCodeResultOfTurn(codeOfTurnResult));
    }

    public void processDataOfResultTurn(String codeOfResultTurn) {
        mapBuilderOfOpponent.processDataOfResultTurn(codeOfResultTurn);
    }

    public void setLastPartOfMessageForLog(String message) {
        lastPartOfMessageForLog = message;
    }

    private void sendMessageAboutLostModeForLog(String modeName) {
        String message = String.format("*%s lost mode of '%s'\n", NICK_NAME, modeName);
        listener.sendMessageToServer(getLogLastPartMessage(message));
    }

    public void blockTurnsForVolley() {
        rechargeForVolley = 0;
        isNotBlockVolleyMode = false;
        changeButtonModeVolley();
        LABEL_RECHARGE_VOLLEY.setText(TEXT_BLOCK_MODE);
        sendMessageAboutLostModeForLog("volley shoot");
    }

    public void blockTurnsForExploration() {
        rechargeForExploration = 0;
        isNotBlockExplorationMode = false;
        changeButtonModeExploration();
        LABEL_RECHARGE_EXPLORATION.setText(TEXT_BLOCK_MODE);
        sendMessageAboutLostModeForLog("exploration of the map");
    }

    public void blockTurnsForShootingOnStraight() {
        rechargeForStraightShooting = 0;
        isNotBlockShootingOnStraightMode = false;
        changeButtonModeShootingOnStraight();
        LABEL_RECHARGE_STRAIGHT.setText(TEXT_BLOCK_MODE);
        sendMessageAboutLostModeForLog("shooting on straight");
    }

    public void changeTurnsForExploration() {
        if (rechargeForExploration > turnsForExploration)
            rechargeForExploration = turnsForExploration;
        turnsForExploration += 2;
        changeButtonModeExploration();
    }

    public void changeTurnsForShootingOnStraight() {
        if (rechargeForStraightShooting > turnsForShootingOnStraight)
            rechargeForStraightShooting = turnsForShootingOnStraight;
        turnsForShootingOnStraight++;
        changeButtonModeShootingOnStraight();
    }

    public void changeTurnsForAllShootingModes() {
        if (isNotBlockVolleyMode) {
            if (rechargeForVolley > turnsForVolley)
                rechargeForVolley = turnsForVolley;
            turnsForVolley++;
            changeButtonModeVolley();
        }
        if (isNotBlockExplorationMode) {
            if (rechargeForExploration > turnsForExploration)
                rechargeForExploration = turnsForExploration;
            turnsForExploration++;
            changeButtonModeExploration();
        }
        if (isNotBlockShootingOnStraightMode) {
            if (rechargeForStraightShooting > turnsForShootingOnStraight)
                rechargeForStraightShooting = turnsForShootingOnStraight;
            turnsForShootingOnStraight++;
            changeButtonModeShootingOnStraight();
        }
    }

    public void increaseRechargingPoints() {
        if (isNotBlockVolleyMode) {
            rechargeForVolley++;
            changeButtonModeVolley();
        }
        if (isNotBlockExplorationMode) {
            rechargeForExploration++;
            changeButtonModeExploration();
        }
        if (isNotBlockShootingOnStraightMode) {
            rechargeForStraightShooting++;
            changeButtonModeShootingOnStraight();

        }
    }

    private boolean isActiveVolleyMode() {
        if (turnsForVolley <= rechargeForVolley && isNotBlockVolleyMode) return true;
        else return false;
    }

    private boolean isActiveExplorationMode() {
        if (turnsForExploration <= rechargeForExploration && isNotBlockExplorationMode) return true;
        else return false;
    }

    private boolean isActiveShootingOnStraightMode() {
        if (turnsForShootingOnStraight <= rechargeForStraightShooting && isNotBlockShootingOnStraightMode) return true;
        else return false;
    }

    private void changeButtonModeVolley() {
        if (isActiveVolleyMode()) {
            BUTTON_MODE_VOLLEY.setBackground(COLOR_FOR_ACTIVE_MODE);
            LABEL_RECHARGE_VOLLEY.setBorder(BORDER_FOR_LABEL_RECHARGE_ACTIVE);
        } else {
            if (modeStatus == 1) setModeSimpleShoot();
            BUTTON_MODE_VOLLEY.setBackground(COLOR_FOR_NO_ACTIVE_MODE);
            LABEL_RECHARGE_VOLLEY.setBorder(BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE);
        }
        LABEL_RECHARGE_VOLLEY.setText(createTextAboutRecharge(rechargeForVolley, turnsForVolley));
    }

    private void changeButtonModeExploration() {
        if (isActiveExplorationMode()) {
            BUTTON_MODE_EXPLORATION.setBackground(COLOR_FOR_ACTIVE_MODE);
            LABEL_RECHARGE_EXPLORATION.setBorder(BORDER_FOR_LABEL_RECHARGE_ACTIVE);
        } else {
            if (modeStatus == 2) setModeSimpleShoot();
            BUTTON_MODE_EXPLORATION.setBackground(COLOR_FOR_NO_ACTIVE_MODE);
            LABEL_RECHARGE_EXPLORATION.setBorder(BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE);
        }
        LABEL_RECHARGE_EXPLORATION.setText(createTextAboutRecharge(rechargeForExploration, turnsForExploration));
    }

    private void changeButtonModeShootingOnStraight() {
        if (isActiveShootingOnStraightMode()) {
            BUTTON_MODE_STRAIGHT.setBackground(COLOR_FOR_ACTIVE_MODE);
            LABEL_RECHARGE_STRAIGHT.setBorder(BORDER_FOR_LABEL_RECHARGE_ACTIVE);
        } else {
            if (modeStatus == 3) setModeSimpleShoot();
            BUTTON_MODE_STRAIGHT.setBackground(COLOR_FOR_NO_ACTIVE_MODE);
            LABEL_RECHARGE_STRAIGHT.setBorder(BORDER_FOR_LABEL_RECHARGE_NO_ACTIVE);
        }
        LABEL_RECHARGE_STRAIGHT.setText(createTextAboutRecharge(rechargeForStraightShooting, turnsForShootingOnStraight));
    }

    private String createTextAboutRecharge(int rechargePoints, int turnsForActivated) {
        int deltaGameTurns;
        if (turnsForActivated - rechargePoints < 0) deltaGameTurns = 0;
        else deltaGameTurns = turnsForActivated - rechargePoints;
        return TEXT_RECHARGE1 + deltaGameTurns + TEXT_RECHARGE2;
    }

    public void changeAllButtonsAfterAction(){
        //set "-1" because after turn of this user all shooting modes will be increased by "1"
        switch (modeStatus) {
            case 1:
                rechargeForVolley = -1;
                changeButtonModeVolley();
                break;
            case 2:
                rechargeForExploration = -1;
                changeButtonModeExploration();
                break;
            case 3:
                rechargeForStraightShooting = -1;
                changeButtonModeShootingOnStraight();
        }
    }
    public void createCodeCellsOfAction(int x, int y, int typeOfAction) {
        mapBuilderOfOpponent.createCodeCellsOfAction(x, y, modeStatus, typeOfAction);
        setModeSimpleShoot();
    }

    public void gameIsLost() {
        listener.sendMessageToServer(VICTORY);
    }

    public void victory() {
        mapBuilderOfOpponent.openSpaceCells();
        listener.sendMessageToServer(getCodeOfMapAfterGameMessage(mapBuilderOfUser.getCodeOfMapAfterGame()));
        SwingUtilities.invokeLater(() -> showEndOfTheGame(1));
    }

    public void openMapOfOpponent(String codeOfMap) {
        mapBuilderOfOpponent.openMapOfOpponent(codeOfMap);
        SwingUtilities.invokeLater(() -> showEndOfTheGame(0));
    }

    /*codeEndOfTheGame:
     * 0-lost
     * 1-victory*/
    private void showEndOfTheGame(int codeEndOfTheGame) {
        PANEL_MAIN_TOP.setVisible(false);

        JButton buttonPlayAgain = new JButton("play with \"" + opponentNickname + "\" again");
        buttonPlayAgain.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> buttonPlayAgainAction());
        });
        LABEL_TURN.setText(" ");
        LABEL_OF_ENDING.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_OF_ENDING.setFont(FONT_OF_LABEL_ENDING);
        buttonPlayAgain.setFont(FONT_OF_BUTTON_PLAY_AGAIN);
        LABEL_OF_ENDING.setForeground(COLOR_FOR_ENDING);
        if (codeEndOfTheGame == 0) {
            LABEL_OF_ENDING.setText("YOU LOST THIS ROUND OF THE GAME");
            sendMessageAboutVictoryForLog(opponentNickname);
        } else {
            LABEL_OF_ENDING.setText("YOU WON THIS ROUND OF THE GAME");
            sendMessageAboutVictoryForLog(NICK_NAME);
        }
        PANEL_OF_ENDING.add(LABEL_OF_ENDING);
        PANEL_OF_ENDING.add(buttonPlayAgain);
        add(PANEL_OF_ENDING, BorderLayout.NORTH);
    }

    private void buttonPlayAgainAction() {
        if (!isPlayAgain) {
            isPlayAgain = true;
            PANEL_OF_ENDING.setVisible(false);
            LABEL_OF_ENDING.setText("<html><br>PLEASE WAIT DECISION OF YOUR OPPONENT</html>");
            add(LABEL_OF_ENDING, BorderLayout.NORTH);
            listener.sendMessageToServer(PLAY_AGAIN);
        }
    }

    private void sendMessageAboutVictoryForLog(String nickName) {
        appendIntoLog(nickName + " won this round of the game");
    }

    public void playAgain() {
        if (isPlayAgain) {
            startNewGameWithPastOpponent();
        } else {
            if (isConfirmMessage(opponentNickname + " suggest you to play again\nDo you agree?")) {
                isPlayAgain = true;
                listener.sendMessageToServer(PLAY_AGAIN);
                startNewGameWithPastOpponent();
            } else {
                exitToMapBuilder();
            }
        }
    }

    private void startNewGameWithPastOpponent() {
        listener.setPreparingForGameWindow(new PreparingForGameFrame(listener.getSocket(), NICK_NAME, opponentNickname, listener, listener.getLogin()));
        this.dispose();
    }
}
