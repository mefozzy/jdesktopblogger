package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.MainForm;

@SuppressWarnings("serial")//$NON-NLS-1$
public class PostDeleteAction extends SuperAction {

	public PostDeleteAction(MainForm fr) {
		this(
				"Delete post",
				MainForm.createImageIcon("images/mail_delete2.png"), //$NON-NLS-1$ 
				KeyEvent.VK_D, null,
				"Delete post from the blog. Action cannot be undone!", fr); //$NON-NLS-1$
		
		setEnabled(false);
	}

	public PostDeleteAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {

		Account account = form.getSelectedAccount();
		Blog blog = form.getSelectedBlog();

		if ((account != null) && (blog != null)) {

			Post post = form.getSelectedPost();

			if (post != null) {

				// Showing confirm dialog
				if (JOptionPane.showConfirmDialog(form.getFrame(), 
						"Do you really want to delete post: " +
						Messages.NEW_LINE + post.getTitle() +
						Messages.NEW_LINE_DOUBLE +
						"Please, note: this action cannot be undone.",
						form.getAppTitle(),
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					try {
						ServiceFactory.getDefaultFactory().getBlogService().deletePost(account, blog, post, form, form);
					} catch (BlogServiceException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(form.getFrame(),
								"Failed to delete post" +
								Messages.NEW_LINE_DOUBLE + e.getLocalizedMessage(),
								form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
					}

				}
			} else {
				JOptionPane.showMessageDialog(form.getFrame(),
						"Please, select a post for deletion", form
								.getAppTitle(), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select a blog to choose the post for deletion",
					form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
