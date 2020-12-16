package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.*;
import static ru.fomin.battleship.common.LibraryOfPrefixes.*;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class Handler implements SocketThreadListener, WorkingWithHandler {
    //This variable is needed for to prevent double re registration of the client who clicked on button "EXIT" from OnlineGameWindow
    private boolean isNotExitToMapBuilder = true;
    private String nickName;
    private boolean isRegistration = false;
    private boolean isValidAuthentication = false;
    private SocketThread socketThread;
    private ClientAuthenticationFrame clientAuthenticationFrame;
    private PreparingForGameFrame preparingForGameFrame;
    private RegistrationFrame registrationFrame;
    private OnlineGameWindow onlineGameWindow;
    private SavingMapWindow savingMapWindow;
    private SearchingOpponent searchingOpponent;
    private String login;
    private Vector<String[]> dataMapVector = new Vector<>();
    private String ip;
    private int port;


    public void login(String ip, int port, ClientAuthenticationFrame authenticationFrame, String login) throws IOException {
        this.clientAuthenticationFrame = authenticationFrame;
        Socket socket = new Socket(ip, port);
        socketThread = new SocketThread(this, login, socket);
        this.login = login;
        this.ip = ip;
        this.port = port;
    }

    public void login(String ip, int port, RegistrationFrame registrationFrame, Boolean isRegistration) throws IOException {
        this.registrationFrame = registrationFrame;
        this.isRegistration = isRegistration;
        Socket socket = new Socket(ip, port);
        socketThread = new SocketThread(this, "registration", socket);
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        System.out.println("Socket was created");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        if (isValidAuthentication) {
            isValidAuthentication = false;
            showConnectError();
            try {
                clientAuthenticationFrame.dispose();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                preparingForGameFrame.dispose();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                onlineGameWindow.dispose();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                savingMapWindow.dispose();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                searchingOpponent.dispose();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            new ClientAuthenticationFrame();
        }


    }

    private void showAuthenticationError() {
        JOptionPane.showMessageDialog(null, "Invalid login or password", "Authentication error", JOptionPane.ERROR_MESSAGE);
    }

    public void showConnectError() {
        JOptionPane.showMessageDialog(null, "The connection was lost", "Connection error", JOptionPane.ERROR_MESSAGE);
    }
//send data to server about authentication or registration
    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        if (isRegistration) {
            String nickName = registrationFrame.getNickName();
            String login = registrationFrame.getLogin();
            String password = new String(registrationFrame.getPassword());
            thread.sendMessage(getRegistrationRequest(login, password, nickName));
            isRegistration = false;
        } else {
            String login = clientAuthenticationFrame.getLogin();
            if (login.equals("")) login = "Invalid_login";
            String password = new String(clientAuthenticationFrame.getPassword());
            if (password.equals("")) password = "Invalid_password";
            thread.sendMessage(getAuthRequest(login, password));
        }
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        onSocketStop(thread);
        if (preparingForGameFrame != null) preparingForGameFrame.stopSearching();
    }
//This method for working with messages from server
    private void handleMessage(String msg) {
        String[] arr = msg.split(DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case AUTH_ACCEPT:
                isValidAuthentication = true;
                clientAuthenticationFrame.dispose();
                nickName = arr[1];
                preparingForGameFrame = new PreparingForGameFrame(socketThread, arr[1], this, login);
                break;
            case AUTH_DENIED:
                showAuthenticationError();
                clientAuthenticationFrame.clearFields();
                break;
            case MSG_FORMAT_ERROR:
                socketThread.close();
                break;
            case REGISTRATION:
                if (arr[1].equals("true")) {
                    registrationFrame.registrationSuccessful();
                } else {
                    registrationFrame.registrationNotSuccessful();
                }
                socketThread.close();
                break;
            case SEARCH_OPPONENT:
                isNotExitToMapBuilder = true;
                preparingForGameFrame.setOpponentNickname(arr[1]);
                break;
            case DISCONNECT_OPPONENT:
                if (isNotExitToMapBuilder) {
                    JOptionPane.showMessageDialog(null, "Connect with your opponent was lost", "ERROR", JOptionPane.ERROR_MESSAGE);
                    reRegistration();
                }
                break;
            case LIST_OF_DATA_MAP:
                writeDataIntoTheList(arr);
                preparingForGameFrame.updateDataMap();
                break;
            case SUCCESSFUL_SAVE:
                preparingForGameFrame.successfulSave();
                break;
            case FAIL_SAVE:
                preparingForGameFrame.failSave();
                break;
            case DUPLICATE_NAME:
                preparingForGameFrame.showMessageAboutDuplicateNameOfSaving();
                break;
            case CHAT_MESSAGE:
                onlineGameWindow.setChatMessage(arr[1]);
                break;
            case TURN:
                onlineGameWindow.setTurnOfUser(arr[1]);
                break;
            case CHANGE_TURN:
                onlineGameWindow.changeTurn();
                break;
            case EXIT_TO_MAP_BUILDER:
                isNotExitToMapBuilder = false;
                reRegistration();
                break;
            case CODE_OF_GAME_TURN:
                onlineGameWindow.processDataOfOpponentTurn(arr[1]);
                break;
            case CODE_OF_RESULT_TURN:
                onlineGameWindow.processDataOfResultTurn(arr[1]);
                break;
            case LOG_MESSAGE:
                onlineGameWindow.appendIntoLog(arr[1]);
                break;
            case VICTORY:
                onlineGameWindow.victory();
                break;
            case CODE_OF_MAP_AFTER_GAME:
                onlineGameWindow.openMapOfOpponent(arr[1]);
                break;
            case LOG_LAST_MESSAGE:
                onlineGameWindow.setLastPartOfMessageForLog(arr[1]);
                break;
            case PLAY_AGAIN:
                onlineGameWindow.playAgain();
                break;
            case READY_PLAY_AGAIN:
                preparingForGameFrame.verifyReadinessForPlayAgain();
                break;
            case START_PLAY_AGAIN:
                isNotExitToMapBuilder=true;
                preparingForGameFrame.startToPlayAgain();
                break;
        }
    }

    private void reRegistration() {
        socketThread.close();
        onlineGameWindow.dispose();
        isValidAuthentication = false;
        try {
            preparingForGameFrame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            login(ip, port, clientAuthenticationFrame, login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//This method for send dta of saving to PreparingForGameFrame
    private void writeDataIntoTheList(String[] dataMapArray) {
        dataMapVector.clear();
        for (int i = 1; i < dataMapArray.length; i += 2) {
            String[] dataRow = new String[2];
            dataRow[0] = dataMapArray[i];
            dataRow[1] = dataMapArray[i + 1];
            dataMapVector.add(dataRow);
        }
    }
//Implement methods by WorkingWithHandler
    @Override
    public void sendMessageToServer(String message) {
        socketThread.sendMessage(message);
    }

    @Override
    public Vector<String[]> getDataMap() {
        return dataMapVector;
    }

    @Override
    public void setSavingMapWindow(SavingMapWindow savingMapWindow) {
        this.savingMapWindow = savingMapWindow;
    }

    @Override
    public void setSearchingOpponent(SearchingOpponent searchingOpponent) {
        this.searchingOpponent = searchingOpponent;
    }

    @Override
    public void setOnlineGameWindow(OnlineGameWindow onlineGameWindow) {
        this.onlineGameWindow = onlineGameWindow;
    }

    @Override
    public SocketThread getSocket() {
        return socketThread;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setPreparingForGameWindow(PreparingForGameFrame preparingForGameFrame) {
        this.preparingForGameFrame = preparingForGameFrame;
    }
    //this message for sending yourself and calling method 'reRegistration'
    @Override
    public void exitToMapBuilder() {
        sendMessageToServer(EXIT_TO_MAP_BUILDER);
    }
}