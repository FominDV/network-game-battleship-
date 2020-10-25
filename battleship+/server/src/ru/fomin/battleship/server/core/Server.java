package ru.fomin.battleship.server.core;

import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.ServerSocketThread;
import ru.fomin.network.ServerSocketThreadListener;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Server implements ServerSocketThreadListener, SocketThreadListener {
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final ServerListener listener;
    private ServerSocketThread thread;
    private final Vector<SocketThread> CLIENTS;

    public Server(ServerListener listener) {
        this.listener = listener;
        CLIENTS =new Vector<>(2);
    }

    public void start(int port) {
        if (thread != null && thread.isAlive()) {
            putLog("Server already started");
        } else {
            thread = new ServerSocketThread(this, "Thread of server", port, 2000);
        }
    }

    public void stop() {
        if (thread == null || !thread.isAlive()) {
            putLog("Server is not running");
        } else {
            thread.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ": " + msg;
        listener.onServerMessage(msg);
    }

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
        SQLClient.connect();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
        SQLClient.disconnect();
        for (SocketThread socket: CLIENTS) {
            socket.close();
        }
        CLIENTS.clear();
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server socket created");
    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {

    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {

    }

    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connected");
        String name = "SocketThread " + socket.getInetAddress() + ":" + socket.getPort();
        new ClientThread(this, name, socket);
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putLog("Socket stopped");
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");
        CLIENTS.add(thread);
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized())
            handleAuthMessage(client, msg);
        else
            handleNonAuthMessage(client, msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {

    }
    private void handleAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(LibraryOfPrefixes.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case "A":
                System.out.println(arr[1]);
            break;
            default:
                client.msgFormatError(msg);

        }
    }
    private void handleNonAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(LibraryOfPrefixes.DELIMITER);
        if (arr.length != 3 || !arr[0].equals(LibraryOfPrefixes.AUTH_REQUEST)) {
            client.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SQLClient.getNickname(login, password);
        if (nickname == null) {
            putLog("Invalid login attempt: " + login);
            client.authFail();
            return;
        } else {
            ClientThread oldClient = findClientByNickname(nickname);
            client.authAccept(nickname);
            if (oldClient == null) {
             putLog("Connect with "+nickname);
            } else {
                putLog("Reconnect "+nickname);
                oldClient.reconnect();
                CLIENTS.remove(oldClient);
            }
        }
        //Send information of athorization
    }
    private synchronized ClientThread findClientByNickname(String nickname) {
        for (int i = 0; i < CLIENTS.size(); i++) {
            ClientThread client = (ClientThread) CLIENTS.get(i);
            if (!client.isAuthorized()) continue;
            if (client.getNickname().equals(nickname))
                return client;
        }
        return null;
    }
}
