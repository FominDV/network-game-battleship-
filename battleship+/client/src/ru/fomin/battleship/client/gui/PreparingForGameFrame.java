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
import java.util.Vector;

public class PreparingForGameFrame extends JFrame implements ActionListener {
    private final String INFO_ABOUT_GAME = "<html>1)Select the \"REMOVE\" or \"POST\" mode\"<br>2)Click on the field and click \"POST\" to place the ship" +
            "<br>3)Click on the ship for removing in the \"REMOVE\" mode" +
            "<br>4)If you want to cancel the selected cells, click to \"CANCEL ACTION\"<br>5)For exit to registration menu click to \"EXIT\"" +
            "<br>6)If you have saved maps, you can download the map by clicking \"LOAD THE MAP\"<br>7)You can only start the game by placing all the ships. To start the game, click \"START\"" +
            "<br>8)There should be an empty space around each ship<br>9)The game has 4 single-deck, 3 double-deck, 2 three-deck and 1 four-deck ships" +
            "<br>10)When all the ships are placed, the button will change color and become active</html>";
    static final String INFO_ABOUT_DEVELOPER = "<html>Developer: Dmitriy Fomin<br>GitHub: https://github.com/FominDV <br> Email: 79067773397@yandex.ru<br>*All rights reserved*</html>";
    private final String START = "START";
    private final String REMOVE_MODE = "REMOVE MODE";
    private final String POST_MODE = "POST MODE";
    private final String TEXT_OPPONENT_NOT_READY = "THE OPPONENT PLACES THE SHIPS";
    private final String TEXT_OPPONENT_READY = "THE OPPONENT IS ALREADY READY";
    private final String WAIT = "PLEASE WAIT YOUR OPPONENT";
    private final int SIZE_OF_MAP = 10;
    private final int WIDTH = 855;
    private int height = 700;
    private final String WINDOW_TITLE = "Map-Maker by ";
    private final SocketThread SOCKET_THREAD;
    private final String NICK_NAME;
    private final String LOGIN;
    private final WorkingWithNetwork listener;
    private boolean isOpponentReady = false;
    private boolean isPlayAgainMode = false;
    private boolean isReadyToPlayAgain = false;
    private boolean isPost = true;
    private boolean isReady = false;
    private boolean isSavedMap = false;
    private String opponentNickname = "empty";
    private SearchOpponentThread searchOpponentThread = null;
    private MapBuilder mapBuilder;
    private final Color COLOR_OF_BACKGROUND = new Color(99, 234, 234);
    private final Color COLOR_OF_START = new Color(215, 99, 99);
    private final Color COLOR_OF_POST_MODE = new Color(50, 104, 1);
    private final Color COLOR_OF_REMOVE_MODE = new Color(68, 4, 4);
    private final Color COLOR_OF_READY = new Color(46, 220, 5);
    private final Color COLOR_OF_PANEL_BACKGROUND = new Color(135, 170, 206);
    private final Color COLOR_OF_LABEL_OPPONENT_PROGRESS = new Color(187, 52, 0, 233);
    private Vector<String[]> dataMapVector = new Vector<>();
    private SearchingOpponent searchingOpponent;

    private final Font FONT_FOR_BUTTONS = new Font(Font.SERIF, Font.BOLD, 16);
    private final Font FONT_FOR_LABEL_SHIPS = new Font(Font.SERIF, Font.BOLD, 24);
    private final Font FONT_FOR_MODE = new Font(Font.SERIF, Font.BOLD, 30);
    private final Font FONT_FOR_LABEL_OPPONENT_PROGRESS = new Font(Font.SERIF, Font.BOLD, 18);

