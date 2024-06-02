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
        gameDAO.addGame(game);
    }

    public void updateGame(Game game) {
        gameDAO.updateGame(game);
    }

    public Game findGame(int id) {
        return gameDAO.findGame(id);
    }

    public void deleteGame(int id) {
        gameDAO.deleteGame(id);
    }

    public List<Game> findAllGames() {
        return gameDAO.findAllGames();
    }
}
