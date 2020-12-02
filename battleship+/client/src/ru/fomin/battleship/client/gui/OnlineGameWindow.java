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
import java.util.Date;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

public class OnlineGameWindow extends JFrame implements ActionListener {
    /*0-simple shoot
     * 1-volley shoot
     * 2-exploration of the map
     * 3-shooting on straight*/
    private int modeStatus = 0;
    private int pastMode;
    private int[] pastUsingCellForActionCoordinates = new int[2];
    private final int TURNS_FOR_VOLLEY = 4;
    private final int TURNS_FOR_EXPLORATION = 3;
    private final int TURNS_FOR_STRAIGHT_SHOOTING = 2;
    private int rechargeForVolley = 0;
    private int rechargeForExploration = 0;
    private int rechargeForStraightShooting = 0;

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
            "14)For recharge of volley shoot " + TURNS_FOR_VOLLEY + " game turns is needed<br>" +
            "15)For recharge of exploration of the map " + TURNS_FOR_EXPLORATION + " game turns is needed<br>" +
            "16)For recharge of shooting on straight " + TURNS_FOR_STRAIGHT_SHOOTING + " game turns is needed<br>" +
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
    private final String TEXT_RECHARGE1 = "recharge in ";
    private final String TEXT_RECHARGE2 = " turns";


    private boolean isTurnOfUser = false;
    private final String opponentNickname;
    private final String NICK_NAME;
    private String mapCodeOfUser;
    private final int SIZE_OF_MAP;
    private WorkingWithNetwork listener;
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

    private final Border BORDER_FOR_LOG_AND_CHAT = BorderFactory.createLineBorder(COLOR_FOR_BORDER_LOG, 20);
    private final Border BORDER_FOR_LABEL_MODE = BorderFactory.createLineBorder(COLOR_FOR_BORDER_MODE, 10);
    private final Border BORDER_FOR_LABEL_RECHARGE = BorderFactory.createLineBorder(COLOR_FOR_NO_ACTIVE_MODE, 8);


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


    private final JTextArea LOG = new JTextArea();
    private final JTextArea CHAT = new JTextArea();
    private final JTextField FIELD_FOR_CHAT_MESSAGE = new JTextField();

    private final JLabel LABEL_TURN = new JLabel(TEXT_TURN_OF_OPPONENT);
    private final JLabel LABEL_MODE = new JLabel(TEXT_MODE_SIMPLE_SHOOT);
    private final JLabel LABEL_RECHARGE_VOLLEY = new JLabel(TEXT_RECHARGE1 + (TURNS_FOR_VOLLEY - rechargeForVolley) + TEXT_RECHARGE2);
    private final JLabel LABEL_RECHARGE_EXPLORATION = new JLabel(TEXT_RECHARGE1 + (TURNS_FOR_EXPLORATION - rechargeForExploration) + TEXT_RECHARGE2);
    private final JLabel LABEL_RECHARGE_STRAIGHT = new JLabel(TEXT_RECHARGE1 + (TURNS_FOR_STRAIGHT_SHOOTING - rechargeForStraightShooting) + TEXT_RECHARGE2);

    private final Font FONT_OF_TURN = new Font(Font.SERIF, Font.BOLD, 24);
    private final Font FONT_OF_MODE = new Font(Font.SERIF, Font.BOLD, 26);
    private final Font FONT_OF_RECHARGE = new Font(Font.SERIF, Font.BOLD, 18);
    private final Font FONT_OF_BUTTON_MODE = new Font(Font.SERIF, Font.BOLD, 25);
    private final Font FONT_OF_BUTTON_BOTTOM = new Font(Font.SERIF, Font.BOLD, 22);
    private final Font FONT_OF_BUTTON_INFO = new Font(Font.SERIF, Font.BOLD, 19);
    private final Font FONT_OF_FIELD_FOR_CHAT_MESSAGE = new Font(Font.SERIF, Font.ITALIC, 20);
    private final Font FONT_OF_CHAT = new Font(Font.SERIF, Font.ITALIC, 16);

