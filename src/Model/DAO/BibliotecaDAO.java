package src.Model.DAO;

import src.Model.Biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BibliotecaDAO {

    private static final Logger logger = Logger.getLogger(BibliotecaDAO.class.getName());

    public List<Integer> findGamesByUsuario(int idUsuario) {
        List<Integer> gameIds = new ArrayList<>();
        String query = "SELECT ID_GAME FROM BIBLIOTECA WHERE ID_USUARIO = ?";

        try (Connection connection = Conexao.getConexao();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idUsuario);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    gameIds.add(resultSet.getInt("ID_GAME"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding games by user ID", e);
        }

        return gameIds;
    }

    public List<Integer> doesUserHaveGame(int userId) {
        List<Integer> idGameList = new ArrayList<>();
        String query = "SELECT ID_GAME FROM BIBLIOTECA WHERE id_user = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    idGameList.add(rs.getInt("ID_GAME"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving games for user", e);
        }
        return idGameList;
    }

    public void insertBiblioteca(Biblioteca biblioteca) throws SQLException {
        String query = "INSERT INTO BIBLIOTECA (ID_GAME, ID_USUARIO) VALUES (?, ?)";

        try (Connection connection = Conexao.getConexao();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, biblioteca.getIdGame());
            preparedStatement.setInt(2, biblioteca.getIdUsuario());
            preparedStatement.executeUpdate();

        }
    }
}
