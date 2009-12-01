package ua.jdesktopblogger.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JScrollPane;

/**
 * Listener for Ctrl key and mouse scrolling event. doing scaling of the
 * JTextPane content
 * 
 * @author Alex Skosyr
 * 
 */
public class TextPaneZoomMouseListener implements MouseWheelListener,
		KeyListener {

	private JTextPaneZoom textPane;
	private JScrollPane scrollPane;

	private boolean ctrlPressed = false;

	public TextPaneZoomMouseListener(JTextPaneZoom textPane, JScrollPane scrollPane) {
		this.textPane = textPane;
		this.scrollPane = scrollPane;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (ctrlPressed) {

			// getting old scale value
			double newScale = textPane.getScale() + notches*0.1;
			// setting new scale value
			textPane.setScale(newScale);
			scrollPane.invalidate();
			scrollPane.validate();
			scrollPane.updateUI();
			textPane.invalidate();
			textPane.validate();
			textPane.updateUI();
			textPane.repaint();
//			textPane.updateUI();
		} else {
			int i = textPane.getCaretPosition();
			i += notches * 40;
//			textPane.getCaret().
			if (i < 0) {
				i = 0;
			} else if (i >= textPane.getText().length()) {
				textPane.setCaretPosition(textPane.getText().length());
			} else {
				textPane.setCaretPosition(i);
			}
			
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 17){
			ctrlPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 17){
			ctrlPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
