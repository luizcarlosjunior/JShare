package ATM;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.dagostini.jshare.comun.Cliente;

public class ModeloCliente extends AbstractTableModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Cliente> lista = new ArrayList<Cliente>();
	
	
	public int getColumnCount() {
		return 3; // NOME, IP, PORTA
	}

	public int getRowCount() {
		return lista.size();
	}
	
	
	public void clear() {
		lista.clear();
	}
		
	public Object getValueAt(int row, int col) {
		
		Cliente c = lista.get(row);
		
		switch (col) {
		case 0:
			return c.getNome();
		case 1:
			return c.getIp();
		case 2:
			return c.getPorta();
		default:
			return "Erro";
		}
	}

	public void setList(ArrayList<Cliente> lista){
		this.lista = lista;
		super.fireTableDataChanged();
	}

	@Override
	public String getColumnName (int col) {
		
		switch (col) {
		case 0:
			return "Nome";
		case 1:
			return "IP";
		case 2:
			return "PORTA";
		default:
			return "Erro";
		}
	}
}