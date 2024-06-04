package src.View.Adm;


import src.Controller.UsuarioController;
import src.Model.Usuario;
import src.MyCustomException;
import src.Session.Session;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class AdmViewUser extends JFrame {

    private UsuarioController usuarioController;
    private Session session;
    private JTable table;
    private DefaultTableModel tableModel;

    public AdmViewUser(Session session) {
        this.session = session;
        this.usuarioController =new UsuarioController();
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
                JMenu jogosMenu = new JMenu("Jogos");
                JMenuItem verPerfilMenuItem = new JMenuItem("Ver Perfil");
                verPerfilMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new PerfilAdm(session).setVisible(true);
                        dispose();
                    }
                });
                perfilMenu.add(verPerfilMenuItem);

                JMenuItem verjogosMenuItem = new JMenuItem("Ver Jogos");
                verjogosMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new AdmViewJogos(session).setVisible(true);
                        dispose();
                    }
                });
                jogosMenu.add(verjogosMenuItem);

                menuBar.add(perfilMenu);
                menuBar.add(jogosMenu);
                setJMenuBar(menuBar);

                JPanel panel = new JPanel(new BorderLayout());
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
        List<Usuario> users = usuarioController.findAllUsers();

        Vector<String> columnNames = new Vector<>();
        columnNames.add("id");
        columnNames.add("Name");
        columnNames.add("Email");
        columnNames.add("Username");
        columnNames.add("MementoId");
        columnNames.add("Deletar");
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        for (Usuario user : users) {
            int id = user.getId();
            String name = user.getName();
            String email = user.getEmail();
            String username = user.getUsername();
            int mementoId = user.getMementoId();
            tableModel.addRow(new Object[]{id, name, email, username, mementoId, "Deletar"});
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

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.getViewport().setBackground(Color.DARK_GRAY);
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
                int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar este usuário?", "Confirmar Deletar", JOptionPane.YES_NO_OPTION);
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
                    List<Usuario> usersInDatabase = usuarioController.findAllUsers();
                    for (Usuario user : usersInDatabase) {
                        boolean found = false;
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            int id = (int) tableModel.getValueAt(i, 1);
                            if (user.getId() == id) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            usuarioController.removeUsuario(user.getId());
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

                new AdmViewUser(session);
            }
        });
    }
}
