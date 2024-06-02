package src.View.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import src.Controller.GameFotoController;
import src.Controller.UsuarioController;
import src.Session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BibliotecaView extends JFrame {
    private Session session;
    private JPanel gamePanelContainer;
    private UsuarioController userController;
    private GameFotoController gameFotoController;
    public BibliotecaView(Session session) {
        gameFotoController = new GameFotoController();
        userController = new UsuarioController();
        this.session = session;
        if (session == null) {
            JOptionPane optionPane = new JOptionPane("Por favor realize login", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);

            JButton customButton = new JButton("Fechar");

            optionPane.setOptions(new Object[] { customButton });

            JDialog dialog = optionPane.createDialog("No Session");

            customButton.addActionListener(e -> {
                dispose();
                dialog.dispose();
            });

            dialog.setModal(true);

            dialog.setResizable(false);

            dialog.setVisible(true);
        } else {
            try {
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
                        new Perfil(session).setVisible(true);
                    }
                });
                JMenuItem verLoja = new JMenuItem("Ver Loja");
                verLoja.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new Loja(session).setVisible(true);
                    }
                });



                menuPerfil.add(irPerfil);
                menuLoja.add(verLoja);


                barraMenu.add(menuLoja);

                barraMenu.add(menuPerfil);

                setJMenuBar(barraMenu);
                setLocationRelativeTo(null);

                readGameListingsFromFile("src/games.json");
            } catch (Exception e) {
                e.printStackTrace();
                dispose();
            }
        }
    }

    private void readGameListingsFromFile(String filePath) {
        try {

            gamePanelContainer = new JPanel(new GridLayout(0, 3, 10, 10));
            gamePanelContainer.setBackground(Color.DARK_GRAY);

            JPanel fillerPanel = new JPanel();
            fillerPanel.setOpaque(false);
            gamePanelContainer.add(fillerPanel);

            ResultSet resultSet = gameFotoController.fetchGameListingsWithPhotos();

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
            e.printStackTrace();
        }
    }
}

