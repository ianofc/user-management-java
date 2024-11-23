
package modeltools;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public final  class table extends AbstractTableModel {

    private ArrayList<Object[]> linhas = null;
    private String[] colunas = null;
    

    public table(ArrayList<Object[]> lin, String[] col) {
        setLinhas(lin);
        setColunas(col);
    }

    
    public ArrayList<Object[]> getLinhas() {
        return linhas;
    }

    public void setLinhas(ArrayList<Object[]> dados) {
        linhas = dados;
    }

    public String[] getColunas() {
        return colunas;
    }

    public void setColunas(String[] nomes) {
        colunas = nomes;
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public String getColumnName(int numCol) {
        return colunas[numCol];
    }

    @Override
    public Object getValueAt(int numLin, int numCol) {
        Object[] linha = linhas.get(numLin);
        return linha[numCol];
    }
}
