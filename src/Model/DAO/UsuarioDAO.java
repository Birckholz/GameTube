package src.Model.DAO;

import src.Model.Game;
import src.Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

    public int validateUser(String email, String password) {
        String query = "SELECT id FROM USUARIO WHERE email = ? AND senha = ?";
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

    public int insertUsuario(Usuario user) {
        String query = "INSERT INTO Usuario (nome, email, senha, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUsername());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting user", e);
        }
        return user.getId();
    }

    public void updateUsuario(Usuario user) {
        String query = "UPDATE Usuario SET nome = ?, email = ?, senha = ?, username = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUsername());
            pstmt.setInt(5, user.getId());
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
                    String name = rs.getString("nome");
                    String email = rs.getString("email");
                    String password = rs.getString("senha");
                    String username = rs.getString("username");
                    Usuario user = new Usuario(email, password, name, username);
                    user.setId(id);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by ID", e);
        }
        return null;
    }
    public List<Usuario> findAllUsuarios() {
        String query = "SELECT * FROM USUARIO";
        List<Usuario> users = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Usuario user = new Usuario(
                rs.getString("email"),
                rs.getString("senha"),
                rs.getString("nome"),
                rs.getString("username")
                );
                user.setId(rs.getInt("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all users", e);
        }
        return users;
    }

}
