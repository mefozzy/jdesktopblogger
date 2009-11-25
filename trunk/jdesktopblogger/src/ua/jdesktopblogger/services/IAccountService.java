package ua.jdesktopblogger.services;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import ua.jdesktopblogger.domain.Account;

/**
 * Interface with function to manage Blog's entities
 * 
 * @author Alex Skosyr
 *
 */
public interface IAccountService {

	/**
	 * Load default account from user home directory (if it exists) or null otherwise
	 * @param login - login to get account info 
	 * @throws JAXBException 
	 * @throws FileNotFoundException 
	 */
	public Account getLoadedAccount(String login) throws JAXBException, FileNotFoundException;
	
	/**
	 * Save a specified account to the file in the user home directory
	 * @param account - account to save
	 * @throws JAXBException
	 * @throws FileNotFoundException 
	 */
	public void saveAccount(Account account) throws JAXBException, FileNotFoundException ;
	
}
