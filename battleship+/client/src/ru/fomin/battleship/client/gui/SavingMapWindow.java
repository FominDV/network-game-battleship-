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
    private Vector<String[]> dataMapVector = new Vector<>();
    private final String TITLE = "Saving map menu";
    private final int WIDTH = 400;
    private final int HEIGHT = 300;
    private final String HEAD_TEXT = "MENU OF SAVINGS";

    private final JLabel LABEL_HEAD = new JLabel(HEAD_TEXT);
    private final JButton BUTTON_LOAD = new JButton("<html><p align='center'>LOAD<br>THE MAP</p></html>");
    private final JButton BUTTON_REMOVE = new JButton("<html><p align='center'>REMOVE<br>THE MAP</p></html>");
    private final JButton BUTTON_EXIT = new JButton("<html><p align='center'>EXIT TO<br>THE MAP BUILDER</p></html>");
    private final JList<String> SAVINGS_OF_MAP_LIST = new JList<>();
    private final Font FONT_FOR_BUTTONS = new Font(Font.SERIF, Font.BOLD, 18);
    private final Font FONT_FOR_LIST = new Font(Font.SERIF, Font.BOLD, 18);
    private final Font FONT_FOR_HEAD = new Font(Font.SERIF, Font.BOLD, 34);
    private final JPanel PANEL_RIGHT = new JPanel(new GridLayout(3, 1));
    private final Color COLOR_OF_HEAD_TEXT = new Color(24, 104, 153);

    public SavingMapWindow(PreparingForGameFrame preparingForGameFrame, WorkingWithNetwork listener, Vector<String[]> dataMapVector) {
        this.preparingForGameFrame = preparingForGameFrame;
        this.listener = listener;
        this.dataMapVector = dataMapVector;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        SAVINGS_OF_MAP_LIST.setFont(FONT_FOR_LIST);
        fillSavingList();
        JScrollPane scrollSavingList = new JScrollPane(SAVINGS_OF_MAP_LIST);
        LABEL_HEAD.setFont(FONT_FOR_HEAD);
        LABEL_HEAD.setForeground(COLOR_OF_HEAD_TEXT);
        LABEL_HEAD.setHorizontalAlignment(SwingConstants.CENTER);
        BUTTON_LOAD.setFont(FONT_FOR_BUTTONS);
        BUTTON_REMOVE.setFont(FONT_FOR_BUTTONS);
        BUTTON_EXIT.setFont(FONT_FOR_BUTTONS);
        BUTTON_LOAD.addActionListener(this);
        BUTTON_REMOVE.addActionListener(this);
        BUTTON_EXIT.addActionListener(this);
        PANEL_RIGHT.add(BUTTON_LOAD);
        PANEL_RIGHT.add(BUTTON_REMOVE);
        PANEL_RIGHT.add(BUTTON_EXIT);
        add(PANEL_RIGHT, BorderLayout.EAST);
        add(LABEL_HEAD, BorderLayout.NORTH);
        add(scrollSavingList, BorderLayout.CENTER);
        setVisible(true);
    }

    private void fillSavingList() {
        String[] namesOfSavings = new String[dataMapVector.size()];
        for (int i = 0; i < dataMapVector.size(); i++) {
            namesOfSavings[i] = dataMapVector.get(i)[0];
        }
        SAVINGS_OF_MAP_LIST.setListData(namesOfSavings);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BUTTON_LOAD)) {

            return;
        }
        if (source.equals(BUTTON_REMOVE)) {
            String selectedName = SAVINGS_OF_MAP_LIST.getSelectedValue();
            if (selectedName != null && preparingForGameFrame.isSavingConfirmMessageYesNo("Are you sure that you want to remove this data?")) {
                preparingForGameFrame.removeData(selectedName);
                int bufferLength = dataMapVector.size();
                //Wait while date will be removed
                while (bufferLength == dataMapVector.size()) {
                    dataMapVector = listener.getDataMap();
                }
                fillSavingList();
            }
            return;
        }
        if (source.equals(BUTTON_EXIT)) {
            exit();
            return;
        }
        throw new RuntimeException("Unknown source: " + source);
    }

    private void exit() {
        preparingForGameFrame.setVisible(true);
        dispose();
    }

}
