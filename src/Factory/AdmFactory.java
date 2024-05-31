package src.Factory;

import src.Model.Adm;
import src.Model.UserBase;

public class AdmFactory implements UserFactory {
    @Override
    public UserBase createUser(String name, String email, String senha, String username) {
        boolean admin = true;
        return new Adm(admin, name, email, senha, username);
    }
}
