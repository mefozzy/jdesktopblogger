package ua.jdesktopblogger.config;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JTable;

import ua.jdesktopblogger.ui.tables.TableSorterWithoutZeroColumn;

public class PreferencesWorker {
	
	private Preferences prefs;

	public PreferencesWorker() {
		super();
		prefs = Preferences.userRoot().node("Yuriy E. Tkach").node("JDesktopBlogger"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Getter for prefs
	 * @return prefs object
	 */
	public Preferences getPrefs() {
		return prefs;
	}
	
	private void flushSettings() {
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saving table information to registry
	 * @param table Table with <code>name</code> property set!
	 */
	public void saveTableInfo(JTable table) {
		Preferences prefsTable = prefs.node(table.getName());
		for (int i = 0; i < table.getModel().getColumnCount(); i++) {
			int sortStatus = ((TableSorterWithoutZeroColumn)table.getModel()).getSortingStatus(i);
			prefsTable.put("Column_"+i+"_Status",String.valueOf(sortStatus)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		flushSettings();
	}
	
	/**
	 * Restoring table information from registy
	 * @param table Table with <code>name</code> property set!
	 */
	public void restoreTableInfo(JTable table) {
		Preferences prefsTable = prefs.node(table.getName());
		for (int i = 0; i < table.getModel().getColumnCount(); i++) {
			int sortStatus = Integer.valueOf(prefsTable.get("Column_"+i+"_Status", //$NON-NLS-1$ //$NON-NLS-2$
					String.valueOf(TableSorterWithoutZeroColumn.NOT_SORTED)));
			((TableSorterWithoutZeroColumn)table.getModel()).setSortingStatus(i, sortStatus);
		}
	}
	
	/**
	 * Setting last used file name
	 * @param fileName File name string
	 */
	public void setLastFileName(String fileName) {
		prefs.put("LastFile",fileName); //$NON-NLS-1$
		flushSettings();
	}
	
	/**
	 * Getting last used file name
	 * @return Filename
	 */
	public String getLastFileName() {
		return prefs.get("LastFile",""); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Setting last used file name
	 * @param file File
	 */
	public void setLastFile(File file) {
		prefs.put("LastFile",file.getAbsolutePath()); //$NON-NLS-1$
		flushSettings();
	}
	
	/**
	 * Getting last used file
	 * @return File
	 */
	public File getLastFile() {
		if (prefs.get("LastFile","").equals("")) //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return null;
		else
			return new File(prefs.get("LastFile","")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public boolean isLoadLastFile() {
		return Boolean.valueOf(prefs.get("startup_loadLastFile",String.valueOf(false))); //$NON-NLS-1$
	}

	public boolean isCheckOnStart() {
		return Boolean.valueOf(prefs.get("startup_doCheckOnStart",String.valueOf(false))); //$NON-NLS-1$
	}

	public boolean isAutoExitSave() {
		return Boolean.valueOf(prefs.get("other_autoExitSave",String.valueOf(false))); //$NON-NLS-1$
	}

	public void setLoadLastFile(boolean b) {
		prefs.put("startup_loadLastFile",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}

	public void setCheckOnStart(boolean b) {
		prefs.put("startup_doCheckOnStart",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}

	public void setAutoExitSave(boolean b) {
		prefs.put("other_autoExitSave",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}

	public boolean isHideProgressBar() {
		return Boolean.valueOf(prefs.get("other_hideProgressBar",String.valueOf(false))); //$NON-NLS-1$
	}

	public void setHideProgressBar(boolean b) {
		prefs.put("other_hideProgressBar",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}
	
	public boolean isShowOnlyEmailsInFrom() {
		return Boolean.valueOf(prefs.get("other_showOnlyEmailsInFrom",String.valueOf(false))); //$NON-NLS-1$
	}

	public void setShowOnlyEmailsInFrom(boolean b) {
		prefs.put("other_showOnlyEmailsInFrom",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}
	
	public boolean isShowOldEmailsNum() {
		return Boolean.valueOf(prefs.get("z_ShowOldEmailsNum",String.valueOf(false))); //$NON-NLS-1$
	}

	public void setShowOldEmailsNum(boolean b) {
		prefs.put("z_ShowOldEmailsNum",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}

	public boolean isShowAccountNames() {
		return Boolean.valueOf(prefs.get("z_ShowAccountNames",String.valueOf(false))); //$NON-NLS-1$
	}

	public void setShowAccountNames(boolean b) {
		prefs.put("z_ShowAccountNames",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}

	public boolean isShowEmailSubjectColumn() {
		return Boolean.valueOf(prefs.get("z_ShowEmailSubjectColumn",String.valueOf(true))); //$NON-NLS-1$
	}

	public void setShowEmailSubjectColumn(boolean b) {
		prefs.put("z_ShowEmailSubjectColumn",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}
	
	public boolean isForbidSecondCopy() {
		return Boolean.valueOf(prefs.get("z_ForbidSecondCopy",String.valueOf(true))); //$NON-NLS-1$
	}

	public void setForbidSecondCopy(boolean b) {
		prefs.put("z_ForbidSecondCopy",String.valueOf(b)); //$NON-NLS-1$
		flushSettings();
	}
}
