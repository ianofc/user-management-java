
package modeltools;

import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import db.conectbd;

public class comboboxdata {

    private final conectbd conexao;

    public comboboxdata() {
        conexao = new conectbd();
    }

    public void preencherComboBox(String query, JComboBox<String> comboBox, String columnName) throws SQLException {
        comboBox.removeAllItems();

        try {
            conexao.conecta();
            conexao.executaSQL(query);

            while (conexao.rs.next()) {
                comboBox.addItem(conexao.rs.getString(columnName));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher ComboBox: " + ex.getMessage());
        } finally {
            conexao.desconecta();
        }
    }
}

