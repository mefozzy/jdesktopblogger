package ua.jdesktopblogger.ui.renderers;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.ui.MainForm;

public class TreeCellAccountRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	private ImageIcon accountIcon;
	private ImageIcon blogIcon;

	public TreeCellAccountRenderer() {
		accountIcon = MainForm.createImageIcon("images/contents.png");
		blogIcon = MainForm.createImageIcon("images/jdesktopblogger16.png");
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (value instanceof Account){
			setIcon(accountIcon);
		} else if (value instanceof Blog) {
			setIcon(blogIcon);
		}
		return this;
	}

}
