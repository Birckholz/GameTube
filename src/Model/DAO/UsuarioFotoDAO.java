package src.Model.DAO;

import src.Model.UsuarioFoto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioFotoDAO {

    private static final Logger logger = Logger.getLogger(UsuarioFotoDAO.class.getName());

    public void addUsuarioFoto(UsuarioFoto usuarioFoto) {
        String query = "INSERT INTO USUARIO_FOTO (ID_USUARIO, PICTURE) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, usuarioFoto.getIdUsuario());
            pstmt.setBytes(2, usuarioFoto.getFoto());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuarioFoto.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding user photo", e);
        }
    }

    public void updateUsuarioFoto(UsuarioFoto usuarioFoto) {
        String query = "UPDATE USUARIO_FOTO SET PICTURE = ? WHERE ID_USUARIO = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBytes(1, usuarioFoto.getFoto());
            pstmt.setInt(2, usuarioFoto.getIdUsuario());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user photo", e);
        }
    }

    public UsuarioFoto findUsuarioFotoByUsuarioId(int idUsuario) {
        String query = "SELECT * FROM USUARIO_FOTO WHERE ID_USUARIO = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioFoto(
                            rs.getInt("ID_USUARIO"),
                            rs.getBytes("PICTURE")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user photo by ID", e);
        }
        return null;
    }

    public void deleteUsuarioFoto(int idUsuario) {
        String query = "DELETE FROM USUARIO_FOTO WHERE ID_USUARIO = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user photo", e);
        }
    }

    public List<UsuarioFoto> findAllUsuarioFotos() {
        String query = "SELECT * FROM USUARIO_FOTO";
        List<UsuarioFoto> usuarioFotos = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                UsuarioFoto usuarioFoto = new UsuarioFoto(
                        rs.getInt("ID_USUARIO"),
                        rs.getBytes("PICTURE")
                );
                usuarioFotos.add(usuarioFoto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all user photos", e);
        }
        return usuarioFotos;
    }
}
