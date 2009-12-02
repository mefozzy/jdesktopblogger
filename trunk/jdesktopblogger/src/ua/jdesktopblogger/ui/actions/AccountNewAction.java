package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.AccountDialog;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class AccountNewAction extends SuperAction {

	public AccountNewAction(MainForm fr) {
		this("New Acount",
				MainForm.createImageIcon("images/filenew.png"), //$NON-NLS-1$
				KeyEvent.VK_N, KeyStroke.getKeyStroke(KeyEvent.VK_N,
						ActionEvent.ALT_MASK),
				"Create new account", fr);
	}

	public AccountNewAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		new AccountDialog(form.getFrame(), null, form).setVisible(true);
	}

}
