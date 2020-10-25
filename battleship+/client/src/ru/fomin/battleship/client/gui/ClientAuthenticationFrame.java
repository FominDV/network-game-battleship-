package ru.fomin.battleship.client.gui;

import ru.fomin.battleship.client.client_core.Handler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientAuthenticationFrame extends JFrame implements ActionListener {
    private final String INVALID_IP = "Invalid ip: ";
    private final String INVALID_PORT = "Invalid port: ";
    private final String EDIT_IP_TEXT = "Input new ip address\nNow ip: ";
    private final String EDIT_PORT_TEXT = "Input new port\nNow port: ";
    public final Handler HANDLER = new Handler();
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final JPanel MAIN_PANEL = new JPanel(new GridLayout(7, 1));
    private final JPanel[] SUB_PANEL = new JPanel[4];
    private final String WINDOW_TITLE = "Authentication";
    private final JButton BTN_LOGIN = new JButton("Login");
    private final JButton BTN_REGISTRATION = new JButton("Registration");
    private final JButton BTN_EDIT_IP = new JButton("Edit IP");
    private final JButton BTN_EDIT_PORT = new JButton("Edit port");
    private final JLabel LABEL_LOGIN = new JLabel("Login: ");
    private final JLabel LABEL_PASSWORD = new JLabel("Password: ");
    private final JPasswordField PASSWORD_FIELD = new JPasswordField();
    private final JTextField LOGIN_FIELD = new JTextField();
    private final JLabel LABEL_IP = new JLabel();
    private final JLabel LABEL_PORT = new JLabel();
    private final String LABEL_IP_TEXT = "ip: ";
    private final String LABEL_PORT_TEXT = "port: ";
    private String ip = "127.0.0.1";
    private int port = 8189;
    private final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 16);
    private final Font TEXT_FONT_CONNECTION = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private final String TEXT_LABEL_CONNECTING = "AUTHORIZATION";
    private final JLabel LABEL_CONNECTING = new JLabel(TEXT_LABEL_CONNECTING);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientAuthenticationFrame());
    }

    public ClientAuthenticationFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        LABEL_IP.setText(LABEL_IP_TEXT + ip);
        LABEL_PORT.setText(LABEL_PORT_TEXT + port);
        BTN_EDIT_IP.addActionListener(this);
        BTN_EDIT_PORT.addActionListener(this);
        BTN_LOGIN.addActionListener(this);
        BTN_REGISTRATION.addActionListener(this);
        LABEL_IP.setFont(TEXT_FONT);
        LABEL_PORT.setFont(TEXT_FONT);
        LABEL_LOGIN.setFont(TEXT_FONT);
        LABEL_PASSWORD.setFont(TEXT_FONT);
        for (int i = 0; i < SUB_PANEL.length; i++) SUB_PANEL[i] = new JPanel(new GridLayout(1, 2));
        SUB_PANEL[0].add(LABEL_IP);
        SUB_PANEL[0].add(BTN_EDIT_IP);
        SUB_PANEL[1].add(LABEL_PORT);
        SUB_PANEL[1].add(BTN_EDIT_PORT);
        SUB_PANEL[2].add(LABEL_LOGIN);
        SUB_PANEL[2].add(LOGIN_FIELD);
        SUB_PANEL[3].add(LABEL_PASSWORD);
        SUB_PANEL[3].add(PASSWORD_FIELD);
        MAIN_PANEL.add(LABEL_CONNECTING);
        for (JPanel panel : SUB_PANEL) MAIN_PANEL.add(panel);
        MAIN_PANEL.add(BTN_LOGIN);
        MAIN_PANEL.add(BTN_REGISTRATION);
        LABEL_CONNECTING.setFont(TEXT_FONT_CONNECTION);
        LABEL_CONNECTING.setHorizontalAlignment(SwingConstants.CENTER);
        add(MAIN_PANEL);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BTN_EDIT_IP)) {
            editIP();
            return;
        }
        if (source.equals(BTN_EDIT_PORT)) {
            editPort();
            return;
        }
        if (source.equals(BTN_LOGIN)) {
            login();
            return;
        }
        if (source.equals(BTN_REGISTRATION)) {
            registration();
            return;
        }
        throw new RuntimeException("Unknown source: " + source);
    }

    private void editIP() {
        try {
            String newIP = JOptionPane.showInputDialog(EDIT_IP_TEXT + ip);
            if (isValidIP(newIP)) {
                ip = newIP;
                LABEL_IP.setText(LABEL_IP_TEXT + ip);
            } else {
                JOptionPane.showMessageDialog(null, INVALID_IP + newIP, "Invalid IP ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void editPort() {
        String stringPort = "unknown";
        try {
            stringPort = JOptionPane.showInputDialog(EDIT_PORT_TEXT + port);
            if(stringPort.equals("")) return;
            int newPort = Integer.parseInt(stringPort);
            if (isValidPort(newPort)) {
                port = newPort;
                LABEL_PORT.setText(LABEL_PORT_TEXT + port);
            } else {
                showValidPortError(stringPort);
            }
        } catch (NumberFormatException e) {
            showValidPortError(stringPort);
        }
    }

    private void showConnectionError(Thread t, Throwable e) {
        JOptionPane.showMessageDialog(null,"connection to the server is not established" , "Connection ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void registration() {

    }

    private void login() {
        {
            try {
                HANDLER.login(ip, port, this, HANDLER, LOGIN_FIELD.getText());
            } catch (IOException exception) {
                showConnectionError(Thread.currentThread(), exception);
            }
        }
    }

    private boolean isValidIP(String ip) {
        String[] octets = ip.split("\\.");
        if (octets.length != 4) return false;
        if (Integer.parseInt(octets[0]) <= 0) return false;
        boolean isOctetsValueZero = true;
        for (int i = 0; i < octets.length; i++) {
            int octetValue = Integer.parseInt(octets[i]);
            if (octetValue < 0 || octetValue > 255) return false;
            if (i > 0 && octetValue != 0) isOctetsValueZero = false;
        }
        if (isOctetsValueZero) return false;
        return true;
    }

    private boolean isValidPort(int port) {
        if (port > 0 && port <= 65536) return true;
        return false;
    }

    private void showValidPortError(String port) {
        JOptionPane.showMessageDialog(null, INVALID_PORT + port, "Invalid port ERROR", JOptionPane.ERROR_MESSAGE);
    }


    public void clearFields() {
        LOGIN_FIELD.setText("");
        PASSWORD_FIELD.setText("");
    }

    public String getLogin() {
        return LOGIN_FIELD.getText();
    }
    public char[] getPassword(){
        return PASSWORD_FIELD.getPassword();
    }
}
