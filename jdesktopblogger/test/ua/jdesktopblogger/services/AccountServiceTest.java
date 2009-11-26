package ua.jdesktopblogger.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.excetions.AccountIOException;

/**
 * @author Alex Skosyr
 * 
 */
public class AccountServiceTest {

	private Account account;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		account = new Account();
		account.setLogin("login");
		account.setId("id");
		account.setPassword("password");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		account = null;
	}

	@Test
	public void testAccountMarshall() {
		IAccountService accountService = ServiceFactory.getDefaultFactory()
				.getAccountService();
		try {
			accountService.saveAccount(account);

			StringBuffer sb = new StringBuffer();
			sb.append(System.getProperty("user.home")).append(
					System.getProperty("file.separator")).append(
					".jdesktopblogger").append(
					System.getProperty("file.separator")).append(
					account.getLogin()).append(".xml");
			File file = new File(sb.toString());
			if (file.exists() && file.getFreeSpace() > 0) {
				// do nothing
			} else {
				fail("Error during file creation");
			}
		} catch (AccountIOException e) {
			e.printStackTrace();
			fail("Error:" + e.getMessage());
		}
	}

	@Test
	public void testAccountUnMarshall() {
		IAccountService accountService = ServiceFactory.getDefaultFactory()
				.getAccountService();
		Account account;
		try {
			account = accountService.getLoadedAccount("login");
			assertNotNull(account);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error during document unmarshalling" + e.getMessage());
		}

	}

}
