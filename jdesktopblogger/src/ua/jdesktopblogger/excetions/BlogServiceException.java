package ua.jdesktopblogger.excetions;

/**
 * Exception that occurs in some blog service while operating
 * @author Yuriy Tkach
 */
public class BlogServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public BlogServiceException() {
	}

	public BlogServiceException(String message) {
		super(message);
	}

	public BlogServiceException(Throwable cause) {
		super(cause);
	}

	public BlogServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
