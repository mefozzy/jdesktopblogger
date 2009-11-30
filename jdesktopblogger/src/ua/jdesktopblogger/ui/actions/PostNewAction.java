package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.ui.MainForm;
import ua.jdesktopblogger.ui.NewPostForm;

/**
 * Action for showing new post form
 * 
 * @author Yuriy Tkach
 */
@SuppressWarnings("serial")//$NON-NLS-1$
public class PostNewAction extends SuperAction {

	public PostNewAction(MainForm fr) {
		this("New post", MainForm.createImageIcon("images/new_post.png"), //$NON-NLS-1$
				KeyEvent.VK_N, KeyStroke.getKeyStroke(KeyEvent.VK_N,
						ActionEvent.CTRL_MASK), "Create new post", fr);
		setEnabled(false);
	}

	public PostNewAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {

		Account account = form.getSelectedAccount();
		if (account != null) {
			new NewPostForm(form, account, form.getSelectedBlog())
					.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select an account or blog to create new post",
					form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
