package src.View.Adm;


import src.Controller.GameController;
import src.Controller.GameFotoController;
import src.Model.Game;
import src.MyCustomException;
import src.Session.Session;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class AdmViewJogos extends JFrame {

    private GameFotoController gameFotoController;
    private GameController gameController;
    private Session session;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel panel;

    public AdmViewJogos(Session session) {
        this.session = session;
        this.gameFotoController = new GameFotoController();
        this.gameController = new GameController();
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
                JMenuBar menuBar = new JMenuBar();

                JMenu perfilMenu = new JMenu("Perfil");
                JMenu usuariosMenu = new JMenu("Usuarios");
                JMenuItem verPerfilMenuItem = new JMenuItem("Ver Perfil");
                verPerfilMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new PerfilAdm(session).setVisible(true);
                        dispose();
                    }
                });
                perfilMenu.add(verPerfilMenuItem);

                JMenuItem verUsuariosMenuItem = new JMenuItem("Ver Usuários");
                verUsuariosMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new AdmViewUser(session).setVisible(true);
                        dispose();
                    }
                });
                JMenuItem registrarJogoMenuItem = new JMenuItem("Registrar Jogo");
                registrarJogoMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new RegistroJogoGUI(session).setVisible(true);
                    }
                });
                perfilMenu.add(registrarJogoMenuItem);
                usuariosMenu.add(verUsuariosMenuItem);

                menuBar.add(perfilMenu);
                menuBar.add(usuariosMenu);
                setJMenuBar(menuBar);

                panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.DARK_GRAY);

                table = new JTable();
                table.setBackground(Color.DARK_GRAY);
                table.setForeground(Color.WHITE);
                JScrollPane scrollPane = new JScrollPane(table);
                panel.add(scrollPane, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
                buttonPanel.setBackground(Color.DARK_GRAY);

                panel.add(buttonPanel, BorderLayout.WEST);

                add(panel, BorderLayout.CENTER);

                loadTableData();

                table.getColumn("Deletar").setCellRenderer((TableCellRenderer) new ButtonRenderer());
                table.getColumn("Deletar").setCellEditor(new ButtonEditor(new JCheckBox()));

                setVisible(true);
                setLocationRelativeTo(null);
            } else {
                throw new MyCustomException("Session undefined");
            }
        } catch (MyCustomException e) {
            System.out.println(e.getMessage());
            descartar();
        }
    }

    private void loadTableData() {
        try {
            List<Game> games = gameController.findAllGames();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("id");
            columnNames.add("Name");
            columnNames.add("Description");
            columnNames.add("APrice");
            columnNames.add("Deletar");
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4;
                }
            };

            for (Game game : games) {
                int id = game.getId();
                String name = game.getName();
                String description = game.getDescricao();
                double aprice = game.getPrice();
                tableModel.addRow(new Object[]{id ,name, description, aprice, "Deletar"});
            }

            table.setModel(tableModel);

            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    cellComponent.setBackground(Color.DARK_GRAY);
                    cellComponent.setForeground(Color.WHITE);
                    return cellComponent;
                }
            });

            JTableHeader tableHeader = table.getTableHeader();
            tableHeader.setBackground(Color.DARK_GRAY);
            tableHeader.setForeground(Color.WHITE);

            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            scrollPane.setBackground(Color.DARK_GRAY);
            scrollPane.getViewport().setBackground(Color.DARK_GRAY);

            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void descartar() {
        new PerfilAdm(session);
        dispose();
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este jogo?", "Confirmar Deletar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION && selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                    tableModel.removeRow(selectedRow);
                    synchronizeTableWithData();
                }
            }
            clicked = false;
            return new String(label);
        }


        private void synchronizeTableWithData() {
            SwingUtilities.invokeLater(() -> {
                try {
                    List<Game> gamesInDatabase = gameController.findAllGames();
                    if (tableModel.getRowCount() == 0) {
                        gameFotoController.deleteGameFoto(gamesInDatabase.get(0).getId());
                        gameController.deleteGame(gamesInDatabase.get(0).getId());
                    }
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        int id = (int) tableModel.getValueAt(i, 1);
                        boolean found = false;
                        for (Game game : gamesInDatabase) {
                            if (game.getId() == id) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            gameFotoController.deleteGameFoto(id);
                            gameController.deleteGame(id);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }



        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Session session = new Session();
                new AdmViewJogos(session);
            }
        });
    }
}
