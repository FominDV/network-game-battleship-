package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.WorkingWithNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class SavingMapWindow extends JFrame implements ActionListener {
    private PreparingForGameFrame preparingForGameFrame;
    private WorkingWithNetwork listener;
    private Vector<String[]> dataMapVector=new Vector<>();
    private final String TITLE="Saving map menu";
    private final int WIDTH = 400;
    private final int HEIGHT = 350;
    private final String HEAD_TEXT="MENU OF SAVINGS";

    private final JLabel LABEL_HEAD =new JLabel(HEAD_TEXT);
    private final JButton BUTTON_LOAD=new JButton("<html>LOAD<br>THE MAP</html>");
    private final JButton BUTTON_REMOVE=new JButton("<html>REMOVE<br>THE MAP</html>");
    private final JButton BUTTON_EXIT=new JButton("<html>EXIT<br>TO MAP BUILDER</html>");
    private final JList<String> SAVINGS_OF_MAP_LIST = new JList<>();
    private final Font FONT_FOR_BUTTONS = new Font(Font.SERIF, Font.BOLD, 32);
    private final Font FONT_FOR_LIST = new Font(Font.SERIF, Font.BOLD, 28);
    private final Font FONT_FOR_HEAD = new Font(Font.SERIF, Font.BOLD, 34);
    private final JPanel PANEL_RIGHT =new JPanel(new GridLayout(3,1));
    private final Color COLOR_OF_HEAD_TEXT = new Color(5, 50, 75);

    public SavingMapWindow(PreparingForGameFrame preparingForGameFrame, WorkingWithNetwork listener, Vector<String[]> dataMapVector){
        this.preparingForGameFrame=preparingForGameFrame;
        this.listener=listener;
        this.dataMapVector=dataMapVector;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        SAVINGS_OF_MAP_LIST.setFont(FONT_FOR_LIST);
        BUTTON_LOAD.setFont(FONT_FOR_BUTTONS);
        LABEL_HEAD.setFont(FONT_FOR_HEAD);
        LABEL_HEAD.setForeground(COLOR_OF_HEAD_TEXT);
        BUTTON_REMOVE.setFont(FONT_FOR_BUTTONS);
        BUTTON_EXIT.setFont(FONT_FOR_BUTTONS);
        PANEL_RIGHT.add(BUTTON_LOAD);
        PANEL_RIGHT.add(BUTTON_REMOVE);
        PANEL_RIGHT.add(BUTTON_EXIT);
        add(PANEL_RIGHT, BorderLayout.EAST);
        add(LABEL_HEAD,BorderLayout.NORTH);
        add(SAVINGS_OF_MAP_LIST, BorderLayout.CENTER);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BUTTON_LOAD)) {

            return;
        }
        if (source.equals(BUTTON_REMOVE)) {

            return;
        }
        if (source.equals(BUTTON_EXIT)) {

            return;
        }
        throw new RuntimeException("Unknown source: " + source);
    }
}
