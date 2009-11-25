package ua.jdesktopblogger.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ua.jdesktopblogger.Messages;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SettingsForm {

	private JDialog dialog;

//	private PreferencesWorker prefs = ModelService.getInstance()
//			.getPreferencesWorker();

	private JTabbedPane tabbedPane;

	private JCheckBox checkLoadLastFile;

	private JCheckBox checkDoCheckOnStart;
	
	private JCheckBox checkForbidSecondCopy;

	private JCheckBox checkAutoExitSave;

	private JCheckBox checkHideProgressBar;

	private JCheckBox checkShowOnlyEmailsInFrom;

	private JButton cancelButton;

	private JButton okButton;

	private JCheckBox checkShowAccountNames;

	private JCheckBox checkShowEmailSubjectColumn;
	
	private JCheckBox checkAutoEmailCheck;
	private JCheckBox checkInformOnAutoCheckChange;
	private JTextField textAutoCheckTimeout;

	//public SettingsForm(JFrame frame) {
	public SettingsForm() {
		super();
//		dialog = new JDialog(frame, Messages
//				.getString("SettingsForm.DialogTitle"), true); //$NON-NLS-1$
		dialog = new JDialog((JFrame)null, "Settings", true); //$NON-NLS-1$
		dialog.addKeyListener(new KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancelButton.doClick();
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					okButton.doClick();
			}
		});

		createComponents(dialog.getContentPane());
		dialog.setResizable(false);
		dialog.setBounds(100, 100, 443, 184);
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.pack();
		//dialog.setLocationRelativeTo(frame);
	}

	public void showForm() {
		restoreSettings();
		dialog.setVisible(true);
	}

	private void createComponents(Container contentPane) {

		final BorderLayout borderLayout = new BorderLayout();
		borderLayout.setVgap(4);
		borderLayout.setHgap(4);
		contentPane.setLayout(borderLayout);

		tabbedPane = new JTabbedPane();
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel;

		// Creating general settings tab
		panel = createTabPage(Messages
				.getString("SettingsForm.GeneralSettingsTitle"), 1, 2); //$NON-NLS-1$

		fillGeneralTab(panel);

		// Creating view settings tab
		panel = createTabPage(Messages.getString("SettingsForm.TabViewTitle"), 1, 2); //$NON-NLS-1$
		
		fillViewTab(panel);
		
		//panel = createTabPage("Email check", 1, 1);
		
		//fillEmailCheckTab(panel);

		createBottomButtons(contentPane);
	}

	/**
	 * Creating tab page with title and returning panel, that corresponds to it
	 * 
	 * @param title
	 *            Title of the tab page
	 * @param cols
	 *            Number of columns to create
	 * @param rows
	 *            Number of rows to create
	 * @return Panel, where to place the components
	 */
	private JPanel createTabPage(String title, int cols, int rows) {
		JPanel panel = new JPanel();

		FormLayout f = new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC });

		for (int i = 0; i < cols; i++) {
			f.appendColumn(new ColumnSpec("default:grow(1.0)")); //$NON-NLS-1$
			//f.appendColumn(FormFactory.DEFAULT_COLSPEC);
			f.appendColumn(FormFactory.RELATED_GAP_COLSPEC);
		}

		for (int i = 0; i < rows; i++) {
			f.appendRow(FormFactory.DEFAULT_ROWSPEC);
			f.appendRow(FormFactory.RELATED_GAP_ROWSPEC);
		}

		panel.setLayout(f);
		tabbedPane.addTab(title, null, panel, null);
		return panel;
	}

	/**
	 * Filling general settings tab page
	 */
	private void fillGeneralTab(JPanel panel) {

		final JPanel panelStartup = new JPanel();
		panelStartup
				.setLayout(new BoxLayout(panelStartup, BoxLayout.PAGE_AXIS));
		panelStartup
				.setBorder(new TitledBorder(
						null,
						Messages.getString("SettingsForm.StartupGroup"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null)); //$NON-NLS-1$
		panel.add(panelStartup, new CellConstraints("2, 2, 1, 1, fill, fill")); //$NON-NLS-1$

		checkLoadLastFile = new JCheckBox();
		checkLoadLastFile.setText(Messages
				.getString("SettingsForm.LoadLastOnStart")); //$NON-NLS-1$
		panelStartup.add(checkLoadLastFile);

		checkDoCheckOnStart = new JCheckBox();
		checkDoCheckOnStart.setText(Messages
				.getString("SettingsForm.AutoCheckOnStart")); //$NON-NLS-1$
		panelStartup.add(checkDoCheckOnStart);
		
		checkForbidSecondCopy = new JCheckBox();
		checkForbidSecondCopy.setText(Messages.getString("SettingsForm.ForbitSecondCopy")); //$NON-NLS-1$
		panelStartup.add(checkForbidSecondCopy);

		final JPanel panelShutdown = new JPanel();
		panelShutdown.setLayout(new BoxLayout(panelShutdown,
				BoxLayout.PAGE_AXIS));
		panelShutdown
				.setBorder(new TitledBorder(
						null,Messages.getString("SettingsForm.ShutDownGroup"), //$NON-NLS-1$
						TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		panel.add(panelShutdown, new CellConstraints("2, 4, 1, 1, fill, fill")); //$NON-NLS-1$

		checkAutoExitSave = new JCheckBox();
		checkAutoExitSave.setText(Messages
				.getString("SettingsForm.AutoSaveOnExit")); //$NON-NLS-1$
		panelShutdown.add(checkAutoExitSave);
	}
	
	/**
	 * Filling general settings tab page
	 */
	private void fillViewTab(JPanel panel) {

		final JPanel panelEmails = new JPanel();
		panelEmails
				.setLayout(new BoxLayout(panelEmails, BoxLayout.PAGE_AXIS));
		panelEmails
				.setBorder(new TitledBorder(
						null,Messages.getString("SettingsForm.EmailsGroup"), //$NON-NLS-1$
						TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		panel.add(panelEmails, new CellConstraints("2, 2, 1, 1, fill, fill")); //$NON-NLS-1$
		
		checkShowEmailSubjectColumn = new JCheckBox();
		checkShowEmailSubjectColumn.setText(Messages.getString("SettingsForm.ShowEmailSubject")); //$NON-NLS-1$
		panelEmails.add(checkShowEmailSubjectColumn);
				
		checkShowAccountNames = new JCheckBox();
		checkShowAccountNames.setText(Messages.getString("SettingsForm.ShowAccountName")); //$NON-NLS-1$
		checkShowAccountNames.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				if (checkShowAccountNames.isSelected()) {
					checkShowOnlyEmailsInFrom.setSelected(false);
					checkShowOnlyEmailsInFrom.setVisible(false);
				} else {
					checkShowOnlyEmailsInFrom.setVisible(true);
				}
			}
			
		});
		panelEmails.add(checkShowAccountNames);
		
		checkShowOnlyEmailsInFrom = new JCheckBox();
		checkShowOnlyEmailsInFrom.setText(Messages
				.getString("SettingsForm.ShowOnlyEmailsInFrom")); //$NON-NLS-1$
		panelEmails.add(checkShowOnlyEmailsInFrom);
				
		final JPanel panelOther = new JPanel();
		panelOther.setLayout(new BoxLayout(panelOther,
				BoxLayout.PAGE_AXIS));
		panelOther
				.setBorder(new TitledBorder(
						null,Messages.getString("SettingsForm.OtherGroup"), //$NON-NLS-1$
						TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		panel.add(panelOther, new CellConstraints("2, 4, 1, 1, fill, fill")); //$NON-NLS-1$

		checkHideProgressBar = new JCheckBox();
		checkHideProgressBar.setText(Messages
				.getString("SettingsForm.HideProgressBar")); //$NON-NLS-1$
		panelOther.add(checkHideProgressBar);
	}
	
	/**
	 * Filling email check settings tab page
	 */
	private void fillEmailCheckTab(JPanel panel) {
		
		final JPanel panelAutoCheck = new JPanel();		
//		panelAutoCheck
//				.setLayout(new BoxLayout(panelAutoCheck, BoxLayout.PAGE_AXIS));
		panelAutoCheck.setLayout(new GridLayout(3,1));
		panelAutoCheck
				.setBorder(new TitledBorder(
						null,Messages.getString("SettingsForm.AutoCheckGroup"),  //$NON-NLS-1$
						TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		panelAutoCheck.setAlignmentX(0);
		panel.add(panelAutoCheck, new CellConstraints("2, 2, 1, 1, fill, fill")); //$NON-NLS-1$
		
		checkAutoEmailCheck = new JCheckBox();
		checkAutoEmailCheck.setText(Messages.getString("SettingsForm.CheckEmailsAutoSetting")); //$NON-NLS-1$
		panelAutoCheck.add(checkAutoEmailCheck);
		
		JPanel panelTimeout = new JPanel();
		//panelTimeout.setLayout(new BoxLayout(panelTimeout, BoxLayout.LINE_AXIS));
		panelTimeout.setLayout(new FlowLayout(5));
		panelAutoCheck.add(panelTimeout);
		
		final JLabel label1 = new JLabel(Messages.getString("SettingsForm.CheckEmailsEverySetting")); //$NON-NLS-1$
		panelTimeout.add(label1);
		
		textAutoCheckTimeout = new JTextField();
		textAutoCheckTimeout.setPreferredSize(new Dimension(50,22));
		panelTimeout.add(textAutoCheckTimeout);
		
		final JLabel label2 = new JLabel(Messages.getString("SettingsForm.min")); //$NON-NLS-1$
		panelTimeout.add(label2);
				
		checkInformOnAutoCheckChange = new JCheckBox();
		checkInformOnAutoCheckChange.setText(Messages.getString("SettingsForm.NotifyOnEmailChangesSetting")); //$NON-NLS-1$
		panelAutoCheck.add(checkInformOnAutoCheckChange);
		
		checkAutoEmailCheck.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				if (checkAutoEmailCheck.isSelected()) {
					label1.setEnabled(true);
					label2.setEnabled(true);
					textAutoCheckTimeout.setEnabled(true);
					checkInformOnAutoCheckChange.setEnabled(true);
				} else {
					label1.setEnabled(false);
					label2.setEnabled(false);
					textAutoCheckTimeout.setEnabled(false);
					checkInformOnAutoCheckChange.setEnabled(false);
				}
			}
			
		});
	}

	/**
	 * @param contentPane
	 */
	private void createBottomButtons(Container contentPane) {
		final JPanel panelBottom = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panelBottom.setLayout(flowLayout);
		contentPane.add(panelBottom, BorderLayout.SOUTH);

		okButton = new JButton();
		okButton.addKeyListener(dialog.getKeyListeners()[0]);
		okButton.setMnemonic(KeyEvent.VK_ACCEPT);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				rememberSettings();
			}
		});
		okButton.setText(Messages.getString("AccountEditForm.Ok")); //$NON-NLS-1$
		panelBottom.add(okButton, new CellConstraints(4, 10,
				CellConstraints.RIGHT, CellConstraints.DEFAULT));

		cancelButton = new JButton();
		cancelButton.addKeyListener(dialog.getKeyListeners()[0]);
		cancelButton.setMnemonic(KeyEvent.VK_CANCEL);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		cancelButton.setText(Messages.getString("AccountEditForm.Cancel")); //$NON-NLS-1$
		panelBottom.add(cancelButton, new CellConstraints(6, 10));

	}

	/**
	 * Restoring settings values from registry
	 */
	private void restoreSettings() {
		// General settings
//		checkLoadLastFile.setSelected(prefs.isLoadLastFile());
//		checkDoCheckOnStart.setSelected(prefs.isCheckOnStart());
//		checkForbidSecondCopy.setSelected(prefs.isForbidSecondCopy());
//		checkAutoExitSave.setSelected(prefs.isAutoExitSave());
//		
//		// View settings
//		checkShowEmailSubjectColumn.setSelected(prefs.isShowEmailSubjectColumn());
//		checkShowAccountNames.setSelected(prefs.isShowAccountNames());
//		checkShowOnlyEmailsInFrom.setSelected(prefs.isShowOnlyEmailsInFrom());
//		checkHideProgressBar.setSelected(prefs.isHideProgressBar());
	}

	/**
	 * Remembering user settings
	 */
	protected void rememberSettings() {
		// General settings
//		prefs.setLoadLastFile(checkLoadLastFile.isSelected());
//		prefs.setCheckOnStart(checkDoCheckOnStart.isSelected());
//		prefs.setForbidSecondCopy(checkForbidSecondCopy.isSelected());
//		prefs.setAutoExitSave(checkAutoExitSave.isSelected());
//		
//		// View settings
//		prefs.setShowAccountNames(checkShowAccountNames.isSelected());
//		prefs.setShowOnlyEmailsInFrom(checkShowOnlyEmailsInFrom.isSelected());
//		prefs.setShowEmailSubjectColumn(checkShowEmailSubjectColumn.isSelected());
//		prefs.setHideProgressBar(checkHideProgressBar.isSelected());		
		
		dialog.setVisible(false);
	}

}
