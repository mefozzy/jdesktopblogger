package ua.jdesktopblogger.providers.blogger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.excetions.AccountAuthenticateException;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.providers.IBlogProvider;

/**
 * Provider that works with
 * <code>com.google.gdata.client.blogger.BloggerService</code>
 * 
 * @author Yuriy Tkach
 */
public class GoogleBloggerProvider implements IBlogProvider {

	private static final String METAFEED_URL = "http://www.blogger.com/feeds/default/blogs";

	private static final String FEED_URI_BASE = "http://www.blogger.com/feeds";
	private static final String POSTS_FEED_URI_SUFFIX = "/posts/default";
	private static final String COMMENTS_FEED_URI_SUFFIX = "/comments/default";

	private static String feedUri;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#loadListOfBlogs(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public Set<Blog> loadListOfBlogs(Account account)
			throws BlogServiceException {
		
		if (account.getProviderObject() == null) {
			login(account);
		}
		
		// Request the feed
		URL feedUrl;
		try {
			feedUrl = new URL(METAFEED_URL);
		} catch (MalformedURLException e) {
			throw new BlogServiceException(e);
		}
		
		BloggerService myService = (BloggerService) account.getProviderObject();
		
		Feed resultFeed;
		try {
			resultFeed = myService.getFeed(feedUrl, Feed.class);
		} catch (Exception e) {
			throw new BlogServiceException(e);
		}
		
		Set<Blog> rez = new TreeSet<Blog>();

		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			Entry entry = resultFeed.getEntries().get(i);
			
			Blog blog = new Blog();
			blog.setName(entry.getTitle().getPlainText());
			blog.setId(entry.getId().split("blog-")[1]);
		}
		
		return rez;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#login(ua.jdesktopblogger.domain
	 * .Account)
	 */
	@Override
	public void login(Account account) throws BlogServiceException,
			AccountAuthenticateException, IllegalArgumentException {
		if (account == null) {
			throw new NullPointerException("Account object is null");
		}
		if (account.getLogin().isEmpty() || account.getPassword().isEmpty()) {
			throw new IllegalArgumentException(
					"Account credentials are not set");
		}

		BloggerService myService = new BloggerService("exampleCo-exampleApp-1");
		// Authenticate using ClientLogin
		try {
			myService.setUserCredentials(account.getLogin(), account
					.getPassword());
		} catch (AuthenticationException e) {
			throw new AccountAuthenticateException(account.getLogin(),
					"http://www.blogger.com/");
		}
		// Get the blog ID from the metatfeed.
		String blogId;
		try {
			blogId = getBlogId(myService);
		} catch (Exception e) {
			throw new BlogServiceException(e);
		}

		account.setId(blogId);
		account.setProviderObject(myService);
		// feedUri = FEED_URI_BASE + "/" + blogId;
	}

	/**
	 * Getting feed uri for accessing blogger feed
	 * 
	 * @param account
	 *            Account object to get blog id from
	 * @return Feed uri to access blogger feed
	 */
	private String getFeedUri(Account account) {
		return FEED_URI_BASE + "/" + account.getId();
	}

	/**
	 * Parses the metafeed to get the blog ID for the authenticated user's
	 * default blog.
	 * 
	 * @param myService
	 *            An authenticated GoogleService object.
	 * @return A String representation of the blog's ID.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             If the URL is malformed.
	 */
	private static String getBlogId(BloggerService myService)
			throws ServiceException, IOException {
		// Get the metafeed
		final URL feedUrl = new URL(METAFEED_URL);
		Feed resultFeed = myService.getFeed(feedUrl, Feed.class);

		// If the user has a blog then return the id (which comes after 'blog-')
		if (resultFeed.getEntries().size() > 0) {
			Entry entry = resultFeed.getEntries().get(0);
			return entry.getId().split("blog-")[1];
		}
		throw new IOException("User has no blogs!");
	}

}
