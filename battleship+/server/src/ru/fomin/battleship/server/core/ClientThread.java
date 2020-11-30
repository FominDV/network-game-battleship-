package ru.fomin.battleship.server.core;

import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import java.net.Socket;
import java.util.Vector;

public class ClientThread extends SocketThread {
    private String nickname;
    private String login;
    private boolean isAuthorized;
    private boolean isReconnecting;
    private boolean isSearching=false;
    private ClientThread opponentThread;
    private String opponentNickname = "empty";

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }
   public void setSearchingOpponent(){
        isSearching=true;
   }
    public void setStopSearchingOpponent(){
        isSearching=false;
    }
    public boolean getSearchingOpponentStatus(){
        return isSearching;
    }
    public String getNickname() {
        return nickname;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public boolean isReconnecting() {
        return isReconnecting;
    }

    void reconnect() {
        isReconnecting = true;
        close();
    }

    public void setOpponentNickname(String opponentNickname) {
        this.opponentNickname = opponentNickname;
    }
    public String getOpponentNickname(){
        return opponentNickname;
    }

    void authAccept(String nickname) {
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(LibraryOfPrefixes.getAuthAccept(nickname));
    }

    void authFail() {
        sendMessage(LibraryOfPrefixes.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(LibraryOfPrefixes.getMsgFormatError(msg));
        close();
    }
public void setLogin(String login){
        this.login=login;
}
    public String getLogin(){
        return login;
    }

    public void updateDataMap(Vector<String> dataMap) {
        sendMessage(LibraryOfPrefixes.getDataOfMapList(dataMap));
    }

}



