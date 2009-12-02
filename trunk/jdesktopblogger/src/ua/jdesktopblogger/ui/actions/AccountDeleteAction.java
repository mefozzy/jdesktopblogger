package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class AccountDeleteAction extends SuperAction {

	public AccountDeleteAction(MainForm fr) {
		this("Delete Acount",
				MainForm.createImageIcon("images/editdelete.png"), //$NON-NLS-1$
				KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_E,
						ActionEvent.ALT_MASK),
				"Delete account from the application", fr);
	}

	public AccountDeleteAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		Account account = form.getSelectedAccount();
		if (account != null) {
			
			// Showing confirm dialog
			if (JOptionPane.showConfirmDialog(form.getFrame(), 
					"Do you really want to delete account: " +
					Messages.NEW_LINE + account.getLogin() +
					Messages.NEW_LINE_DOUBLE +
					"Please, note: account won't be deleted from the server.",
					form.getAppTitle(),
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				try {
					ServiceFactory.getDefaultFactory().getAccountService().deleteAccount(account);
					
					form.accountDeleted(account);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(form.getFrame(),
							"Failed to delete account" +
							Messages.NEW_LINE_DOUBLE + e.getLocalizedMessage(),
							form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
				}

			}
			
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select an account to delete",
					form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
