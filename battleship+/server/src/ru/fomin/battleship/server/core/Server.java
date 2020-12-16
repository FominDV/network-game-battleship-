package ru.fomin.battleship.server.core;

import static ru.fomin.battleship.common.LibraryOfPrefixes.*;
import ru.fomin.network.ServerSocketThread;
import ru.fomin.network.ServerSocketThreadListener;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import static java.lang.Math.random;
import static java.lang.String.format;

public class Server implements ServerSocketThreadListener, SocketThreadListener {
    private Random random;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final ServerListener listener;
    private ServerSocketThread thread;
    private final Vector<SocketThread> CLIENTS;

    public Server(ServerListener listener) {
        this.listener = listener;
        CLIENTS = new Vector<>();
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
        for (SocketThread socket : CLIENTS) {
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
        ClientThread client = (ClientThread) thread;
        ClientThread otherClient = findClientByNickname(client.getOpponentNickname());
        CLIENTS.remove(thread);
        client.close();
        if (otherClient != null) {
            otherClient.sendMessage(DISCONNECT_OPPONENT);
            otherClient.setStopSearchingOpponent();
            putLog("Connection with " + client.getNickname() + " was lost");
            putLog(client.getNickname() + " was disconnected with " + otherClient.getNickname());
        }
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
        String[] arr = msg.split(DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case MESSAGE_ABOUT_START_SEARCHING:
                putLog(client.getNickname() + " started searching opponent");
                break;
            case SEARCH_OPPONENT:
                searchingOpponent(client);
                break;
            case STOP_SEARCHING:
                client.setStopSearchingOpponent();
                putLog(client.getNickname() + " stopped searching opponent");
                break;
            case DATA_SAVING:
                if (!SQLClient.isValidNameForSave(arr[2], arr[1])) {
                    client.sendMessage(DUPLICATE_NAME);
                    break;
                }
                if (SQLClient.setNewDataMap(arr)) {
                    putLog(client.getNickname() + " saved the map");
                    client.sendMessage(SUCCESSFUL_SAVE);
                } else {
                    putLog(client.getNickname() + "saving the map failed");
                    client.sendMessage(FAIL_SAVE);
                    break;
                }
                client.updateDataMap(SQLClient.getDataMap(client.getLogin()));
                break;
            case REMOVE_DATA:
                SQLClient.removeData(arr[1], arr[2]);
                client.updateDataMap(SQLClient.getDataMap(client.getLogin()));
                putLog(client.getNickname() + " removed the \"" + arr[2] + "\" data");
                break;
            case CHAT_MESSAGE:
                sendMessageToOpponent(client,getChatMessage(editChatMessage(arr[1], client.getNickname())));
                putLog(client.getNickname() + " send message to " + client.getOpponentNickname());
                break;
            case CHANGE_TURN:
                sendMessageToOpponent(client,CHANGE_TURN);
                putLog("Now the turn of " + client.getOpponentNickname());
                break;
            case EXIT_TO_MAP_BUILDER:
                client.sendMessage(EXIT_TO_MAP_BUILDER);
                putLog(client.getOpponentNickname()+" left the game VS "+client.getOpponentNickname());
                break;
            case CODE_OF_GAME_TURN:
                sendMessageToOpponent(client,getCodeOfGameTurnMessage(arr[1]));
                putLog(client.getNickname()+" made game turn");
                break;
            case CODE_OF_RESULT_TURN:
                sendMessageToOpponent(client,getCodeResultOfTurn(arr[1]));
                break;
            case LOG_MESSAGE:
                sendMessageToOpponent(client,getLogMessage(arr[1]));
                putLog(arr[1]);
                break;
            case VICTORY:
                sendMessageToOpponent(client,VICTORY);
                putLog(client.getNickname()+" lost the game\n"+client.getOpponentNickname()+" won the game");
                break;
            case CODE_OF_MAP_AFTER_GAME:
                sendMessageToOpponent(client,getCodeOfMapAfterGameMessage(arr[1]));
                break;
            case LOG_LAST_MESSAGE:
                sendMessageToOpponent(client,getLogLastPartMessage(arr[1]));
                break;
            case PLAY_AGAIN:
                sendMessageToOpponent(client,PLAY_AGAIN);
                putLog(client.getNickname()+" want to play again with "+client.getOpponentNickname());
                break;
            case READY_PLAY_AGAIN:
                sendMessageToOpponent(client,READY_PLAY_AGAIN);
                putLog(client.getNickname()+" is ready to play again with "+client.getOpponentNickname());
                break;
            case START_PLAY_AGAIN:
                sendMessageToOpponent(client,START_PLAY_AGAIN);
                putLog(client.getNickname()+" start to play again with "+client.getOpponentNickname());
                break;
            case GET_FIRST_TURN:
                setFirstTurnForGame(client,findClientByNickname(client.getOpponentNickname()));
                break;
            default:
                client.msgFormatError(msg);
        }
    }
    private void sendMessageToOpponent(ClientThread client, String message){
        findClientByNickname(client.getOpponentNickname()).sendMessage(message);
    }
    private String editChatMessage(String message, String nickName) {
        Date date = new Date();
        return format("%s(%tR):\n%s", nickName, date, message);
    }

    private synchronized void searchingOpponent(ClientThread client) {
        client.setSearchingOpponent();
        for (SocketThread searchingClient : CLIENTS) {
            if (((ClientThread) searchingClient).getOpponentNickname().equals("empty") && ((ClientThread) searchingClient).getSearchingOpponentStatus() && !(client.getNickname().equals(((ClientThread) searchingClient).getNickname())) && ((ClientThread) searchingClient).getNickname() != null) {
                client.setOpponentNickname(((ClientThread) searchingClient).getNickname());
                ((ClientThread) searchingClient).setOpponentNickname(client.getNickname());
                client.sendMessage(getSearchOpponent(((ClientThread) searchingClient).getNickname()));
                searchingClient.sendMessage((getSearchOpponent(client.getNickname())));
                putLog(client.getNickname() + " connected with " + ((ClientThread) searchingClient).getNickname());
               setFirstTurnForGame(client,searchingClient);
            }
        }
    }

    private void setFirstTurnForGame(ClientThread user1, SocketThread user2){
        if (turnOfClient()) {
            user1.sendMessage(getTurnMessage(1));
            user2.sendMessage(getTurnMessage(0));
            putLog(user1.getNickname() + " get the first turn");
        } else {
            user1.sendMessage(getTurnMessage(0));
            user2.sendMessage(getTurnMessage(1));
            putLog(((ClientThread)user2).getNickname() + " get the first turn");
        }
    }
    private boolean turnOfClient() {
        random = new Random(System.currentTimeMillis());
        if (random()>0.5) return true;
        else return false;
    }

    private void handleNonAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(DELIMITER);
        if (arr.length == 4 && arr[0].equals(REGISTRATION)) {
            try {
                if (SQLClient.setClientData(arr[1], arr[2], arr[3])) {
                    client.sendMessage(getRegistrationAnswer("true"));
                    putLog(String.format("New client '%s' with nickname '%s' was created into database.", arr[1], arr[3]));
                } else {
                    client.sendMessage(getRegistrationAnswer("false"));
                    putLog(String.format("Error of creating new client. Client '%s' is already registered.", arr[1]));
                }
            } catch (SQLException e) {
                putLog("SQL error: " + e.getStackTrace().toString());
            }
            return;
        }
        if (arr.length != 3 || !arr[0].equals(AUTH_REQUEST)) {
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
            client.updateDataMap(SQLClient.getDataMap(arr[1]));
            client.setLogin(arr[1]);
            if (oldClient == null) {
                putLog("Connect with " + nickname);
            } else {
                putLog("Reconnect " + nickname);
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
