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
@XmlType(name="account", propOrder= { "name", "login", "password" } )
public class Account {

	/** Name of the account */
	@XmlElement(required=true)
	private String name;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

}
