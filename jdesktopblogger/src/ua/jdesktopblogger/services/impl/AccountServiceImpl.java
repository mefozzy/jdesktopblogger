package ua.jdesktopblogger.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

	@SuppressWarnings("unchecked")
	@Override
	public Account getLoadedAccount(String login) throws AccountIOException {
		try {
			if (jc == null) {
				createJAXBContext();
			}
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			StringBuffer sb = buildSettingsDir();

			sb.append(login).append(".xml");
			JAXBElement<Account> res = (JAXBElement<Account>) unmarshaller
					.unmarshal(new FileInputStream(sb.toString()));
			Account account = res.getValue();
			account.decryptPasswd();
			return account;

		} catch (JAXBException e) {
			throw new AccountIOException(e);
		} catch (FileNotFoundException e1) {
			throw new AccountIOException(e1);
		}
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

	@Override
	public void saveAccount(Account account) throws AccountIOException {
		try {
			if (jc == null) {
				createJAXBContext();
			}
			Marshaller marshaller = jc.createMarshaller();
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

	private void createJAXBContext() throws JAXBException {
		jc = JAXBContext.newInstance(PACKAGE_NAME);
	}

}
