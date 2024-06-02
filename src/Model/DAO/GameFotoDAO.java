package src.DAO;

import src.Model.DAO.Conexao;
import src.Model.GameFoto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameFotoDAO {

    public void addGameFoto(GameFoto gameFoto) throws SQLException {
        String query = "INSERT INTO GAME_FOTO (ID_GAME, PICTURE) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, gameFoto.getIdGame());
            pstmt.setBytes(2, gameFoto.getPicture());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    gameFoto.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateGameFoto(GameFoto gameFoto) throws SQLException {
        String query = "UPDATE GAME_FOTO SET PICTURE = ? WHERE ID_GAME = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBytes(1, gameFoto.getPicture());
            pstmt.setInt(2, gameFoto.getIdGame());
            pstmt.executeUpdate();
        }
    }

    public GameFoto findGameFotoByGameId(int idGame) throws SQLException {
        String query = "SELECT * FROM GAME_FOTO WHERE ID_GAME = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idGame);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new GameFoto(
                            rs.getInt("ID"),
                            rs.getInt("ID_GAME"),
                            rs.getBytes("PICTURE")
                    );
                }
            }
        }
        return null;
    }

    public void deleteGameFoto(int idGame) throws SQLException {
        String query = "DELETE FROM GAME_FOTO WHERE ID_GAME = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idGame);
            pstmt.executeUpdate();
        }
    }

    public List<GameFoto> findAllGameFotos() throws SQLException {
        String query = "SELECT * FROM GAME_FOTO";
        List<GameFoto> gameFotos = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                GameFoto gameFoto = new GameFoto(
                        rs.getInt("ID"),
                        rs.getInt("ID_GAME"),
                        rs.getBytes("PICTURE")
                );
                gameFotos.add(gameFoto);
            }
        }
        return gameFotos;
    }

    public ResultSet gameComFotos() throws SQLException {
            String query = ("SELECT GAME.ID, GAME.NOME, GAME.DESCRICAO, GAME.PRECO, GAME_FOTO.PICTURE " +
                    "FROM GAME " +
                    "INNER JOIN GAME_FOTO ON GAME.ID = GAME_FOTO.ID_GAME");

            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet resultSet = null;

            try {
                conn = Conexao.getConexao();
                pstmt = conn.prepareStatement(query);
                resultSet = pstmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle any SQL exceptions appropriately
                throw e;
            } finally {
                // Close resources in a finally block to ensure they are always closed
                if (resultSet != null) {
                    resultSet.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            return resultSet;
        }
    }

