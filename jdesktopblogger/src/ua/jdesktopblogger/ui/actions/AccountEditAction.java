package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class AccountEditAction extends SuperAction {

	public AccountEditAction(MainForm fr) {
		this("Edit Acount",
				MainForm.createImageIcon("images/edit.png"), //$NON-NLS-1$
				KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_E,
						ActionEvent.ALT_MASK),
				"Edit account information, such as login and password", fr);

		this.setEnabled(false);
	}

	public AccountEditAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		
	}

}
