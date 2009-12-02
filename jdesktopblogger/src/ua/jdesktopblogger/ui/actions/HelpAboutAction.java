package ua.jdesktopblogger.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.ui.MainForm;


@SuppressWarnings("serial") //$NON-NLS-1$
public class HelpAboutAction extends SuperAction {

	public HelpAboutAction(MainForm fr) {
		this("About...", null,
				//MainForm.createImageIcon("images/configure.png"),
		KeyEvent.VK_A, null, "Display information about the program", fr);
	}

	public HelpAboutAction(String caption, ImageIcon icon, int keyEvent,
			KeyStroke keyStroke, String description, MainForm fr) {
		super(caption, icon, keyEvent, keyStroke, description, fr);
	}

	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(form.getFrame(),
				form.getAppTitle() +
				Messages.NEW_LINE_DOUBLE+
				"Yuriy Tkach, Oleksandr Skosyr"+
				Messages.NEW_LINE+
				"2009"+
				Messages.NEW_LINE_DOUBLE+
				"Created in spare time to prolong spare time",
				form.getAppTitle(),
				JOptionPane.INFORMATION_MESSAGE);
	}

}