    private final JButton BUTTON_SEND = new JButton("SEND MESSAGE");
    private final JButton BUTTON_INSTRUCTION = new JButton("INSTRUCTION MANUAL");
    private final JButton BUTTON_DEVELOPER_INFO = new JButton("DEVELOPER");
    private final JButton BUTTON_MODE_SIMPLE = new JButton("<html>SIMPLE<br>SHOOT</html>");
    private final JButton BUTTON_MODE_VOLLEY = new JButton("<html>VOLLEY<br>SHOOT</html>");
    private final JButton BUTTON_MODE_EXPLORATION = new JButton("<html>EXPLORATION<br>OF THE MAP</html>");
    private final JButton BUTTON_MODE_STRAIGHT = new JButton("<html>SHOOTING<br>ON STRAIGHT</html>");
    private final JButton BUTTON_EXIT = new JButton("EXIT");


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
        LABEL_RECHARGE_VOLLEY.setBorder(BORDER_FOR_LABEL_RECHARGE);
        LABEL_RECHARGE_EXPLORATION.setBorder(BORDER_FOR_LABEL_RECHARGE);
        LABEL_RECHARGE_STRAIGHT.setBorder(BORDER_FOR_LABEL_RECHARGE);

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

    public void appendIntoLog(String message){
        LOG.append(message);
    }

    public void appendIntoLog(String message, boolean isActionAgain) {
        message=NICK_NAME + ":\n*Used mode of " + createMessageAboutMode()+"\n"+message+"\n*"+createMessageAboutNextTurn(isActionAgain)+"\n";
      LOG.append(message);
      listener.sendMessageToServer(LibraryOfPrefixes.getLogMessage(message));
    }

    private String createMessageAboutNextTurn(boolean isActionAgain){
        if(isActionAgain) return "Can action again"; else return "End game turn";
    }

    private String createMessageAboutMode() {
        switch (pastMode){
            case 0:
                return format("'simple shoot' (%s;%s)",pastUsingCellForActionCoordinates[0]+1,pastUsingCellForActionCoordinates[1]+1);
            case 1:
                return format("'volley shoot' (%s;%s)",pastUsingCellForActionCoordinates[0]+1,pastUsingCellForActionCoordinates[1]+1);
            case 2:
                return format("'exploration of the map' (%s;%s)",pastUsingCellForActionCoordinates[0]+1,pastUsingCellForActionCoordinates[1]+1);
            case 3:
                return format("'shooting on straight' (%s;%s)",pastUsingCellForActionCoordinates[0]+1,pastUsingCellForActionCoordinates[1]+1);
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

            return;
        }
        if (source == BUTTON_MODE_EXPLORATION) {

            return;
        }
        if (source == BUTTON_MODE_STRAIGHT) {

            return;
        }
        if (source == BUTTON_MODE_SIMPLE) {

            return;
        }

        throw new RuntimeException("Unknown source: " + source);
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
    }

    private void sendMessageIntoChat(String message) {
        listener.sendMessageToServer(LibraryOfPrefixes.getChatMessage(message));
        Date date = new Date();
        CHAT.append(format("YOU(%tR):\n%s\n", date, message));
        FIELD_FOR_CHAT_MESSAGE.setText("");
        FIELD_FOR_CHAT_MESSAGE.grabFocus();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
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
        listener.sendMessageToServer(LibraryOfPrefixes.CHANGE_TURN);
    }

    public void sendCodeOfGameTurn(String codeOfGameTurn) {
        listener.sendMessageToServer(LibraryOfPrefixes.getCodeOfGameTurnMessage(codeOfGameTurn));
    }

    public void processDataOfOpponentTurn(String codeOfGameTurn) {
        mapBuilderOfUser.processDataOfOpponentTurn(codeOfGameTurn);
    }

    public void sendCodeResultOfGameTurn(String codeOfTurnResult) {
        listener.sendMessageToServer(LibraryOfPrefixes.getCodeResultOfTurn(codeOfTurnResult));
    }

    public void processDataOfResultTurn(String codeOfResultTurn) {
        mapBuilderOfOpponent.processDataOfResultTurn(codeOfResultTurn);
    }
}
