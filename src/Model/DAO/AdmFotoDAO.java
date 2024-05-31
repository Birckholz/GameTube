package src.Model.DAO;

import src.Model.AdmFoto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdmFotoDAO {

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
        }
    }

    public void updateAdmFoto(AdmFoto admFoto) throws SQLException {
        String query = "UPDATE ADM_FOTO SET PICTURE = ? WHERE ID_ADM = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBytes(1, admFoto.getFoto());
            pstmt.setInt(2, admFoto.getIdAdm());
            pstmt.executeUpdate();
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
        }
        return null; // No photo found for the specified admin ID
    }

    public void deleteAdmFoto(int idAdm) throws SQLException {
        String query = "DELETE FROM ADM_FOTO WHERE ID_ADM = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idAdm);
            pstmt.executeUpdate();
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
        }
        return admFotos;
    }
}
