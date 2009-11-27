package ua.jdesktopblogger.ui.renderers;

import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")//$NON-NLS-1$
public class DateRenderer extends JLabel implements TableCellRenderer {

	public DateRenderer() {
		super();
		setOpaque(true); //MUST do this for background to show up.
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		//setEnabled(true);
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		
		setFont(new Font("", Font.PLAIN, 12)); //$NON-NLS-1$
		
		setText(String.format("%1$tm %1$te,%1$tY", (Calendar)value));
		
		return this;
	}

}
