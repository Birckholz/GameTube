package src.Model;


public class Biblioteca {
    private int idGame;
    private int idUsuario;

    public Biblioteca(int idGame, int idUsuario) {
        this.idGame = idGame;
        this.idUsuario = idUsuario;
    }

    public int getIdGame() {
        return idGame;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
}
