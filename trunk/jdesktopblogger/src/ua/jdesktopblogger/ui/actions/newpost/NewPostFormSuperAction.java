package ua.jdesktopblogger.ui.actions.newpost;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.NewPostForm;
import ua.jdesktopblogger.ui.actions.SuperAction;

/**
 * Super class for all actions of the {@link NewPostForm} form.
 * @author Yuriy Tkach
 */
public abstract class NewPostFormSuperAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private NewPostForm newPostForm;
	
	public NewPostFormSuperAction(String caption, Icon icon, int keyEvent,
			KeyStroke keyStroke, String description, NewPostForm form) {
		super(caption, icon, keyEvent, keyStroke, description, null);
		this.newPostForm = form;
	}

	/**
	 * @return the newPostForm
	 */
	public NewPostForm getNewPostForm() {
		return newPostForm;
	}

}
