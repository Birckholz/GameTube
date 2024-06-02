package src.DAO;

import src.Model.DAO.Conexao;
import src.Model.GameFoto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameFotoDAO {

    private static final Logger logger = Logger.getLogger(GameFotoDAO.class.getName());

    public void addGameFoto(GameFoto gameFoto) {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding game photo", e);
        }
    }

    public void updateGameFoto(GameFoto gameFoto) {
        String query = "UPDATE GAME_FOTO SET PICTURE = ? WHERE ID_GAME = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBytes(1, gameFoto.getPicture());
            pstmt.setInt(2, gameFoto.getIdGame());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating game photo", e);
        }
    }

    public GameFoto findGameFotoByGameId(int idGame) {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding game photo by game ID", e);
        }
        return null;
    }

    public void deleteGameFoto(int idGame) {
        String query = "DELETE FROM GAME_FOTO WHERE ID_GAME = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idGame);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting game photo", e);
        }
    }

    public List<GameFoto> findAllGameFotos() {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all game photos", e);
        }
        return gameFotos;
    }

    public ResultSet gameComFotos() throws SQLException {
        String query = "SELECT GAME.ID, GAME.NOME, GAME.DESCRICAO, GAME.PRECO, GAME_FOTO.PICTURE " +
                "FROM GAME " +
                "INNER JOIN GAME_FOTO ON GAME.ID = GAME_FOTO.ID_GAME";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        try {
            conn = Conexao.getConexao();
            pstmt = conn.prepareStatement(query);
            resultSet = pstmt.executeQuery();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding games with photos", e);
            throw e;
        }

        return resultSet;
        }
    }

