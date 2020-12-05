package ru.fomin.battleship.server.gui;

import ru.fomin.battleship.server.core.Server;
import ru.fomin.battleship.server.core.ServerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ServerListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final Server SERVER = new Server(this);
    private final JButton BTN_START = new JButton("Start");
    private final JButton BTN_STOP = new JButton("Stop");
    private final JPanel MAIN_PANEL_TOP =new JPanel(new GridLayout(2,1));
    private final JPanel PANEL_TOP = new JPanel(new GridLayout(1, 2));
    private final JTextArea LOG = new JTextArea();
    private final JLabel HEADER_LABEL =new JLabel("Server GUI");
    Font TEXT_HEADER=new Font(Font.SANS_SERIF, Font.BOLD, 26);
    Font TEXT_BUTTON=new Font(Font.SANS_SERIF, Font.BOLD, 20);


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
    }

    private ServerGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize( WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("SERVER");
        HEADER_LABEL.setFont(TEXT_HEADER);
        HEADER_LABEL.setHorizontalAlignment(SwingConstants.CENTER);
        LOG.setEditable(false);
        LOG.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(LOG);
        BTN_START.addActionListener(this);
        BTN_STOP.addActionListener(this);
        BTN_START.setFont(TEXT_BUTTON);
        BTN_STOP.setFont(TEXT_BUTTON);
        PANEL_TOP.add(BTN_START);
        PANEL_TOP.add(BTN_STOP);
        MAIN_PANEL_TOP.add(HEADER_LABEL);
        MAIN_PANEL_TOP.add(PANEL_TOP);
        add(MAIN_PANEL_TOP, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void onServerMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            LOG.append(msg + "\n");
            LOG.setCaretPosition(LOG.getDocument().getLength());
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == BTN_STOP) {
            SERVER.stop();
        } else if (src == BTN_START) {
//            throw new RuntimeException("Hello from EDT!");
            SERVER.start(8189);
        } else {
            throw new RuntimeException("Unknown source: " + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        msg = "Exception in " + t.getName() + " " +
                e.getClass().getCanonicalName() + ": " +
                e.getMessage() + "\n\t at " + ste[0];
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
