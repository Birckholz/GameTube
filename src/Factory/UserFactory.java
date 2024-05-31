package src.Factory;

import src.Model.UserBase;

public interface UserFactory {
    UserBase createUser(String name, String email, String senha, String username);
}
