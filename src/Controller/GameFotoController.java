package src.Controller;

import src.DAO.GameFotoDAO;
import src.Model.GameFoto;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class GameFotoController {
    private GameFotoDAO gameFotoDAO;

    public GameFotoController() {
        this.gameFotoDAO = new GameFotoDAO();
    }

    public void addGameFoto(GameFoto gameFoto) {
        try {
            gameFotoDAO.addGameFoto(gameFoto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGameFoto(GameFoto gameFoto) {
        try {
            gameFotoDAO.updateGameFoto(gameFoto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GameFoto getGameFotoByGameId(int idGame) {
        try {
            return gameFotoDAO.findGameFotoByGameId(idGame);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteGameFoto(int idGame) {
        try {
            gameFotoDAO.deleteGameFoto(idGame);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<GameFoto> getAllGameFotos() {
        try {
            return gameFotoDAO.findAllGameFotos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
