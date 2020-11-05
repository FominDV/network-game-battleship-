package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreparingForGameFrame extends JFrame {
    JButton btn = new JButton("Connect");
    private final int WIDTH = 600;
    private final int HEIGHT = 500;
    private final String WINDOW_TITLE = "Map-Maker by ";

    private final SocketThread SOCKET_THREAD;
    private final String NICK_NAME;
    private final WorkingWithNetwork listener;
    private String opponentNickname = "empty";

    public PreparingForGameFrame(SocketThread socketThread, String nickname, WorkingWithNetwork listener) {
        NICK_NAME = nickname;
        SOCKET_THREAD = socketThread;
        this.listener = listener;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE + NICK_NAME);
        setResizable(false);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOpponent();
            }
        });
        add(btn);
        setVisible(true);

    }

    private void searchOpponent() {
        long time = System.currentTimeMillis() / 1000;
        while (opponentNickname.equals("empty")) {
            listener.sendMessageToServer(LibraryOfPrefixes.getSearchOpponent(NICK_NAME));
            if (System.currentTimeMillis() / 1000 - time >= 5 && opponentNickname.equals("empty")) {
                timeOut();
                break;
            }
        }
        setTitle(NICK_NAME + " VS " + opponentNickname);
    }

    public void setOpponentNickname(String opponentNickname) {
        this.opponentNickname = opponentNickname;
    }


    public void reconnect() {
        this.opponentNickname = "empty";
        setTitle(WINDOW_TITLE + NICK_NAME);
    }

    public void timeOut() {
        JOptionPane.showMessageDialog(null, "The opponent was not found", "Exception", JOptionPane.ERROR_MESSAGE);
    }
}
