package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.common.LibraryOfPrefixes;
import ru.fomin.network.SocketThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreparingForGameFrame extends JFrame {
    private final int WIDTH=600;
    private final int HEIGHT=500;
    private final String WINDOW_TITLE="Map-Maker";
    private final JButton send = new JButton("send");
    private SocketThread socketThread;
    public PreparingForGameFrame(SocketThread socketThread){
        this.socketThread=socketThread;
        SwingUtilities.invokeLater(()->initialization());
    }
    private void initialization(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        setResizable(false);
        send.addActionListener(e -> socketThread.sendMessage("A"+ LibraryOfPrefixes.DELIMITER+"a"));
        add(send);
        setVisible(true);
    }

}
