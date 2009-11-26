package ua.jdesktopblogger.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ua.cn.yet.common.ui.popup.PopupFactory;
import ua.cn.yet.common.ui.popup.PopupListener;
import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.ui.actions.AccountEditAction;
import ua.jdesktopblogger.ui.models.BlogsTreeDataModel;

public class MainForm implements IAccountListener {

	// Specify the look and feel to use. Valid values:
	// null (use the default), "Metal", "System", "Motif", "GTK+"
	private static String LOOKANDFEEL = "System";

	// Specify the right-to-left mode to use.
	public final static boolean RIGHT_TO_LEFT = false;

	private JFrame frame = null;

	private String appTitle;

	private JMenuBar menuBar;

	private JSplitPane splitPaneUpDown;

	private JSplitPane splitPaneLeftRight;

	private JTextArea textAreaLog;

	private JTable tableAccounts;

	private JTable tableEmails;

	private JToolBar toolBar;

	private JProgressBar progressBar;
	
	private JButton cancelButton; 

	private JLabel labelStatusBar;

	private JLabel labelLoading;

	private TrayIcon trayIcon;
	
	private JTree treeBlogs;
	
	private BlogsTreeDataModel blogsTreeModel; 
	
	////////////////////////////////////////////////////////////////////////////////////

	private AccountEditAction accountEditAction;

	/**
	 * Create the class and frame
	 */
	public MainForm(String title) {
		super();
		
		this.appTitle = title;

//		try {
//			ModelService.getInstance().initialize(this);
//		} catch (StartFailException e) {
//			JOptionPane.showMessageDialog(frame, Messages
//					.getString("MainForm.ErrorAppStart") //$NON-NLS-1$
//					+ Messages.getString("MainForm.ErrorStartJAXB") //$NON-NLS-1$
//					+ e.getLocalizedMessage(), appTitle,
//					JOptionPane.ERROR_MESSAGE);
//			e.printStackTrace();
//			System.exit(0);
//		}

		createAndShowGUI();
	}


	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = MainForm.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			//System.err.println(Messages.getString("MainForm.CannotFindIconFile") + path); //$NON-NLS-1$
			System.err.println("Cannot find image " + path); //$NON-NLS-1$
			return new ImageIcon();
		}
	}
	
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private void createAndShowGUI() {
		// set the look adn feel.
		initLookAndFeel();

		// Make sure we have new window decorations
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window
		frame = new JFrame(appTitle);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				//InstanceLock.getInstance().releaseLock();				
				
				//fileExitAction.actionPerformed(null);
				System.exit(0);
			}
		});
		
		GraphicsEnvironment env = 
			GraphicsEnvironment.getLocalGraphicsEnvironment(); 
		frame.setMaximizedBounds(env.getMaximumWindowBounds()); // taskbar not covered

		frame.setIconImage(createImageIcon("images/mail_generic22.png").getImage()); //$NON-NLS-1$

		createActions(frame);
		
		createComponents(frame.getContentPane());
		
//		createMenu(frame);
//		
//		setToggleMenusAndButtons(null);
		
		//frame.setVisible(false);
		
		//frame.doLayout();
				
		// Display the window
		frame.pack();
		
		// Restore properties
		restoreProperties();
		
		// Perform startup actions
		startup();
		
		frame.setVisible(true);
		
		// Loading systray icon and menu
		loadSystemTray();
		
	}

	/**
	 * Loading system tray icon with menu
	 */
	private void loadSystemTray() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(createImageIcon(
								"images/prog16.png").getImage(), //$NON-NLS-1$ 
								appTitle);
			
