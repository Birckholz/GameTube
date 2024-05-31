package src.Controller;

import src.Model.Adm;
import src.Model.DAO.AdmDAO;

import java.sql.SQLException;
import java.util.List;

public class AdmController {
    private AdmDAO admDAO;

    public AdmController() {
        this.admDAO = new AdmDAO();
    }

    public void addAdm(Adm adm) {
        try {
            admDAO.addAdm(adm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAdm(Adm adm) {
        try {
            admDAO.updateAdm(adm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Adm findAdm(int id) {
        try {
            return admDAO.findAdm(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAdm(int id) {
        try {
            admDAO.deleteAdm(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Adm> findAllAdms() {
        try {
            return admDAO.findAllAdms();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
