package src.View.User;


import src.Controller.BibliotecaController;
import src.Controller.GameFotoController;
import src.Controller.UsuarioController;
import src.Session.SessionCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BibliotecaView extends JFrame {
    private static final Logger logger = Logger.getLogger(BibliotecaView.class.getName());

    private SessionCustom sessionCustom;
    private JPanel gamePanelContainer;
    private UsuarioController userController;
    private GameFotoController gameFotoController;
    private BibliotecaController bibliotecaController;

    public BibliotecaView(SessionCustom sessionCustom) {
        gameFotoController = new GameFotoController();
        userController = new UsuarioController();
        bibliotecaController = new BibliotecaController();
        this.sessionCustom = sessionCustom;

        try {
            if (sessionCustom == null) {
                showSessionErrorDialog();
            } else {
                initializeUI();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing BibliotecaView", e);
            dispose();
        }
    }

    private void showSessionErrorDialog() {
        JOptionPane optionPane = new JOptionPane("Por favor realize login", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);

        JButton customButton = new JButton("Fechar");

        optionPane.setOptions(new Object[]{customButton});

        JDialog dialog = optionPane.createDialog("No Session");

        customButton.addActionListener(e -> {
            dispose();
            dialog.dispose();
        });

        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        getContentPane().setBackground(Color.DARK_GRAY);
        setVisible(true);

        JMenuBar barraMenu = new JMenuBar();
        JMenu menuLoja = new JMenu("Loja");
        JMenu menuLista = new JMenu("Lista de Desejos");
        JMenu menuPerfil = new JMenu("Perfil");

        JMenuItem irPerfil = new JMenuItem("Ir para o Perfil");
        irPerfil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Perfil(sessionCustom).setVisible(true);
            }
        });
        JMenuItem verLoja = new JMenuItem("Ver Loja");
        verLoja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Loja(sessionCustom).setVisible(true);
            }
        });

        menuPerfil.add(irPerfil);
        menuLoja.add(verLoja);

        barraMenu.add(menuLoja);
        barraMenu.add(menuPerfil);

        setJMenuBar(barraMenu);
        setLocationRelativeTo(null);

        readGameListingsFromFile();
    }

    private void readGameListingsFromFile() {
        try {
            gamePanelContainer = new JPanel(new GridLayout(0, 3, 10, 10));
            gamePanelContainer.setBackground(Color.DARK_GRAY);

            JPanel fillerPanel = new JPanel();
            fillerPanel.setOpaque(false);
            gamePanelContainer.add(fillerPanel);

            ResultSet resultSet = bibliotecaController.pegarGames(sessionCustom.getUserAtual().getId());

            while (resultSet.next()) {
                String name = resultSet.getString("NOME");
                byte[] pictureBytes = resultSet.getBytes("PICTURE");

                ImageIcon imageIcon = new ImageIcon(pictureBytes);
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                JPanel gamePanel = new JPanel();
                gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
                gamePanel.setBackground(Color.DARK_GRAY);
                JLabel imageLabel = new JLabel();
                imageLabel.setIcon(scaledImageIcon);
                gamePanel.add(imageLabel);
                JPanel namePanel = new JPanel();
                namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
                namePanel.setBackground(Color.DARK_GRAY);
                JLabel nameLabel = new JLabel(name);
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setForeground(Color.WHITE);
                namePanel.add(nameLabel);
                gamePanel.add(namePanel);
                gamePanelContainer.add(gamePanel);
                getContentPane().add(gamePanelContainer, BorderLayout.CENTER);
                getContentPane().revalidate();
                getContentPane().repaint();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error reading game listings from file", e);
        }
    }
}
