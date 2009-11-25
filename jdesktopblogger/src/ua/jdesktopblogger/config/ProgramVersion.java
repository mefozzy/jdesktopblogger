package ua.jdesktopblogger.config;

import java.util.ResourceBundle;

import ua.cn.yet.common.ui.Utf8ResourceBundle;

/**
 * Class that loads information about program version from the 
 * build_number.properties file
 * 
 * @author Yuriy Tkach
 */
public class ProgramVersion {
	
	private String version;
	
	private ProgramVersion() {
		version = ""; //$NON-NLS-1$
		try {
			ResourceBundle bundle = Utf8ResourceBundle.getBundle("ua.jdesktopblogger.build_number"); //$NON-NLS-1$
			version = bundle.getString("major.version.number") +  //$NON-NLS-1$
				"." + bundle.getString("minor.version.number") +  //$NON-NLS-1$ //$NON-NLS-2$
				"." + bundle.getString("build.number"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getVersion() {
		return version;
	}
	
	private static ProgramVersion _instance = new ProgramVersion();
	
	public static ProgramVersion getInstance() {
		return _instance;
	}

}
