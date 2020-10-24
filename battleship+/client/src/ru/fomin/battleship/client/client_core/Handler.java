package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.ClientAuthenticationFrame;
import ru.fomin.battleship.client.gui.PreparingForGameFrame;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;




public class Handler implements SocketThreadListener {
private SocketThread socketThread;
private ClientAuthenticationFrame clientAuthenticationFrame;
private PreparingForGameFrame preparingForGameFrame;
    public void login(String ip, int port, ClientAuthenticationFrame authenticationFrame, Handler handler, String login) throws IOException {
        this.clientAuthenticationFrame=authenticationFrame;
        Socket socket = new Socket(ip, port);
        socketThread=new SocketThread(this, login, socket );
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        System.out.println("Socket was created");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        clientAuthenticationFrame.dispose();
        preparingForGameFrame.dispose();
        new ClientAuthenticationFrame();
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        clientAuthenticationFrame.dispose();
        new PreparingForGameFrame();
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {

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
}