//			trayIcon.setPopupMenu(PopupFactory.getGeneralPopupAwt(
//					viewShowHideAppAction, null, emailCheckAllAction,
//					null, helpAboutAction, fileExitAction));

			//trayIcon.setImageAutoSize(true);
			
			trayIcon.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if ((e.getClickCount() > 1) && (e.getClickCount() < 3)) {
						//viewShowHideAppAction.actionPerformed(null);
					}
				}
			});
			
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added."); //$NON-NLS-1$
			}

		} else {
			System.out.println("System tray is not supported"); //$NON-NLS-1$
		}
	}

	/**
	 * Performing startup actions if they were specified in preferences
	 */
	private void startup() {
//		if (ModelService.getInstance().getPreferencesWorker().isLoadLastFile()) {
//			fileOpenAction.setOpenedFile(ModelService.getInstance().getPreferencesWorker().getLastFile());
//			try {
//				ModelService.getInstance().getAccountWorker().openFile(
//						ModelService.getInstance().getPreferencesWorker().getLastFileName());
//				updateUI(fileOpenAction);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if (ModelService.getInstance().getPreferencesWorker().isCheckOnStart()) {
//			emailCheckAllAction.actionPerformed(null);
//		}
		
	}

	/**
	 * Restore properties from registry
	 */
	private void restoreProperties() {
//		Preferences prefs = ModelService.getInstance().getPreferencesWorker()
//				.getPrefs();
//
//		Double areaLogHeight = Double.valueOf(prefs.get("areaLogHeight", "0")); //$NON-NLS-1$ //$NON-NLS-2$
//		areaLogScrollPane.setSize(-1, areaLogHeight.intValue());
//		if (areaLogHeight == 0) {
//			splitPaneUpDown.setDividerLocation(10000);
//		}
//		updateViewLogAction(true);
//
//		// Restoring tables info
//		ModelService.getInstance().getPreferencesWorker().restoreTableInfo(
//				tableEmails);
//		ModelService.getInstance().getPreferencesWorker().restoreTableInfo(
//				tableAccounts);
	}

	/**
	 * Saving properties to registry
	 */
	public void saveProperties() {
//		Preferences prefs = ModelService.getInstance().getPreferencesWorker()
//				.getPrefs();
//
//		prefs.put("areaLogHeight", String.valueOf(areaLogScrollPane.getSize() //$NON-NLS-1$
//				.getHeight()));
//
//		// Saving tables info
//		ModelService.getInstance().getPreferencesWorker().saveTableInfo(
//				tableEmails);
//		ModelService.getInstance().getPreferencesWorker().saveTableInfo(
//				tableAccounts);

	}

	/**
	 * Creating actions
	 * 
	 * @param fr
	 *            Frame
	 */
	private void createActions(JFrame fr) {
//		fileSaveAction = new FileSaveAction(this);
//		fileSaveAsAction = new FileSaveAsAction(this);
//		fileExitAction = new FileExitAction(this, fileSaveAction);
//		fileNewAction = new FileNewAction(this, fileSaveAction);
//		fileOpenAction = new FileOpenAction(this, fileSaveAction);
//		
//		viewShowOldEmailsAction = new ViewShowOldEmailsAction(this);
//		viewSelectAllEmailsAction = new ViewSelectAllEmailsAction(this);
//		viewSelectAllEmailsButLastAction = new ViewSelectAllEmailsButLastAction(this);
//		viewShowHideAppAction = new ViewShowHideAppAction(this);
//
//		accountEmailRetrieveAction = new AccountEmailRetrieveAction(this);
//		accountAddAction = new AccountAddAction(this);
		accountEditAction = new AccountEditAction(this);
//		accountDelAction = new AccountDeleteAction(this);
//
//		emailCheckAllAction = new EmailCheckAllAction(this);
//		emailCheckAgainsMainAccount = new EmailCheckAgainstMainAccountAction(this);
//		emailCheckAgainsMainAccountAll = new EmailCheckAgainstMainAccountActionAll(this);
//		emailCheckCancelAction = new EmailCheckCancelAction(this);
//		emailDeleteAccountEmailsAction = new EmailDeleteAccountEmailsAction(this, accountEmailRetrieveAction);
//		emailDeleteAllEmailsButOneAction = new EmailDeleteAllEmailsButOneAction(this);
//		emailDeleteAllEmailsAction = new EmailDeleteAllEmailsAction(this);
//		
//		otherSettingsAction = new OtherSettingsAction(this);
//		otherViewLogAction = new OtherViewLogAction(this);
//
//		logSaveAsAction = new LogSaveAsAction(this);
//
//		helpAboutAction = new HelpAboutAction(this);
	}


	/**
	 * Creating all visual components
	 * 
	 * @param contentPane Pane to add components to
	 */
	private void createComponents(Container contentPane) {

		JScrollPane scrollPane;

		if (RIGHT_TO_LEFT) {
			contentPane
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		frame.setBounds(100, 100, 400, 375);

		splitPaneUpDown = new JSplitPane();
		splitPaneUpDown.setResizeWeight(0.5);
		splitPaneUpDown.setOneTouchExpandable(true);
		splitPaneUpDown.setDividerSize(8);
		splitPaneUpDown.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneUpDown.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent e) {
				//updateViewLogAction(true);
			}
		});
		contentPane.add(splitPaneUpDown);

		splitPaneLeftRight = new JSplitPane();
		splitPaneLeftRight.setDividerSize(8);
		splitPaneLeftRight.setOneTouchExpandable(true);
		splitPaneUpDown.setLeftComponent(splitPaneLeftRight);

		splitPaneUpDown.setRightComponent(createEntryViewArea());

		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(300, 0));
		splitPaneLeftRight.setLeftComponent(scrollPane);

		createAccountsTree(scrollPane);
		
