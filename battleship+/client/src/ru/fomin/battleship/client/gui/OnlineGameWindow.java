package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;
import ru.fomin.battleship.common.LibraryOfPrefixes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnlineGameWindow extends JFrame implements ActionListener {
    private final String opponentNickname;
    private final String NICK_NAME;
    private String mapCodeOfUser;
    private String mapCodeOfOpponent="";
    private WorkingWithNetwork listener;
    private final int WIDTH=1200;
    private final int HEIGHT=700;



    JButton BUTTON_SEND=new JButton("SEND MESSAGE");

    public OnlineGameWindow(String opponentNickname, String nickName, String mapCodeOfUser, WorkingWithNetwork listener) {
        this.opponentNickname = opponentNickname;
        NICK_NAME = nickName;
        this.mapCodeOfUser = mapCodeOfUser;
        this.listener=listener;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        listener.sendMessageToServer(LibraryOfPrefixes.getMapCodeMessage(mapCodeOfUser));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(NICK_NAME + " VS " + opponentNickname);
        setResizable(false);

        BUTTON_SEND.addActionListener(this);






        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BUTTON_SEND)) {
            listener.sendMessageToServer(LibraryOfPrefixes.getChatMessage(""));
            return;
        }

        throw new RuntimeException("Unknown source: " + source);
    }

    public void setChatMessage(String message){

    }
    public void setMapCodeOfOpponent(String mapCodeOfOpponent){
        this.mapCodeOfOpponent=mapCodeOfOpponent;
    }
}
