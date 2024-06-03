package src.Controller;

import src.DAO.GameFotoDAO;
import src.Model.GameFoto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class GameFotoController {
    private GameFotoDAO gameFotoDAO;

    public GameFotoController() {
        this.gameFotoDAO = new GameFotoDAO();
    }

    public void addGameFoto(GameFoto gameFoto) {
        gameFotoDAO.addGameFoto(gameFoto);
    }

    public void updateGameFoto(GameFoto gameFoto) {
        gameFotoDAO.updateGameFoto(gameFoto);
    }

    public GameFoto getGameFotoByGameId(int idGame) {
        return gameFotoDAO.findGameFotoByGameId(idGame);
    }

    public void deleteGameFoto(int idGame) {
        gameFotoDAO.deleteGameFoto(idGame);
    }

    public List<GameFoto> getAllGameFotos() {
        return gameFotoDAO.findAllGameFotos();
    }
    public ResultSet fetchGameListingsWithPhotos() {
        try {
            return gameFotoDAO.gameComFotos();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
