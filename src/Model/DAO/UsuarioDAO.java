package src.Model.DAO;
import src.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public int validateUser(String email, String password) {
        String query = "SELECT id FROM users WHERE email = ? AND password = ?";

        try (Connection connection = Conexao.getConexao();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void insertUsuario(Usuario user) throws SQLException {
        String query = "INSERT INTO Usuario (name, email, password, username, mementoId, profilePic) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUsername());
            pstmt.setInt(5, user.getMementoId());
            pstmt.setString(6, user.getProfilePic());
            pstmt.executeUpdate();
        }
    }

    public void updateUsuario(Usuario user) throws SQLException {
        String query = "UPDATE Usuario SET name = ?, email = ?, password = ?, username = ?, profilePic = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUsername());
            pstmt.setString(5, user.getProfilePic());
            pstmt.setInt(6, user.getId());
            pstmt.executeUpdate();
        }
    }

    public void removeUsuario(int id) throws SQLException {
        String query = "DELETE FROM Usuario WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Usuario findUsuarioById(int id) throws SQLException {
        String query = "SELECT * FROM Usuario WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String username = rs.getString("username");
                    int mementoId = rs.getInt("mementoId");
                    String profilePic = rs.getString("profilePic");
                    Usuario user = new Usuario(email, password, name, username);
                    user.setId(id);
                    user.setMementoId(mementoId);
                    user.setProfilePic(profilePic);
                    return user;
                }
            }
        }
        return null;
    }

}
