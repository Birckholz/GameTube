package src.Model.DAO;

import src.Model.Adm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdmDAO {

    private static final Logger logger = Logger.getLogger(AdmDAO.class.getName());

    public int validateUser(String email, String password) {
        String query = "SELECT id FROM adm WHERE email = ? AND senha = ?";

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

    public void addAdm(Adm adm) {
        String query = "INSERT INTO ADM (administrador, nome, email, senha, username) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setBoolean(1, adm.isAdmin());
            pstmt.setString(2, adm.getName());
            pstmt.setString(3, adm.getEmail());
            pstmt.setString(4, adm.getSenha());
            pstmt.setString(5, adm.getUsername());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    adm.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding Adm", e);
        }
    }

    public void updateAdm(Adm adm) {
        String query = "UPDATE ADM SET admin = ?, nome = ?, email = ?, senha = ?, username = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBoolean(1, adm.isAdmin());
            pstmt.setString(2, adm.getName());
            pstmt.setString(3, adm.getEmail());
            pstmt.setString(4, adm.getSenha());
            pstmt.setString(5, adm.getUsername());
            pstmt.setInt(6, adm.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating Adm", e);
        }
    }

    public Adm findAdm(int id) {
        String query = "SELECT * FROM ADM WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Adm(
                            rs.getBoolean("admin"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding Adm by id", e);
        }
        return null;
    }

    public void deleteAdm(int id) {
        String query = "DELETE FROM ADM WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting Adm", e);
        }
    }

    public List<Adm> findAllAdms() {
        String query = "SELECT * FROM ADM";
        List<Adm> adms = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Adm adm = new Adm(
                        rs.getBoolean("admin"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("username")
                );
                adm.setId(rs.getInt("id"));
                adms.add(adm);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all Adms", e);
        }
        return adms;
    }
}
