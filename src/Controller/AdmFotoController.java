package src.Controller;

import src.Model.DAO.AdmFotoDAO;
import src.Model.AdmFoto;
import java.sql.SQLException;

public class AdmFotoController {
    private AdmFotoDAO admFotoDAO;

    public AdmFotoController() {
        this.admFotoDAO = new AdmFotoDAO();
    }

    public void addAdmFoto(AdmFoto admFoto) {
        try {
            admFotoDAO.addAdmFoto(admFoto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAdmFoto(AdmFoto admFoto) {
        try {
            admFotoDAO.updateAdmFoto(admFoto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AdmFoto getAdmFotoByAdmId(int idAdm) {
        try {
            return admFotoDAO.findAdmFotoByAdmId(idAdm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAdmFoto(int idAdm) {
        try {
            admFotoDAO.deleteAdmFoto(idAdm);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
