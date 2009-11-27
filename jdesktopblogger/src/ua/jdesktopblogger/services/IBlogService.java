package ua.jdesktopblogger.services;

import ua.jdesktopblogger.domain.Account;
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
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void refreshAccount(Account account) throws BlogServiceException;

}
