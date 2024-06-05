package src.View.User;



import src.Controller.BibliotecaController;
import src.Controller.GameFotoController;
import src.MyCustomException;
import src.Session.SessionCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Loja extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(Loja.class.getName());

    private SessionCustom sessionCustom;
    private JPanel gamePanelContainer;
    private GameFotoController gameFotoController;
    private BibliotecaController bibliotecaController;

    public Loja(SessionCustom sessionCustom) {
        this.sessionCustom = sessionCustom;
        this.bibliotecaController = new BibliotecaController();
        this.gameFotoController = new GameFotoController();
        if (sessionCustom == null) {
            JOptionPane.showMessageDialog(null, "Por favor, realize o login", "No Session", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }

        try {
            if (sessionCustom != null) {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(1000, 700);
                getContentPane().setBackground(Color.DARK_GRAY);

                JPanel contentPanel = new JPanel(new BorderLayout());

                JMenuBar barraMenu = new JMenuBar();
                JMenu menuBiblioteca = new JMenu("Biblioteca");
                JMenu menuPerfil = new JMenu("Perfil");

                JMenuItem verJogos = new JMenuItem("Ver Jogos");
                verJogos.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new BibliotecaView(sessionCustom).setVisible(true);
                    }
                });
                JMenuItem irPerfil = new JMenuItem("Ir para o Perfil");
                irPerfil.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new Perfil(sessionCustom).setVisible(true);
                    }
                });

                menuBiblioteca.add(verJogos);
                menuPerfil.add(irPerfil);

                barraMenu.add(menuBiblioteca);
                barraMenu.add(menuPerfil);

                contentPanel.add(BorderLayout.NORTH, barraMenu);

                gamePanelContainer = new JPanel(new GridLayout(0, 3, 10, 10));
                gamePanelContainer.setBackground(Color.DARK_GRAY);

                JScrollPane scrollPane = new JScrollPane(gamePanelContainer);
                scrollPane.setBackground(Color.DARK_GRAY);
                scrollPane.getViewport().setBackground(Color.DARK_GRAY);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                contentPanel.add(BorderLayout.CENTER, scrollPane);

                getContentPane().setLayout(new BorderLayout());
                getContentPane().add(contentPanel, BorderLayout.CENTER);
                setVisible(true);
                setLocationRelativeTo(null);
            } else {
                throw new MyCustomException("Session undefined");
            }

        } catch (MyCustomException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            dispose();
        }
        readGameListingsFromDatabase();
    }

    private void readGameListingsFromDatabase() {
        try {
            ResultSet resultSet = gameFotoController.fetchGameListingsWithPhotos();

            while (resultSet.next()) {
                int gameId = resultSet.getInt("ID");
                String name = resultSet.getString("NOME");
                String description = resultSet.getString("DESCRICAO");
                double price = resultSet.getDouble("PRECO");
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

                JLabel nameLabel = new JLabel(name);
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setForeground(Color.WHITE);
                gamePanel.add(nameLabel);

                JLabel descriptionLabel = new JLabel(description);
                descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
                descriptionLabel.setForeground(Color.WHITE);
                gamePanel.add(descriptionLabel);

                JLabel priceLabel = new JLabel(String.valueOf(price));
                priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                priceLabel.setForeground(Color.WHITE);
                gamePanel.add(priceLabel);

                JButton pagarButton = new JButton("Comprar");
                pagarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                gamePanel.add(pagarButton);

                pagarButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (bibliotecaController.checkUserGames(gameId, sessionCustom.getUserAtual().getId())) {
                            bibliotecaController.insertBiblioteca(gameId, sessionCustom.getUserAtual().getId());
                            System.out.println("Compra realizada com sucesso");
                        } else {
                            showErrorPopup("Jogo já comprado", "Fechar");
                        }
                    }
                });

                gamePanelContainer.add(gamePanel);
            }

            getContentPane().revalidate();
            getContentPane().repaint();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reading game listings from database: ", e);
        }
    }

    public void descartar() {
        dispose();
    }

    private void showErrorPopup(String message, String buttonText) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{buttonText});
        JDialog dialog = optionPane.createDialog(this, "Erro");
        dialog.setVisible(true);
    }
}
