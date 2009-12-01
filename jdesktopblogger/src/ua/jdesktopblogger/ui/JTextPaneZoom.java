package ua.jdesktopblogger.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextPane;
/**
 * class that is inherited from TextPane and add zooom functionality
 * 
 * @author Alex Skosyr
 *
 */
public class JTextPaneZoom extends JTextPane {

	private double scale = 1;
	
	private static final long serialVersionUID = 1L;

	
	
	public JTextPaneZoom() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D gd = (Graphics2D)g;
		gd.scale(scale, scale);
		super.paint(g);
	}
	
	public void repaint(int x, int y, int width, int height) {
		super.repaint(0, 0, getWidth(), getHeight());
	}
	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

}
