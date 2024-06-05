package src.Controller;

import src.DAO.GameFotoDAO;
import src.Model.DAO.UsuarioDAO;
import src.Model.GameFoto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameFotoController {
    private GameFotoDAO gameFotoDAO;
    private static final Logger logger = Logger.getLogger(GameFotoController.class.getName());

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
            logger.log(Level.SEVERE, "Error finding games with photos", e);
        }
        return null;
    }
}
