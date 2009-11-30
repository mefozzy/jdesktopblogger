package ua.jdesktopblogger.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ua.cn.yet.common.ui.popup.PopupFactory;
import ua.cn.yet.common.ui.popup.PopupListener;
import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.domain.IPostListener;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.AccountIOException;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.actions.AccountEditAction;
import ua.jdesktopblogger.ui.actions.AccountRefreshAction;
import ua.jdesktopblogger.ui.actions.PostNewAction;
import ua.jdesktopblogger.ui.actions.PostsLoadAction;
import ua.jdesktopblogger.ui.models.BlogsTreeDataModel;
import ua.jdesktopblogger.ui.renderers.DateRenderer;
import ua.jdesktopblogger.ui.renderers.TreeCellAccountRenderer;
import ua.jdesktopblogger.ui.tables.TablePostModel;
import ua.jdesktopblogger.ui.tables.TableSorterWithoutZeroColumn;

public class MainForm implements IAccountListener, IPostListener {

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

	private JTextPaneZoom textPanePost;

	private JTable tablePosts;

	private JTable tableEmails;

	private JToolBar toolBar;

	private JProgressBar progressBar;

	private JButton cancelButton;

	private JLabel labelStatusBar;

	private JLabel labelLoading;

	private TrayIcon trayIcon;

	private JTree treeBlogs;

	private BlogsTreeDataModel treeModelBlogs;
	
	private TablePostModel tablePostModel;
	
	private JLabel labelPostName;

	private JLabel labelPostDatePublish;

	private JLabel labelPostDateEdit;

	private JLabel labelPostKeywords;
	
	private JPanel panelInfoPost; 
	// //////////////////////////////////////////////////////////////////////////////////

	private AccountEditAction accountEditAction;
	private AccountRefreshAction accountRefreshAction;	
	private PostsLoadAction postsLoadAction;
	
	private PostNewAction postNewAction;


