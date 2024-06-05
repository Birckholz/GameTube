package src.View.User;

import src.Controller.AdmController;
import src.Controller.UsuarioController;
import src.Controller.UsuarioFotoController;
import src.Model.UsuarioFoto;
import src.MyCustomException;
import src.Session.SessionCustom;
import src.View.Adm.PerfilAdm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class profileEditGUI extends JFrame {

    private UsuarioFotoController userFotoController;
    private SessionCustom sessionCustom;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField senhaField;
    private JTextField usernameField;
    private JTextField profilePicField;
    private JLabel profilePicLabel;
    private JButton selectFileButton;
    private String selectedFilePath;
    private UsuarioController userController;
    private AdmController admController;
    private static final Logger logger = Logger.getLogger(profileEditGUI.class.getName());

    public profileEditGUI(SessionCustom sessionCustom) throws SQLException {
        this.sessionCustom = sessionCustom;
        this.userFotoController = new UsuarioFotoController();
        this.userController = new UsuarioController();
        this.admController = new AdmController();
        if (sessionCustom == null) {
            JOptionPane.showMessageDialog(null, "Por favor realize login", "No Session", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }


        try {
            if (sessionCustom != null) {
                nameField = new JTextField(20);
                emailField = new JTextField(20);
                senhaField = new JTextField(20);
                usernameField = new JTextField(20);
                profilePicField = new JTextField(20);
                profilePicLabel = new JLabel("Foto de Perfil:");
                selectFileButton = new JButton("Select File");
                selectFileButton.setBackground(Color.DARK_GRAY);
                selectFileButton.setForeground(Color.BLACK);
                selectFileButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = fileChooser.getSelectedFile();
                            selectedFilePath = selectedFile.getAbsolutePath();
                        }
                    }
                });

                JLabel nameLabel = new JLabel("Nome:");
                JLabel emailLabel = new JLabel("Email:");
                JLabel senhaLabel = new JLabel("Senha:");
                JLabel usernameLabel = new JLabel("Username:");

                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Click");
                        try {
                            saveProfileChanges();
                        } catch (SQLException | IOException ex) {
                            logger.log(Level.SEVERE, "Error on edit", ex);
                        }
                    }
                });

                JButton undoButton = new JButton("Desfazer");
                undoButton.setBackground(Color.DARK_GRAY);
                undoButton.setForeground(Color.BLACK);
                undoButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Clicka");
                        try {
                            undoChanges();
                        } catch (SQLException ex) {
                            logger.log(Level.SEVERE, "Error on login", ex);
                        }
                    }
                });

                saveButton.setBackground(Color.DARK_GRAY);
                saveButton.setForeground(Color.BLACK);

                JButton goBackButton = new JButton("Go Back");
                goBackButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (sessionCustom.getAdmAtual() != null) {
                            PerfilAdm perfilAdm = new PerfilAdm(sessionCustom);
                            perfilAdm.setVisible(true);
                            dispose();
                        } else {
                            Perfil perfil = new Perfil(sessionCustom);
                            perfil.setVisible(true);
                            dispose();
                        }
                    }
                });
                goBackButton.setBackground(Color.DARK_GRAY);
                goBackButton.setForeground(Color.BLACK);

                setLayout(new GridBagLayout());
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(500, 500);
                getContentPane().setBackground(Color.DARK_GRAY);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);

                gbc.gridx = 0;
                gbc.gridy = 0;
                add(nameLabel, gbc);

                gbc.gridx = 1;
                add(nameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                add(emailLabel, gbc);

                gbc.gridx = 1;
                add(emailField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                add(senhaLabel, gbc);

                gbc.gridx = 1;
                add(senhaField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 3;
                add(usernameLabel, gbc);

                gbc.gridx = 1;
                add(usernameField, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                add(profilePicLabel, gbc);

                gbc.gridx = 1;
                add(selectFileButton, gbc);

                gbc.gridx = 0;
                gbc.gridy = 6;
                gbc.insets = new Insets(10, 0, 0, 0);
                add(goBackButton, gbc);

                gbc.gridx = 1;
                gbc.gridy = 6;
                gbc.insets = new Insets(10, 10, 0, 0);
                add(saveButton, gbc);

                gbc.gridx = 2;
                gbc.insets = new Insets(10, 10, 0, 0);
                add(undoButton, gbc);

                Color blackColor = Color.BLACK;
                nameLabel.setForeground(blackColor);
                emailLabel.setForeground(blackColor);
                senhaLabel.setForeground(blackColor);
                usernameLabel.setForeground(blackColor);
                profilePicLabel.setForeground(blackColor);
                nameField.setForeground(blackColor);
                emailField.setForeground(blackColor);
                senhaField.setForeground(blackColor);
                usernameField.setForeground(blackColor);

                setLocationRelativeTo(null);
                setVisible(true);
                if(sessionCustom.getAdmAtual() != null) {
                    nameField.setText(sessionCustom.getAdmAtual().getName());
                    senhaField.setText(sessionCustom.getAdmAtual().getSenha());
                    usernameField.setText(sessionCustom.getAdmAtual().getUsername());
                    emailField.setText(sessionCustom.getAdmAtual().getEmail());
                }else {
                    nameField.setText(sessionCustom.getUserAtual().getName());
                    senhaField.setText(sessionCustom.getUserAtual().getSenha());
                    usernameField.setText(sessionCustom.getUserAtual().getUsername());
                    emailField.setText(sessionCustom.getUserAtual().getEmail());
                }
            } else {
                throw new MyCustomException("Session undefined");
            }

        } catch (MyCustomException e) {
            System.out.println(e.getMessage());
            dispose();
        }
    }

    public void saveProfileChanges() throws SQLException, IOException {
        String profilePic = "";
        String name = nameField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String username = usernameField.getText();
        int idUser;
        boolean isAdmin = false;
        if (sessionCustom.getAdmAtual() == null) {
            sessionCustom.getUserAtual().setName(name);
            sessionCustom.getUserAtual().setEmail(email);
            sessionCustom.getUserAtual().setPassword(senha);
            sessionCustom.getUserAtual().setUsername(username);
            userController.updateUsuario(sessionCustom.getUserAtual());
            idUser = sessionCustom.getUserAtual().getId();
            userFotoController.updateUsuarioFoto(new UsuarioFoto(idUser, Files.readAllBytes(Paths.get(selectedFilePath))));
        } else {
            sessionCustom.getAdmAtual().setName(name);
            sessionCustom.getAdmAtual().setEmail(email);
            sessionCustom.getAdmAtual().setSenha(senha);
            sessionCustom.getAdmAtual().setUsername(username);
            admController.updateAdm(sessionCustom.getAdmAtual());
            idUser = sessionCustom.getAdmAtual().getId();
            isAdmin = true;
        }
    }

    public void undoChanges() throws SQLException {
        String nameAtual;
        String emailAtual;
        String senhaAtual;
        String usernameAtual;
        int userID;
        int mementoId;
        boolean isAdmin;

        String nameMemento;
        String emailMemento;
        String senhaMemento;
        String usernameMemento;
        if (sessionCustom.getAdmAtual() == null) {
            nameAtual = sessionCustom.getUserAtual().getName();
            emailAtual = sessionCustom.getUserAtual().getEmail();
            senhaAtual = sessionCustom.getUserAtual().getSenha();
            usernameAtual = sessionCustom.getUserAtual().getUsername();
            isAdmin = false;
            userID = sessionCustom.getUserAtual().getId();
        } else {
            nameAtual = sessionCustom.getAdmAtual().getName();
            emailAtual = sessionCustom.getAdmAtual().getEmail();
            senhaAtual = sessionCustom.getAdmAtual().getSenha();
            usernameAtual = sessionCustom.getAdmAtual().getUsername();
            isAdmin = true;
            userID = sessionCustom.getAdmAtual().getId();
        }






    }


    public static void main(String[] args) throws SQLException {
        SessionCustom sessionCustom = new SessionCustom();
        new profileEditGUI(sessionCustom);
    }
}
