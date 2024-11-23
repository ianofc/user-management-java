package templatemain;

import modelclass.user;
import templateregister.formuser;
import templatesearch.searchuser;
import db.conectbd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Home extends javax.swing.JFrame {

    private conectbd conn;

    private user usuarioAutenticado;

    public Home(user usuario) {
    try {
        initComponents();
        this.usuarioAutenticado = usuario; // Define o usuário autenticado
        conn = new conectbd();
        conn.conecta();
        jLUserHom.setText(usuarioAutenticado.getName_user()); // Usa o nome do usuário autenticado

        // Busca a permissão do usuário e atualiza jLUserPermHom
        conn.executaSQL("SELECT p.desc_perm FROM users u JOIN permission p ON u.id_perm = p.id_perm WHERE u.name_user='" + usuarioAutenticado.getName_user() + "'");
        if (conn.rs.next()) {
            String descPerm = conn.rs.getString("desc_perm");
            jLUserPermHom.setText(descPerm); // Exibe a permissão
        } else {
            jLUserPermHom.setText("Permissão não encontrada");
        }
    } catch (SQLException ex) {
        Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(rootPane, "Erro ao carregar informações do usuário: " + ex.getMessage());
    }
}

    public void abrirCadastroUsuario() {
        try {
            conn.executaSQL("SELECT * FROM users WHERE name_user='" + jLUserHom.getText() + "'");
            conn.rs.first();
            int userPermission = conn.rs.getInt("id_perm"); // Supondo que 'id_perm' armazene a permissão

            // Verificando se o usuário tem permissão de administrador
            if (userPermission == 1) { // Permissão '1' para admin
                formuser form = new formuser(usuarioAutenticado); // Passa o usuário autenticado
                form.setVisible(true);
                //dispose();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Você não possui permissão para acessar esta funcionalidade.");
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao verificar permissões: " + erro.getMessage());
        }
    }

    private void abrirPesquisaUsuario() {
    try {
        searchuser pesquisa = new searchuser(usuarioAutenticado); // Passa o usuário autenticado
        pesquisa.setVisible(true); // Exibe a tela
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(rootPane, "Erro ao abrir pesquisa de usuário:\n" + ex.getMessage());
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLUserHom = new javax.swing.JLabel();
        jLUserPermHom = new javax.swing.JLabel();
        jLHRHom = new javax.swing.JLabel();
        jLDateHom = new javax.swing.JLabel();
        jLUsuLogHom = new javax.swing.JLabel();
        jLBackgroundHome = new javax.swing.JLabel();
        jMenuHome = new javax.swing.JMenuBar();
        jMCadHome = new javax.swing.JMenu();
        jMUsuarioCadHom = new javax.swing.JMenuItem();
        jMPesqHome = new javax.swing.JMenu();
        jMUsuarioPesqHom = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HOME");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        jLUserHom.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLUserHom.setForeground(new java.awt.Color(255, 204, 0));
        getContentPane().add(jLUserHom);
        jLUserHom.setBounds(170, 450, 100, 20);

        jLUserPermHom.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLUserPermHom.setForeground(new java.awt.Color(255, 204, 0));
        getContentPane().add(jLUserPermHom);
        jLUserPermHom.setBounds(270, 450, 60, 20);

        jLHRHom.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLHRHom.setForeground(new java.awt.Color(255, 204, 0));
        getContentPane().add(jLHRHom);
        jLHRHom.setBounds(420, 450, 90, 20);

        jLDateHom.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLDateHom.setForeground(new java.awt.Color(255, 204, 0));
        getContentPane().add(jLDateHom);
        jLDateHom.setBounds(330, 450, 80, 20);

        jLUsuLogHom.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLUsuLogHom.setForeground(new java.awt.Color(255, 255, 255));
        jLUsuLogHom.setText("USUÁRIO LOGADO:");
        getContentPane().add(jLUsuLogHom);
        jLUsuLogHom.setBounds(30, 450, 140, 20);

        jLBackgroundHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/449895.jpg"))); // NOI18N
        getContentPane().add(jLBackgroundHome);
        jLBackgroundHome.setBounds(-390, -170, 1430, 670);

        jMenuHome.setBackground(new java.awt.Color(0, 51, 102));
        jMenuHome.setForeground(new java.awt.Color(0, 51, 102));

        jMCadHome.setText("Cadastro");

        jMUsuarioCadHom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMUsuarioCadHom.setText("Usuário");
        jMUsuarioCadHom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMUsuarioCadHomActionPerformed(evt);
            }
        });
        jMCadHome.add(jMUsuarioCadHom);

        jMenuHome.add(jMCadHome);

        jMPesqHome.setText("Pesquisa");

        jMUsuarioPesqHom.setText("Usuario");
        jMUsuarioPesqHom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMUsuarioPesqHomActionPerformed(evt);
            }
        });
        jMPesqHome.add(jMUsuarioPesqHom);

        jMenuHome.add(jMPesqHome);

        setJMenuBar(jMenuHome);

        setSize(new java.awt.Dimension(903, 558));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMUsuarioCadHomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMUsuarioCadHomActionPerformed
        abrirCadastroUsuario();
    }//GEN-LAST:event_jMUsuarioCadHomActionPerformed


    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String currentDate = dateFormat.format(dt);
        String currentTime = timeFormat.format(dt);

        jLDateHom.setText(currentDate);

        Timer timer = new Timer(1000, new hora());
        timer.start();

        // Diretório base para salvar os arquivos
        String baseDir = "./backups/";
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria o diretório se não existir
        }

        // Incrementar nome do arquivo
        int fileCount = 1;
        File file;
        do {
            file = new File(baseDir + "backupusuarioacesso" + fileCount + ".txt");
            fileCount++;
        } while (file.exists());

        try (PrintWriter arq = new PrintWriter(file)) {
            // Escreve os dados no arquivo
            arq.println("Usuário: " + jLUserHom.getText());
            arq.println("Permissão: " + jLUserPermHom.getText());
            arq.println("Data: " + currentDate);
            arq.println("Hora: " + currentTime);

            //JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso: " + file.getName());
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + erro.getMessage());
        }
    }//GEN-LAST:event_formWindowOpened

    private void jMUsuarioPesqHomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMUsuarioPesqHomActionPerformed
        abrirPesquisaUsuario();
    }//GEN-LAST:event_jMUsuarioPesqHomActionPerformed

    class hora implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Calendar now = Calendar.getInstance();
            //jLHRHom.setText(String.format("%1$tH:%1$tM:%1$tS", now));
            jLHRHom.setText(String.format("%1$tH:%1$tM", now));

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLBackgroundHome;
    private javax.swing.JLabel jLDateHom;
    private javax.swing.JLabel jLHRHom;
    private javax.swing.JLabel jLUserHom;
    private javax.swing.JLabel jLUserPermHom;
    private javax.swing.JLabel jLUsuLogHom;
    private javax.swing.JMenu jMCadHome;
    private javax.swing.JMenu jMPesqHome;
    private javax.swing.JMenuItem jMUsuarioCadHom;
    private javax.swing.JMenuItem jMUsuarioPesqHom;
    private javax.swing.JMenuBar jMenuHome;
    // End of variables declaration//GEN-END:variables
}
