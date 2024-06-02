package src.Model.DAO;
import src.Model.Memento;

import java.sql.*;

public class MementoDAO {
    private Connection conn;

    public MementoDAO() throws SQLException {
        this.conn = Conexao.getConexao();
    }

    public int addMemento(Memento memento) throws SQLException {
        String query = "INSERT INTO Memento (name, email, username,password, admin) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, memento.getName());
        stmt.setString(2, memento.getUsername());
        stmt.setString(3, memento.getEmail());
        stmt.setString(4, memento.getSenha());
        stmt.setBoolean(5, memento.isAdmin());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            memento.setId(rs.getInt(1));
        }
        return rs.getInt((1));
    }

    public Memento getMemento(int id) throws SQLException {
        String query = "SELECT * FROM Memento WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Memento tempMemento = new Memento(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getBoolean("admin"),
                    rs.getString("username")
            );
            tempMemento.setId(rs.getInt("id"));
            return tempMemento;
        }
        return null;
    }

    public void updateMemento(Memento memento) throws SQLException {
        String query = "UPDATE Memento SET name = ?, email = ?, password = ?, admin = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, memento.getName());
        stmt.setString(2, memento.getEmail());
        stmt.setString(3, memento.getSenha());
        stmt.setBoolean(4, memento.isAdmin());
        stmt.setInt(5, memento.getId());
        stmt.executeUpdate();
    }
}
