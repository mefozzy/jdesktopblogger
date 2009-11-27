package ua.jdesktopblogger.domain;

import java.security.Key;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Represents a single account that is stored on the user machine. Account holds
 * auth information and collection of blogs.
 * 
 * @author Yuriy Tkach
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "account", propOrder = { "login", "password" , "encrypt"})
public class Account {
	
	private static final String ALGORITHM = "AES";
	
    private static final byte[] keyValue = 
        new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
	
    /**
	 * Blog provider access object that is used by blog provider to store its
	 * connection or some other needed information. For every method call of
	 * blog provider the account object is passed, so blog provider can store
	 * and then access some information in this object.
	 */
	@XmlTransient
	private Object providerObject;

	/** User login */
	@XmlElement(required = true)
	private String login;

	/** User password */
	@XmlElement(required = true)
	private String password;
	
	@XmlElement(name="en", required = true)
	private boolean encrypt = false;

	/** Collection of account blogs */
	@XmlTransient
	private Set<Blog> blogs = new HashSet<Blog>();


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the blogs
	 */
	public Set<Blog> getBlogs() {
		return blogs;
	}

	/**
	 * @param blogs
	 *            the blogs to set
	 */
	public void setBlogs(Set<Blog> blogs) {
		this.blogs = blogs;
	}

	public void encryptPasswd(){
		if (encrypt == true){
    		return;
    	}
		String encryptedValue = null;
		try {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGORITHM);
	        c.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encValue = c.doFinal(this.password.getBytes());
	        encryptedValue = new BASE64Encoder().encode(encValue);
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		encrypt = true;
        password = encryptedValue;
    }
	
    public void decryptPasswd() {
    	if (encrypt == false){
    		return;
    	}
    	String decryptedValue = null;
    	try {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGORITHM);
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = new BASE64Decoder().decodeBuffer(this.password);
	        byte[] decValue = c.doFinal(decordedValue);
	        decryptedValue = new String(decValue);
    	} catch (Exception e){
    		e.printStackTrace();
    		return;
    	}
    	encrypt = false;
        password = decryptedValue;
    }
    
    private static Key generateKey() {
    	Key key = null;
    	try {
	        key = new SecretKeySpec(keyValue, ALGORITHM);
	        // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
	        // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
    	} catch (Exception e){
    		e.printStackTrace();
    	}
        return key;
    }
	/**
	 * @return the providerObject
	 */
	public Object getProviderObject() {
		return providerObject;
	}

	/**
	 * @param providerObject
	 *            the providerObject to set
	 */
	public void setProviderObject(Object providerObject) {
		this.providerObject = providerObject;
	}

}
