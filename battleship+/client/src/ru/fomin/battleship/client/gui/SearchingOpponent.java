package ru.fomin.battleship.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchingOpponent extends JFrame implements ActionListener {
    private final String TITLE = "Searching opponent";
    private final String TEXT_LABEL_PROCESS = "Searching opponent";
    private final int WIDTH = 300;
    private final int HEIGHT = 200;
    private PreparingForGameFrame preparingForGameFrame;
    private final JLabel LABEL_PROCESS_SHOW = new JLabel(TEXT_LABEL_PROCESS);
    private final JButton BUTTON_STOP=new JButton("STOP");

    private final Font FONT_FOR_BUTTON = new Font(Font.SERIF, Font.BOLD, 30);
    private final Font FONT_FOR_TEXT = new Font(Font.SERIF, Font.BOLD, 28);
    private final Color COLOR_OF_BUTTON = new Color(151, 5, 41);

    public SearchingOpponent(PreparingForGameFrame preparingForGameFrame) {
        this.preparingForGameFrame = preparingForGameFrame;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);

        LABEL_PROCESS_SHOW.setFont(FONT_FOR_TEXT);
        LABEL_PROCESS_SHOW.setHorizontalAlignment(SwingConstants.CENTER);
        BUTTON_STOP.setFont(FONT_FOR_BUTTON);
        BUTTON_STOP.setBackground(COLOR_OF_BUTTON);
        BUTTON_STOP.addActionListener(this);
        add(LABEL_PROCESS_SHOW,BorderLayout.CENTER);
        add(BUTTON_STOP,BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        preparingForGameFrame.stopSearching();
        preparingForGameFrame.setVisible(true);
        dispose();
    }
    public void showProcess(int allDots){
        allDots=allDots % 17;
        String dots="*";
        for(int i=0; i<allDots; i++)
        dots+="*";
        LABEL_PROCESS_SHOW.setText("<html>"+TEXT_LABEL_PROCESS+"<br>"+dots+"</html>");
    }
}
