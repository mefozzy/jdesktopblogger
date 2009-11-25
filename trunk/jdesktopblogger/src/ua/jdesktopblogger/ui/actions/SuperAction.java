package ua.jdesktopblogger.ui.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.ui.MainForm;


@SuppressWarnings("serial") //$NON-NLS-1$
public abstract class SuperAction extends AbstractAction {

	public static final String ACTION_DESC = "ActionDesc"; //$NON-NLS-1$
	
	public static final String ACCOUNT_NAME = "acc_name"; //$NON-NLS-1$
	
	protected MainForm form;

	public SuperAction(String caption, Icon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm form) {
		super(caption, icon);
		putValue(ACCELERATOR_KEY, keyStroke);
		putValue(MNEMONIC_KEY, keyEvent);
		putValue(SHORT_DESCRIPTION, description);

		this.form = form;
	}
	
	public void setCaption(String newCaption) {
		putValue(NAME, newCaption);
	}
	
}
