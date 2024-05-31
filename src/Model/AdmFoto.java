package src.Model;

public class AdmFoto {
    private int id;
    private int idAdm;
    private byte[] foto;

    // Constructors
    public AdmFoto() {
    }

    public AdmFoto(int id, int idAdm, byte[] foto) {
        this.id = id;
        this.idAdm = idAdm;
        this.foto = foto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAdm() {
        return idAdm;
    }

    public void setIdAdm(int idAdm) {
        this.idAdm = idAdm;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] picture) {
        this.foto = picture;
    }
}

