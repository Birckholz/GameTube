package src.Model.DAO;

import src.Model.Memento;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MementoDAO {
    private static final Logger logger = Logger.getLogger(MementoDAO.class.getName());
    private Connection conn;

    public MementoDAO() {
        try {
            this.conn = Conexao.getConexao();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao conectar ao banco de dados", e);
        }
    }

    public int addMemento(Memento memento) {
        String query = "INSERT INTO Memento (nome, email, username, senha, admin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, memento.getName());
            stmt.setString(2, memento.getEmail());
            stmt.setString(3, memento.getUsername());
            stmt.setString(4, memento.getSenha());
            stmt.setBoolean(5, memento.isAdmin());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                memento.setId(rs.getInt(1));
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao adicionar memento", e);
        }
        return -1;
    }

    public Memento getMemento(int id) {
        String query = "SELECT * FROM Memento WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Memento tempMemento = new Memento(
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getBoolean("admin"),
                            rs.getString("username")
                    );
                    tempMemento.setId(rs.getInt("id"));
                    return tempMemento;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao obter memento", e);
        }
        return null;
    }

    public void updateMemento(Memento memento) {
        String query = "UPDATE Memento SET nome = ?, email = ?, senha = ?, admin = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, memento.getName());
            stmt.setString(2, memento.getEmail());
            stmt.setString(3, memento.getSenha());
            stmt.setBoolean(4, memento.isAdmin());
            stmt.setInt(5, memento.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar memento", e);
        }
    }
}
