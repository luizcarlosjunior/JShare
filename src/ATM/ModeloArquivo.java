package ATM;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.dagostini.jshare.comum.pojos.Arquivo;

public class ModeloArquivo extends AbstractTableModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Arquivo> lista = new ArrayList<Arquivo>();
	
	
	public int getColumnCount() {
		return 2; // NOME, IP, PORTA
	}

	public int getRowCount() {
		return lista.size();
	}
	
	
	public void clear() {
		lista.clear();
	}
		
	public Object getValueAt(int row, int col) {
		
		Arquivo a = lista.get(row);
		
		switch (col) {
		case 0:
			return a.getNome();
		case 1:
			return a.getTamanho();
		default:
			return "Erro";
		}
	}

	public void setList(ArrayList<Arquivo> lista){
		this.lista = lista;
		super.fireTableDataChanged();
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
}