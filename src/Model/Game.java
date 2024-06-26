package src.Model;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Game {

    private int id;
    private String name;
    private String descricao;
    private Double price;
            
    public Game(String name, String descricao, Double preco) {
        this.name = name;
        this.descricao = descricao;
        this.price = preco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

}