//		scrollPane = new JScrollPane();
//		splitPaneLeftRight.setRightComponent(scrollPane);

		//createMailsTable(scrollPane);
	
		contentPane.add(createStatusBar(), BorderLayout.SOUTH);
		
		createToolBar(contentPane);
	}
	
	/**
	 * Creating toolbar
	 * 
	 * @param contentPane
	 */
	private void createToolBar(Container contentPane) {
		JButton button;

		toolBar = new JToolBar();
		toolBar.setAutoscrolls(true);
		toolBar.setFloatable(true);
		contentPane.add(toolBar, BorderLayout.NORTH);

		button = new JButton(accountEditAction);
		button.setText(null);
		toolBar.add(button);
	}


	/**
	 * Creating status bar component with all controls
	 * 
	 * @return Created status bar component
	 */
	private JComponent createStatusBar() {
		JPanel panelStatusBar = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelStatusBar.setLayout(flowLayout);

		labelLoading = new JLabel();
		labelLoading.setIcon(MainForm.createImageIcon("images/loading3.gif")); //$NON-NLS-1$
		labelLoading.setHorizontalTextPosition(SwingConstants.CENTER);
		labelLoading.setHorizontalAlignment(SwingConstants.CENTER);
		labelLoading.setFocusable(false);
		labelLoading.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelLoading.setText(""); //$NON-NLS-1$
		labelLoading.setVisible(false);
		panelStatusBar.add(labelLoading);
		
		labelStatusBar = new JLabel();
		labelStatusBar.setText(" "); //$NON-NLS-1$
		panelStatusBar.add(labelStatusBar);
		
		return panelStatusBar;
	}


	private JComponent createEntryViewArea() {
		JPanel panel = new JPanel(new BorderLayout());
		
		textAreaLog = new JTextArea();
		textAreaLog.setEditable(false);
		textAreaLog.setLineWrap(true);
		textAreaLog.setWrapStyleWord(true);
		textAreaLog.addMouseListener(new PopupListener(PopupFactory
				.getGeneralPopup(PopupFactory.getEditPopup())));
		
		JScrollPane areaLogScrollPane = new JScrollPane(textAreaLog);
		areaLogScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaLogScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Log"), BorderFactory
								.createEmptyBorder(0, 0, 0, 0)),
				areaLogScrollPane.getBorder()));
		panel.add(areaLogScrollPane);
		return panel;
	}
	
	/**
	 * Creating components for displaying account with blogs
	 * @param scrollPane Pane to add components to
	 */
	private void createAccountsTree(JScrollPane scrollPane) {
		blogsTreeModel = new BlogsTreeDataModel();
		treeBlogs = new JTree(blogsTreeModel){
			public String convertValueToText(Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus){
				if (value instanceof Account){
					Account account = (Account) value;
					return account.getLogin();
				} else if (value instanceof Blog){
					Blog blog = (Blog) value;
					return blog.getName();
				}
				return "";
			}
		};
		scrollPane.setViewportView(treeBlogs);
		
	}

	private void initLookAndFeel() {
		String lookAndFeel = null;
		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) { //$NON-NLS-1$
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("System")) { //$NON-NLS-1$
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("Motif")) { //$NON-NLS-1$
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; //$NON-NLS-1$
			} else if (LOOKANDFEEL.equals("GTK+")) { // new in 1.4.2  //$NON-NLS-1$
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"; //$NON-NLS-1$
			} else {
//				System.err.println(Messages
//						.getString("MainForm.UnexpectedLFValue") //$NON-NLS-1$
//						+ LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
//				System.err.println(Messages.getString("MainForm.NoClassForLF") //$NON-NLS-1$
//						+ lookAndFeel);
//				System.err.println(Messages.getString("MainForm.LibLFThere")); //$NON-NLS-1$
//				System.err.println(Messages.getString("MainForm.UseDefLF")); //$NON-NLS-1$
			} catch (UnsupportedLookAndFeelException e) {
//				System.err.println(Messages.getString("MainForm.UnableUseLF") //$NON-NLS-1$
//						+ lookAndFeel
//						+ Messages.getString("MainForm.OnThisPlatform")); //$NON-NLS-1$
//				System.err.println(Messages.getString("MainForm.UseDefLF")); //$NON-NLS-1$
			} catch (Exception e) {
//				System.err.println(Messages.getString("MainForm.UnableGetLF") //$NON-NLS-1$
//						+ lookAndFeel
//						+ Messages.getString("MainForm.ForSomeReason")); //$NON-NLS-1$
//				System.err.println(Messages.getString("MainForm.UseDefLF")); //$NON-NLS-1$
				e.printStackTrace();
			}
		}
	}

	/**
	 * Displaying form
	 */
	public void show() {
		frame.setVisible(true);
		// Center window on screen
		frame.setLocationRelativeTo(null);
	}


	public JFrame getFrame() {
		return frame;
	}

	public String getAppTitle() {
		return appTitle;
	}

	/**
	 * Adding log message to the textArea
	 * 
	 * @param message
	 *            Message to add
	 */
	public synchronized void addLog(String message) {
		Calendar now = Calendar.getInstance();
		if (textAreaLog.getText().equals("")) //$NON-NLS-1$
			textAreaLog.setText(
					String.format("%tT", now) //$NON-NLS-1$
					+ ": "  //$NON-NLS-1$
					+ message);
		else
			textAreaLog.setText(textAreaLog.getText()
					+ Messages.NEW_LINE
					+ String.format("%tT", now) //$NON-NLS-1$
					+ ": "  //$NON-NLS-1$
					+ message);
		textAreaLog.setSelectionStart(textAreaLog.getText().length()-1);
	}

	/**
	 * Getting log string
	 * 
	 * @return log information
	 */
	public String getLogText() {
		return textAreaLog.getText();
	}
	
	/**
	 * Getting table object that corresponds to emails table
	 * @return Emails table
	 */
	public JTable getTableEmails() {
		return tableEmails;
	}

	@Override
	public void accountCreated(Account account) {
		System.out.println("Hello " + account + " account.name:" + account.getLogin() 
				+ " account.passwd:" + account.getPassword());
		blogsTreeModel.addAccount(account);
		treeBlogs.updateUI();
	}

	@Override
	public void accountEdited(Account account) {
		
	}

	@Override
	public void accountRefreshed(Account account) {
		// TODO Auto-generated method stub
		
	}

}
