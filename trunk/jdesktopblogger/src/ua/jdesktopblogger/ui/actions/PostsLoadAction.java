package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial")//$NON-NLS-1$
public class PostsLoadAction extends SuperAction {

	public PostsLoadAction(MainForm fr) {
		this("Load posts", MainForm.createImageIcon("images/mail_get.png"), //$NON-NLS-1$
				KeyEvent.VK_L, KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0),
				"Load posts for the blog", fr);

		setEnabled(false);
	}

	public PostsLoadAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {

		Account account = form.getSelectedAccount();
		if (account != null) {

			Blog blog = form.getSelectedBlog();

			if (blog != null) {

				try {
					ServiceFactory.getDefaultFactory().getBlogService()
							.loadPosts(account, blog, form, form);
				} catch (BlogServiceException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(form.getFrame(),
							"Failed to load posts for blog."
									+ Messages.NEW_LINE_DOUBLE
									+ e.getLocalizedMessage(), form
									.getAppTitle(), JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(form.getFrame(),
						"Please, select blog to load posts for", form
								.getAppTitle(), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select an account for refreshing", form
							.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
