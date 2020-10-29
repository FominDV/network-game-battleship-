package ru.fomin.battleship.server.core;

import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;
import ru.fomin.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {
    private String nickname;
    private boolean isAuthorized;
    private boolean isReconnecting;
    private String otherNickname = null;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
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

    public void setOtherNickname(String otherNickname) {
        this.otherNickname = otherNickname;
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


}


