package src.View.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import src.Controller.BibliotecaController;
import src.Controller.GameFotoController;
import src.Model.Game;
import src.MyCustomException;
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
import java.util.List;

public class Loja extends JFrame {
    private Session session;
    private JPanel gamePanelContainer;
    private GameFotoController gameFotoController;
    private BibliotecaController bibliotecaController;

    public Loja(Session session) {
        this.session = session;
        this.bibliotecaController = new BibliotecaController();
        this.gameFotoController =new GameFotoController();
        if (session == null) {
            JOptionPane optionPane = new JOptionPane("Por favor realize login", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);

            JButton customButton = new JButton("Fechar");

            optionPane.setOptions(new Object[]{customButton});

            JDialog dialog = optionPane.createDialog("No Session");

            customButton.addActionListener(e -> {
                descartar();
                dialog.dispose();
            });

            dialog.setModal(true);

            dialog.setResizable(false);

            dialog.setVisible(true);
        }

        try {
            if (session != null) {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(1000, 700);
                getContentPane().setBackground(Color.DARK_GRAY);

                JPanel contentPanel = new JPanel(new BorderLayout());

                JMenuBar barraMenu = new JMenuBar();
                JMenu menuBiblioteca = new JMenu("Biblioteca");
                JMenu menuLista = new JMenu("Lista de Desejos");
                JMenu menuPerfil = new JMenu("Perfil");

                JMenuItem verJogos = new JMenuItem("Ver Jogos");
                verJogos.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new BibliotecaView(session).setVisible(true);
                    }
                });
                JMenuItem irPerfil = new JMenuItem("Ir para o Perfil");
                irPerfil.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new Perfil(session).setVisible(true);
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
            System.out.println(e.getMessage());
            descartar();
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
                        List<Integer> bib = bibliotecaController.findGamesByUsuario(session.getUserAtual().getId());

                        if (bib.contains(gameId)){
                            showErrorPopup("Jogo Já Comprado", "Fechar");
                            return;
                        }
                        bibliotecaController.insertBiblioteca(gameId, session.getUserAtual().getId());
                            System.out.println("Compra Realizada com Sucesso");
                    }
                });

                gamePanelContainer.add(gamePanel);
            }

            getContentPane().revalidate();
            getContentPane().repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void descartar() {
        dispose();
    }
    public static boolean comprarGame(JSONObject session, Game game){
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get("src/usuarios.json")));
            JSONArray jsonArray;
            jsonArray = new JSONArray(fileContent);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (session.getString("name").equals(jsonObject.getString("name"))) {
                    JSONArray currentBiblioteca = jsonObject.getJSONArray("biblioteca");
                    if (currentBiblioteca.length() > 0){
                        for (int j = 0; j < currentBiblioteca.length(); j++) {
                            String element = currentBiblioteca.getString(j);
                            if (element.equals(game.getName())) {
                                return false;
                            }
                        }
                    }

                    currentBiblioteca.put(game.getName());
                    jsonObject.put("biblioteca", currentBiblioteca);

                    Files.write(Paths.get("src/usuarios.json"), jsonArray.toString().getBytes());

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }
    private void showErrorPopup(String message, String buttonText) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{buttonText});
        JDialog dialog = optionPane.createDialog(this, "Erro");
        dialog.setVisible(true);
    }
}
