package ru.fomin.battleship.client.client_core;

import ru.fomin.battleship.client.gui.ClientAuthenticationFrame;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import java.io.IOException;
import java.net.Socket;




public class Handler implements SocketThreadListener {
private SocketThread socketThread;
private ClientAuthenticationFrame clientAuthenticationFrame;
    public void login(String ip, int port, ClientAuthenticationFrame authenticationFrame, Handler handler, String login) throws IOException {
        this.clientAuthenticationFrame=authenticationFrame;
        Socket socket = new Socket(ip, port);
        socketThread=new SocketThread(this, login, socket );

    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {

    }

    @Override
    public void onSocketStop(SocketThread thread) {
        clientAuthenticationFrame.dispose();
        new ClientAuthenticationFrame();
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {

    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {

    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {

    }
}
