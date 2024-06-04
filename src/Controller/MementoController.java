package src.Controller;

import src.Model.Usuario;
import src.Model.Memento;
import src.Model.DAO.MementoDAO;

import java.sql.SQLException;

public class MementoController {
    private MementoDAO mementoDAO;
    private UsuarioController userController;
    private AdmController admController;

    public MementoController() throws SQLException {
        this.mementoDAO = new MementoDAO();
        this.admController = new AdmController();
        this.userController = new UsuarioController();
    }

    public int updateUser(String name, String email, String password, boolean admin, String username,int userID) throws SQLException {
        int mementoID;
        boolean user;
        if (admin) {
            mementoID = admController.findAdm(userID).getMementoId();
            user = true;
        } else {
            mementoID = userController.findUsuarioById(userID).getMementoId();
            user = false;
        }

        if (mementoDAO.getMemento(mementoID) == null) {
            int newMementoId = mementoDAO.addMemento(new Memento(name, email, password, admin, username));
            if (user) {
                userController.findUsuarioById(userID).setMementoId(newMementoId);
            } else {
                admController.findAdm(userID).setMementoId(newMementoId);
            }
            return newMementoId;
        } else {
            mementoDAO.updateMemento(new Memento(name, email, password, admin, username));
        }
        return 0;
    }

    public Memento restoreUser(int mementoId) throws SQLException {
        Memento memento = mementoDAO.getMemento(mementoId);
        if (memento != null) {
            return mementoDAO.getMemento(mementoId);
        }
        return  null;
    }
}
