package src.Controller;

import src.Model.Game;
import src.Model.DAO.GameDAO;

import java.sql.SQLException;
import java.util.List;

public class GameController {
    private GameDAO gameDAO;

    public GameController() {
        this.gameDAO = new GameDAO();
    }

    public void addGame(Game game) {
        try {
            gameDAO.addGame(game);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGame(Game game) {
        try {
            gameDAO.updateGame(game);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Game findGame(int id) {
        try {
            return gameDAO.findGame(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteGame(int id) {
        try {
            gameDAO.deleteGame(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Game> findAllGames() {
        try {
            return gameDAO.findAllGames();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
