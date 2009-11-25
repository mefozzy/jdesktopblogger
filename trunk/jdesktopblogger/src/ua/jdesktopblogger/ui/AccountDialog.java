package ua.jdesktopblogger.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ua.cn.yet.common.ui.popup.PopupFactory;
import ua.cn.yet.common.ui.popup.PopupListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AccountDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;

	private JButton okButton;

	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public AccountDialog(JFrame owner) {
		super(owner);
		setModal(true);
		setResizable(false);
		setTitle("Account information");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 145);
		setLocationRelativeTo(owner);
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
				FormFactory.RELATED_GAP_ROWSPEC, }));

		addKeyListener(new KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancelButton.doClick();
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					okButton.doClick();
			}
		});

		MouseListener popupListener = new PopupListener(PopupFactory
				.getEditPopup());

		JLabel lblLogin = new JLabel("Login:");
		contentPanel.add(lblLogin, "2, 2, right, default");

		textField = new JTextField();
		contentPanel.add(textField, "4, 2, fill, default");
		textField.setColumns(10);
		textField.addKeyListener(getKeyListeners()[0]);
		textField.addMouseListener(popupListener);

		lblLogin.setLabelFor(textField);

		JLabel lblPassword = new JLabel("Password:");
		contentPanel.add(lblPassword, "2, 4, right, default");

		passwordField = new JPasswordField();
		passwordField.addKeyListener(getKeyListeners()[0]);
		passwordField.addMouseListener(popupListener);
		contentPanel.add(passwordField, "4, 4, fill, default");

		lblPassword.setLabelFor(passwordField);

		JPanel buttonPane = new JPanel();
		contentPanel.add(buttonPane, "4, 6");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		//okButton.addKeyListener(getKeyListeners()[0]);
		okButton.setMnemonic(KeyEvent.VK_ACCEPT);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				rememberAccountInfo();
			}
		});

		cancelButton = new JButton("Cancel");
		//cancelButton.addKeyListener(getKeyListeners()[0]);
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
	 * Remembering account information and closing dialog if successful
	 */
	protected void rememberAccountInfo() {
		if (textField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Account login cannot be empty", this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (passwordField.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this,
					"Account password cannot be empty", this.getTitle(),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// TODO: Add account saving code
		JOptionPane.showMessageDialog(this, "Save account action");
		dispose();
	}

}
