package src.Controller;

import src.Model.Usuario;
import src.Model.DAO.UsuarioDAO;

import java.sql.SQLException;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void insertUsuario(Usuario usuario) {
        try {
            usuarioDAO.insertUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUsuario(Usuario usuario) {
        try {
            usuarioDAO.updateUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUsuario(int id) {
        try {
            usuarioDAO.removeUsuario(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario findUsuarioById(int id) {
        try {
            return usuarioDAO.findUsuarioById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
