package src.Controller;

import src.Model.Adm;
import src.Model.DAO.AdmDAO;
import src.Model.DAO.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdmController {
    private AdmDAO admDAO;

    public AdmController() {
        this.admDAO = new AdmDAO();
    }

    public int doLogin(String email, String password) {
        return admDAO.validateUser(email, password);
    }

    public void addAdm(Adm adm) {
        admDAO.addAdm(adm);
    }

    public void updateAdm(Adm adm) {
        admDAO.updateAdm(adm);
    }

    public Adm findAdm(int id) {
        return admDAO.findAdm(id);
    }

    public void deleteAdm(int id) {
        admDAO.deleteAdm(id);
    }

    public List<Adm> findAllAdms() {
        return admDAO.findAllAdms();
    }
}
