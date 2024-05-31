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
        try {
            usuarioFotoDAO.addUsuarioFoto(usuarioFoto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUsuarioFoto(UsuarioFoto usuarioFoto) {
        try {
            usuarioFotoDAO.updateUsuarioFoto(usuarioFoto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UsuarioFoto getUsuarioFotoByUsuarioId(int idUsuario) {
        try {
            return usuarioFotoDAO.findUsuarioFotoByUsuarioId(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteUsuarioFoto(int idUsuario) {
        try {
            usuarioFotoDAO.deleteUsuarioFoto(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UsuarioFoto> getAllUsuarioFotos() {
        try {
            return usuarioFotoDAO.findAllUsuarioFotos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
