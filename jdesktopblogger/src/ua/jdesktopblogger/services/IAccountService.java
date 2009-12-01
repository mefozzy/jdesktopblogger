package ua.jdesktopblogger.services;

import java.util.Collection;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.excetions.AccountIOException;

/**
 * Interface with function to manage Blog's entities
 * 
 * @author Alex Skosyr
 * 
 */
public interface IAccountService {

	/**
	 * Load default account from user home directory (if it exists) or null
	 * otherwise
	 * 
	 * @param login
	 *            - login to get account info
	 * @throws AccountIOException
	 *             If io error occurs
	 */
	public Account getLoadedAccount(String login) throws AccountIOException;

	/**
	 * Save a specified account to the file in the user home directory
	 * 
	 * @param account
	 *            - account to save
	 * @throws AccountIOException
	 *             If io error occurs
	 */
	public void saveAccount(Account account) throws AccountIOException;

	/**
	 * Loading all saved accounts
	 * 
	 * @return Collection of saved accounts
	 * @throws AccountIOException
	 *             If io error occurs
	 */
	public Collection<Account> loadSavedAccounts() throws AccountIOException;

	/**
	 * Deleting account file
	 * 
	 * @param account
	 *            Account to delete
	 * @throws AccountIOException
	 *             If io error occurs
	 */
	public void deleteAccount(Account account) throws AccountIOException;

}
