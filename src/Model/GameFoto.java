package src.Model;

public class GameFoto {
    private int id;
    private int idGame;
    private byte[] foto;

    public GameFoto() {
    }

    public GameFoto(int idGame, byte[] foto) {
        this.idGame = idGame;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public byte[] getPicture() {
        return foto;
    }

    public void setPicture(byte[] picture) {
        this.foto = picture;
    }
}