    private final JPanel PANEL_MAP = new JPanel(new GridLayout(SIZE_OF_MAP + 1, SIZE_OF_MAP + 1));
    private final JPanel WRAPPER_FOR_MAP = new JPanel(new GridBagLayout());
    private final JPanel PANEL_BOTTOM = new JPanel(new GridLayout(2, 3));
    private final JPanel PANEL_TOP = new JPanel(new GridLayout(1, 2));
    private final JPanel PANEL_MAIN_TOP = new JPanel(new GridLayout(2, 1));
    private final JPanel PANEL_LEFT_MAIN = new JPanel(new GridLayout(5, 1));
    private final JPanel PANEL_LEFT_TOP = new JPanel(new GridLayout(1, 1));
    private final JPanel[] PANEL_LEFT_BOTTOM = new JPanel[4];
    private final JPanel PANEL_MAIN = new JPanel(new BorderLayout());

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
    private JLabel labelOfOpponentProgress = new JLabel(TEXT_OPPONENT_NOT_READY);


    public PreparingForGameFrame(SocketThread socketThread, String nickname, WorkingWithNetwork listener, String login) {
        NICK_NAME = nickname;
        SOCKET_THREAD = socketThread;
        this.listener = listener;
        LOGIN = login;
        SwingUtilities.invokeLater(() -> initialization());
    }

    public PreparingForGameFrame(SocketThread socketThread, String nickname, String opponentNickname, WorkingWithNetwork listener, String login) {
        NICK_NAME = nickname;
        this.opponentNickname = opponentNickname;
        SOCKET_THREAD = socketThread;
        this.listener = listener;
        LOGIN = login;
        updateDataMap();
        height=800;
        SwingUtilities.invokeLater(() -> initialization());
        SwingUtilities.invokeLater(() -> addLabelOfOpponentProgress());
    }

    private void addLabelOfOpponentProgress() {
        labelOfOpponentProgress.setHorizontalAlignment(SwingConstants.CENTER);
        labelOfOpponentProgress.setBackground(COLOR_OF_PANEL_BACKGROUND);
        labelOfOpponentProgress.setFont(FONT_FOR_LABEL_OPPONENT_PROGRESS);
        labelOfOpponentProgress.setForeground(COLOR_OF_LABEL_OPPONENT_PROGRESS);
        PANEL_MAIN_TOP.add(PANEL_LEFT_TOP);
        PANEL_MAIN_TOP.add((labelOfOpponentProgress));
        add(PANEL_MAIN_TOP, BorderLayout.NORTH);
        isPlayAgainMode = true;
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, height);
        setLocationRelativeTo(null);
        setTitle(WINDOW_TITLE + NICK_NAME);
        setResizable(false);

        WRAPPER_FOR_MAP.setSize(SIZE_OF_MAP * 50, SIZE_OF_MAP * 50);
        fillMap();
        WRAPPER_FOR_MAP.add(PANEL_MAP);
        setContentPane(PANEL_MAIN);
        for (int i = 0; i < PANEL_LEFT_BOTTOM.length; i++) {
            PANEL_LEFT_BOTTOM[i] = new JPanel(new GridLayout(1, 2));
            PANEL_LEFT_BOTTOM[i].setBackground(COLOR_OF_PANEL_BACKGROUND);
        }
        PANEL_MAP.setBackground(COLOR_OF_PANEL_BACKGROUND);
        WRAPPER_FOR_MAP.setBackground(COLOR_OF_PANEL_BACKGROUND);
        PANEL_TOP.setBackground(COLOR_OF_PANEL_BACKGROUND);
        PANEL_BOTTOM.setBackground(COLOR_OF_PANEL_BACKGROUND);
        PANEL_LEFT_TOP.setBackground(COLOR_OF_PANEL_BACKGROUND);
        PANEL_LEFT_MAIN.setBackground(COLOR_OF_PANEL_BACKGROUND);
        PANEL_MAIN.setBackground(COLOR_OF_PANEL_BACKGROUND);

