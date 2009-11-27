package ua.jdesktopblogger.ui.tables;

import java.util.Calendar;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class TablePostModel extends AbstractTableModel {

	private MainForm mainForm;
	
	public static final int ACCOUNT_COLUMN_DATE_PUBLISHED = 2;
	
	public static final int ACCOUNT_COLUMN_DATE_EDITED = 3;

	public static final int ACCOUNT_COLUMN_NAME = 1;

	public static final int ACCOUNT_COLUMN_NUM = 0;
	
	private final String[] names = {
			"Num",
			"Name", //$NON-NLS-1$  
			"Date" //$NON-NLS-1$
	}; 
	
	public TablePostModel(MainForm mainForm){
		this.mainForm = mainForm;
	}
	
	public int getRowCount() {
		if (mainForm != null){
			Blog blog = mainForm.getSelectedBlog();
			if ((blog != null) && (blog.getPosts() != null)){
				return blog.getPosts().size();
			}
		}
		return 0;
	}

	public int getColumnCount() {
		return 3;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == -1)
			return ""; //$NON-NLS-1$
		Blog blog = mainForm.getSelectedBlog();
		if ((blog != null) && (blog.getPosts() != null)){
			Post post =  (Post) blog.getPosts().toArray()[rowIndex];
			switch (columnIndex) {
			case ACCOUNT_COLUMN_NUM: return rowIndex+1;
			case ACCOUNT_COLUMN_NAME: 
				return post.getTitle();
			case ACCOUNT_COLUMN_DATE_PUBLISHED: 
				return post.getPublishDate();
			case ACCOUNT_COLUMN_DATE_EDITED:
				return post.getEditDate();
			}
		}
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
		case ACCOUNT_COLUMN_DATE_EDITED:
		case ACCOUNT_COLUMN_DATE_PUBLISHED:
			return Calendar.class;
		default:
			if (getValueAt(0, columnIndex) != null)
				return getValueAt(0, columnIndex).getClass();
			else
				return String.class;
		}
	}
}
