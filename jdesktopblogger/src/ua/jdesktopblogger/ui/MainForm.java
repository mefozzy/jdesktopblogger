package ua.jdesktopblogger.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
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
import ua.jdesktopblogger.ui.actions.AccountDeleteAction;
import ua.jdesktopblogger.ui.actions.AccountEditAction;
import ua.jdesktopblogger.ui.actions.AccountNewAction;
import ua.jdesktopblogger.ui.actions.AccountRefreshAction;
import ua.jdesktopblogger.ui.actions.FileExitAction;
import ua.jdesktopblogger.ui.actions.HelpAboutAction;
import ua.jdesktopblogger.ui.actions.PostDeleteAction;
import ua.jdesktopblogger.ui.actions.PostEditAction;
import ua.jdesktopblogger.ui.actions.PostNewAction;
import ua.jdesktopblogger.ui.actions.PostsLoadAction;
import ua.jdesktopblogger.ui.actions.ViewShowHideAppAction;
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

	//private JProgressBar progressBar;

	//private JButton cancelButton;

	private JLabel labelStatusBar;

	private JLabel labelLoading;

	private TrayIcon trayIcon;

	private JTree treeBlogs;

	private BlogsTreeDataModel treeModelBlogs;

	private TablePostModel tablePostModel;

	private JLabel labelPostName;

	private JLabel labelPostDatePublish;

	private JLabel labelPostDateEdit;

	private JLabel labelPostUrl;

	private JPanel panelInfoPost;
	// //////////////////////////////////////////////////////////////////////////////////

	private AccountNewAction accountNewAction;
	private AccountEditAction accountEditAction;
	private AccountDeleteAction accountDeleteAction;
	private AccountRefreshAction accountRefreshAction;
	private PostsLoadAction postsLoadAction;

	private PostEditAction postEditAction;
	private PostNewAction postNewAction;
	private PostDeleteAction postDeleteAction;

	private ViewShowHideAppAction viewShowHideAppAction;
	
	private FileExitAction fileExitAction;
	private HelpAboutAction helpAboutAction;

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

		createMenu(frame);
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
			trayIcon = new TrayIcon(createImageIcon(
					"images/jdesktopblogger24.png").getImage(), //$NON-NLS-1$ 
					appTitle);

			trayIcon.setPopupMenu(PopupFactory
					.getGeneralPopupAwt(viewShowHideAppAction
			, null, helpAboutAction, fileExitAction));

			trayIcon.setImageAutoSize(true);

			trayIcon.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if ((e.getClickCount() > 1) && (e.getClickCount() < 3)) {
						viewShowHideAppAction.actionPerformed(null);
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
		loadSavedAccounts();

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
	 * Loading all saved accounts to the tree
	 */
	private void loadSavedAccounts() {
		try {
			Collection<Account> accounts = ServiceFactory.getDefaultFactory()
					.getAccountService().loadSavedAccounts();

			treeModelBlogs.removeAllAccounts();

			for (Account account : accounts) {
				treeModelBlogs.addAccount(account);
			}
			
			// Selecting first account if exists
			if (accounts.size() > 0) {
				TreePath path = new TreePath(treeBlogs.getModel().getRoot());
				path = path.pathByAddingChild(accounts.iterator().next());
				treeBlogs.setSelectionPath(path);
				
				updateAccountActions(true);
			}
			
			treeBlogs.updateUI();

		} catch (AccountIOException e) {
			e.printStackTrace();
		}
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
		fileExitAction = new FileExitAction(this);
		
		accountNewAction = new AccountNewAction(this);
		accountEditAction = new AccountEditAction(this);
		accountDeleteAction = new AccountDeleteAction(this);
		accountRefreshAction = new AccountRefreshAction(this);

		postsLoadAction = new PostsLoadAction(this);

		postNewAction = new PostNewAction(this);
		postDeleteAction = new PostDeleteAction(this);
		postEditAction = new PostEditAction(this);

		viewShowHideAppAction = new ViewShowHideAppAction(this);
		helpAboutAction = new HelpAboutAction(this);
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

	/**
	 * create tree for list of blogs and added listener to it
	 * 
	 * @param scrollPane
	 *            - scroll pane to add jTree
	 */
	private void createBlogsTable(JScrollPane scrollPane) {
		tablePostModel = new TablePostModel(this);

		TableSorterWithoutZeroColumn sorter = new TableSorterWithoutZeroColumn(
				tablePostModel);
		sorter.setSortingStatus(3, TableSorterWithoutZeroColumn.DESCENDING);

		tablePosts = new JTable(sorter);
		sorter.setTableHeader(tablePosts.getTableHeader());
		tablePosts.setName("tableMsgs");

		assignRendererForColumnsInBlogsTable();

		tablePosts.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					updatePostInfoPanel();
					updatePostActions(true, true);
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
		// namesRenderer.setHorizontalAlignment(SwingConstants.CENTER);
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
	 * Creating menus
	 * 
	 * @param fr
	 *            Frame
	 */
	private void createMenu(JFrame fr) {
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();
		
		fr.setJMenuBar(menuBar);

		// ================================================
		// ACCOUNT MENU

		menu = new JMenu("Account");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);

		menuItem = new JMenuItem(accountNewAction);
		menu.add(menuItem);

		menuItem = new JMenuItem(accountEditAction);
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem(accountDeleteAction);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem(accountRefreshAction);
		menu.add(menuItem);
		
		// ================================================
		// POST MENU

		menu = new JMenu("Blog");
		menu.setMnemonic(KeyEvent.VK_B);
		menuBar.add(menu);
		
		menuItem = new JMenuItem(postsLoadAction);
		menu.add(menuItem);
		
		menu.addSeparator();

		menuItem = new JMenuItem(postNewAction);
		menu.add(menuItem);

		menuItem = new JMenuItem(postEditAction);
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem(postDeleteAction);
		menu.add(menuItem);
		
		// ===============================================
		// Help MENU

		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);

		menuItem = new JMenuItem(helpAboutAction);
		menu.add(menuItem);
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

		button = new JButton(accountNewAction);
		button.setText(null);
		toolBar.add(button);

		button = new JButton(accountEditAction);
		button.setText(null);
		toolBar.add(button);

		button = new JButton(accountDeleteAction);
		button.setText(null);
		toolBar.add(button);

		toolBar.addSeparator();

		button = new JButton(accountRefreshAction);
		button.setText(null);
		toolBar.add(button);

		toolBar.addSeparator();

		button = new JButton(postsLoadAction);
		button.setText(null);
		toolBar.add(button);

		toolBar.addSeparator();

		button = new JButton(postNewAction);
		button.setText(null);
		toolBar.add(button);

		button = new JButton(postEditAction);
		button.setText(null);
		toolBar.add(button);

		button = new JButton(postDeleteAction);
		button.setText(null);
		toolBar.add(button);
		
		toolBar.add(Box.createHorizontalGlue());
		
		button = new JButton(viewShowHideAppAction);
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

	/**
	 * creating text pane for post content outputting and panel with detailed
	 * post information
	 * 
	 * @return created area
	 */
	private JComponent createEntryViewArea() {
		JPanel panel = new JPanel(new BorderLayout());
		// adding panel with blog info
		panelInfoPost = new JPanel(new BorderLayout());
		labelPostName = new JLabel();
		labelPostName.setFont(new Font("Serif", Font.BOLD, 16));
		panelInfoPost.add(labelPostName, BorderLayout.NORTH);

		// adding panel to output information about post dates (publish and
		// edit)
		JPanel panelForPostDates = new JPanel(new FlowLayout());
		createDatesInfoPanel(panelForPostDates);
		panelInfoPost.add(panelForPostDates, BorderLayout.WEST);
		panelInfoPost.setVisible(false);

		MouseListener popupListener = new PopupListener(PopupFactory
				.getEditPopup());

		JPanel panelForUrl = new JPanel(new FlowLayout(FlowLayout.LEFT));

		createURLInfoPanel(panelForUrl);

		panelInfoPost.add(panelForUrl, BorderLayout.SOUTH);

		// adding editor pane to output post body
		textPanePost = new JTextPaneZoom();
		JScrollPane areaLogScrollPane = createTextPanePostPanel(popupListener);

		panel.add(panelInfoPost, BorderLayout.NORTH);
		panel.add(areaLogScrollPane, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * creating text pane for post content
	 * 
	 * @param popupListener
	 *            - popup listener for text pane
	 * @return - scroll pane with all components
	 */
	private JScrollPane createTextPanePostPanel(MouseListener popupListener) {
		textPanePost.setEditable(false);
		textPanePost.setContentType("text/html");
		textPanePost.setBackground(Color.WHITE);
		textPanePost.addMouseListener(popupListener);

		JScrollPane areaLogScrollPane = new JScrollPane(textPanePost);
		areaLogScrollPane.setPreferredSize(new Dimension(700, 300));
		areaLogScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaLogScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		areaLogScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory
						.createTitledBorder(""), BorderFactory
						.createEmptyBorder(0, 0, 0, 0)), areaLogScrollPane
						.getBorder()));

		HyperlinkListener hyperlinkListener = new ActivatedHyperlinkListener();
		textPanePost.addHyperlinkListener(hyperlinkListener);
		TextPaneZoomMouseListener keyListener = new TextPaneZoomMouseListener(
				textPanePost, areaLogScrollPane);
		textPanePost.addMouseWheelListener(keyListener);
		textPanePost.addKeyListener(keyListener);
		return areaLogScrollPane;
	}

	/**
	 * creating panel for information about post dates (publish and edit)
	 * 
	 * @param panelForPostDates
	 *            - panel to fill with components
	 */
	private void createDatesInfoPanel(JPanel panelForPostDates) {
		labelPostDateEdit = new JLabel();
		panelForPostDates.add(new JLabel("Edit date:"));
		panelForPostDates.add(labelPostDateEdit);
		panelForPostDates.add(new JLabel("Publish date:"));
		labelPostDatePublish = new JLabel();
		panelForPostDates.add(labelPostDatePublish);
	}

	/**
	 * creating panel for information about URL of the post
	 * 
	 * @param panelForUrl
	 *            - panel to add components
	 */
	private void createURLInfoPanel(JPanel panelForUrl) {
		panelForUrl.add(new JLabel("Url of the post:"));
		labelPostUrl = new JLabel();
		labelPostUrl.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelPostUrl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Post post = MainForm.this.getSelectedPost();
					if (post != null) {
						if (!java.awt.Desktop.isDesktopSupported()) {

							System.err
									.println("Desktop is not supported (fatal)");
							System.exit(1);
						}
						java.awt.Desktop desktop = java.awt.Desktop
								.getDesktop();
						if (!desktop
								.isSupported(java.awt.Desktop.Action.BROWSE)) {

							System.err
									.println("Desktop doesn't support the browse action (fatal)");
							return;
						}
						try {
							desktop.browse(new URI(post.getUrl()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		panelForUrl.add(labelPostUrl);
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
			private static final long serialVersionUID = 1L;

			public String convertValueToText(Object value, boolean selected,
					boolean expanded, boolean leaf, int row, boolean hasFocus) {
				if (value instanceof Account) {
					Account account = (Account) value;
					return account.getLogin();
				} else if (value instanceof Blog) {
					Blog blog = (Blog) value;
					return blog.getName();
				} else if (value instanceof String) {
					return (String) value;
				}
				return "";
			}
		};
		treeBlogs.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					
					TreePath path = treeBlogs.getSelectionPath();
					if (path.getPath().length <= 1) {
						// root is selected
						updateAccountActions(false);
						updatePostActions(false, false);
					} else if (path.getPath().length == 2) {
						// Account is selected
						updateAccountActions(true);
						updatePostActions(false, false);
					} else {
						// Blog is selected
						tablePostModel.fireTableDataChanged();
						updatePostInfoPanel();

						updateAccountActions(true);
						updatePostActions(false, true);
					}
				}
			}
		});
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
		loadSavedAccounts();
		
		Account addedAccount = treeModelBlogs.getAccountByLogin(account.getLogin());
		
		TreePath path = new TreePath(treeBlogs.getModel().getRoot());
		path = path.pathByAddingChild(addedAccount);
		treeBlogs.setSelectionPath(path);
		
		updateAccountActions(true);
		updatePostActions(false, false);
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
		accountCreated(account);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IAccountListener#accountDeleted(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public void accountDeleted(Account account) {
		loadSavedAccounts();
		
		TreePath path = new TreePath(treeBlogs.getModel().getRoot());
		if (treeModelBlogs.getSize() > 0) {
			path = path.pathByAddingChild(treeBlogs.getModel().getChild(
					treeBlogs.getModel().getRoot(), 0));
			updateAccountActions(true);
		} else {
			updateAccountActions(false);
		}
		treeBlogs.setSelectionPath(path);
		
		tablePostModel.fireTableDataChanged();
		updatePostInfoPanel();
		
		updatePostActions(false, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeua.jdesktopblogger.domain.IAccountListener#accountRefreshed(ua.
	 * jdesktopblogger.domain.Account)
	 */
	@Override
	public void accountRefreshed(Account account) {		
		selectFirstBlogOfSelectedAccount();
		treeBlogs.updateUI();
	}

	/**
	 * Getting selected account from the tree
	 * 
	 * @return Selected account or <code>null</code>
	 */
	public Account getSelectedAccount() {
		TreePath path = treeBlogs.getSelectionPath();
		if ((path != null) && (path.getPathCount() > 1)) {
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
	public Blog getSelectedBlog() {
		TreePath path = treeBlogs.getSelectionPath();
		if ((path != null) && (path.getPathCount() > 2)) {
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
	public Post getSelectedPost() {
		int iSelPost = tablePosts.getSelectedRow();
		if (iSelPost == -1)
			return null;
		return (Post) tablePosts.getModel().getValueAt(iSelPost,
				TablePostModel.POST_COLUMN_WHOLE_POST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IPostListener#postsLoaded(ua.jdesktopblogger
	 * .domain.Blog)
	 */
	@Override
	public void postsLoaded(Blog blog) {
		tablePostModel.fireTableDataChanged();
	}

	/**
	 * select first blog of the selected account
	 */
	public void selectFirstBlogOfSelectedAccount() {
		// get selected path
		TreePath path = treeBlogs.getSelectionPath();
		if ((path != null) && (path.getPathCount() > 1)) {
			// get selected account
			Account account = (Account) path.getPath()[1];
			if (account.getBlogs().size() > 0) {
				// create new path by adding first child to the end of the path
				TreePath newPath = path.pathByAddingChild(account.getBlogs()
						.iterator().next());
				treeBlogs.setSelectionPath(newPath);
				
				updatePostActions(false, true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IPostListener#postPublished(ua.jdesktopblogger
	 * .domain.Blog, ua.jdesktopblogger.domain.Post)
	 */
	@Override
	public void postPublished(Blog blog, Post publishedPost) {
		tablePostModel.fireTableDataChanged();
		updatePostInfoPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IPostListener#postUpdated(ua.jdesktopblogger
	 * .domain.Blog, ua.jdesktopblogger.domain.Post)
	 */
	@Override
	public void postUpdated(Blog blog, Post publishedPost) {
		tablePostModel.fireTableDataChanged();
		updatePostInfoPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.domain.IPostListener#postDeleted(ua.jdesktopblogger
	 * .domain.Blog)
	 */
	@Override
	public void postDeleted(Blog blog) {
		tablePostModel.fireTableDataChanged();
		updatePostInfoPanel();
	}

	/**
	 * update information about selected post
	 */
	private void updatePostInfoPanel() {
		// getting selected post and load its content to the
		// editorPane
		Post post = getSelectedPost();
		if (post == null) {
			panelInfoPost.setVisible(false);
			textPanePost.setText("");

			updatePostActions(false, true);
			return;
		}
		textPanePost.setText(post.getBody());
		textPanePost.setCaretPosition(0);
		labelPostName.setText(post.getTitle());

		// format dates of the post
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT);

		labelPostDateEdit.setText(df.format(post.getPublishDate().getTime()));
		labelPostDatePublish.setText(df.format(post.getEditDate().getTime()));
		labelPostUrl.setText("<html><a href=\"" + post.getUrl() + "\">"
				+ post.getUrl() + "</a></html>");

		// show the panel with post's information
		panelInfoPost.setVisible(true);
	}

	/**
	 * Updating all actions that relate to the post
	 * @param enabled Specify if actions should be enabled or disabled
	 * @param enabledBlogRelated Specify if actions related to the whole blog should be enabled
	 */
	private void updatePostActions(boolean enabled, boolean enabledBlogRelated) {
		postDeleteAction.setEnabled(enabled);
		postEditAction.setEnabled(enabled);
		
		postNewAction.setEnabled(enabledBlogRelated);
		postsLoadAction.setEnabled(enabledBlogRelated);
	}
	
	/**
	 * Updating all actions that relate to the account
	 * @param enabled Specify if actions should be enabled or disabled
	 */
	private void updateAccountActions(boolean enabled) {
		accountEditAction.setEnabled(enabled);
		accountDeleteAction.setEnabled(enabled);
		accountRefreshAction.setEnabled(enabled);
	}

}
