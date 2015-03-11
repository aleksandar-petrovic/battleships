//Aleksandar Petrovic

package start;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<String> lista;

	TableModel() {
		lista = new LinkedList<>();
	}

	public void setUsers(Set<String> users) {
		if (!lista.containsAll(users) || !users.containsAll(lista)) {
			lista.clear();
			lista.addAll(users);
			fireTableDataChanged();
		}
	}

	public String getSelectedUser(int row) {
		return lista.get(row);
	}

	@Override
	public int getRowCount() {
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int column) {
		return "User";
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (lista == null)
			return null;
		if (row < 0 || row >= lista.size())
			return null;
		String user = lista.get(row);
		return user;
	}

}
