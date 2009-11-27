package ua.jdesktopblogger.ui.tables;

import java.util.Date;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial") //$NON-NLS-1$
public class TableMessageModel extends AbstractTableModel {
	
	public static final int ACCOUNT_COLUMN_OLD_EMAILS = 3;
	
	public static final int ACCOUNT_COLUMN_DATE = 2;

	public static final int ACCOUNT_COLUMN_NAME = 1;

	public static final int ACCOUNT_COLUMN_NUM = 0;
	
	private final String[] names = {
			"Num",
			"Name", //$NON-NLS-1$  
			"Date" //$NON-NLS-1$
	}; 
	
	public int getRowCount() {
		return 0;
//		return ModelService.getInstance().getAccountWorker().getAccounts().size();
	}

	public int getColumnCount() {
		return 3;
//		if (ModelService.getInstance().getPreferencesWorker().isShowOldEmailsNum()) {
//			return names.length;
//		} else {
//			return names.length - 1;
//		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == -1)
			return ""; //$NON-NLS-1$
//		if (account != null) {
//			switch (columnIndex) {
//			case ACCOUNT_COLUMN_NUM: return rowIndex+1;
//			case ACCOUNT_COLUMN_NAME: return account.getName();
//			case ACCOUNT_COLUMN_EMAILS: 
//				switch (account.getEmailNum()) {
//					case 0: 
//						// Checking if account is in less changes.
//						// That means, that there was not 0, but became zero.
//						// Displaying 0 in that case, or "" otherwise
//						if (ModelService.getInstance().getAccountChanges().getLessChanges().containsKey(account)) {
//							return 0;
//						} else
//							return ""; //$NON-NLS-1$
//					case Account.EMAILS_NA: return "N/A"; //$NON-NLS-1$
//					case Account.EMAILS_ERROR: return "ERR"; //$NON-NLS-1$
//					default: return account.getEmailNum();
//				}
//			case ACCOUNT_COLUMN_OLD_EMAILS:
//				if (ModelService.getInstance().getAccountChanges().hasChanges(account))
//					return ModelService.getInstance().getAccountChanges().getOldValue(account);
//				else
//					return ""; //$NON-NLS-1$
//			}
//		}
		return ""; //$NON-NLS-1$
	}

	public String getColumnName(int column) {
		return names[column];
	}

	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case ACCOUNT_COLUMN_NUM:
			return Integer.class;
		case ACCOUNT_COLUMN_NAME:
			return String.class;
		case ACCOUNT_COLUMN_DATE:
			return Date.class;
		default:
			if (getValueAt(0, columnIndex) != null)
				return getValueAt(0, columnIndex).getClass();
			else
				return String.class;
		}
	}
}
