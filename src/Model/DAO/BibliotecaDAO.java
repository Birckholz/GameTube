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
        String query = "SELECT ID_GAME FROM BIBLIOTECA WHERE id_usuario = ?";
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

    public ResultSet pegarGamesUsuario(int id_user) throws SQLException {
        String query = "SELECT GAME.ID, GAME.NOME, GAME.DESCRICAO, GAME.PRECO, GAME_FOTO.PICTURE " +
                "FROM GAME " +
                "INNER JOIN GAME_FOTO ON GAME.ID = GAME_FOTO.ID_GAME " +
                "INNER JOIN BIBLIOTECA ON GAME.ID = BIBLIOTECA.ID_GAME " +
                "INNER JOIN USUARIO ON USUARIO.ID = BIBLIOTECA.ID_USUARIO " +
                "WHERE USUARIO.ID = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id_user);
            System.out.println(resultSet);
            resultSet = pstmt.executeQuery();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding games with photos", e);
            throw e;
        }

        return resultSet;
    }
}
