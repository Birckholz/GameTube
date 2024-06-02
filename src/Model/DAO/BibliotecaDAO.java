package src.Model.DAO;

import src.Model.Biblioteca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaDAO {


    public List<Integer> findGamesByUsuario(int idUsuario) throws SQLException {
        Connection connection = Conexao.getConexao();
        List<Integer> gameIds = new ArrayList<>();
        String query = "SELECT ID_GAME FROM BIBLIOTECA WHERE ID_USUARIO = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gameIds.add(resultSet.getInt("ID_GAME"));
            }
        }
        return gameIds;
    }

    public void insertBiblioteca(Biblioteca biblioteca) throws SQLException {
        String query = "INSERT INTO BIBLIOTECA (ID_GAME, ID_USUARIO) VALUES (?, ?)";
        Connection connection = Conexao.getConexao();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, biblioteca.getIdGame());
            preparedStatement.setInt(2, biblioteca.getIdUsuario());
            preparedStatement.executeUpdate();
        }
    }
}

