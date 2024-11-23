package controlsearch;

import db.conectbd;
import modelclass.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class contsearch {

    private final conectbd conuser = new conectbd();
    private final conectbd conperm = new conectbd();

    public contsearch() {
    }

    // Método para listar todos os usuários
    public List<user> listarUsuarios() throws SQLException {
        try {
            conuser.conecta();
            List<user> users = new ArrayList<>();
            String sql = "SELECT id_user, name_user, email_user, password_user FROM users";
            PreparedStatement stmt = conuser.conn.prepareStatement(sql);
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
            conuser.desconecta();
        }
    }

    // Método para buscar um usuário por ID
    public user buscarPorID(int id_user) throws SQLException {
        try {
            conuser.conecta();
            String sql = "SELECT * FROM users WHERE id_user = ?";
            PreparedStatement stmt = conuser.conn.prepareStatement(sql);
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
            throw new SQLException("Erro ao buscar usuário por ID:\nErro: " + ex.getMessage());
        } finally {
            conuser.desconecta();
        }
    }

    // Método para obter permissão de usuário
    public permission obterPermissao(int cod) throws SQLException {
        conperm.conecta();
        conperm.executaSQL("SELECT * FROM permission WHERE id_perm = " + cod + " LIMIT 1");

        try {
            if (conperm.rs.first()) {
                int id_perm = conperm.rs.getInt("id_perm");
                String desc_perm = conperm.rs.getString("desc_perm");

                return new permission(id_perm, desc_perm);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar permissão:\nErro: " + ex);
        } finally {
            conperm.desconecta();
        }
        return null;
    }

    // Método para buscar um usuário por nome
    public void buscarPorNome(user modelo) throws SQLException {
        conuser.conecta();
        conuser.executaSQL("SELECT u.*, p.desc_perm FROM users u JOIN permission p ON u.id_perm = p.id_perm WHERE LOWER(u.name_user) LIKE '%" + modelo.getPesquisa().toLowerCase() + "%'");

        try {
            if (conuser.rs.next()) {
                modelo.setId_user(conuser.rs.getInt("id_user"));
                modelo.setName_user(conuser.rs.getString("name_user"));
                modelo.setEmail_user(conuser.rs.getString("email_user"));
                modelo.setPassword_user(conuser.rs.getString("password_user"));
                modelo.setDesc_perm(conuser.rs.getString("desc_perm").toUpperCase());
                modelo.setId_perm(conuser.rs.getInt("id_perm"));

                int id_perm = conuser.rs.getInt("id_perm");
                permission perm = obterPermissao(id_perm);
                modelo.setPerm(perm);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar usuários por nome:\nErro: " + ex.getMessage());
        } finally {
            conuser.desconecta();
        }
    }

    // Método para buscar um usuário por e-mail
    public user buscarPorEmail(String email) throws SQLException {
        try {
            conuser.conecta();
            String sql = "SELECT * FROM users WHERE email_user = ?";
            PreparedStatement stmt = conuser.conn.prepareStatement(sql);
            stmt.setString(1, email);
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
            throw new SQLException("Erro ao buscar usuário por e-mail:\nErro: " + ex.getMessage());
        } finally {
            conuser.desconecta();
        }
    }

    public List<user> buscarUsuariosPorNomeEEmail(String nome, String email) throws SQLException {
        List<user> usuarios = new ArrayList<>();
        try {
            conuser.conecta();
            String sql = "SELECT u.*, p.desc_perm FROM users u JOIN permission p ON u.id_perm = p.id_perm WHERE LOWER(u.name_user) LIKE ? AND LOWER(u.email_user) LIKE ?";
            PreparedStatement stmt = conuser.conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome.toLowerCase() + "%");
            stmt.setString(2, "%" + email.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user usuario = new user();
                usuario.setId_user(rs.getInt("id_user"));
                usuario.setName_user(rs.getString("name_user"));
                usuario.setEmail_user(rs.getString("email_user"));
                usuario.setPassword_user(rs.getString("password_user"));
                usuario.setId_perm(rs.getInt("id_perm"));
                usuario.setDesc_perm(rs.getString("desc_perm"));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException ex) {
            throw new SQLException("Erro ao buscar usuários por nome e email:\n" + ex.getMessage());
        } finally {
            conuser.desconecta();
        }
    }

    // Método para atualizar um usuário
    public void atualizarUsuario(user user) throws SQLException {
        try {
            conuser.conecta();
            String sql = "UPDATE users SET name_user = ?, email_user = ?, password_user = ?, id_perm = ? WHERE id_user = ?";
            PreparedStatement stmt = conuser.conn.prepareStatement(sql);
            stmt.setString(1, user.getName_user());
            stmt.setString(2, user.getEmail_user());
            stmt.setString(3, user.getPassword_user());
            stmt.setInt(4, user.getId_perm());
            stmt.setInt(5, user.getId_user());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar usuário.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao atualizar usuário:\nErro: " + ex.getMessage());
        } finally {
            conuser.desconecta();
        }
    }

    // Método para deletar um usuário
    public void deletarUsuario(user user) throws SQLException {
        try {
            conuser.conecta();
            String sql = "DELETE FROM users WHERE id_user = ?";
            PreparedStatement stmt = conuser.conn.prepareStatement(sql);
            stmt.setInt(1, user.getId_user());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao deletar usuário.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao deletar usuário:\nErro: " + ex.getMessage());
        } finally {
            conuser.desconecta();
        }
    }
}
