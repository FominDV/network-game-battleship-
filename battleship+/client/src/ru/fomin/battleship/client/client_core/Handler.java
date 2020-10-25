package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.ClientAuthenticationFrame;
import ru.fomin.battleship.client.gui.PreparingForGameFrame;
import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;


public class Handler implements SocketThreadListener {
    private boolean isValidAuthentication = false;
    private SocketThread socketThread;
    private ClientAuthenticationFrame clientAuthenticationFrame;
    private PreparingForGameFrame preparingForGameFrame;

    public void login(String ip, int port, ClientAuthenticationFrame authenticationFrame, Handler handler, String login) throws IOException {
        this.clientAuthenticationFrame = authenticationFrame;
        Socket socket = new Socket(ip, port);
        socketThread = new SocketThread(this, login, socket);
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        System.out.println("Socket was created");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        if (isValidAuthentication) {
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
            new ClientAuthenticationFrame();
        }
        isValidAuthentication = false;

    }

    private void showAuthenticationError() {
        JOptionPane.showMessageDialog(null, "Invalid login or password", "Authentication error", JOptionPane.ERROR_MESSAGE);
    }

    private void showConnectError() {
        JOptionPane.showMessageDialog(null, "The connection was lost", "Connection error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        String login = clientAuthenticationFrame.getLogin();
        if(login.equals("")) login="Invalid_login";
        String password = new String(clientAuthenticationFrame.getPassword());
        if(password.equals("")) password="Invalid_password";
        thread.sendMessage(LibraryOfPrefixes.getAuthRequest(login, password));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        showException(thread, exception);
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
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
                preparingForGameFrame = new PreparingForGameFrame(socketThread);
                break;
            case LibraryOfPrefixes.AUTH_DENIED:
                showAuthenticationError();
                clientAuthenticationFrame.clearFields();
                break;
            case LibraryOfPrefixes.MSG_FORMAT_ERROR:
                socketThread.close();
                break;

        }
    }
}
