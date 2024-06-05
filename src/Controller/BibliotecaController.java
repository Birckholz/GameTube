package src.Controller;

import src.Model.Biblioteca;
import src.Model.DAO.BibliotecaDAO;

import src.Model.Biblioteca;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BibliotecaController {
    private BibliotecaDAO bibliotecaDAO;

    private static final Logger LOGGER = Logger.getLogger(BibliotecaController.class.getName());

    public BibliotecaController() {
            bibliotecaDAO = new BibliotecaDAO();
    }

    public List<Integer> findGamesByUsuario(int idUsuario) {
        return bibliotecaDAO.findGamesByUsuario(idUsuario);
    }

    public boolean insertBiblioteca(int idGame, int idUsuario) {
        Biblioteca biblioteca = new Biblioteca(idGame, idUsuario);
        try {
            bibliotecaDAO.insertBiblioteca(biblioteca);
            return true; // Indica que a inserção foi bem-sucedida
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting into Biblioteca: ", e);
            return false; // Indica que a inserção falhou
        }
    }

    public boolean checkUserGames(int idUser, int idGame) {
        List<Integer> gameIDs = bibliotecaDAO.doesUserHaveGame(idUser);
        for (int id : gameIDs) {
            if (id == idGame) {
                return false;
            }
        }
        return true;
    }
}
