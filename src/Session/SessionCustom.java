package src.Session;

import src.Model.Adm;
import src.Model.Usuario;

public class SessionCustom {
    private static Usuario userAtual;
    private static Adm     admAtual;

    public  void setUserAtual(Usuario user) {
        userAtual = user;
    }

    public  Usuario getUserAtual() {
        return userAtual;
    }

    public  Adm getAdmAtual() {
        return admAtual;
    }

    public  void setAdmAtual(Adm adm){
        admAtual = adm;
    }
}
