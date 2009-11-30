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
import ua.jdesktopblogger.Messages;
import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.excetions.AccountIOException;
import ua.jdesktopblogger.services.ServiceFactory;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class NewPostForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JButton okButton;

	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public NewPostForm() {
		
		setResizable(false);
		setTitle("New Post");
		setBounds(100, 100, 678, 432);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));

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
		
		JLabel lbTitle = new JLabel("Title");
		contentPanel.add(lbTitle, "2, 2");
		
		JTextField fieldTitle = new JTextField();
		lbTitle.setLabelFor(fieldTitle);
		contentPanel.add(fieldTitle, "4, 2");

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
		
	}

}
