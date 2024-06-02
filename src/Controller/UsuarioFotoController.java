package src.Controller;

import src.Model.DAO.UsuarioFotoDAO;
import src.Model.UsuarioFoto;

import java.sql.SQLException;
import java.util.List;

public class UsuarioFotoController {
    private UsuarioFotoDAO usuarioFotoDAO;

    public UsuarioFotoController() {
        this.usuarioFotoDAO = new UsuarioFotoDAO();
    }

    public void addUsuarioFoto(UsuarioFoto usuarioFoto) {
        usuarioFotoDAO.addUsuarioFoto(usuarioFoto);
    }

    public void updateUsuarioFoto(UsuarioFoto usuarioFoto) {
        usuarioFotoDAO.updateUsuarioFoto(usuarioFoto);
    }

    public UsuarioFoto getUsuarioFotoByUsuarioId(int idUsuario) {
        return usuarioFotoDAO.findUsuarioFotoByUsuarioId(idUsuario);
    }

    public void deleteUsuarioFoto(int idUsuario) {
        usuarioFotoDAO.deleteUsuarioFoto(idUsuario);
    }

    public List<UsuarioFoto> getAllUsuarioFotos() {
        return usuarioFotoDAO.findAllUsuarioFotos();
    }
}
