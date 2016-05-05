package ATM;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import br.dagostini.jshare.comun.Cliente;

public class ModeloCliente extends AbstractTableModel implements TableModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object[][] matriz;
	private int linhas;

	public ModeloCliente(Map<String, Cliente> mapa) {
		setMap(mapa);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return linhas;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return matriz[rowIndex][columnIndex];
	}
	
	 @Override
	 public String getColumnName (int col) {
         switch (col) {
         case 0:
             return "Nome";
         case 1:
             return "Tamanho";
         default:
             return "Erro";
         }
     }

	public void refresh(){
		super.fireTableDataChanged();
	}

    public void setMap(Map<String, Cliente> mapa) {
    	// definindo a quantidade de linhas
		linhas = mapa.size();
		
		//gerando uma noma matriz...
		matriz = new Object[linhas][3];
		
		int linha = 0;
		
		for (Entry<String, Cliente> c : mapa.entrySet()) {
				matriz[linha][0] = c.getValue().getNome();
				matriz[linha][1] = c.getValue().getIp();
				matriz[linha][2] = c.getValue().getPorta();
				linha++;
		}
		
		refresh();
    }
}