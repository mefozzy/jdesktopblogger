package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class AccountRefreshAction extends SuperAction {

	public AccountRefreshAction(MainForm fr) {
		this("Refresh Acount",
				MainForm.createImageIcon("images/reload.png"), //$NON-NLS-1$
				KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_E,
						ActionEvent.ALT_MASK),
				"Refresh list of blogs for account", fr);
		
		setEnabled(false);
	}

	public AccountRefreshAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(form.getFrame(), "Refreshing");
	}

}
