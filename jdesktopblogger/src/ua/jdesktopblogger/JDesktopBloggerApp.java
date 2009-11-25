package ua.jdesktopblogger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ua.jdesktopblogger.config.InstanceLock;
import ua.jdesktopblogger.config.ProgramVersion;
import ua.jdesktopblogger.ui.MainForm;

public class JDesktopBloggerApp {

	public static void main(String[] args) {

		String appTitle = "JBlogger "
				+ ProgramVersion.getInstance().getVersion();

		// TODO: Check if check is needed
		if (true) {
			if (!InstanceLock.registerInstanceLock()) {
				JOptionPane.showMessageDialog(
					null,
					//Messages.getString("MainForm.ErrorSecondInstance"), //$NON-NLS-1$
					"Another instance of the application is alreary running.",
					appTitle, JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}

		// Creating main form
		final MainForm form = new MainForm(appTitle);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				form.show();
			}
		});

	}

}
