package ua.jdesktopblogger.domain;

import java.util.SortedSet;

/** 
 * Class that holds information about single blog along with all
 * posts.
 *   
 * @author Yuriy Tkach
 */
public class Blog {
	
	/** Name of the blog */
	private String name;
	
	/** Blog id that is used in requests */
	private String id;

	/** Set of all blog posts, sorted by date */
	private SortedSet<Post> posts;

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
	 * @return the posts
	 */
	public SortedSet<Post> getPosts() {
		return posts;
	}

	/**
	 * @param posts the posts to set
	 */
	public void setPosts(SortedSet<Post> posts) {
		this.posts = posts;
	}
}
