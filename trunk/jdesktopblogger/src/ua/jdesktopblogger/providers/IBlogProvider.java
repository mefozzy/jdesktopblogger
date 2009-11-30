package ua.jdesktopblogger.providers;

import java.util.Set;
import java.util.SortedSet;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.AccountAuthenticateException;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.excetions.ProviderIOException;

/**
 * Interface defines methods for all blog services.
 * 
 * @author Yuriy Tkach
 */
public interface IBlogProvider {

	/**
	 * Blog provider must perform login in this method and throw appropriate
	 * exception if login fails due to authentication reasons. Blog provider can
	 * store the account id using {@link Account#setId(String)} method and also
	 * store some additional information using
	 * {@link Account#setProviderObject(Object)}.
	 * 
	 * Login method will be called always prior calling any other method, if
	 * account id or provider object is <code>null</code> in
	 * <code>account</code>.
	 * 
	 * @param account
	 *            Account to use for login
	 * @throws IllegalArgumentException
	 *             If account's object credentials are invalid
	 * @throws BlogServiceException
	 *             If error occurs
	 * @throws AccountAuthenticateException
	 *             If authentication error occurs
	 */
	public void login(Account account) throws IllegalArgumentException,
			BlogServiceException, AccountAuthenticateException;

	/**
	 * Loading set of user blogs for the specified accountF
	 * 
	 * @param account
	 *            Account to load set of blogs for
	 * @return Set of blogs
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	public Set<Blog> loadListOfBlogs(Account account)
			throws BlogServiceException, ProviderIOException;

	/**
	 * Loading set of user posts within specified blog
	 * 
	 * @param account
	 *            Account to use
	 * @param blog
	 *            Blog to load posts for
	 * @return Set of posts
	 * @throws BlogServiceException
	 *             If error occurs
	 * @throws IllegalArgumentException
	 *             If blog is not valid
	 */
	public SortedSet<Post> loadListOfPosts(Account account, Blog blog)
			throws BlogServiceException, ProviderIOException,
			IllegalArgumentException;

	/**
	 * Publishing new post to the blog. Uploading post data to server. If
	 * successfull then adding published post object to the blog's set of posts.
	 * 
	 * @param account
	 *            Account to use
	 * @param blog
	 *            Blog to publish to
	 * @param newPost
	 *            New post
	 * @throws BlogServiceException
	 *             If error occurs
	 * @throws IllegalArgumentException
	 *             If blog is not valid
	 */
	public void publishNewPost(Account account, Blog blog, Post newPost)
			throws BlogServiceException, ProviderIOException,
			IllegalArgumentException;

}
