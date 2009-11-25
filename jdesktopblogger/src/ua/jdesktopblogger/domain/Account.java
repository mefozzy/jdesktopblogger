package ua.jdesktopblogger.domain;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


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
	private Set<Blog> blogs;

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
