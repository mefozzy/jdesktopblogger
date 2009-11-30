package ua.jdesktopblogger.ui.tables;

import java.util.Calendar;

import javax.swing.table.AbstractTableModel;

import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class TablePostModel extends AbstractTableModel {

	private MainForm mainForm;
	
	public static final int POST_COLUMN_DATE_EDITED = 4;
	
	public static final int POST_COLUMN_DATE_PUBLISHED = 3;
	
	public static final int POST_COLUMN_IS_DRAFT = 2;
	
	public static final int POST_COLUMN_NAME = 1;

	public static final int POST_COLUMN_NUM = 0;
	
	private final String[] names = {
			"Num",
			"Name",  
			"",
			"Date Publish",  
			"Date Edit"
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
		return 5;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == -1)
			return ""; //$NON-NLS-1$
		Blog blog = mainForm.getSelectedBlog();
		if ((blog != null) && (blog.getPosts() != null)){
			Post post =  (Post) blog.getPosts().toArray()[rowIndex];
			switch (columnIndex) {
			case POST_COLUMN_NUM: return rowIndex+1;
			case POST_COLUMN_NAME: 
				return !post.getTitle().isEmpty() ? post.getTitle() : post.getKeywords();
			case POST_COLUMN_IS_DRAFT: 
				return post.isDraft() ? "D" : "";
			case POST_COLUMN_DATE_PUBLISHED: 
				return post.getPublishDate();
			case POST_COLUMN_DATE_EDITED:
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
		case POST_COLUMN_NUM:
			return Integer.class;
		case POST_COLUMN_NAME:
			return String.class;
		case POST_COLUMN_IS_DRAFT:
			return Boolean.class;
		case POST_COLUMN_DATE_EDITED:
		case POST_COLUMN_DATE_PUBLISHED:
			return Calendar.class;
		default:
			if (getValueAt(0, columnIndex) != null)
				return getValueAt(0, columnIndex).getClass();
			else
				return String.class;
		}
	}
	/**
	 * return Post by its index in the table of the posts
	 * @param iSelRow - index of row to get post
	 * @return Selected post or <code>null</code>
	 */
	public Post getPost(int iSelRow) {
		Blog blog = mainForm.getSelectedBlog();
		if ((blog != null) && (blog.getPosts() != null)){
			Post post =  (Post) blog.getPosts().toArray()[iSelRow];
			return post;
		}
		return null;
	}
}
