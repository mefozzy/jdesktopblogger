package ua.jdesktopblogger.ui.actions;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ua.cn.yet.common.ui.popup.actions.IAwtMenuItemAction;
import ua.jdesktopblogger.ui.MainForm;


@SuppressWarnings("serial") //$NON-NLS-1$
public class ViewShowHideAppAction extends SuperAction implements IAwtMenuItemAction {
	
	private static String SHOW_NAME = "Show";
	private static String SHOW_DESC = "Show JDesktopBlogger application";
	private static ImageIcon SHOW_ICON = MainForm.createImageIcon("images/maximize_to_tray.png"); //$NON-NLS-1$
	
	private static String HIDE_NAME = "Hide";
	private static String HIDE_DESC = "Hide JDesktopBlogger application";
	private static ImageIcon HIDE_ICON = MainForm.createImageIcon("images/minimize_to_tray.png"); //$NON-NLS-1$
	
	private MenuItem menuItem;

	public ViewShowHideAppAction(MainForm fr) {
		this(HIDE_NAME, HIDE_ICON,
		KeyEvent.VK_S, 
		KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK), 
		HIDE_DESC, fr);
	}

	public ViewShowHideAppAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (form.getFrame().isVisible()) {
			form.getFrame().setVisible(false);
			putValue(Action.NAME, SHOW_NAME);
			putValue(SHORT_DESCRIPTION, SHOW_DESC);
			putValue(SMALL_ICON, SHOW_ICON);
			menuItem.setLabel(SHOW_NAME);
		} else {
			form.getFrame().setVisible(true);
			putValue(Action.NAME, HIDE_NAME);
			putValue(SHORT_DESCRIPTION, HIDE_DESC);
			putValue(SMALL_ICON, HIDE_ICON);
			menuItem.setLabel(HIDE_NAME);
		}
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

}
