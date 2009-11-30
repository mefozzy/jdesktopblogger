package ua.jdesktopblogger.ui.actions.newpost;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.NewPostForm;

@SuppressWarnings("serial") //$NON-NLS-1$
public class PublishPostAction extends NewPostFormSuperAction {

	public PublishPostAction(NewPostForm form) {
		this("Publish",
				null,
				KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_ACCEPT,
						ActionEvent.CTRL_MASK),
				"Publish post to the server", form);
	}

	public PublishPostAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, NewPostForm form) {
		super(caption, icon, keyEvent, keyStroke, description, form);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		getNewPostForm().dispose();
	}

}
