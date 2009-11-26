package ua.jdesktopblogger.excetions;

/**
 * This exception is thrown, when authentication fails for account.
 * Exception objects provides information about the user login that failed
 * to authenticate on server url.
 * 
 * @author Yuriy Tkach
 */
public class AccountAuthenticateException extends BlogServiceException {

	private static final long serialVersionUID = 1L;
	private String login;
	private String serverUrl;

	public AccountAuthenticateException(String login, String serverUrl) {
		this.login = login;
		this.serverUrl = serverUrl;
	}

	public AccountAuthenticateException(String login, String serverUrl, String message) {
		super(message);
		this.login = login;
		this.serverUrl = serverUrl;
	}

	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

}
