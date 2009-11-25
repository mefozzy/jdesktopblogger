package ua.jdesktopblogger.services;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import ua.jdesktopblogger.domain.Account;
import junit.framework.TestCase;

/**
 * @author Alex Skosyr
 *
 */
public class AccountServiceTest extends TestCase {

	private Account account;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		account = new Account();
		account.setLogin("login");
		account.setName("name");
		account.setPassword("password");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		account = null;
	}

	public void testAccountMarshall(){
		IAccountService accountService = ServiceFactory.getDefaultFactory().getAccountService();
		try {
			accountService.saveAccount(account);
			
			StringBuffer sb = new StringBuffer();
			sb.append(System.getProperty("user.home")).append("/").append(account.getLogin()).append(".xml");
			File file = new File(sb.toString());
			if (file.exists() && file.getFreeSpace() > 0){
				// do nothing
			} else {
				fail("Error during file creation");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("Error during file creation:" + e.getMessage());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail("Error during marshalling:" + e.getMessage());
		}
	}

	public void testAccountUnMarshall(){
		IAccountService accountService = ServiceFactory.getDefaultFactory().getAccountService();
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
