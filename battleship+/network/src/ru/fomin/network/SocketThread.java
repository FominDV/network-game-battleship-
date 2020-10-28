package ru.fomin.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class SocketThread extends Thread {
    private final SocketThreadListener listener;
    private final Socket SOCKET;
    private DataOutputStream out;
    private DataInputStream in;

    public SocketThread(SocketThreadListener listener, String name, Socket socket) {
        super(name);
        this.SOCKET = socket;
        this.listener = listener;
        start();
    }


    @Override
    public void run() {
        try {
            listener.onSocketStart(this, SOCKET);
            in = new DataInputStream(SOCKET.getInputStream());
            out = new DataOutputStream(SOCKET.getOutputStream());
            listener.onSocketReady(this, SOCKET);
            while (!isInterrupted()) {
                String msg = in.readUTF();
                listener.onReceiveString(this, SOCKET, msg);
            }
        } catch (EOFException | SocketException e) {
        } catch (IOException e) {
            listener.onSocketException(this, e);
        } finally {
            try {
                SOCKET.close();
            } catch (IOException e) {
                listener.onSocketException(this, e);
            }
            listener.onSocketStop(this);
        }
    }

    public synchronized boolean sendMessage(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
            return true;
        } catch (IOException e) {
            listener.onSocketException(this, e);
            close();
            return false;
        }
    }

    public synchronized void close() {
        try {
            in.close();
            out.close();
            SOCKET.close();
        } catch (IOException e) {
            listener.onSocketException(this, e);
        }
        interrupt();
    }
}


