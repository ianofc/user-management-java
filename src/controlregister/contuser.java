package controlregister;

import db.conectbd;
import modelclass.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class contuser {

    private final conectbd bd = new conectbd();

    //Método para inserir um usuário no banco de dados
     public void inserirUser(user user) throws SQLException {
        try {
            bd.conecta();
            String sql = "INSERT INTO users (name_user, email_user, password_user) VALUES (?, ?, ?)";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setString(1, user.getName_user());
            stmt.setString(2, user.getEmail_user());
            stmt.setString(3, user.getPassword_user());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário inserido com sucesso!");
        } catch (SQLException ex) {
            throw new SQLException("Erro ao inserir usuário:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }

    // Método para atualizar um usuário no banco de dados
    public void atualizarUser(user user) throws SQLException {
        try {
            bd.conecta();
            String sql = "UPDATE users SET name_user = ?, email_user = ?, password_user = ? WHERE id_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setString(1, user.getName_user());
            stmt.setString(2, user.getEmail_user());
            stmt.setString(3, user.getPassword_user());
            stmt.setInt(4, user.getId_user());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");
        } catch (SQLException ex) {
            throw new SQLException("Erro ao atualizar usuário:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }

    // Método para deletar um usuário do banco de dados pelo ID
    public void deletarUser(int id_user) throws SQLException {
        try {
            bd.conecta();
            String sql = "DELETE FROM users WHERE id_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setInt(1, id_user);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
        } catch (SQLException ex) {
            throw new SQLException("Erro ao deletar usuário:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }

    // Método para listar todos os usuários
    public List<user> listarUsers() throws SQLException {
        try {
            bd.conecta();
            List<user> users = new ArrayList<>();
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user user = new user();
                user.setId_user(rs.getInt("id_user"));
                user.setName_user(rs.getString("name_user"));
                user.setEmail_user(rs.getString("email_user"));
                user.setPassword_user(rs.getString("password_user"));
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            throw new SQLException("Erro ao listar usuários:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }
    
    //Metodo para listar um usuario pelo id
    public user obteruserpeloid(int id_user) throws SQLException {
        try {
            bd.conecta();
            String sql = "SELECT * FROM users WHERE id_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setInt(1, id_user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user user = new user();
                user.setId_user(rs.getInt("id_user"));
                user.setName_user(rs.getString("name_user"));
                user.setEmail_user(rs.getString("email_user"));
                user.setPassword_user(rs.getString("password_user"));
                return user;
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Erro ao obter Usuario pelo ID:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }

public user obterUsuarioPeloNomeEEmail(String nome, String email) throws SQLException {
        try {
            bd.conecta();
            String sql = "SELECT * FROM users WHERE name_user = ? AND email_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user usuario = new user();
                usuario.setId_user(rs.getInt("id_user"));
                usuario.setName_user(rs.getString("name_user"));
                usuario.setEmail_user(rs.getString("email_user"));
                usuario.setPassword_user(rs.getString("password_user"));
                usuario.setId_perm(rs.getInt("id_perm"));
                return usuario;
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Erro ao obter usuário pelo nome e email:" + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }
    
    
    //Metodo para listar um usuario pelo nome
    public user obterusuariopelonome(String name_user) throws SQLException {
        try {
            bd.conecta();
            String sql = "SELECT * FROM users WHERE name_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setString(1, name_user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user user = new user();
                user.setId_user(rs.getInt("id_user"));
                user.setName_user(rs.getString("name_user"));
                user.setEmail_user(rs.getString("email_user"));
                user.setPassword_user(rs.getString("password_user"));
                return user;
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Erro ao obter usuario pelo nome:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }
    
    //Metodo para listar um usuario pelo email
    public user obterusuariopeloemail (String email_user) throws SQLException {
        try {
            bd.conecta();
            String sql = "SELECT * FROM users WHERE email_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setString(1, email_user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user user = new user();
                user.setId_user(rs.getInt("id_user"));
                user.setName_user(rs.getString("name_user"));
                user.setEmail_user(rs.getString("email_user"));
                user.setPassword_user(rs.getString("password_user"));
                return user;
            }
            return null;
        } catch (SQLException ex) {
            throw new SQLException("Erro ao obter usuario pelo email:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }
    
     // Método para verificar se um usuário já existe no banco de dados
    public boolean verificarUsuarioExistente(String nomeUsuario, String emailUsuario, String senhaUsuario) throws SQLException {
        try {
            bd.conecta();
            String sql = "SELECT COUNR(*) FROM users WHERE name_user = ? AND email_user = ?";
            PreparedStatement stmt = bd.conn.prepareStatement(sql);
            stmt.setString(1, nomeUsuario);
            stmt.setString(2, emailUsuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true se existir uma correspondência, caso contrário, false
        } catch (SQLException ex) {
            throw new SQLException("Erro ao verificar usuário existente:\nErro: " + ex.getMessage());
        } finally {
            bd.desconecta();
        }
    }
}

