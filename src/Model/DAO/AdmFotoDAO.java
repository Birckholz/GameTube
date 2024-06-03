package src.Model.DAO;

import src.Model.AdmFoto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdmFotoDAO {

    private static final Logger logger = Logger.getLogger(AdmFotoDAO.class.getName());

    public void addAdmFoto(AdmFoto admFoto) throws SQLException {
        String query = "INSERT INTO ADM_FOTO (ID_ADM, PICTURE) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, admFoto.getIdAdm());
            pstmt.setBytes(2, admFoto.getFoto());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    admFoto.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding AdmFoto", e);
            throw e;
        }
    }

    public void updateAdmFoto(AdmFoto admFoto) throws SQLException {
        String query = "UPDATE ADM_FOTO SET PICTURE = ? WHERE ID_ADM = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBytes(1, admFoto.getFoto());
            pstmt.setInt(2, admFoto.getIdAdm());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating AdmFoto", e);
            throw e;
        }
    }

    public AdmFoto findAdmFotoByAdmId(int idAdm) throws SQLException {
        String query = "SELECT * FROM ADM_FOTO WHERE ID_ADM = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idAdm);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new AdmFoto(
                            rs.getInt("ID"),
                            rs.getInt("ID_ADM"),
                            rs.getBytes("PICTURE")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding AdmFoto by AdmId", e);
            throw e;
        }
        return null; // No photo found for the specified admin ID
    }

    public void deleteAdmFoto(int idAdm) throws SQLException {
        String query = "DELETE FROM ADM_FOTO WHERE ID_ADM = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idAdm);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting AdmFoto", e);
            throw e;
        }
    }

    public List<AdmFoto> findAllAdmFotos() throws SQLException {
        String query = "SELECT * FROM ADM_FOTO";
        List<AdmFoto> admFotos = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                AdmFoto admFoto = new AdmFoto(
                        rs.getInt("ID"),
                        rs.getInt("ID_ADM"),
                        rs.getBytes("PICTURE")
                );
                admFotos.add(admFoto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all AdmFotos", e);
            throw e;
        }
        return admFotos;
    }
}
