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

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ua.cn.yet.common.ui.popup.PopupFactory;
import ua.cn.yet.common.ui.popup.PopupListener;
import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.excetions.AccountIOException;
import ua.jdesktopblogger.services.ServiceFactory;
import ua.jdesktopblogger.ui.actions.newpost.PublishPostAction;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class NewPostForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JButton okButton;

	private JButton cancelButton;

	private JTextField fieldTitle;

	// private JTextField fieldKeywords;

	private JCheckBox fieldDraft;

	private JTextPane editBody;

	private PublishPostAction publishPostAction;

	/**
	 * Create the dialog.
	 */
	public NewPostForm() {

		setTitle("New Post");
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
		
		createActions();

		MouseListener popupListener = new PopupListener(PopupFactory
				.getEditPopup());

		JLabel lbTitle = new JLabel("Title");
		contentPanel.add(lbTitle, "2, 2");

		fieldTitle = new JTextField();
		lbTitle.setLabelFor(fieldTitle);
		fieldTitle.addMouseListener(popupListener);
		final JFrame form = this;
		fieldTitle.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				String text = ((JTextField) e.getComponent()).getText();
				
				if (!text.isEmpty()) {
					form.setTitle("New Post - " + text);
				} else {
					form.setTitle("New Post");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		contentPanel.add(fieldTitle, "4, 2");

		// JLabel lbKeywords = new JLabel("Keywords");
		// contentPanel.add(lbKeywords, "2, 4");
		//
		// fieldKeywords = new JTextField();
		// lbKeywords.setLabelFor(fieldKeywords);
		// fieldKeywords.addMouseListener(popupListener);
		// contentPanel.add(fieldKeywords, "4, 4");

		JLabel lbDraft = new JLabel("Draft");
		contentPanel.add(lbDraft, "2, 6");

		fieldDraft = new JCheckBox();
		lbDraft.setLabelFor(fieldDraft);
		fieldDraft.addMouseListener(popupListener);
		fieldDraft.setSelected(true);
		contentPanel.add(fieldDraft, "4, 6");

		createEditor();

		JPanel buttonPane = new JPanel();
		contentPanel.add(buttonPane, "4, 10");
		buttonPane.setLayout(new FlowLayout());

		okButton = new JButton(publishPostAction);
		buttonPane.add(okButton);
		//getRootPane().setDefaultButton(okButton);

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
	 * Creating actions that are used on buttons
	 */
	private void createActions() {
		publishPostAction = new PublishPostAction(this);
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

	/**
	 * Remembering account information and closing dialog if successful
	 */
	protected void rememberAccountInfo() {

	}

}
