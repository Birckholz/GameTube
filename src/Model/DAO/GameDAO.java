package src.Model.DAO;

import src.Model.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public void addGame(Game game) throws SQLException {
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
        }
    }

    public void updateGame(Game game) throws SQLException {
        String query = "UPDATE GAME SET name = ?, descricao = ?, price = ?, directory = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, game.getName());
            pstmt.setString(2, game.getDescricao());
            pstmt.setDouble(3, game.getPrice());
            pstmt.setString(4, game.getDirectory());
            pstmt.setInt(5, game.getId());
            pstmt.executeUpdate();
        }
    }

    public Game findGame(int id) throws SQLException {
        String query = "SELECT * FROM GAME WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Game(
                            rs.getString("name"),
                            rs.getString("descricao"),
                            rs.getDouble("price"),
                            rs.getString("directory")
                    );
                }
            }
        }
        return null;
    }

    public void deleteGame(int id) throws SQLException {
        String query = "DELETE FROM GAME WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Game> findAllGames() throws SQLException {
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
                games.add(game);
            }
        }
        return games;
    }
}
