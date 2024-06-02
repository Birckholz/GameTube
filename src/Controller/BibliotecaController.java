package src.Controller;

import src.Model.Biblioteca;
import src.Model.DAO.BibliotecaDAO;

import src.Model.Biblioteca;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BibliotecaController {
    private BibliotecaDAO bibliotecaDAO;

    public BibliotecaController() {
            bibliotecaDAO = new BibliotecaDAO();
    }

    public List<Integer> findGamesByUsuario(int idUsuario) {
        try {
            return bibliotecaDAO.findGamesByUsuario(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertBiblioteca(int idGame, int idUsuario) {
        try {
            Biblioteca biblioteca = new Biblioteca(idGame, idUsuario);
            bibliotecaDAO.insertBiblioteca(biblioteca);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
