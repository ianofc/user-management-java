package templatesearch;

import db.conectbd;

import modeltools.table;
import modelclass.*;
import controlsearch.contsearch;
import controldocs.contdocs;
import controlregister.contuser;

import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.HeadlessException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JOptionPane;

public class searchuser extends javax.swing.JFrame {

    private conectbd conexao = new conectbd();
    private conectbd conuser = new conectbd();
    private conectbd conperm = new conectbd();

    private final contsearch contsearch = new contsearch();
    private final contdocs contdocs = new contdocs();
    private final contuser contuser = new contuser();

    private final user moduser = new user();
    private final permission modperm = new permission();

    private LocalDateTime ultimoHorarioExportacao = null;
    private String ultimoFormatoExportacao = null;
    private LocalDateTime horarioExportacao;

    private user usuarioAutenticado; // Usuário logado

    public searchuser(user usuario) {
        this.usuarioAutenticado = usuario; // Recebe o usuário autenticado
        initComponents();

        try {
            preencherTabela("SELECT u.id_user, u.name_user, u.email_user, u.password_user, p.desc_perm FROM users u LEFT JOIN permission p ON u.id_perm = p.id_perm ORDER BY u.id_user");
            verificarPermissaoAdmin(); // Verifica a permissão do usuário logado

            jTFBusca = new javax.swing.JTextField();
            // Configurações do campo jTFBusca
            jTFBusca.setToolTipText("Digite o nome ou e-mail do usuário");

            // Adiciona o KeyListener ao campo de busca
            adicionarKeyListenerCampoBusca();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao selecionar dados da tabela: " + ex.getMessage());
        }
    }

    private void verificarPermissaoAdmin() {
    if (usuarioAutenticado != null && usuarioAutenticado.getId_perm() == 1) {
        // Configurações para administrador
        jTFPerm.setEnabled(true);
        jBDeleta.setEnabled(true); // Botão de exclusão habilitado apenas para admin
    } else {
        // Configurações para usuários comuns
        jTFPerm.setEnabled(false);
        jBDeleta.setEnabled(false);
    }
}

    private void limparCampos() {
        jTFCod.setText("");
        jTFSenha.setText("");
        jTFEmail.setText("");
        jTFSenha.setText("");

        jTFSenha.setEnabled(true);
        jTFEmail.setEnabled(true);
        jTFSenha.setEnabled(true);
    }

