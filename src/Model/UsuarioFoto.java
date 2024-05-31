package src.Model;

public class UsuarioFoto {
    private int id;
    private int idUsuario;
    private byte[] foto;

    // Constructors
    public UsuarioFoto() {
    }

    public UsuarioFoto(int id, int idUsuario, byte[] foto) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.foto = foto;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] picture) {
        this.foto = picture;
    }
}


