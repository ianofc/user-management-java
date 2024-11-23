
package db;

import java.sql.*;
import javax.swing.JOptionPane;

public class conectbd {

    private Statement stm; // Responsável por preparar e realizar pesquisas no BD
    public ResultSet rs; // Responsável por armazenar o resultado de uma pesquisa
    private String driver = "org.postgresql.Driver"; // Responsável por identificar o serviço de BD
    private String caminho = "jdbc:postgresql://localhost:5439/crudian"; // Responsável por setar o local do BD 
    private String usuario = "postgres";
    private String senha = "ianbd";
    public Connection conn; // Responsável por realizar a conexão com o BD

    // Método para conectar ao banco de dados
    public Connection conecta() throws SQLException {
        try {
            Class.forName(driver); // Carrega o driver de conexão
            conn = DriverManager.getConnection(caminho, usuario, senha); // Realiza a conexão com o BD
            // JOptionPane.showMessageDialog(null, "Conectado com sucesso!"); // Imprime uma caixa de mensagem
            return conn;
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Driver de conexão não encontrado!\nErro: " + ex.getMessage());
        } catch (SQLException ex) {
            throw new SQLException("Erro de conexão!\nErro: " + ex.getMessage());
        }
    }

    // Método para executar uma consulta SQL
    public void executaSQL(String sql) throws SQLException {
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            throw new SQLException("Erro ao executar consulta SQL:\nErro: " + ex.getMessage());
        }
    }

    // Método para fechar a conexão com o BD
    public void desconecta() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão:\nErro: " + ex.getMessage());
        }
    }

    // Método para configurar o AutoCommit da conexão
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        try {
            conn.setAutoCommit(autoCommit);
        } catch (SQLException ex) {
            throw new SQLException("Erro ao configurar o AutoCommit:\nErro: " + ex.getMessage());
        }
    }

    // Método para efetuar o commit das transações no BD
    public void commit() throws SQLException {
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new SQLException("Erro ao efetuar o commit da transação:\nErro: " + ex.getMessage());
        }
    }

    // Método para efetuar o rollback das transações no BD
    public void rollback() throws SQLException {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            throw new SQLException("Erro ao efetuar o rollback da transação:\nErro: " + ex.getMessage());
        }
    }
}
