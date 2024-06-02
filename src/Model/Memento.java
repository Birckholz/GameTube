package src.Model;

public class Memento extends UserBase{
    private int id;
    private final boolean admin;
    private String username;

    public Memento(String name, String email, String password, boolean admin, String username) {
        super(name, email, password);
        this.admin = admin;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getSenha() {
        return super.getSenha();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setSenha(String senha) {
        super.setSenha(senha);
    }
}

