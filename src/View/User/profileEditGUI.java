package src.View.User;

import org.json.JSONArray;
import org.json.JSONObject;
import src.Controller.AdmController;
import src.Controller.MementoController;
import src.Controller.UsuarioController;
import src.Model.Adm;
import src.Model.Memento;
import src.Model.Usuario;
import src.MyCustomException;
import src.Session.Session;
import src.View.Adm.PerfilAdm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class profileEditGUI extends JFrame {

    private Session session;
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
    private MementoController mementoController;

    public profileEditGUI(Session session) throws SQLException {
        this.session = session;
        this.userController = new UsuarioController();
        this.admController = new AdmController();
        this.mementoController = new MementoController();
        if (session == null) {
            JOptionPane.showMessageDialog(null, "Por favor realize login", "No Session", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }


        try {
            if (session != null) {
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
                        } catch (SQLException ex) {
                            ex.printStackTrace();
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
                            ex.printStackTrace();
                        }
                    }
                });

                saveButton.setBackground(Color.DARK_GRAY);
                saveButton.setForeground(Color.BLACK);

                JButton goBackButton = new JButton("Go Back");
                goBackButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (session.getAdmAtual() != null) {
                            PerfilAdm perfilAdm = new PerfilAdm(session);
                            perfilAdm.setVisible(true);
                            dispose();
                        } else {
                            Perfil perfil = new Perfil(session);
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

                nameField.setText(session.getUserAtual().getName());
                senhaField.setText(session.getUserAtual().getSenha());
                usernameField.setText(session.getUserAtual().getUsername());
                emailField.setText(session.getUserAtual().getEmail());

            } else {
                throw new MyCustomException("Session undefined");
            }

        } catch (MyCustomException e) {
            System.out.println(e.getMessage());
            dispose();
        }
    }

    public void saveProfileChanges() throws SQLException {
        String profilePic = "";
        String name = nameField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String username = usernameField.getText();
        int idUser;
        boolean isAdmin = false;
        if (session.getAdmAtual() == null) {
            session.getUserAtual().setName(name);
            session.getUserAtual().setEmail(email);
            session.getUserAtual().setPassword(senha);
            session.getUserAtual().setUsername(username);
            userController.updateUsuario(session.getUserAtual());
            idUser = session.getUserAtual().getId();
        } else {
            session.getAdmAtual().setName(name);
            session.getAdmAtual().setEmail(email);
            session.getAdmAtual().setSenha(senha);
            session.getAdmAtual().setUsername(username);
            admController.updateAdm(session.getAdmAtual());
            idUser = session.getAdmAtual().getId();
            isAdmin = true;
        }
        mementoController.updateUser(name,email,senha,isAdmin,username,idUser);
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
        if (session.getAdmAtual() == null) {
            nameAtual = session.getUserAtual().getName();
            emailAtual = session.getUserAtual().getEmail();
            senhaAtual = session.getUserAtual().getSenha();
            usernameAtual = session.getUserAtual().getUsername();
            mementoId = session.getUserAtual().getMementoId();
            isAdmin = false;
            userID = session.getUserAtual().getId();
        } else {
            nameAtual = session.getAdmAtual().getName();
            emailAtual = session.getAdmAtual().getEmail();
            senhaAtual = session.getAdmAtual().getSenha();
            usernameAtual = session.getAdmAtual().getUsername();
            mementoId = session.getAdmAtual().getMementoId();
            isAdmin = true;
            userID = session.getAdmAtual().getId();
        }
        nameMemento = mementoController.restoreUser(mementoId).getName();
        emailMemento = mementoController.restoreUser(mementoId).getEmail();
        senhaMemento = mementoController.restoreUser(mementoId).getSenha();
        usernameMemento= mementoController.restoreUser(mementoId).getUsername();

        if (session.getAdmAtual() == null) {
            Usuario temp = new Usuario(emailMemento, senhaMemento, nameMemento, usernameMemento);
            temp.setId(session.getUserAtual().getId());
            userController.updateUsuario(temp);
        } else {
            Adm temp = new Adm(true ,emailMemento, senhaMemento, nameMemento, usernameMemento);
            temp.setId(session.getUserAtual().getId());
            admController.updateAdm(temp);
        }
        mementoController.updateUser(nameAtual, emailAtual,senhaAtual, isAdmin, usernameAtual, userID);



    }


    public static JSONObject findUser(JSONObject session) {
        String username = session.getString("username");
        String email = session.getString("email");
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get("src/usuarios.json")));
            JSONArray jsonArray;
            jsonArray = new JSONArray(fileContent);
            for (Object item : jsonArray) {
                if (item instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) item;

                    if (username.equals(jsonObject.getString("username")) || email.equals(jsonObject.getString("email"))) {
                        return jsonObject;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }


    public static boolean checkGame(JSONObject session, String name){
        JSONObject user = findUser(session);
        JSONArray biblioteca = user.getJSONArray("biblioteca");
        for (Object elemento : biblioteca) {
            String nameGame = (String) elemento;
            if (name.equals(nameGame)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) throws SQLException {
        Session session = new Session();
        new profileEditGUI(session);
    }
}
