package ua.jdesktopblogger.services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.services.IAccountService;

public class AccountServiceImpl implements IAccountService {

	private final String PACKAGE_NAME = "ua.jdesktopblogger.domain";
	
	private JAXBContext jc = null;
	
	@Override
	public Account getLoadedAccount(String login) throws JAXBException, FileNotFoundException {
		if (jc == null){
			createJAXBContext();
		}
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("user.home")).append("/").append(login).append(".xml");
		JAXBElement<Account> res = (JAXBElement<Account>) unmarshaller.unmarshal(new FileInputStream(sb.toString()));
		return res.getValue();
	}

	@Override
	public void saveAccount(Account account) throws JAXBException, FileNotFoundException {
		if (jc == null){
			createJAXBContext();
		}
		Marshaller marshaller = jc.createMarshaller();
		if (account != null && account.getLogin() != "") {
			StringBuffer sb = new StringBuffer();
			sb.append(System.getProperty("user.home")).append("/").append(account.getLogin()).append(".xml");
			marshaller.marshal(account, new FileOutputStream(sb.toString()));
		} else {
			System.err.println("Can't create output file. Account's login is empty");
		}
	}

	private void createJAXBContext() {
		try {
			jc = JAXBContext.newInstance(PACKAGE_NAME);
		} catch (JAXBException e) {
			e.printStackTrace();
			return;
		}
	}

}
