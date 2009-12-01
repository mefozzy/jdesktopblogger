package ua.jdesktopblogger.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import ua.cn.yet.common.ui.popup.PopupFactory;
import ua.cn.yet.common.ui.popup.PopupListener;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.ui.actions.newpost.PublishPostAction;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Form for creating new post
 * 
 * @author Yuriy Tkach
 */
public class NewPostForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JButton okButton;

	private JButton cancelButton;

	private JTextField fieldTitle;

	private JCheckBox fieldDraft;

	private JTextPane editBody;

	private PublishPostAction publishPostAction;

	private Account account;

	private JComboBox comboBlog;

	private MainForm mainForm;

	private Post selectedPost;

	/**
	 * Create the dialog.
	 */
	public NewPostForm(MainForm mainForm, Account account, Blog selectedBlog,
			Post selectedPost) {

		this.mainForm = mainForm;
		this.account = account;
		this.selectedPost = selectedPost;

		if (selectedPost == null) {
			setTitle("New Post");
		} else {
			setTitle("Post - " + selectedPost.getTitle());
		}
		setBounds(100, 100, 678, 432);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, }));

		addKeyListener(new KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancelButton.doClick();
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					okButton.doClick();
			}
		});

		createActions(selectedPost);

		MouseListener popupListener = new PopupListener(PopupFactory
				.getEditPopup());

		JLabel lbBlog = new JLabel("Blog");
		contentPanel.add(lbBlog, "2, 2");

		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		BlogComboWrapper selected = null;
		for (Blog blog : account.getBlogs()) {
			BlogComboWrapper wrapper = new BlogComboWrapper(blog);
			if (selected == null) {
				selected = wrapper;
			}
			if (blog.equals(selectedBlog)) {
				selected = wrapper;
			}
			comboBoxModel.addElement(wrapper);
		}
		comboBlog = new JComboBox(comboBoxModel);
		comboBlog.setEditable(false);
		lbBlog.setLabelFor(comboBlog);
		comboBlog.setSelectedItem(selected);
		contentPanel.add(comboBlog, "4, 2");
		if (selectedPost != null) {
			comboBlog.setEnabled(false);
		}

		JLabel lbTitle = new JLabel("Title");
		contentPanel.add(lbTitle, "2, 4");

		fieldTitle = new JTextField();
		lbTitle.setLabelFor(fieldTitle);
		fieldTitle.addMouseListener(popupListener);
		fieldTitle.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				updateFormCaptionOnPostTitleEntry();
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		contentPanel.add(fieldTitle, "4, 4");
		if (selectedPost != null) {
			fieldTitle.setText(selectedPost.getTitle());
		}

		JLabel lbDraft = new JLabel("Draft");
		contentPanel.add(lbDraft, "2, 6");

		fieldDraft = new JCheckBox();
		lbDraft.setLabelFor(fieldDraft);
		fieldDraft.addMouseListener(popupListener);
		fieldDraft.setSelected(true);
		contentPanel.add(fieldDraft, "4, 6");
		if (selectedPost != null) {
			fieldDraft.setSelected(selectedPost.isDraft());
		}

		createEditor();
		if (selectedPost != null) {
			editBody.setText(selectedPost.getBody());
		}

		JPanel buttonPane = new JPanel();
		contentPanel.add(buttonPane, "4, 10");
		buttonPane.setLayout(new FlowLayout());

		okButton = new JButton(publishPostAction);
		buttonPane.add(okButton);
		// getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic(KeyEvent.VK_CANCEL);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setText("Cancel");
		buttonPane.add(cancelButton);

	}

	/**
	 * Updating form's caption when post's title was entered
	 */
	protected void updateFormCaptionOnPostTitleEntry() {
		String text = fieldTitle.getText();
		String titlePrefix = (selectedPost == null) ? "New " : "";

		if (!text.isEmpty()) {
			this.setTitle(titlePrefix + "Post - " + text);
		} else {
			this.setTitle(titlePrefix + "Post");
		}
	}

	/**
	 * Creating actions that are used on buttons
	 * 
	 * @param selectedPost
	 */
	private void createActions(Post selectedPost) {
		publishPostAction = PublishPostAction.createAction(this,
				(selectedPost != null), account, mainForm, mainForm);
	}

	/**
	 * Creating editor pane
	 */
	private void createEditor() {
		JPanel panel = new JPanel(new BorderLayout());

		// createEditorToolbar(panel);

		editBody = new JTextPane();
		editBody.setEditable(true);
		editBody.setContentType("text/html");
		editBody.setBackground(Color.WHITE);

		MouseListener popupListener = new PopupListener(PopupFactory
				.getEditPopup());

		editBody.addMouseListener(popupListener);

		JScrollPane areaLogScrollPane = new JScrollPane(editBody);
		areaLogScrollPane.setPreferredSize(new Dimension(700, 300));
		areaLogScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaLogScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory
						.createTitledBorder(""), BorderFactory
						.createEmptyBorder(0, 0, 0, 0)), areaLogScrollPane
						.getBorder()));
		panel.add(areaLogScrollPane);

		contentPanel.add(panel, new CellConstraints(2, 8, 3, 1));
	}

	/**
	 * Getting blog object that is selected in the combo box
	 * 
	 * @return selected blog object
	 */
	public Blog getSelectedBlog() {
		if (comboBlog.getSelectedItem() != null) {
			return ((BlogComboWrapper) comboBlog.getSelectedItem()).getBlog();
		} else {
			return null;
		}
	}

	/**
	 * Getting edited post or <code>null</code> if not all fields are set
	 * 
	 * @return post object or <code>null</code>
	 */
	public Post getEditedPost() {
		if (fieldTitle.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Please, enter title for the post", mainForm
							.getAppTitle(), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		Post post = (selectedPost == null) ? new Post() : selectedPost;
		post.setTitle(fieldTitle.getText());
		post.setDraft(fieldDraft.isSelected());
		post.setBody(editBody.getText());
		return post;
	}

	// /**
	// * Creating editor toolbar
	// *
	// * @param panel
	// * Panel where toolbar should be added
	// */
	// private void createEditorToolbar(JPanel panel) {
	// JToolBar toolbar = new JToolBar();
	// toolbar.setAutoscrolls(true);
	// toolbar.setFloatable(false);
	// panel.add(toolbar, BorderLayout.NORTH);
	//
	// JButton button;
	// button = new JButton(new AbstractAction("Bold", MainForm
	// .createImageIcon("images/camera_test.png")) {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// StyledDocument sd = editBody.getStyledDocument();
	// Style style = editBody.addStyle("Bold", null);
	// StyleConstants.setBold(style, true);
	//
	// sd.setCharacterAttributes(editBody.getSelectionStart(),
	// editBody.getSelectionEnd()-editBody.getSelectionStart(),
	// editBody.getStyle("Bold"),
	// false);
	// }
	// });
	// button.setText(null);
	// toolbar.add(button);
	// }

}
