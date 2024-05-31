package src.Model.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexao {
    private static final String url = "jdbc:mysql://localhost:3306/gametube";
    private static final String user = "root";
    private static final String senha = "admin";
    private static Connection conexao = null;

    private Conexao() {
    }

    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(url, user, senha);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("Database driver not found", e);
            }
        }
        return conexao;
    }

    public static void closeConnection() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }
}
