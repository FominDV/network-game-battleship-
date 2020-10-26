package ru.fomin.battleship.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationFrame extends JFrame implements ActionListener {
    private final String TITLE = "Registration";
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final ClientAuthenticationFrame CLIENT_AUTHENTICATION_FRAME;
    private final JPanel MAIN_PANEL = new JPanel(new GridLayout(6, 1));
    private final JPanel[] SUB_PANEL = new JPanel[4];
    private final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 16);
    private final Font TEXT_BOTTOM_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private final Font TEXT_FONT_HEADER = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private final JLabel LABEL_HEADER = new JLabel("REGISTRATION");
    private final JLabel LABEL_LOGIN = new JLabel("Login:");
    private final JLabel LABEL_EMAIL = new JLabel("Email:");
    private final JLabel LABEL_PASSWORD = new JLabel("Password:");
    private final JLabel LABEL_REPEAT_PASSWORD = new JLabel("Repeat password:");
    private final JTextField FIELD_LOGIN = new JTextField();
    private final JTextField FIELD_EMAIL = new JTextField();
    private final JPasswordField FIELD_PASSWORD = new JPasswordField();
    private final JPasswordField FIELD_REPEAT_PASSWORD = new JPasswordField();
    private final JPanel BOTTOM_PANEL=new JPanel(new GridLayout(2,1));
    private final JPanel NETWORK_PANEL=new JPanel(new GridLayout(1,2));
    private final JButton BUTTON_REGISTRATION=new JButton("REGISTRATION");
    private final JButton BUTTON_CANCEL=new JButton("CANCEL");
    private final JLabel LABEL_IP=new JLabel();
    private final JLabel LABEL_PORT=new JLabel();
    private final String IP;
    private final int PORT;

    public RegistrationFrame(ClientAuthenticationFrame CLIENT_AUTHENTICATION_FRAME, String ip, int port) {
        this.CLIENT_AUTHENTICATION_FRAME = CLIENT_AUTHENTICATION_FRAME;
        IP=ip;
        PORT=port;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(TITLE);
        setResizable(false);
        for (int i = 0; i < SUB_PANEL.length; i++) SUB_PANEL[i] = new JPanel(new GridLayout(1, 2));
        LABEL_HEADER.setFont(TEXT_FONT_HEADER);
        LABEL_HEADER.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_EMAIL.setFont(TEXT_FONT);
        LABEL_LOGIN.setFont(TEXT_FONT);
        LABEL_PASSWORD.setFont(TEXT_FONT);
        LABEL_REPEAT_PASSWORD.setFont(TEXT_FONT);
        FIELD_EMAIL.setFont(TEXT_FONT);
        FIELD_LOGIN.setFont(TEXT_FONT);
        FIELD_PASSWORD.setFont(TEXT_FONT);
        FIELD_REPEAT_PASSWORD.setFont(TEXT_FONT);
        SUB_PANEL[0].add(LABEL_EMAIL);
        SUB_PANEL[0].add(FIELD_EMAIL);
        SUB_PANEL[1].add(LABEL_LOGIN);
        SUB_PANEL[1].add(FIELD_LOGIN);
        SUB_PANEL[2].add(LABEL_PASSWORD);
        SUB_PANEL[2].add(FIELD_PASSWORD);
        SUB_PANEL[3].add(LABEL_REPEAT_PASSWORD);
        SUB_PANEL[3].add(FIELD_REPEAT_PASSWORD);
        MAIN_PANEL.add(LABEL_HEADER);
        for (JPanel panel : SUB_PANEL) MAIN_PANEL.add(panel);
        MAIN_PANEL.add(BUTTON_REGISTRATION);
        BUTTON_REGISTRATION.setFont(TEXT_FONT_HEADER);
        BUTTON_CANCEL.setFont(TEXT_FONT_HEADER);
        BUTTON_CANCEL.setBackground(Color.RED);
        LABEL_IP.setText("IP: "+IP);
        LABEL_PORT.setText("PORT: "+PORT);
        LABEL_IP.setFont(TEXT_BOTTOM_FONT);
        LABEL_PORT.setFont(TEXT_BOTTOM_FONT);
        NETWORK_PANEL.add(LABEL_IP);
        NETWORK_PANEL.add(LABEL_PORT);
        BOTTOM_PANEL.add(NETWORK_PANEL);
        BOTTOM_PANEL.add(BUTTON_CANCEL);
        BUTTON_REGISTRATION.addActionListener(this);
        BUTTON_CANCEL.addActionListener(this);
        add(MAIN_PANEL, BorderLayout.CENTER);
        add(BOTTOM_PANEL,BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        if(source.equals(BUTTON_CANCEL)){
            CLIENT_AUTHENTICATION_FRAME.setVisible(true);
            dispose();
            return;
        }
        if (source.equals(BUTTON_REGISTRATION)){

            return;
        }
        throw new RuntimeException("Unknown source: " + source);
    }
}
