package ua.jdesktopblogger.excetions;


/**
 * Exception that occurs on account io operations
 * 
 * @author Yuriy Tkach
 */
public class AccountIOException extends Exception {

	private static final long serialVersionUID = 1L;

	public AccountIOException() {
	}

	public AccountIOException(String message) {
		super(message);
	}

	public AccountIOException(Throwable cause) {
		super(cause);
	}

	public AccountIOException(String message, Throwable cause) {
		super(message, cause);
	}

}
