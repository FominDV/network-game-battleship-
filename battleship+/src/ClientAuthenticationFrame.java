import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientAuthenticationFrame extends JFrame implements ActionListener {
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final JPanel MAIN_PANEL = new JPanel(new GridLayout(6, 1));
    private final JPanel[] SUB_PANEL = new JPanel[4];
    private final String WINDOW_TITLE = "Authentication";
    private final JButton BTN_LOGIN = new JButton("Login");
    private final JButton BTN_REGISTRATION = new JButton("Registration");
    private final JButton BTN_EDIT_IP = new JButton("Edit IP");
    private final JButton BTN_EDIT_PORT = new JButton("Edit port");
    private final JLabel LABEL_LOGIN = new JLabel("Login: ");
    private final JLabel LABEL_PASSWORD = new JLabel("Password: ");
    private final JPasswordField PASSWORD_FIELD=new JPasswordField();
    private final JTextField LOGIN_FIELD=new JTextField();
    private final JLabel LABEL_IP = new JLabel();
    private final JLabel LABEL_PORT = new JLabel();
    private final String LABEL_IP_TEXT = "ip: ";
    private final String LABEL_PORT_TEXT = "port: ";
    private String ip = "127.0.0.1";
    private int port = 8189;
    private final Font TEXT_FONT=new Font(Font.SANS_SERIF,Font.ITALIC,16);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientAuthenticationFrame());
    }

    private ClientAuthenticationFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        LABEL_IP.setText(LABEL_IP_TEXT+ip);
        LABEL_PORT.setText(LABEL_PORT_TEXT+ port);
        BTN_EDIT_IP.addActionListener(this);
        BTN_EDIT_PORT.addActionListener(this);
        BTN_LOGIN.addActionListener(this);
        BTN_REGISTRATION.addActionListener(this);
        LABEL_IP.setFont(TEXT_FONT);
        LABEL_PORT.setFont(TEXT_FONT);
        LABEL_LOGIN.setFont(TEXT_FONT);
        LABEL_PASSWORD.setFont(TEXT_FONT);
        for(int i=0; i<SUB_PANEL.length;i++) SUB_PANEL[i]=new JPanel(new GridLayout(1, 2));
        SUB_PANEL[0].add(LABEL_IP);
        SUB_PANEL[0].add(BTN_EDIT_IP);
        SUB_PANEL[1].add(LABEL_PORT);
        SUB_PANEL[1].add(BTN_EDIT_PORT);
        SUB_PANEL[2].add(LABEL_LOGIN);
        SUB_PANEL[2].add(LOGIN_FIELD);
        SUB_PANEL[3].add(LABEL_PASSWORD);
        SUB_PANEL[3].add(PASSWORD_FIELD);
        for(JPanel panel: SUB_PANEL) MAIN_PANEL.add(panel);
        MAIN_PANEL.add(BTN_LOGIN);
        MAIN_PANEL.add(BTN_REGISTRATION);
        add(MAIN_PANEL);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
