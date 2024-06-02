package src.Model.DAO;

import src.Model.Adm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdmDAO {


    public int validateUser(String email, String password) {
        String query = "SELECT id FROM administarador WHERE email = ? AND password = ?";

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

    public void addAdm(Adm adm) throws SQLException {
        String query = "INSERT INTO ADM (admin, name, email, senha, username) VALUES (?, ?, ?, ?, ?)";
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
        }
    }

    public void updateAdm(Adm adm) throws SQLException {
        String query = "UPDATE ADM SET admin = ?, name = ?, email = ?, senha = ?, username = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBoolean(1, adm.isAdmin());
            pstmt.setString(2, adm.getName());
            pstmt.setString(3, adm.getEmail());
            pstmt.setString(4, adm.getSenha());
            pstmt.setString(5, adm.getUsername());
            pstmt.setInt(6, adm.getId());
            pstmt.executeUpdate();
        }
    }

    public Adm findAdm(int id) throws SQLException {
        String query = "SELECT * FROM ADM WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Adm(
                            rs.getBoolean("admin"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("username")
                    );
                }
            }
        }
        return null;
    }

    public void deleteAdm(int id) throws SQLException {
        String query = "DELETE FROM ADM WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Adm> findAllAdms() throws SQLException {
        String query = "SELECT * FROM ADM";
        List<Adm> adms = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Adm adm = new Adm(
                        rs.getBoolean("admin"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("username")
                );
                adm.setId(rs.getInt("id"));
                adms.add(adm);
            }
        }
        return adms;
    }
}
