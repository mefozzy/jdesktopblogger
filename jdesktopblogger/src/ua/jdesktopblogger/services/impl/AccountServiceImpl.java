package ua.jdesktopblogger.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.excetions.AccountIOException;
import ua.jdesktopblogger.services.IAccountService;

public class AccountServiceImpl implements IAccountService {

	private final String PACKAGE_NAME = "ua.jdesktopblogger.domain";

	private JAXBContext jc = null;

	@Override
	public Account getLoadedAccount(String login) throws AccountIOException {
		try {
			StringBuffer sb = buildSettingsDir();
			sb.append(login).append(".xml");
			String fileName = sb.toString();

			return loadAccountFromFile(fileName);

		} catch (JAXBException e) {
			throw new AccountIOException(e);
		} catch (FileNotFoundException e1) {
			throw new AccountIOException(e1);
		}
	}

	/**
	 * Loading account from file, by parsing file
	 * 
	 * @param fileName
	 *            File name to load account from
	 * @return Loaded account object
	 * @throws JAXBException
	 *             If parsing fails
	 * @throws FileNotFoundException
	 *             If file is not found
	 */
	@SuppressWarnings("unchecked")
	private Account loadAccountFromFile(String fileName) throws JAXBException,
			FileNotFoundException {
		Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();

		JAXBElement<Account> res = (JAXBElement<Account>) unmarshaller
				.unmarshal(new FileInputStream(fileName));
		Account account = res.getValue();
		account.decryptPasswd();
		return account;
	}

	/**
	 * Building settings directory that is the program directory in the user's
	 * home directory
	 * 
	 * @return String buffer for settings directory
	 */
	private StringBuffer buildSettingsDir() {
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("user.home")).append(
				System.getProperty("file.separator"))
				.append(".jdesktopblogger").append(
						System.getProperty("file.separator"));
		return sb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.services.IAccountService#saveAccount(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public void saveAccount(Account account) throws AccountIOException {
		try {

			Marshaller marshaller = getJAXBContext().createMarshaller();
			if (account != null && account.getLogin() != "") {
				StringBuffer sb = buildSettingsDir();

				File dir = new File(sb.toString());
				if ((dir.exists()) || (dir.mkdirs())) {

					sb.append(account.getLogin()).append(".xml");
					// encrypt data before storing
					account.encryptPasswd();
					marshaller.marshal(account, new FileOutputStream(sb
							.toString()));
					// decrypt data for application use
					account.decryptPasswd();
				} else {
					throw new AccountIOException("Failed to create directory: "
							+ sb.toString());
				}
			} else {
				System.err
						.println("Can't create output file. Account's login is empty");
				throw new AccountIOException("Account login is empty");
			}
		} catch (JAXBException e) {
			throw new AccountIOException(e);
		} catch (FileNotFoundException e1) {
			throw new AccountIOException(e1);
		}
	}

	private JAXBContext getJAXBContext() throws JAXBException {
		if (jc == null) {
			jc = JAXBContext.newInstance(PACKAGE_NAME);
		}
		return jc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ua.jdesktopblogger.services.IAccountService#loadSavedAccounts()
	 */
	@Override
	public Collection<Account> loadSavedAccounts() throws AccountIOException {
		StringBuffer dirName = buildSettingsDir();
		File dir = new File(dirName.toString());
		Collection<Account> rez = new ArrayList<Account>();
		if ((dir.exists()) || (dir.mkdirs())) {

			for (File file : dir.listFiles()) {
				if ((!file.isDirectory()) && (file.canRead())) {
					try {
						Account account = loadAccountFromFile(file
								.getAbsolutePath());

						rez.add(account);
					} catch (Exception e) {
						System.err.println("Failed to load account from "
								+ file.getAbsolutePath() + " >>> " + e);
					}
				}
			}
		}

		return rez;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.services.IAccountService#deleteAccount(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public void deleteAccount(Account account) throws AccountIOException {
		StringBuffer sb = buildSettingsDir();
		sb.append(account.getLogin()).append(".xml");
		File file = new File(sb.toString());
		if (!file.delete()) {
			throw new AccountIOException("Failed to delete account file");
		}
	}

}
