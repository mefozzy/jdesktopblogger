package ua.jdesktopblogger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import ua.cn.yet.common.ui.Utf8ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "ua.cn.yet.emailChecker.messages"; //$NON-NLS-1$

	private static final ResourceBundle UTF_RESOURCE_BUNDLE = Utf8ResourceBundle.
			getBundle(BUNDLE_NAME);
	
	public static final String NEW_LINE = "\r\n"; //$NON-NLS-1$
	public static final String NEW_LINE_DOUBLE = "\r\n\r\n"; //$NON-NLS-1$

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return UTF_RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
