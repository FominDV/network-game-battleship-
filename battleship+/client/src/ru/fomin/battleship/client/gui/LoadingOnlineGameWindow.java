package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

public class LoadingOnlineGameWindow extends JFrame implements Runnable {
    private final int WIDTH=350;
    private final int HEIGHT=200;
    private final String TITLE="LOADING GAME WINDOW";
    private final String TEXT_FOR_LABEL_LOADING_PROCESS="LOADING WINDOW";
    private OnlineGameWindow onlineGameWindow;
    private WorkingWithNetwork listener;
    private Thread loadingThread = new Thread(this);
    private final JLabel LABEL_LOADING_PROCESS=new JLabel(TEXT_FOR_LABEL_LOADING_PROCESS);
    private final Font FONT_FOR_TEXT = new Font(Font.SERIF, Font.BOLD, 30);

    public LoadingOnlineGameWindow(OnlineGameWindow onlineGameWindow, WorkingWithNetwork listener) {
        this.onlineGameWindow = onlineGameWindow;
        this.listener=listener;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        LABEL_LOADING_PROCESS.setFont(FONT_FOR_TEXT);
        LABEL_LOADING_PROCESS.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_LOADING_PROCESS.setForeground(Color.RED);
        add(LABEL_LOADING_PROCESS,BorderLayout.CENTER);
        setVisible(true);
        loadingThread.start();
    }

    @Override
    public void run() {
        String dot="*";
        String dotsString;
        int dots=0;
        while (!onlineGameWindow.isGotMapCodeOfOpponent()){
            dots=dots % 14;
            dotsString="*";
            for(int i=0;i<dots;i++){
                dotsString+=dot;
            }
            dots++;
            LABEL_LOADING_PROCESS.setText("<html>"+TEXT_FOR_LABEL_LOADING_PROCESS+"<br>"+dotsString+"</html>");
            try {
                sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        onlineGameWindow.addShipsOnTheMap();
        onlineGameWindow.setVisible(true);
        dispose();
    }
}
