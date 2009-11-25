package ua.jdesktopblogger.domain;

import java.util.Set;


/**
 * Represents a single account that is stored on the
 * user machine. Account holds auth information and collection of blogs.
 * 
 * @author Yuriy Tkach  
 */
public class Account {

	/** Name of the account */
	private String name;
	
	/** User login */
	private String login;
	
	/** User password */
	private String password;
	
	/** Collection of account blogs */
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