        labelCount1Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelCount2Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelCount3Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelCount4Ship.setFont(FONT_FOR_LABEL_SHIPS);
        labelMode.setFont(FONT_FOR_MODE);
        labelMode.setForeground(COLOR_OF_POST_MODE);
        labelMode.setHorizontalAlignment(SwingConstants.CENTER);
        mapBuilder.setCountOfShips();
        LABEL_SHIP_4_IMAGE.setIcon(new ImageIcon(getClass().getResource("../img/ship4.png")));
        LABEL_SHIP_4_IMAGE.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_SHIP_3_IMAGE.setIcon(new ImageIcon(getClass().getResource("../img/ship3.png")));
        LABEL_SHIP_3_IMAGE.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_SHIP_2_IMAGE.setIcon(new ImageIcon(getClass().getResource("../img/ship2.png")));
        LABEL_SHIP_2_IMAGE.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_SHIP_1_IMAGE.setIcon(new ImageIcon(getClass().getResource("../img/ship1.png")));
        LABEL_SHIP_1_IMAGE.setHorizontalAlignment(SwingConstants.CENTER);


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
        PANEL_LEFT_BOTTOM[0].add(LABEL_SHIP_4_IMAGE);
        PANEL_LEFT_BOTTOM[0].add(labelCount4Ship);
        PANEL_LEFT_BOTTOM[1].add(LABEL_SHIP_3_IMAGE);
        PANEL_LEFT_BOTTOM[1].add(labelCount3Ship);
        PANEL_LEFT_BOTTOM[2].add(LABEL_SHIP_2_IMAGE);
        PANEL_LEFT_BOTTOM[2].add(labelCount2Ship);
        PANEL_LEFT_BOTTOM[3].add(LABEL_SHIP_1_IMAGE);
        PANEL_LEFT_BOTTOM[3].add(labelCount1Ship);

        PANEL_LEFT_MAIN.add(PANEL_LEFT_TOP);
        PANEL_LEFT_MAIN.add(PANEL_LEFT_BOTTOM[0]);
        PANEL_LEFT_MAIN.add(PANEL_LEFT_BOTTOM[1]);
        PANEL_LEFT_MAIN.add(PANEL_LEFT_BOTTOM[2]);
        PANEL_LEFT_MAIN.add(PANEL_LEFT_BOTTOM[3]);

        PANEL_TOP.add(BUTTON_INFO);
        PANEL_TOP.add(BUTTON_DEVELOPER_INFO);
        PANEL_BOTTOM.add(BUTTON_POST);
        PANEL_BOTTOM.add(BUTTON_REMOVE);
        PANEL_BOTTOM.add(BUTTON_CANCEL);
        PANEL_BOTTOM.add(BUTTON_LOAD);
        PANEL_BOTTOM.add(BUTTON_START);
        PANEL_BOTTOM.add(BUTTON_EXIT);

        PANEL_MAIN.add(WRAPPER_FOR_MAP, BorderLayout.EAST);
        PANEL_MAIN.add(PANEL_BOTTOM, BorderLayout.SOUTH);
        PANEL_MAIN.add(PANEL_TOP, BorderLayout.NORTH);
        PANEL_MAIN.add(PANEL_LEFT_MAIN, BorderLayout.WEST);

