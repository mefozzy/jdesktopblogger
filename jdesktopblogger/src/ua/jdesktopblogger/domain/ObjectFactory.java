package ua.jdesktopblogger.domain;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName _Account_QNAME = new QName(XMLConstants.NULL_NS_URI, "account");
	
	/**
	 * create an instance of {@link Account}
	 * @return
	 */
	public Account createAccount(){
		return new Account();
	}
	
	@XmlElementDecl(name="account")
	public JAXBElement<Account> createAccount(Account value){
		return new JAXBElement<Account>(_Account_QNAME, Account.class, null, value);
	}
	
}
