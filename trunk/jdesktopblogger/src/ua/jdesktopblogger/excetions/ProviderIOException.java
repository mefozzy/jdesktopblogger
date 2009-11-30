package ua.jdesktopblogger.excetions;

/**
 * Exception is thrown if IO error occurs
 * 
 * @author Yuriy Tkach
 */
public class ProviderIOException extends BlogServiceException {

	private static final long serialVersionUID = 1L;

	public ProviderIOException() {
	}

	public ProviderIOException(String message) {
		super(message);
	}

	public ProviderIOException(Throwable cause) {
		super(cause);
	}

	public ProviderIOException(String message, Throwable cause) {
		super(message, cause);
	}

}
