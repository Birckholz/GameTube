package src.View.User;


import javax.swing.JTextField;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import src.Controller.UsuarioController;
import src.Controller.UsuarioFotoController;
import src.Factory.UserFactory;
import src.Factory.UsuarioFactory;
import src.Model.UserBase;
import src.Model.Usuario;
import src.Model.UsuarioFoto;
import src.View.ConfirmaSignUp;
import src.config.CognitoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RegistroGUI extends JFrame {
    private static RegistroGUI instance;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField nicknameField;
    private JPasswordField passwordField;
    private UsuarioController userController;
    private UsuarioFotoController userFotoController;
    private CognitoService cognitoService;
    private static final Logger logger = Logger.getLogger(RegistroGUI.class.getName());

    private RegistroGUI() throws SQLException {
        // aws aqui
        userController = new UsuarioController();
        userFotoController = new UsuarioFotoController();
        setTitle("Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.5);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.LIGHT_GRAY);

        JLabel welcomeLabel = new JLabel("Bem vindo ao GAMETube");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);

        Font font = new Font("Arial", Font.BOLD, 20);
        welcomeLabel.setFont(font);

        centerPanel.add(welcomeLabel, BorderLayout.CENTER);

        leftPanel.add(centerPanel, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nameLabel = new JLabel("Name:");
        rightPanel.add(nameLabel, gbc);

        gbc.gridy = 2;
        JLabel nicknameLabel = new JLabel("Username:");
        rightPanel.add(nicknameLabel, gbc);

        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        rightPanel.add(emailLabel, gbc);

        gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Password:");
        rightPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        nameField = new JTextField(20);
        rightPanel.add(nameField, gbc);

        gbc.gridy = 2;
        nicknameField = new JTextField(20);
        rightPanel.add(nicknameField, gbc);

        gbc.gridy = 3;
        emailField = new JTextField(20);
        rightPanel.add(emailField, gbc);

        gbc.gridy = 4;
        passwordField = new JPasswordField(20);
        rightPanel.add(passwordField, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registroButton = new JButton("Registrar");
        rightPanel.add(registroButton, gbc);

        gbc.gridy = 6;
        JLabel loginLabel = new JLabel("<html><u>Já possuo login</u></html>");
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(loginLabel, gbc);

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginGUI loginGUI = new LoginGUI(cognitoService);
                loginGUI.setVisible(true);
                dispose();

            }
        });

        registroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    SignUpResult signUpResult = cognitoService.registerUser(nicknameField.getText(), passwordField.getText(), emailField.getText());
                    if (signUpResult.getUserConfirmed()) {

                        JOptionPane.showMessageDialog(RegistroGUI.this, "Registrado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        ConfirmaSignUp confirmaSignUp = new ConfirmaSignUp(cognitoService);
                        confirmaSignUp.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(RegistroGUI.this, "Confirme sua conta no seu email.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        ConfirmaSignUp confirmaSignUp = new ConfirmaSignUp(cognitoService);
                        confirmaSignUp.setVisible(true);
                    }

                    UserFactory Factory = new UsuarioFactory();
                    UserBase temp1 = Factory.createUser(nameField.getText(), emailField.getText(), passwordField.getText(), nicknameField.getText());
                    Usuario temp = (Usuario) temp1;
                    int userID = userController.insertUsuario(temp);

                    try {
                        userFotoController.addUsuarioFoto(new UsuarioFoto(userID, Files.readAllBytes(Paths.get("image/img_2.png"))));
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Error on register", ex);
                    }
                }catch (AmazonServiceException ex) {
                    JOptionPane.showMessageDialog(RegistroGUI.this, "Erro realizando registro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.SEVERE, "Error on register", ex);
                }

//                temp.registrarUsuario(temp);
                dispose();
            }
        });

        splitPane.setRightComponent(rightPanel);

        panel.add(splitPane, BorderLayout.CENTER);

        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static synchronized RegistroGUI getInstance() throws SQLException {
        if (instance == null) {
            instance = new RegistroGUI();
        }
        return instance;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new RegistroGUI());
//    }

}
