package ua.jdesktopblogger.services;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.excetions.BlogServiceException;

/**
 * Interface for the blog service that operates on different blog providers
 * 
 * @author Yuriy Tkach
 */
public interface IBlogService {

	/**
	 * Refreshes account, by logging in and loading all available blogs
	 * 
	 * @param account
	 *            Account to refresh
	 * @param accountListener
	 *            Listener to the account events
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void refreshAccount(Account account, IAccountListener accountListener)
			throws BlogServiceException;

}
