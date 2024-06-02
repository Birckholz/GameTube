package src.Controller;

import src.Model.Usuario;
import src.Model.DAO.UsuarioDAO;

import java.sql.SQLException;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public int doLogin(String email, String password) {
        return usuarioDAO.validateUser(email, password);
    }

    public void insertUsuario(Usuario usuario) {
        usuarioDAO.insertUsuario(usuario);
    }

    public void updateUsuario(Usuario usuario) {
        usuarioDAO.updateUsuario(usuario);
    }

    public void removeUsuario(int id) {
        usuarioDAO.removeUsuario(id);
    }

    public Usuario findUsuarioById(int id) {
        return usuarioDAO.findUsuarioById(id);
    }
}
