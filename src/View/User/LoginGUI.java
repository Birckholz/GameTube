package src.View.User;

import src.Controller.AdmController;
import src.Controller.UsuarioController;
import src.MyCustomException;
import src.Session.Session;
import src.View.Adm.PerfilAdm;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Session session;
    private final UsuarioController userController;
    private final AdmController admController;


    public LoginGUI() {
        setTitle("Login");
        userController = new UsuarioController();
        session = new Session();
        admController = new AdmController();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel profilePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillOval(x, y, size, size);

                ImageIcon profileImage = new ImageIcon("image/Logo.png");
                Image img = profileImage.getImage();
                g2d.drawImage(img, x, y, size, size, this);

                g2d.dispose();
            }
        };
        profilePanel.setPreferredSize(new Dimension(150, 106));
        profilePanel.setBackground(Color.LIGHT_GRAY);

        topPanel.add(profilePanel, BorderLayout.CENTER);

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        topPanel.add(usernamePanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        centerPanel.add(loginButton, gbc);
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int userId = userController.doLogin(usernameField.getText(), new String(passwordField.getPassword()));
                int admId = 0;
                if (userId == -1) {
                    admId = admController.doLogin(usernameField.getText(), new String(passwordField.getPassword()));
                    if (admId == -1) {
                        JOptionPane.showMessageDialog(null, "Login Invalido.");
                    } else {
                        session.setAdmAtual(admController.findAdm(admId));
                    }
                } else {
                    session.setUserAtual(userController.findUsuarioById(userId));
                }
                try{
                    if (session != null) {
                        if ( session.getAdmAtual() != null){
                            PerfilAdm perfilAdm = new PerfilAdm(session);
                            perfilAdm.setVisible(true);
                            dispose();
                        } else {
                            Perfil perfil = new Perfil(session);
                            perfil.setVisible(true);
                            dispose();
                        }
                    } else {
                        throw new MyCustomException("Login Invalido");
                }

                } catch (MyCustomException s) {
                    System.out.println(s.getMessage());
                }
            }
        });

        panel.add(centerPanel, BorderLayout.CENTER);

        add(panel);

        centerPanel.setBackground(Color.LIGHT_GRAY);
        usernamePanel.setBackground(Color.LIGHT_GRAY);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}




