package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.ui.MainForm;
import ua.jdesktopblogger.ui.NewPostForm;

@SuppressWarnings("serial")//$NON-NLS-1$
public class PostEditAction extends SuperAction {

	public PostEditAction(MainForm fr) {
		this("Edit post", MainForm.createImageIcon("images/mail_edit.png"), //$NON-NLS-1$
				KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_E,
						ActionEvent.CTRL_MASK), "Edit post data", fr);
		setEnabled(false);
	}

	public PostEditAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		Account account = form.getSelectedAccount();
		Blog blog = form.getSelectedBlog();
		if ((account != null) && (blog != null)) {
			Post post = form.getSelectedPost();
			if (post != null) {
				new NewPostForm(form, account, blog, post).setVisible(true);
			} else {
				JOptionPane.showMessageDialog(form.getFrame(),
						"Please, select post to edit",
						form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(form.getFrame(),
					"Please, select an account or blog to edit post",
					form.getAppTitle(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
