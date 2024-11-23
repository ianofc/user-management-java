package templateregister;

import db.conectbd;

import modelclass.user;
import modeltools.comboboxdata;

import controlregister.contuser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class formuser extends javax.swing.JFrame {

    conectbd conexao = new conectbd();
    contuser contuser = new contuser();
    user user = new user();
    public user usuarioAutenticado;
   
    
    public formuser(user usuario) throws SQLException {
        
        this.usuarioAutenticado = usuario;
        
        initComponents();

        preencherComboBox();
    }
  

    //METODO COMBO BOX ESTADO
    private void preencherComboBox() throws SQLException {
        conexao.conecta();
        conexao.executaSQL("SELECT * FROM permission ORDER BY id_perm");
        jCBPerm.removeAllItems();

        try {
            conexao.rs.first();
            do {
                jCBPerm.addItem(conexao.rs.getString("desc_perm"));
            } while (conexao.rs.next());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher ComboBox Permissão: " + ex.getMessage());
        } finally {
            //conexao.desconecta();
        }
    }

// Método para limpar os campos
    private void limparCampos() {
        jTFNomeUsuario.setText("");
        jTFEmailUsuario.setText("");
        jPasswordUsuario.setText("");
        
        jTFNomeUsuario.setEnabled(true);
        jTFEmailUsuario.setEnabled(true);
        jPasswordUsuario.setEnabled(true);
        
        jBNew.setEnabled(true);
        jBSalvar.setEnabled(true); // Libera o botão "Salvar"
    }

    private void salvarUsuario() {

        try {
            conexao.conecta();
            String nomeUsuario = jTFNomeUsuario.getText();
            String emailUsuario = jTFEmailUsuario.getText();
            String senhaUsuario = new String(jPasswordUsuario.getPassword());

            // Validação de Campos Vazios
            if (nomeUsuario.isEmpty() || emailUsuario.isEmpty() || senhaUsuario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos obrigatórios.");
                return;
            }

            // Validação do Formato do Email
            if (!emailUsuario.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um email válido.");
                return;
            }

            // Validação do Comprimento da Senha
            if (senhaUsuario.length() < 6) {
                JOptionPane.showMessageDialog(null, "A senha deve ter pelo menos 6 caracteres.");
                return;
            }

            // Validação de Nome de Usuário Único
            boolean usuarioExistente = contuser.verificarUsuarioExistente(nomeUsuario, emailUsuario, senhaUsuario);

            if (usuarioExistente) {
                JOptionPane.showMessageDialog(rootPane, "Já existe um usuário com esse nome de usuário e email.");
            } else {
                user.setName_user(nomeUsuario);
                user.setEmail_user(emailUsuario);
                user.setPassword_user(senhaUsuario);

                String permselecionado = (String) jCBPerm.getSelectedItem();
                conexao.rs.first();
                conexao.executaSQL("Select * From permission where desc_perm='" + permselecionado + "'");

                if (conexao.rs.first())   {
                    int id_perm = conexao.rs.getInt("id_perm");
                    user.setId_perm(id_perm);
                    contuser.inserirUser(user);

                    String desc_perm = conexao.rs.getString("desc_perm");
                    JOptionPane.showMessageDialog(rootPane, "Usuário cadastrada com sucesso: " + user.getName_user()+ " - Permissão: " + desc_perm);

                    limparCampos();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao obter informações da permissão selecionada.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao salvar:\nERRO: " + ex.getMessage());
            limparCampos();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLTitulo = new javax.swing.JLabel();
        jBNew = new javax.swing.JButton();
        jBSalvar = new javax.swing.JButton();
        jLDadosBasicos = new javax.swing.JLabel();
        jLNome = new javax.swing.JLabel();
        jTFNomeUsuario = new javax.swing.JTextField();
        jLEmail = new javax.swing.JLabel();
        jTFEmailUsuario = new javax.swing.JTextField();
        jLSenha = new javax.swing.JLabel();
        jPasswordUsuario = new javax.swing.JPasswordField();
        jCBPerm = new javax.swing.JComboBox<>();
        jLBG = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLTitulo.setForeground(new java.awt.Color(0, 0, 255));
        jLTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitulo.setText("Cadastro de Usuários");
        getContentPane().add(jLTitulo);
        jLTitulo.setBounds(0, 30, 500, 30);

        jBNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/novo 25X25.png"))); // NOI18N
        jBNew.setToolTipText("Novo");
        jBNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNewActionPerformed(evt);
            }
        });
        getContentPane().add(jBNew);
        jBNew.setBounds(320, 230, 70, 31);

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/salvar 25X25.png"))); // NOI18N
        jBSalvar.setToolTipText("Salvar");
        jBSalvar.setEnabled(false);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });
        getContentPane().add(jBSalvar);
        jBSalvar.setBounds(399, 230, 31, 31);

        jLDadosBasicos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLDadosBasicos.setForeground(new java.awt.Color(0, 0, 255));
        jLDadosBasicos.setText("DADOS BÁSICOS");
        getContentPane().add(jLDadosBasicos);
        jLDadosBasicos.setBounds(50, 110, 120, 20);

        jLNome.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLNome.setForeground(new java.awt.Color(0, 0, 255));
        jLNome.setText("Nome:");
        getContentPane().add(jLNome);
        jLNome.setBounds(50, 150, 50, 20);
        getContentPane().add(jTFNomeUsuario);
        jTFNomeUsuario.setBounds(100, 140, 250, 30);

        jLEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLEmail.setForeground(new java.awt.Color(0, 0, 255));
        jLEmail.setText("Email:");
        getContentPane().add(jLEmail);
        jLEmail.setBounds(50, 200, 50, 20);
        getContentPane().add(jTFEmailUsuario);
        jTFEmailUsuario.setBounds(100, 190, 370, 30);

        jLSenha.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLSenha.setForeground(new java.awt.Color(0, 0, 255));
        jLSenha.setText("Senha:");
        getContentPane().add(jLSenha);
        jLSenha.setBounds(50, 240, 50, 20);

        jPasswordUsuario.setText("jPasswordField1");
        getContentPane().add(jPasswordUsuario);
        jPasswordUsuario.setBounds(100, 232, 210, 30);

        getContentPane().add(jCBPerm);
        jCBPerm.setBounds(360, 140, 100, 30);

        jLBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/BackgroundESQ.png"))); // NOI18N
        getContentPane().add(jLBG);
        jLBG.setBounds(0, -60, 690, 570);

        setSize(new java.awt.Dimension(509, 306));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarActionPerformed
        salvarUsuario();
    }//GEN-LAST:event_jBSalvarActionPerformed

    private void jBNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNewActionPerformed
        limparCampos();
    }//GEN-LAST:event_jBNewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBNew;
    private javax.swing.JButton jBSalvar;
    private javax.swing.JComboBox<String> jCBPerm;
    private javax.swing.JLabel jLBG;
    private javax.swing.JLabel jLDadosBasicos;
    private javax.swing.JLabel jLEmail;
    private javax.swing.JLabel jLNome;
    private javax.swing.JLabel jLSenha;
    private javax.swing.JLabel jLTitulo;
    private javax.swing.JPasswordField jPasswordUsuario;
    private javax.swing.JTextField jTFEmailUsuario;
    private javax.swing.JTextField jTFNomeUsuario;
    // End of variables declaration//GEN-END:variables
}