    //METODO TABELA
    public void preencherTabela(String SQL) throws SQLException {

        ArrayList<Object[]> dados = new ArrayList<>();
        String[] colunas = {"Código", "Nome", "Email", "Senha", "Permissao"};

        conexao.conecta();
        conexao.executaSQL(SQL);

        try {
            while (conexao.rs.next()) {
                Object[] linha = new Object[5];
                linha[0] = conexao.rs.getInt("id_user");
                linha[1] = conexao.rs.getString("name_user");
                linha[2] = conexao.rs.getString("email_user");
                linha[3] = conexao.rs.getString("password_user");
                linha[4] = conexao.rs.getString("desc_perm");
                dados.add(linha);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher tabela: " + ex.getMessage());
        } finally {
            conexao.desconecta();
        }

        table tabela = new table(dados, colunas);
        jTSearhcuser.setModel(tabela);
        jTSearhcuser.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTSearhcuser.getColumnModel().getColumn(0).setResizable(false);
        jTSearhcuser.getColumnModel().getColumn(1).setPreferredWidth(90);
        jTSearhcuser.getColumnModel().getColumn(1).setResizable(false);
        jTSearhcuser.getColumnModel().getColumn(2).setPreferredWidth(140);
        jTSearhcuser.getColumnModel().getColumn(2).setResizable(false);
        jTSearhcuser.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTSearhcuser.getColumnModel().getColumn(3).setResizable(false);
        jTSearhcuser.getColumnModel().getColumn(4).setPreferredWidth(80);
        jTSearhcuser.getColumnModel().getColumn(4).setResizable(false);

        jTSearhcuser.getTableHeader().setReorderingAllowed(false);
        jTSearhcuser.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTSearhcuser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    private void atualizarTabela(String nomePesquisa, String emailPesquisa) {
        try {
            contsearch busca = new contsearch();
            List<user> usuarios = busca.buscarUsuariosPorNomeEEmail(nomePesquisa, emailPesquisa);
            String[] colunas = {"ID", "Nome", "Email", "Senha", "Permissão"};
            ArrayList<Object[]> dados = new ArrayList<>();
            for (user usuario : usuarios) {
                dados.add(new Object[]{
                    usuario.getId_user(),
                    usuario.getName_user(),
                    usuario.getEmail_user(),
                    usuario.getPassword_user(),
                    usuario.getDesc_perm()
                });
            }
            table tabela = new table(dados, colunas);
            jTSearhcuser.setModel(tabela);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar tabela:\n" + ex.getMessage());
        }
    }

    //METODO PARA EXPORTAR DOCUMENTOS
    public void exportarDados() {
        ArrayList<Object[]> dados = new ArrayList<>();
        String[] colunas = {"Id", "Nome", "Email", "Senha", "Permissao"};
        int numRows = jTSearhcuser.getRowCount();

        for (int i = 0; i < numRows; i++) {
            Object[] linha = new Object[4];
            linha[0] = jTSearhcuser.getValueAt(i, 0);
            linha[1] = jTSearhcuser.getValueAt(i, 1);
            linha[2] = jTSearhcuser.getValueAt(i, 2);
            linha[3] = jTSearhcuser.getValueAt(i, 1);
            linha[4] = jTSearhcuser.getValueAt(i, 2);
            dados.add(linha);
        }

        LocalDateTime horarioPesquisa = LocalDateTime.now();
        LocalDateTime horarioExportacao = LocalDateTime.now();

        String formatoSelecionado = null;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Documentos do Word (.docx)", "docx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos PDF (.pdf)", "pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Planilhas do Excel (.xlxs)", "xlxs"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos XML (.xml)", "xml"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de Texto (.txt)", "txt"));
        int resultado = fileChooser.showSaveDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            String extensao = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            if (extensao.equals("txt")) {
                formatoSelecionado = "txt";
            } else if (extensao.equals("pdf")) {
                formatoSelecionado = "pdf";
            } else if (extensao.equals("xml")) {
                formatoSelecionado = "xml";
            } else if (extensao.equals("docx")) {
                formatoSelecionado = "docx";
            } else if (extensao.equals("xlxs")) {
                formatoSelecionado = "xlxs";
            }

            contdocs.exportarDados(dados, horarioPesquisa, horarioExportacao, formatoSelecionado);
            ultimoHorarioExportacao = horarioExportacao;
            ultimoFormatoExportacao = formatoSelecionado;
        } else {
            JOptionPane.showMessageDialog(null, "Exportação cancelada pelo usuário.");
        }
    }

    public void botaoexportar() {
        if (ultimoFormatoExportacao != null && ultimoHorarioExportacao != null) {
            LocalDateTime horarioAtual = LocalDateTime.now();
            long diffMinutes = java.time.Duration.between(ultimoHorarioExportacao, horarioAtual).toMinutes();

            if (diffMinutes >= 1 || !ultimoFormatoExportacao.equals(contdocs.obterFormatoSelecionado())) {
                horarioExportacao = LocalDateTime.now();
                exportarDados();
            } else {
                JOptionPane.showMessageDialog(null, "Você acabou de salvar neste formato, escolha outro ou espere 1 min para salvar novamente.");
            }
        } else {
            exportarDados();
        }
    }

    // MÉTODO PESQUISAR
    public void pesquisa() throws SQLException {
        String pesquisa = jTFBusca.getText();
        moduser.setPesquisa(pesquisa);

        // Buscar usuário pelo nome
        contsearch.buscarPorNome(moduser);

        if (moduser.getId_user() != 0) {
            contsearch.obterPermissao(moduser.getId_perm());

            jTFCod.setText(String.valueOf(moduser.getId_user()));
            jTFNome.setText(moduser.getName_user());
            jTFEmail.setText(moduser.getEmail_user());
            jTFSenha.setText(moduser.getPassword_user());
            jTFPerm.setText(modperm.getDesc_perm());

            //String sql = "SELECT * FROM user ORDER BY id_user";
            String sql = "SELECT u.id_user, u.name_user, u.email_user, u.password_user, p.desc_perm "
                    + "FROM users u "
                    + "JOIN permission p ON u.id_perm = p.id_perm "
                    + "WHERE LOWER(u.name_user) LIKE '%" + moduser.getPesquisa().toLowerCase() + "%' "
                    + "OR LOWER(u.email_user) LIKE '%" + moduser.getPesquisa().toLowerCase() + "%'";

            preencherTabela(sql);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Nenhum registro encontrado!");
            limparCampos();
        }
    }

    private void adicionarKeyListenerCampoBusca() {
        jTFBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Chamando o método de pesquisa com tratamento de erros
                try {
                    pesquisa();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao realizar a pesquisa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

// MÉTODOS PARA ANDAR ENTRE REGISTROS
    public void primeiro() {
        try {
            conuser.executaSQL("SELECT * FROM users ORDER BY id_user");
            if (conuser.rs.first()) {
                preencherCampos();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nenhum registro encontrado!");
                limparCampos();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao buscar usuário: " + ex.getMessage());
        }
    }

    public void ultimo() {
        try {
            conuser.executaSQL("SELECT * FROM users ORDER BY id_user DESC");
            if (conuser.rs.first()) {
                preencherCampos();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Nenhum registro encontrado!");
                limparCampos();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao buscar usuário: " + ex.getMessage());
        }
    }

    public void proximo() {
        try {
            if (!conuser.rs.isLast()) {
                conuser.rs.next();
                preencherCampos();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Último registro!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao buscar usuário: " + ex.getMessage());
        }
    }

    public void anterior() {
        try {
            if (!conuser.rs.isFirst()) {
                conuser.rs.previous();
                preencherCampos();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Primeiro registro!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao buscar usuário: " + ex.getMessage());
        }
    }

    private void preencherCampos() {
        try {
            jTFCod.setText(String.valueOf(conuser.rs.getInt("id_user")));
            jTFSenha.setText(conuser.rs.getString("name_user"));
            jTFEmail.setText(conuser.rs.getString("email_user"));
            jTFSenha.setText(conuser.rs.getString("password_user"));
            jTFPerm.setText(conuser.rs.getString("desc_perm"));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher campos: " + ex.getMessage());
        }
    }

// MÉTODO PARA DELETAR UM USUÁRIO
    public void deleta() throws SQLException {
        if (jTSearhcuser.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja realmente excluir o usuário selecionado?");
            if (resposta == JOptionPane.YES_OPTION) {
                try {
                    // Obtém o ID do usuário selecionado na tabela
                    int idUser = Integer.parseInt(jTFCod.getText());
                    moduser.setId_user(idUser);

                    // Chama o método de exclusão na classe contuser
                    contuser.deletarUser(moduser.getId_user());

                    // Atualiza a tabela após a exclusão
                    preencherTabela("SELECT * FROM users ORDER BY id_user");

                    // Limpa os campos do formulário
                    limparCampos();

                    JOptionPane.showMessageDialog(rootPane, "Usuário deletado com sucesso!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao obter ID do usuário: " + ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Erro ao deletar usuário: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Selecione um usuário para excluir!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLTitulo = new javax.swing.JLabel();
        jTFBusca = new javax.swing.JTextField();
        jBPesq = new javax.swing.JButton();
        jTFSenha = new javax.swing.JTextField();
        jLSenha = new javax.swing.JLabel();
        jLPerm = new javax.swing.JLabel();
        jTFPerm = new javax.swing.JTextField();
        jTFNome = new javax.swing.JTextField();
        jTFEmail = new javax.swing.JTextField();
        jLNome = new javax.swing.JLabel();
        jTFCod = new javax.swing.JTextField();
        jLCod = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jBExportar = new javax.swing.JButton();
        jBAtualiza = new javax.swing.JButton();
        jBDeleta = new javax.swing.JButton();
        jBSair = new javax.swing.JButton();
        jBPri = new javax.swing.JButton();
        jBUlt = new javax.swing.JButton();
        jBAnt = new javax.swing.JButton();
        jBProx = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTSearhcuser = new javax.swing.JTable();
        jLEmail = new javax.swing.JLabel();
        jLBG = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTitulo.setText("Pesquisa de Usuário");
        getContentPane().add(jLTitulo);
        jLTitulo.setBounds(0, 10, 680, 40);
        getContentPane().add(jTFBusca);
        jTFBusca.setBounds(30, 80, 590, 30);

        jBPesq.setBackground(new java.awt.Color(204, 255, 204));
        jBPesq.setForeground(new java.awt.Color(51, 153, 255));
        jBPesq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/LUPA 25X25.png"))); // NOI18N
        jBPesq.setToolTipText("");
        jBPesq.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBPesq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesqActionPerformed(evt);
            }
        });
        getContentPane().add(jBPesq);
        jBPesq.setBounds(630, 80, 30, 30);
        getContentPane().add(jTFSenha);
        jTFSenha.setBounds(470, 120, 150, 30);

        jLSenha.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLSenha.setText("Senha:");
        getContentPane().add(jLSenha);
        jLSenha.setBounds(410, 130, 60, 17);

        jLPerm.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLPerm.setText("Permissão:");
        getContentPane().add(jLPerm);
        jLPerm.setBounds(410, 170, 90, 20);

        jTFPerm.setEnabled(false);
        getContentPane().add(jTFPerm);
        jTFPerm.setBounds(490, 160, 130, 30);
        getContentPane().add(jTFNome);
        jTFNome.setBounds(210, 120, 190, 30);
        getContentPane().add(jTFEmail);
        jTFEmail.setBounds(90, 160, 310, 30);

        jLNome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLNome.setText("Nome:");
        getContentPane().add(jLNome);
        jLNome.setBounds(150, 130, 60, 17);

        jTFCod.setEditable(false);
        getContentPane().add(jTFCod);
        jTFCod.setBounds(90, 120, 50, 30);

        jLCod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLCod.setText("Código:");
        getContentPane().add(jLCod);
        jLCod.setBounds(30, 130, 60, 17);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/exportar 25X25.png"))); // NOI18N
        jBExportar.setToolTipText("");
        jBExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBExportarActionPerformed(evt);
            }
        });
        jPanel1.add(jBExportar);

        jBAtualiza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/atualizar 25X25.png"))); // NOI18N
        jBAtualiza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizaActionPerformed(evt);
            }
        });
        jPanel1.add(jBAtualiza);

        jBDeleta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/deletar 25X25.png"))); // NOI18N
        jBDeleta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletaActionPerformed(evt);
            }
        });
        jPanel1.add(jBDeleta);

        jBSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/sair25X25.png"))); // NOI18N
        jBSair.setToolTipText("Sair");
        jBSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSairActionPerformed(evt);
            }
        });
        jPanel1.add(jBSair);

        jBPri.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/primeiro 25X25.png"))); // NOI18N
        jBPri.setToolTipText("Primeiro");
        jBPri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPriActionPerformed(evt);
            }
        });
        jPanel1.add(jBPri);

        jBUlt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/ultimo 25X25.png"))); // NOI18N
        jBUlt.setToolTipText("Último");
        jBUlt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBUltActionPerformed(evt);
            }
        });
        jPanel1.add(jBUlt);

        jBAnt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/anterior 25X25.png"))); // NOI18N
        jBAnt.setToolTipText("Anterior");
        jBAnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAntActionPerformed(evt);
            }
        });
        jPanel1.add(jBAnt);

        jBProx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/proximo 25X25.png"))); // NOI18N
        jBProx.setToolTipText("Próximo");
        jBProx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBProxActionPerformed(evt);
            }
        });
        jPanel1.add(jBProx);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(20, 210, 160, 210);

        jTSearhcuser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTSearhcuser);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(190, 210, 460, 210);

        jLEmail.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLEmail.setText("Emai:");
        getContentPane().add(jLEmail);
        jLEmail.setBounds(40, 170, 40, 17);

        jLBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMGS/BackgroundESQ.png"))); // NOI18N
        getContentPane().add(jLBG);
        jLBG.setBounds(0, -60, 690, 590);

        setSize(new java.awt.Dimension(706, 468));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jBPesqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPesqActionPerformed
        try {
            pesquisa();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa: ");
        }
    }//GEN-LAST:event_jBPesqActionPerformed

    private void jBExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBExportarActionPerformed
        botaoexportar();
    }//GEN-LAST:event_jBExportarActionPerformed

    private void jBAtualizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAtualizaActionPerformed
        try {

            if (jTSearhcuser.getSelectedRow() != -1) {
                int resposta = JOptionPane.showConfirmDialog(rootPane, "Deseja realmente atualizar o usuário selecionado?");

                if (resposta == JOptionPane.YES_OPTION) {
                    moduser.setId_user(Integer.parseInt(jTFCod.getText()));
                    moduser.setName_user(jTFSenha.getText());
                    moduser.setEmail_user(jTFEmail.getText());
                    moduser.setPassword_user(jTFSenha.getText());

                    contuser.atualizarUser(moduser);
                    preencherTabela("SELECT * FROM users ORDER BY id_user");
                    limparCampos();

                    JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "Selecione um usuário para atualizar!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao atualizar usuário!");
        }
    }//GEN-LAST:event_jBAtualizaActionPerformed

    private void jBDeletaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDeletaActionPerformed
        try {
            deleta();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar usuario: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jBDeletaActionPerformed

    private void jBSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSairActionPerformed
        dispose();
    }//GEN-LAST:event_jBSairActionPerformed

    private void jBPriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPriActionPerformed
        jTSearhcuser.setRowSelectionInterval(0, 0);
        primeiro();
    }//GEN-LAST:event_jBPriActionPerformed

    private void jBUltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBUltActionPerformed
        int ultimaLinha = jTSearhcuser.getRowCount() - 1;
        jTSearhcuser.setRowSelectionInterval(ultimaLinha, ultimaLinha);
        ultimo();
    }//GEN-LAST:event_jBUltActionPerformed

    private void jBAntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAntActionPerformed
        int linhaAnterior = jTSearhcuser.getSelectedRow() - 1;
        if (linhaAnterior >= 0) {
            jTSearhcuser.setRowSelectionInterval(linhaAnterior, linhaAnterior);
        }
        anterior();
    }//GEN-LAST:event_jBAntActionPerformed

    private void jBProxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBProxActionPerformed
        int proximaLinha = jTSearhcuser.getSelectedRow() + 1;
        if (proximaLinha < jTSearhcuser.getRowCount()) {
            jTSearhcuser.setRowSelectionInterval(proximaLinha, proximaLinha);
        }
        proximo();
    }//GEN-LAST:event_jBProxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAnt;
    private javax.swing.JButton jBAtualiza;
    private javax.swing.JButton jBDeleta;
    private javax.swing.JButton jBExportar;
    private javax.swing.JButton jBPesq;
    private javax.swing.JButton jBPri;
    private javax.swing.JButton jBProx;
    private javax.swing.JButton jBSair;
    private javax.swing.JButton jBUlt;
    private javax.swing.JLabel jLBG;
    private javax.swing.JLabel jLCod;
    private javax.swing.JLabel jLEmail;
    private javax.swing.JLabel jLNome;
    private javax.swing.JLabel jLPerm;
    private javax.swing.JLabel jLSenha;
    private javax.swing.JLabel jLTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFBusca;
    private javax.swing.JTextField jTFCod;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFPerm;
    private javax.swing.JTextField jTFSenha;
    private javax.swing.JTable jTSearhcuser;
    // End of variables declaration//GEN-END:variables
}
