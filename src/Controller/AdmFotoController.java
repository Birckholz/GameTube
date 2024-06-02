package src.Controller;

import src.Model.DAO.AdmFotoDAO;
import src.Model.AdmFoto;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdmFotoController {
    private static final Logger logger = Logger.getLogger(AdmFotoController.class.getName());

    private AdmFotoDAO admFotoDAO;

    public AdmFotoController() {
        this.admFotoDAO = new AdmFotoDAO();
    }

    public void addAdmFoto(AdmFoto admFoto) {
        try {
            admFotoDAO.addAdmFoto(admFoto);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding administrator photo", e);
        }
    }

    public void updateAdmFoto(AdmFoto admFoto) {
        try {
            admFotoDAO.updateAdmFoto(admFoto);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating administrator photo", e);
        }
    }

    public AdmFoto getAdmFotoByAdmId(int idAdm) {
        try {
            return admFotoDAO.findAdmFotoByAdmId(idAdm);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding administrator photo by ID", e);
            return null;
        }
    }

    public void deleteAdmFoto(int idAdm) {
        try {
            admFotoDAO.deleteAdmFoto(idAdm);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting administrator photo", e);
        }
    }
}
