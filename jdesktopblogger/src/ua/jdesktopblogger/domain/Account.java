package ua.jdesktopblogger.domain;

import java.security.Key;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ua.jdesktopblogger.utils.Base64Coder;
import sun.misc.*;


/**
 * Represents a single account that is stored on the
 * user machine. Account holds auth information and collection of blogs.
 * 
 * @author Yuriy Tkach
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="account", propOrder= { "id", "login", "password" } )
public class Account {

	private static final String ENCODE_WORD = "bambarbiya";

	/** Name of the account */
	@XmlElement(required=true)
	private String id;
	
	/** 
	 * Blog provider access object that is used by blog provider
	 * to store its connection or some other needed information.
	 * For every method call of blog provider the account object
	 * is passed, so blog provider can store and then access
	 * some information in this object. 
	 */
	@XmlTransient
	private Object providerObject;
	
	/** User login */
	@XmlElement(required=true)
	private String login;
	
	/** User password */
	@XmlElement(required=true)
	private String password;
	
	/** Collection of account blogs */
	@XmlTransient
	private Set<Blog> blogs = new HashSet<Blog>();

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
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
	 * @param password the password to set
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
	 * @param blogs the blogs to set
	 */
	public void setBlogs(Set<Blog> blogs) {
		this.blogs = blogs;
	}

	public byte[] encryptString(String stringToEncrypt){
		byte[] res = null;
		try {
			Cipher cipher = Cipher.getInstance("DES");
			Key key = KeyGenerator.getInstance("DES", "hex").generateKey();
			cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] inputBytes = stringToEncrypt.getBytes();
	        res = cipher.doFinal(inputBytes);
	        
			
//			DESKeySpec keySpec = new DESKeySpec(ENCODE_WORD.getBytes("UTF8"));
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			SecretKey key = keyFactory.generateSecret(keySpec);
//			sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();
//		
//			
//			// ENCODE plainTextPassword String
//			byte[] cleartext = stringToEncrypt.getBytes("UTF8");      
//
//			Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
//			cipher.init(Cipher.ENCRYPT_MODE, key);
//			res = base64encoder.encode(resByteArr);
		} catch (Exception e){
			e.printStackTrace();
			res = null;
		}
		return res;
	}
	
	public String decryptString(byte[] stringToDecrypt){
		String res = null;
		try {
			Cipher cipher = Cipher.getInstance("DES");
			Key key = KeyGenerator.getInstance("DES", "hex").generateKey();
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] resBytes = cipher.doFinal(stringToDecrypt);
	        res = new String(resBytes);
			
//			DESKeySpec keySpec = new DESKeySpec("Your secret Key phrase".getBytes("UTF8"));
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			SecretKey key = keyFactory.generateSecret(keySpec);
//	
//			// DECODE encryptedPwd String
//
//			Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
//			cipher.init(Cipher.DECRYPT_MODE, key);
//			byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
//
//			res = new String(plainTextPwdBytes);
		} catch (Exception e){
			e.printStackTrace();
			res = null;
		}
		return res;
	}

	/**
	 * @return the providerObject
	 */
	public Object getProviderObject() {
		return providerObject;
	}

	/**
	 * @param providerObject the providerObject to set
	 */
	public void setProviderObject(Object providerObject) {
		this.providerObject = providerObject;
	}


}
