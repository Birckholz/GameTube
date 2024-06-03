package src.View.User;

import src.Controller.UsuarioFotoController;
import src.MyCustomException;
import src.Session.Session;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Perfil extends JFrame {

    private Session session;
    private UsuarioFotoController userFotoController;
    private ImageIcon imageIcon;
    private ResultSet resultSet;

    public Perfil(Session session) {
        this.userFotoController = new UsuarioFotoController();
        this.session = session;
        this.resultSet = fetchUserProfile();

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
                setLayout(new BorderLayout());

                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBackground(Color.DARK_GRAY);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(10, 10, 0, 10);
                gbc.anchor = GridBagConstraints.CENTER;

                if (userFotoController.getUsuarioFotoByUsuarioId(session.getUserAtual().getId()) != null) {
                    byte[] blob = userFotoController.getUsuarioFotoByUsuarioId(session.getUserAtual().getId()).getFoto();
                    ByteArrayInputStream bis = new ByteArrayInputStream(blob);
                    BufferedImage bufferedImage = ImageIO.read(bis);
                    Image originalImage = bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_SMOOTH);

                    int maxWidth = 200;
                    int maxHeight = 200;

                    int scaledWidth = originalImage.getWidth(null);
                    int scaledHeight = originalImage.getHeight(null);
                    if (scaledWidth > maxWidth || scaledHeight > maxHeight) {
                        double widthScaleFactor = (double) maxWidth / scaledWidth;
                        double heightScaleFactor = (double) maxHeight / scaledHeight;
                        double scaleFactor = Math.min(widthScaleFactor, heightScaleFactor);
                        scaledWidth = (int) (scaledWidth * scaleFactor);
                        scaledHeight = (int) (scaledHeight * scaleFactor);
                    }

                    Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

                    imageIcon = new ImageIcon(scaledImage);
                } else {
                    imageIcon = new ImageIcon("image/img_2.png");
                }

                JButton imageButton = new JButton(imageIcon);
                imageButton.setBorderPainted(false);
                imageButton.setContentAreaFilled(false);
                imageButton.setFocusPainted(false);
                imageButton.setOpaque(false);
                panel.add(imageButton, gbc);

                gbc.gridy = 1;
                gbc.insets = new Insets(5, 10, 10, 10);
                gbc.anchor = GridBagConstraints.PAGE_START;

                JLabel nomeLabel = new JLabel();
                nomeLabel.setText(session.getUserAtual().getName());
                nomeLabel.setForeground(Color.WHITE);
                nomeLabel.setHorizontalAlignment(JLabel.CENTER);
                panel.add(nomeLabel, gbc);

                add(panel, BorderLayout.CENTER);

                imageButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        try {
                            new profileEditGUI(session).setVisible(true);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                JMenuBar barraMenu = new JMenuBar();
                JMenu menuBiblioteca = new JMenu("Biblioteca");
                JMenu menuLoja = new JMenu("Loja");
                JMenu menuLista = new JMenu("Lista de Desejos");

                JMenuItem verJogos = new JMenuItem("Ver Jogos");
                verJogos.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new BibliotecaView(session).setVisible(true);
                    }
                });

                JMenuItem verLoja = new JMenuItem("Ver Loja");
                verLoja.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        new Loja(session).setVisible(true);
                    }
                });


                menuBiblioteca.add(verJogos);
                menuLoja.add(verLoja);


                barraMenu.add(menuBiblioteca);
                barraMenu.add(menuLoja);


                setJMenuBar(barraMenu);
                setVisible(true);
                setLocationRelativeTo(null);
            } else {
                throw new MyCustomException("Session undefined");
            }
        } catch (MyCustomException e) {
            System.out.println(e.getMessage());
            descartar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Método adicionado para buscar o perfil do usuário no banco de dados
    private ResultSet fetchUserProfile() {
        // Lógica para buscar o perfil do usuário no banco de dados e retornar um ResultSet
        return null; // Retorno temporário; substitua pela lógica de busca no banco de
    }

    public void descartar() {
        dispose();
    }

    public static void main(String[] args) {
        Session a = new Session();
        Perfil p = new Perfil(a);
    }
}
