package ua.jdesktopblogger.services;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.domain.IPostListener;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.BlogServiceException;

/**
 * Interface for the blog service that operates on different blog providers
 * 
 * @author Yuriy Tkach
 */
public interface IBlogService {

	/**
	 * Refreshes account, by logging in and loading all available blogs
	 * 
	 * @param account
	 *            Account to refresh
	 * @param accountListener
	 *            Listener to the account events
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void refreshAccount(Account account, IAccountListener accountListener)
			throws BlogServiceException;

	/**
	 * Loading all posts for the specified blog
	 * 
	 * @param account
	 *            Account to use
	 * @param blog
	 *            Blog to load posts for
	 * @param accountListener
	 *            Listener to the account events
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void loadPosts(Account account, Blog blog,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException;

	/**
	 * Publishing new post to the blog
	 * 
	 * @param account
	 *            Account to use
	 * @param blog
	 *            Blog to post new comment to
	 * @param newPost
	 *            new post object
	 * @param accountListener
	 *            account events listener
	 * @param postListener
	 *            post events listener
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void publishPost(Account account, Blog blog, Post newPost,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException;

	/**
	 * Edit existing post of the blog
	 * 
	 * @param account
	 *            Account to use
	 * @param blog
	 *            Blog of the post
	 * @param post
	 *            edit post object
	 * @param accountListener
	 *            account events listener
	 * @param postListener
	 *            post events listener
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void editPost(Account account, Blog blog, Post post,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException;

	/**
	 * Deleting existing post from the blog
	 * 
	 * @param account
	 *            Account to use
	 * @param blog
	 *            Blog of the post
	 * @param post
	 *            post to delete
	 * @param accountListener
	 *            account events listener
	 * @param postListener
	 *            post events listener
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public void deletePost(Account account, Blog blog, Post post,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException;

}
