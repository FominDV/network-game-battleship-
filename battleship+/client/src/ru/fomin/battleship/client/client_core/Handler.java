package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.*;
import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class Handler implements SocketThreadListener, WorkingWithNetwork {
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

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        if (isRegistration) {
            String nickName = registrationFrame.getNickName();
            String login = registrationFrame.getLogin();
            String password = new String(registrationFrame.getPassword());
            thread.sendMessage(LibraryOfPrefixes.getRegistrationRequest(login, password, nickName));
            isRegistration = false;
        } else {
            String login = clientAuthenticationFrame.getLogin();
            if (login.equals("")) login = "Invalid_login";
            String password = new String(clientAuthenticationFrame.getPassword());
            if (password.equals("")) password = "Invalid_password";
            thread.sendMessage(LibraryOfPrefixes.getAuthRequest(login, password));
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

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    private void handleMessage(String msg) {
        String[] arr = msg.split(LibraryOfPrefixes.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case LibraryOfPrefixes.AUTH_ACCEPT:
                isValidAuthentication = true;
                clientAuthenticationFrame.dispose();
                nickName = arr[1];
                preparingForGameFrame = new PreparingForGameFrame(socketThread, arr[1], this, login);
                break;
            case LibraryOfPrefixes.AUTH_DENIED:
                showAuthenticationError();
                clientAuthenticationFrame.clearFields();
                break;
            case LibraryOfPrefixes.MSG_FORMAT_ERROR:
                socketThread.close();
                break;
            case LibraryOfPrefixes.REGISTRATION:
                if (arr[1].equals("true")) {
                    registrationFrame.registrationSuccessful();
                } else {
                    registrationFrame.registrationNotSuccessful();
                }
                socketThread.close();
                break;
            case LibraryOfPrefixes.SEARCH_OPPONENT:
                preparingForGameFrame.setOpponentNickname(arr[1]);
                break;
            case LibraryOfPrefixes.DISCONNECT_OPPONENT:
                JOptionPane.showMessageDialog(null, "Connect with your opponent was lost", "ERROR", JOptionPane.ERROR_MESSAGE);
                socketThread.close();
                onlineGameWindow.dispose();
                isValidAuthentication = false;
                try {
                    login(ip, port, clientAuthenticationFrame, login);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LibraryOfPrefixes.LIST_OF_DATA_MAP:
                writeDataIntoTheList(arr);
                preparingForGameFrame.updateDataMap();
                break;
            case LibraryOfPrefixes.SUCCESSFUL_SAVE:
                preparingForGameFrame.successfulSave();
                break;
            case LibraryOfPrefixes.FAIL_SAVE:
                preparingForGameFrame.failSave();
                break;
            case LibraryOfPrefixes.DUPLICATE_NAME:
                preparingForGameFrame.showMessageAboutDuplicateNameOfSaving();
                break;
            case LibraryOfPrefixes.CHAT_MESSAGE:
                onlineGameWindow.setChatMessage(arr[1]);
                break;
            case LibraryOfPrefixes.TURN:
                onlineGameWindow.setTurnOfUser(arr[1]);
                break;
        }
    }

    private void writeDataIntoTheList(String[] dataMapArray) {
        dataMapVector.clear();
        for (int i = 1; i < dataMapArray.length; i += 2) {
            String[] dataRow = new String[2];
            dataRow[0] = dataMapArray[i];
            dataRow[1] = dataMapArray[i + 1];
            dataMapVector.add(dataRow);
        }
    }

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

}