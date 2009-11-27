package ua.jdesktopblogger.domain;

/**
 * Interface defines methods to implement by listener to the different event
 * that occur with posts
 * 
 * @author Yuriy Tkach
 */
public interface IPostListener {

	/**
	 * Event occurs when posts are being loaded for the blog
	 * 
	 * @param blog
	 *            Blog that has loaded posts
	 */
	public void postsLoaded(Blog blog);

}
