package ua.jdesktopblogger.ui.actions.newpost;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.domain.IPostListener;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.MainForm;
import ua.jdesktopblogger.ui.NewPostForm;

/**
 * Action for publishing new post
 * 
 * @author Yuriy Tkach
 */
@SuppressWarnings("serial")//$NON-NLS-1$
public class PublishPostAction extends NewPostFormSuperAction {

	private Account account;
	private IAccountListener accountListener;
	private IPostListener postsListener;

	public PublishPostAction(NewPostForm form, Account account,
			IAccountListener accountListener, IPostListener postListener) {
		this("Publish", MainForm.createImageIcon("images/mail_send.png"),
				KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_ACCEPT,
						ActionEvent.CTRL_MASK), "Publish post to the server",
				form);

		this.account = account;
		this.accountListener = accountListener;
		this.postsListener = postListener;
	}

	public PublishPostAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, NewPostForm form) {
		super(caption, icon, keyEvent, keyStroke, description, form);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		Blog blog = getNewPostForm().getSelectedBlog();
		if (blog != null) {

			Post post = getNewPostForm().getEditedPost();

			if (post != null) {

				try {
					ServiceFactory.getDefaultFactory().getBlogService()
							.publishPost(account, blog, post, accountListener,
									postsListener);

					getNewPostForm().dispose();
				} catch (BlogServiceException e) {
					e.printStackTrace();

					JOptionPane.showMessageDialog(form.getFrame(),
							"Failed to publish post."
									+ Messages.NEW_LINE_DOUBLE
									+ e.getLocalizedMessage(), form
									.getAppTitle(), JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select a blog to create new post", form
							.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
