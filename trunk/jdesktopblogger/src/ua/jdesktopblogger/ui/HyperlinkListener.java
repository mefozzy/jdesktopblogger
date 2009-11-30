package ua.jdesktopblogger.ui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
/**
 * Listener that open a hyperlink, that has been clicked on textPane
 * 
 * @author Alex Skosyr
 *
 */
class ActivatedHyperlinkListener implements HyperlinkListener {

	public ActivatedHyperlinkListener() {
	}

	public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
		HyperlinkEvent.EventType type = hyperlinkEvent.getEventType();
		final URL url = hyperlinkEvent.getURL();
		if (type == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println("Activated" + url);
			if( !java.awt.Desktop.isDesktopSupported() ) {

	            System.err.println( "Desktop is not supported (fatal)" );
	            System.exit( 1 );
	        }
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

	            System.err.println( "Desktop doesn't support the browse action (fatal)" );
	            return;
	        }
			try {
				desktop.browse( url.toURI() );
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}