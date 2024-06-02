package src.Model;

public class Adm extends UserBase{
    private final boolean admin;
    private int id;
    private String username;
    private int mementoId;

    public Adm(boolean admin, String name, String email, String senha, String username){
        super(name, email, senha);
        this.admin = admin;
        this.username = username;
    }

    public int getMementoId() {
        return mementoId;
    }

    public void setMementoId(int mementoId) {
        this.mementoId = mementoId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
