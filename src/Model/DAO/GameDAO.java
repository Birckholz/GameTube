package src.Model.DAO;

import src.Model.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameDAO {

    private static final Logger logger = Logger.getLogger(GameDAO.class.getName());

    public void addGame(Game game) {
        String query = "INSERT INTO GAME (name, descricao, price, directory) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, game.getName());
            pstmt.setString(2, game.getDescricao());
            pstmt.setDouble(3, game.getPrice());
            pstmt.setString(4, game.getDirectory());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding game", e);
        }
    }

    public void updateGame(Game game) {
        String query = "UPDATE GAME SET name = ?, descricao = ?, price = ?, directory = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, game.getName());
            pstmt.setString(2, game.getDescricao());
            pstmt.setDouble(3, game.getPrice());
            pstmt.setString(4, game.getDirectory());
            pstmt.setInt(5, game.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating game", e);
        }
    }

    public Game findGame(int id) {
        String query = "SELECT * FROM GAME WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Game game = new Game(
                            rs.getString("name"),
                            rs.getString("descricao"),
                            rs.getDouble("price"),
                            rs.getString("directory")
                    );
                    game.setId(rs.getInt("id"));
                    return game;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding game by id", e);
        }
        return null;
    }

    public void deleteGame(int id) {
        String query = "DELETE FROM GAME WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting game", e);
        }
    }

    public List<Game> findAllGames() {
        String query = "SELECT * FROM GAME";
        List<Game> games = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Game game = new Game(
                        rs.getString("name"),
                        rs.getString("descricao"),
                        rs.getDouble("price"),
                        rs.getString("directory")
                );
                game.setId(rs.getInt("id"));
                games.add(game);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding all games", e);
        }
        return games;
    }
}