        setVisible(true);
    }

    protected void fillMap() {
        PANEL_MAP.add(new JLabel());
        for (int i = 1; i <= SIZE_OF_MAP; i++) {
            JLabel number = new JLabel((String.valueOf(i)));
            number.setHorizontalAlignment(SwingConstants.CENTER);
            PANEL_MAP.add(number);
        }
        Cell[][] map = new Cell[SIZE_OF_MAP][SIZE_OF_MAP];
        for (int i = 0; i < map.length; i++) {
            PANEL_MAP.add(new JLabel(String.valueOf(i + 1)));
            for (int j = 0; j < map.length; j++) {
                map[i][j] = new Cell(5, this, i, j);
                PANEL_MAP.add(map[i][j]);
            }
        }
        mapBuilder = new MapBuilder(map, this);
    }

    private void searchOpponent() {
        if (searchOpponentThread == null || !(searchOpponentThread.isAlive())) {
            listener.sendMessageToServer(LibraryOfPrefixes.MESSAGE_ABOUT_START_SEARCHING);
            searchingOpponent = new SearchingOpponent(this);
            listener.setSearchingOpponent(searchingOpponent);
            searchOpponentThread = new SearchOpponentThread(this, searchingOpponent);
            setVisible(false);
        }
    }


    public void setOpponentNickname(String opponentNickname) {
        this.opponentNickname = opponentNickname;
        searchingOpponent.dispose();
        startOnlineGame();
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
        searchingOpponent.dispose();
        listener.sendMessageToServer(LibraryOfPrefixes.STOP_SEARCHING);
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
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
                SOCKET_THREAD.close();
            return;
        }
        if (source.equals(BUTTON_CANCEL)) {
            if (isReadyToPlayAgain) {
                listener.exitToMapBuilder();
            } else {
                mapBuilder.cancelStatus4();
            }
            return;
        }
        if (source.equals(BUTTON_POST)) {
            if (isReadyToPlayAgain) return;
            isPost = true;
            labelMode.setText(POST_MODE);
            labelMode.setForeground(COLOR_OF_POST_MODE);
            mapBuilder.post();
            return;
        }
        if (source.equals(BUTTON_REMOVE)) {
            if (isReadyToPlayAgain) return;
            isPost = false;
            labelMode.setText(REMOVE_MODE);
            labelMode.setForeground(COLOR_OF_REMOVE_MODE);
            mapBuilder.cancelStatus4();
            return;
        }
        if (source.equals(BUTTON_START)) {
            if (isReadyToPlayAgain) return;
            if (!isReady) {
                JOptionPane.showMessageDialog(null, "To start the game you should post all the ships", "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }
          if(isSavedMap){ if(!isPlayAgainMode) searchOpponent(); } else {  savingDialog();return;}
            actionOfButtonStartForPlayAgainMode();
            return;
        }
        if (source.equals(BUTTON_LOAD)) {
            if (isReadyToPlayAgain) return;
            if (dataMapVector.size() != 0)
                goToSavingMapWindow();
            else
                JOptionPane.showMessageDialog(null, "<html>You have not any saved maps now!<br>You should fill the map and save it after click on button \"START\"</html>", "EMPTY LIST OF SAVINGS", JOptionPane.ERROR_MESSAGE);
            return;
        }
        throw new RuntimeException("Unknown source: " + source);
    }
private void actionOfButtonStartForPlayAgainMode(){
    if (!isPlayAgainMode) return;
        if (isOpponentReady) {
            listener.sendMessageToServer(LibraryOfPrefixes.START_PLAY_AGAIN);
            startToPlayAgain();
        } else {
            isReadyToPlayAgain = true;
            labelOfOpponentProgress.setText(WAIT);
            listener.sendMessageToServer(LibraryOfPrefixes.READY_PLAY_AGAIN);
        }

}
    public void verifyReadinessForPlayAgain() {
        if (isReadyToPlayAgain) {
            listener.sendMessageToServer(LibraryOfPrefixes.START_PLAY_AGAIN);
            startToPlayAgain();
        } else {
            isOpponentReady = true;
            labelOfOpponentProgress.setText(TEXT_OPPONENT_READY);
        }
    }

    public void startToPlayAgain() {
        listener.setOnlineGameWindow(new OnlineGameWindow(opponentNickname, NICK_NAME, mapBuilder.getDataSaving(), listener, SIZE_OF_MAP));
        listener.sendMessageToServer(LibraryOfPrefixes.GET_FIRST_TURN);
        dispose();
    }

    private void goToSavingMapWindow() {
        listener.setSavingMapWindow(new SavingMapWindow(this, listener, dataMapVector));
        setVisible(false);
    }

    private void showInstruction() {
        JOptionPane.showMessageDialog(null, INFO_ABOUT_GAME, "INSTRUCTION MANUAL", JOptionPane.INFORMATION_MESSAGE);
    }

    static void showDeveloperInfo() {
        JOptionPane.showMessageDialog(null, INFO_ABOUT_DEVELOPER, "INFORMATION ABOUT DEVELOPER", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setCountOfShips(int ship1, int ship2, int ship3, int ship4) {
        labelCount1Ship.setText("- " + ship1 + "/4");
        labelCount2Ship.setText("- " + ship2 + "/3");
        labelCount3Ship.setText("- " + ship3 + "/2");
        labelCount4Ship.setText("- " + ship4 + "/1");
        if (ship1 == 4 && ship2 == 3 && ship3 == 2 && ship4 == 1) {
            isReady = true;
            BUTTON_START.setBackground(COLOR_OF_READY);
        } else {
            isReady = false;
            isSavedMap = false;
            BUTTON_START.setBackground(COLOR_OF_START);
        }
    }

    public boolean validCellForPreparingPost(int x, int y) {
        return mapBuilder.validCellForPreparingPost(isPost, x, y);
    }

    public void remove(int x, int y) {
        mapBuilder.remove(x, y);
    }

    public boolean getMode() {
        return isPost;
    }

    private void savingDialog() {
            if (dataMapVector.size() == 5) {
                if (isSavingConfirmMessageYesNo("<html>You should have less or equal 5 savings of map!<br>Do you want to delete some of your saves </html>"))
                    goToSavingMapWindow();
                else if(!isPlayAgainMode){ searchOpponent();}else{actionOfButtonStartForPlayAgainMode();}
                return;
            }
            if (isSavingConfirmMessageYesNo("Do you want to save the map?")) {
                String nameData;
                while (true) {
                    nameData = JOptionPane.showInputDialog(null, "Input name of your saving data:");
                    if (nameData.length() > 18) {
                        if (isSavingConfirmMessageYesNo("<html>Length fo data name should be less or equal than 18<br>Do you want try again?</html>"))
                            continue;
                        else return;
                    } else {
                        break;
                    }
                }
                listener.sendMessageToServer(LibraryOfPrefixes.getSavingMapMessage(LOGIN, nameData, mapBuilder.getDataSaving()));
            } else {
                if(!isPlayAgainMode) searchOpponent(); else actionOfButtonStartForPlayAgainMode();
            }

    }

    public void updateDataMap() {
        dataMapVector = listener.getDataMap();
    }

    public void startOnlineGame() {
        listener.setOnlineGameWindow(new OnlineGameWindow(opponentNickname, NICK_NAME, mapBuilder.getDataSaving(), listener, SIZE_OF_MAP));
        dispose();
    }

    public void failSave() {
        JOptionPane.showMessageDialog(null, "Saving map is failed", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public void successfulSave() {
        JOptionPane.showMessageDialog(null, "Saving map is successful");
        isSavedMap = true;
        if(!isPlayAgainMode) searchOpponent(); else actionOfButtonStartForPlayAgainMode();
    }

    protected boolean isSavingConfirmMessageYesNo(String message) {
        if (JOptionPane.showConfirmDialog(null, message, "Saving map dialog", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
            return true;
        return false;
    }

    public void showMessageAboutDuplicateNameOfSaving() {
        JOptionPane.showMessageDialog(null, "This name of saving has already been used by you!", "ERROR OF DUPLICATING", JOptionPane.ERROR_MESSAGE);
    }

    public void removeData(String selectedName) {
        listener.sendMessageToServer(LibraryOfPrefixes.getRemoveDataMessage(LOGIN, selectedName));
    }

    public void loadMap(String selectedName) {
        String dataMap = "";
        for (String[] data : dataMapVector) {
            if (data[0].equals(selectedName)) dataMap = data[1];
        }
        mapBuilder.loadMap(dataMap);
        isSavedMap = true;
    }

}
