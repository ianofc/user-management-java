
package templatemain;

import db.conectbd;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import modelclass.user;

public class Login extends javax.swing.JFrame {

    conectbd conn = new conectbd();
    
    public Login() {
        try {
            initComponents();
            conn.conecta();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
private JDialog dialogCarregamento;

private void mostrarCarregamento(String mensagem) {
    dialogCarregamento = new JDialog(this, "Aguarde...", true);
    JLabel labelMensagem = new JLabel(mensagem);
    dialogCarregamento.add(labelMensagem);
    dialogCarregamento.setSize(300, 100);
    dialogCarregamento.setLocationRelativeTo(this);
    new Thread(() -> dialogCarregamento.setVisible(true)).start();
}

private void esconderCarregamento() {
    if (dialogCarregamento != null) {
        dialogCarregamento.dispose();
    }
}

    public void logar() {
    mostrarCarregamento("Verificando credenciais...");
    new Thread(() -> {
        try {
            conn.executaSQL("SELECT * FROM users WHERE name_user=?");
            PreparedStatement stmt = conn.conn.prepareStatement("SELECT * FROM users WHERE name_user=?");
            stmt.setString(1, jTFLogLogin.getText());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getString("password_user").equals(jPFSenhaLogin.getText())) {
                user usuario = new user();
                usuario.setId_user(rs.getInt("id_user"));
                usuario.setName_user(rs.getString("name_user"));
                usuario.setEmail_user(rs.getString("email_user"));
                usuario.setPassword_user(rs.getString("password_user"));
                usuario.setId_perm(rs.getInt("id_perm"));

                Home hm = new Home(usuario);
                SwingUtilities.invokeLater(() -> {
                    esconderCarregamento();
                    hm.setVisible(true);
                    dispose();
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    esconderCarregamento();
                    JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                });
            }
        } catch (SQLException ex) {
            SwingUtilities.invokeLater(() -> {
                esconderCarregamento();
                JOptionPane.showMessageDialog(this, "Erro ao realizar login. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            });
        }
    }).start();
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLTitulo = new javax.swing.JLabel();
        jLLogin = new javax.swing.JLabel();
        jLSenhaLog = new javax.swing.JLabel();
        jTFLogLogin = new javax.swing.JTextField();
        jPFSenhaLogin = new javax.swing.JPasswordField();
        jBEntrarLogin = new javax.swing.JButton();
        jLBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LOGIN");
        getContentPane().setLayout(null);

        jLTitulo.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 48)); // NOI18N
        jLTitulo.setForeground(new java.awt.Color(0, 0, 102));
        jLTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitulo.setText("BEM VINDO");
        jLTitulo.setToolTipText("");
        getContentPane().add(jLTitulo);
        jLTitulo.setBounds(0, 70, 530, 60);

        jLLogin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLLogin.setForeground(new java.awt.Color(153, 255, 255));
        jLLogin.setText("Login:");
        jLLogin.setToolTipText("");
        getContentPane().add(jLLogin);
        jLLogin.setBounds(100, 210, 50, 17);

        jLSenhaLog.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLSenhaLog.setForeground(new java.awt.Color(153, 255, 255));
        jLSenhaLog.setText("Senha:");
        getContentPane().add(jLSenhaLog);
        jLSenhaLog.setBounds(100, 260, 50, 17);

        jTFLogLogin.setForeground(new java.awt.Color(0, 51, 102));
        jTFLogLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFLogLoginActionPerformed(evt);
            }
        });
        getContentPane().add(jTFLogLogin);
        jTFLogLogin.setBounds(160, 200, 230, 30);

        jPFSenhaLogin.setForeground(new java.awt.Color(0, 51, 102));
        getContentPane().add(jPFSenhaLogin);
        jPFSenhaLogin.setBounds(160, 250, 230, 30);

        jBEntrarLogin.setBackground(new java.awt.Color(0, 51, 102));
        jBEntrarLogin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jBEntrarLogin.setForeground(new java.awt.Color(255, 255, 255));
        jBEntrarLogin.setText("ENTRAR");
        jBEntrarLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEntrarLoginActionPerformed(evt);
            }
        });
        getContentPane().add(jBEntrarLogin);
        jBEntrarLogin.setBounds(220, 300, 100, 30);

        jLBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/pexels-francesco-ungaro-281260.jpg"))); // NOI18N
        getContentPane().add(jLBackground);
        jLBackground.setBounds(0, -60, 800, 580);

        setSize(new java.awt.Dimension(543, 399));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTFLogLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFLogLoginActionPerformed
        
    }//GEN-LAST:event_jTFLogLoginActionPerformed

    private void jBEntrarLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEntrarLoginActionPerformed
       logar();
    }//GEN-LAST:event_jBEntrarLoginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBEntrarLogin;
    private javax.swing.JLabel jLBackground;
    private javax.swing.JLabel jLLogin;
    private javax.swing.JLabel jLSenhaLog;
    private javax.swing.JLabel jLTitulo;
    private javax.swing.JPasswordField jPFSenhaLogin;
    private javax.swing.JTextField jTFLogLogin;
    // End of variables declaration//GEN-END:variables
}
