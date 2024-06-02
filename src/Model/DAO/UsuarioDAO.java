package src.Model.DAO;

import src.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

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
            logger.log(Level.SEVERE, "Error validating user", e);
            return -1;
        }
    }

    public void insertUsuario(Usuario user) {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting user", e);
        }
    }

    public void updateUsuario(Usuario user) {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user", e);
        }
    }

    public void removeUsuario(int id) {
        String query = "DELETE FROM Usuario WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user", e);
        }
    }

    public Usuario findUsuarioById(int id) {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by ID", e);
        }
        return null;
    }

}