	/**
	 * Create the class and frame
	 */
	public MainForm(String title) {
		super();

		this.appTitle = title;

		// try {
		// ModelService.getInstance().initialize(this);
		// } catch (StartFailException e) {
		// JOptionPane.showMessageDialog(frame, Messages
		//					.getString("MainForm.ErrorAppStart") //$NON-NLS-1$
		//					+ Messages.getString("MainForm.ErrorStartJAXB") //$NON-NLS-1$
		// + e.getLocalizedMessage(), appTitle,
		// JOptionPane.ERROR_MESSAGE);
		// e.printStackTrace();
		// System.exit(0);
		// }

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
				// InstanceLock.getInstance().releaseLock();

				// fileExitAction.actionPerformed(null);
				System.exit(0);
			}
		});

		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		frame.setMaximizedBounds(env.getMaximumWindowBounds()); // taskbar not
																// covered

		frame
				.setIconImage(createImageIcon("images/mail_generic22.png").getImage()); //$NON-NLS-1$

		createActions(frame);

		createComponents(frame.getContentPane());

		// createMenu(frame);
		//		
		// setToggleMenusAndButtons(null);

		// frame.setVisible(false);

		// frame.doLayout();

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
			trayIcon = new TrayIcon(
					createImageIcon("images/prog16.png").getImage(), //$NON-NLS-1$ 
					appTitle);

			// trayIcon.setPopupMenu(PopupFactory.getGeneralPopupAwt(
			// viewShowHideAppAction, null, emailCheckAllAction,
			// null, helpAboutAction, fileExitAction));

			// trayIcon.setImageAutoSize(true);

			trayIcon.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if ((e.getClickCount() > 1) && (e.getClickCount() < 3)) {
						// viewShowHideAppAction.actionPerformed(null);
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
		try {
			Collection<Account> accounts = ServiceFactory.getDefaultFactory().getAccountService().loadSavedAccounts();
			
			for (Account account : accounts) {
				accountCreated(account);
			}
			
		} catch (AccountIOException e) {
			e.printStackTrace();
		}
		
		// if
		// (ModelService.getInstance().getPreferencesWorker().isLoadLastFile())
		// {
		// fileOpenAction.setOpenedFile(ModelService.getInstance().getPreferencesWorker().getLastFile());
		// try {
		// ModelService.getInstance().getAccountWorker().openFile(
		// ModelService.getInstance().getPreferencesWorker().getLastFileName());
		// updateUI(fileOpenAction);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//		
		// if
		// (ModelService.getInstance().getPreferencesWorker().isCheckOnStart())
		// {
		// emailCheckAllAction.actionPerformed(null);
		// }

	}

	/**
	 * Restore properties from registry
	 */
	private void restoreProperties() {
		// Preferences prefs = ModelService.getInstance().getPreferencesWorker()
		// .getPrefs();
		//
		//		Double areaLogHeight = Double.valueOf(prefs.get("areaLogHeight", "0")); //$NON-NLS-1$ //$NON-NLS-2$
		// areaLogScrollPane.setSize(-1, areaLogHeight.intValue());
		// if (areaLogHeight == 0) {
		// splitPaneUpDown.setDividerLocation(10000);
		// }
		// updateViewLogAction(true);
		//
		// // Restoring tables info
		// ModelService.getInstance().getPreferencesWorker().restoreTableInfo(
		// tableEmails);
		// ModelService.getInstance().getPreferencesWorker().restoreTableInfo(
		// tableAccounts);
	}

	/**
	 * Saving properties to registry
	 */
	public void saveProperties() {
		// Preferences prefs = ModelService.getInstance().getPreferencesWorker()
		// .getPrefs();
		//
		//		prefs.put("areaLogHeight", String.valueOf(areaLogScrollPane.getSize() //$NON-NLS-1$
		// .getHeight()));
		//
		// // Saving tables info
		// ModelService.getInstance().getPreferencesWorker().saveTableInfo(
		// tableEmails);
		// ModelService.getInstance().getPreferencesWorker().saveTableInfo(
		// tableAccounts);

	}

	/**
	 * Creating actions
	 * 
	 * @param fr
	 *            Frame
	 */
	private void createActions(JFrame fr) {
		accountEditAction = new AccountEditAction(this);
		accountRefreshAction = new AccountRefreshAction(this);
		
		postsLoadAction = new PostsLoadAction(this);
		
		postNewAction = new PostNewAction(this);
		
		// helpAboutAction = new HelpAboutAction(this);
	}

	/**
	 * Creating all visual components
	 * 
	 * @param contentPane
	 *            Pane to add components to
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
				// updateViewLogAction(true);
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

		scrollPane = new JScrollPane();
		splitPaneLeftRight.setRightComponent(scrollPane);
		scrollPane.setPreferredSize(new Dimension(700, 300));
		createBlogsTable(scrollPane);

		contentPane.add(createStatusBar(), BorderLayout.SOUTH);

		createToolBar(contentPane);
	}

	private void createBlogsTable(JScrollPane scrollPane) {
		tablePostModel = new TablePostModel(this);
		TableSorterWithoutZeroColumn sorter = 
			new TableSorterWithoutZeroColumn(tablePostModel);
		sorter.setSortingStatus(3, TableSorterWithoutZeroColumn.DESCENDING);
		
		tablePosts = new JTable(sorter);
		sorter.setTableHeader(tablePosts.getTableHeader());
		tablePosts.setName("tableMsgs");
		
		assignRendererForColumnsInBlogsTable();
		
		tablePosts.addMouseListener(new MouseAdapter(){
			public void mouseClicked(final MouseEvent e){
				if (e.getButton() == MouseEvent.BUTTON1){
					// getting selected post and load its content to the editorPane
					Post post = MainForm.this.getSelectedPost();
					MainForm.this.textPanePost.setText(post.getBody());
					MainForm.this.textPanePost.setCaretPosition(0);
					MainForm.this.labelPostName.setText(post.getTitle());

					// format dates of the post
					DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
					
					MainForm.this.labelPostDateEdit.setText(df.format(post.getPublishDate().getTime()));
					MainForm.this.labelPostDatePublish.setText(df.format(post.getEditDate().getTime()));
//					MainForm.this.labelPostKeywords.setText(post.getKeywords().toString());
					
					// show the panel with post's information
					MainForm.this.panelInfoPost.setVisible(true);
				}
			}
		});
		scrollPane.setViewportView(tablePosts);
	}

	private void assignRendererForColumnsInBlogsTable() {
		TableColumn col;
		
		// Setting table props
		tablePosts.setRowSelectionAllowed(true);
		tablePosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Setting column name props
		col = tablePosts.getColumnModel().getColumn(
				TablePostModel.POST_COLUMN_NAME);
		DefaultTableCellRenderer namesRenderer = new DefaultTableCellRenderer();
		//namesRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		col.setCellRenderer(namesRenderer);

		// Setting column num props
		col = tablePosts.getColumnModel().getColumn(
				TablePostModel.POST_COLUMN_NUM);
		col.setMaxWidth(100);
		col.setPreferredWidth(50);
		DefaultTableCellRenderer numRenderer = new DefaultTableCellRenderer();
		col.setCellRenderer(numRenderer);
		
		// Setting column num props
		col = tablePosts.getColumnModel().getColumn(
				TablePostModel.POST_COLUMN_IS_DRAFT);
		col.setMaxWidth(50);
		col.setPreferredWidth(20);
		DefaultTableCellRenderer boolRenderer = new DefaultTableCellRenderer();
		boolRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		col.setCellRenderer(boolRenderer);
		
		// Setting column edited date props
		col = tablePosts.getColumnModel().getColumn(
				TablePostModel.POST_COLUMN_DATE_EDITED);
		col.setMaxWidth(170);
		col.setPreferredWidth(150);
		DateRenderer dateEditRenderer = new DateRenderer();
		dateEditRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		col.setCellRenderer(dateEditRenderer);

		// Setting column published date props
		col = tablePosts.getColumnModel().getColumn(
				TablePostModel.POST_COLUMN_DATE_PUBLISHED);
		col.setMaxWidth(170);
		col.setPreferredWidth(150);
		DateRenderer datePublishRenderer = new DateRenderer();
		datePublishRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		col.setCellRenderer(datePublishRenderer);

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

		button = new JButton(accountRefreshAction);
		button.setText(null);
		toolBar.add(button);
		
		button = new JButton(postsLoadAction);
		button.setText(null);
		toolBar.add(button);
		
		button = new JButton(postNewAction);
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
		// adding panel with blog info
		panelInfoPost = new JPanel(new BorderLayout());
		labelPostName = new JLabel();
		labelPostName.setFont(new Font("Serif", Font.BOLD, 16));
		panelInfoPost.add(labelPostName, BorderLayout.NORTH);
		
		// adding panel to output information about post dates (publish and edit)
		JPanel panelForPostDates = new JPanel(new FlowLayout());
		labelPostDateEdit = new JLabel();
		panelForPostDates.add(new JLabel("Edit date:"));
		panelForPostDates.add(labelPostDateEdit);
		panelForPostDates.add(new JLabel("Publish date:"));
		labelPostDatePublish = new JLabel();
		panelForPostDates.add(labelPostDatePublish);
		panelInfoPost.add(panelForPostDates, BorderLayout.WEST);

//		labelPostKeywords = new JLabel();
//		panelInfoPost.add(labelPostKeywords	, BorderLayout.SOUTH);
		panelInfoPost.setVisible(false);
		
		MouseListener popupListener = new PopupListener(PopupFactory
				.getEditPopup());
		
		// adding editor pane to output post body
		textPanePost = new JTextPaneZoom();
		textPanePost.setEditable(false);
		textPanePost.setContentType("text/html");
		textPanePost.setBackground(Color.WHITE);
		textPanePost.addMouseListener(popupListener);

		JScrollPane areaLogScrollPane = new JScrollPane(textPanePost);
		areaLogScrollPane.setPreferredSize(new Dimension(700, 300));
		areaLogScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaLogScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		areaLogScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory
						.createTitledBorder(""), BorderFactory
						.createEmptyBorder(0, 0, 0, 0)), areaLogScrollPane
						.getBorder()));
		
		HyperlinkListener hyperlinkListener = new ActivatedHyperlinkListener();
		textPanePost.addHyperlinkListener(hyperlinkListener);
		TextPaneZoomMouseListener keyListener = new TextPaneZoomMouseListener(textPanePost, areaLogScrollPane);
		textPanePost.addMouseWheelListener(keyListener);
		textPanePost.addKeyListener(keyListener);
		    
		panel.add(panelInfoPost, BorderLayout.NORTH);
		panel.add(areaLogScrollPane, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Creating components for displaying account with blogs
	 * 
	 * @param scrollPane
	 *            Pane to add components to
	 */
	private void createAccountsTree(JScrollPane scrollPane) {
		treeModelBlogs = new BlogsTreeDataModel();
		treeBlogs = new JTree(treeModelBlogs) {
			public String convertValueToText(Object value, boolean selected,
					boolean expanded, boolean leaf, int row, boolean hasFocus) {
				if (value instanceof Account) {
					Account account = (Account) value;
					return account.getLogin();
				} else if (value instanceof Blog) {
					Blog blog = (Blog) value;
					return blog.getName();
				} else if (value instanceof String){
					return (String)value;
				}
				return "";
			}
		};
		TreeCellAccountRenderer treeCellAccountRenderer = new TreeCellAccountRenderer(); 
		treeBlogs.setCellRenderer(treeCellAccountRenderer);
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
				// System.err.println(Messages
				//						.getString("MainForm.UnexpectedLFValue") //$NON-NLS-1$
				// + LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				//				System.err.println(Messages.getString("MainForm.NoClassForLF") //$NON-NLS-1$
				// + lookAndFeel);
				//				System.err.println(Messages.getString("MainForm.LibLFThere")); //$NON-NLS-1$
				//				System.err.println(Messages.getString("MainForm.UseDefLF")); //$NON-NLS-1$
			} catch (UnsupportedLookAndFeelException e) {
				//				System.err.println(Messages.getString("MainForm.UnableUseLF") //$NON-NLS-1$
				// + lookAndFeel
				//						+ Messages.getString("MainForm.OnThisPlatform")); //$NON-NLS-1$
				//				System.err.println(Messages.getString("MainForm.UseDefLF")); //$NON-NLS-1$
			} catch (Exception e) {
				//				System.err.println(Messages.getString("MainForm.UnableGetLF") //$NON-NLS-1$
				// + lookAndFeel
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
		if (textPanePost.getText().equals("")) //$NON-NLS-1$
			textPanePost.setText(String.format("%tT", now) //$NON-NLS-1$
					+ ": " //$NON-NLS-1$
					+ message);
		else
			textPanePost.setText(textPanePost.getText() + Messages.NEW_LINE
					+ String.format("%tT", now) //$NON-NLS-1$
					+ ": " //$NON-NLS-1$
					+ message);
		textPanePost.setSelectionStart(textPanePost.getText().length() - 1);
	}

	/**
	 * Getting log string
	 * 
	 * @return log information
	 */
	public String getLogText() {
		return textPanePost.getText();
	}

	/**
	 * Getting table object that corresponds to emails table
	 * 
	 * @return Emails table
	 */
	public JTable getTableEmails() {
		return tableEmails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IAccountListener#accountCreated(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public void accountCreated(Account account) {
		treeModelBlogs.addAccount(account);
		treeBlogs.updateUI();

		// Enabling actions
		accountRefreshAction.setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IAccountListener#accountEdited(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public void accountEdited(Account account) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeua.jdesktopblogger.domain.IAccountListener#accountRefreshed(ua.
	 * jdesktopblogger.domain.Account)
	 */
	@Override
	public void accountRefreshed(Account account) {
		treeBlogs.updateUI();
		
		postsLoadAction.setEnabled(true);
		postNewAction.setEnabled(true);
	}

	/**
	 * Getting selected account from the tree
	 * 
	 * @return Selected account or <code>null</code>
	 */
	public Account getSelectedAccount() {
		TreePath path = treeBlogs.getSelectionPath();
		if ((path != null) && (path.getPathCount()>1)) {
			return (Account) path.getPath()[1];
		} else {
			return null;
		}
	}

	/**
	 * Getting selected blog from the tree
	 * 
	 * @return Selected blog or <code>null</code>
	 */
	public Blog getSelectedBlog(){
		TreePath path = treeBlogs.getSelectionPath();
		if ((path != null) && (path.getPathCount()>2)) {
			return (Blog) path.getPath()[2];
		} else {
			return null;
		}
	}

	/**
	 * Getting selected post from the table
	 * 
	 * @return Selected post or <code>null</code>
	 */
	public Post getSelectedPost(){
		int iSelPost = tablePosts.getSelectedRow();
		return (Post)tablePosts.getModel().getValueAt(iSelPost, TablePostModel.POST_COLUMN_WHOLE_POST);
	}
	
	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.domain.IPostListener#postsLoaded(ua.jdesktopblogger.domain.Blog)
	 */
	@Override
	public void postsLoaded(Blog blog) {
		tablePostModel.fireTableDataChanged();
	}
	/**
	 * select first blog of the selected account
	 */
	public void openSelectedAccountTreeNode(){
		// get selected path
		TreePath path = treeBlogs.getSelectionPath();
		if ((path != null) && (path.getPathCount()>1)) {
			// get selected account
			Account account = (Account) path.getPath()[1];
			if (account.getBlogs().size() > 0){
				// create new path by adding first child to the end of the path
				TreePath newPath = path.pathByAddingChild(account.getBlogs().toArray()[0]);
				treeBlogs.setSelectionPath(newPath);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.domain.IPostListener#postPublished(ua.jdesktopblogger.domain.Blog, ua.jdesktopblogger.domain.Post)
	 */
	@Override
	public void postPublished(Blog blog, Post publishedPost) {
		tablePostModel.fireTableDataChanged();		
	}

	
}
