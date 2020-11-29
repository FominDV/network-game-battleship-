package ru.fomin.battleship.client.gui;

import javax.swing.*;

public class SearchingOpponent extends JFrame {
    private final String TITLE="Searching opponent";
    private final int WIDTH=800;
    private final int HEIGHT=400;
    public SearchingOpponent(){

        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);

        setVisible(true);
    }
}
