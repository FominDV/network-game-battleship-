package ru.fomin.battleship.client.gui;

import javax.swing.*;

public class PreparingForGameFrame extends JFrame {
    private final int WIDTH=600;
    private final int HEIGHT=500;
    private final String WINDOW_TITLE="Map-Maker";
    public PreparingForGameFrame(){
        SwingUtilities.invokeLater(()->initialization());
    }
    private void initialization(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        setVisible(true);
    }

}
