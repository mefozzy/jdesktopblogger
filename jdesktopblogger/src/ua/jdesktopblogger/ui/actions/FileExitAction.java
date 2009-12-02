package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class FileExitAction extends SuperAction {

	public FileExitAction(MainForm fr) {
		this("Exit application",
				MainForm.createImageIcon("images/exit.png"), //$NON-NLS-1$
				KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_F4,
						ActionEvent.ALT_MASK), 
				"Exit JDesktopBlogger application", fr); //$NON-NLS-1$
	}

	public FileExitAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {;
		System.exit(0);
	}

}
