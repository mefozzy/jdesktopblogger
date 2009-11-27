package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial")//$NON-NLS-1$
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

		Account account = form.getSelectedAccount();
		if (account != null) {

			try {
				ServiceFactory.getDefaultFactory().getBlogService().refreshAccount(account);
			} catch (BlogServiceException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(form.getFrame(),
						"Failed to refresh account." +
						Messages.NEW_LINE_DOUBLE +
						e.getLocalizedMessage(),
						form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
			}
			form.accountRefreshed(account);
			
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select an account for refreshing", form
							.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
