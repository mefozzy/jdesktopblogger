package ua.jdesktopblogger.providers.blogger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.AccountAuthenticateException;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.excetions.ProviderIOException;
import ua.jdesktopblogger.providers.IBlogProvider;

import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

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

	// private static final String COMMENTS_FEED_URI_SUFFIX =
	// "/comments/default";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#loadListOfBlogs(ua.jdesktopblogger
	 * .domain.Account)
	 */
	@Override
	public Set<Blog> loadListOfBlogs(Account account)
			throws BlogServiceException, ProviderIOException {

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

		Feed resultFeed = getFeedFromAccount(account, feedUrl);

		Set<Blog> rez = new TreeSet<Blog>();

		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			Entry entry = resultFeed.getEntries().get(i);

			Blog blog = new Blog();
			blog.setName(entry.getTitle().getPlainText());
			blog.setId(entry.getId().split("blog-")[1]);

			rez.add(blog);
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

		BloggerService myService = new BloggerService("gnu-jdesktopblogger-1");
		// Authenticate using ClientLogin
		try {
			myService.setUserCredentials(account.getLogin(), account
					.getPassword());
		} catch (AuthenticationException e) {
			throw new AccountAuthenticateException(account.getLogin(),
					"http://www.blogger.com/");
		}

		account.setProviderObject(myService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#loadListOfPosts(ua.jdesktopblogger
	 * .domain.Account, ua.jdesktopblogger.domain.Blog)
	 */
	@Override
	public SortedSet<Post> loadListOfPosts(Account account, Blog blog)
			throws BlogServiceException, ProviderIOException,
			IllegalArgumentException {

		checkAccountAndBlogForValidity(account, blog);

		URL feedUrl = constructFeedUrlForBlog(blog);

		Feed resultFeed = getFeedFromAccount(account, feedUrl);

		SortedSet<Post> rez = new TreeSet<Post>();

		// Print the results
		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			Entry entry = resultFeed.getEntries().get(i);
			Post post = constructPostFromEntry(entry);

			rez.add(post);
		}
		return rez;
	}

	/**
	 * Constructing new post object and filling information from entry
	 * 
	 * @param entry
	 *            Entry to get information from
	 * @return constructed post object
	 */
	private Post constructPostFromEntry(Entry entry) {
		Post post = new Post();
		post.setTitle(entry.getTitle().getPlainText());

		if (entry.getTextContent().getContent() instanceof HtmlTextConstruct) {
			post.setBody(((HtmlTextConstruct) entry.getTextContent()
					.getContent()).getHtml());
		} else {
			post.setBody(entry.getTextContent().getContent().getPlainText());
		}

		post.setEditDate(Calendar.getInstance());
		post.getEditDate().setTimeInMillis(entry.getEdited().getValue());

		post.setPublishDate(Calendar.getInstance());
		post.getPublishDate().setTimeInMillis(entry.getPublished().getValue());

		post.setDraft(entry.isDraft());

		// post.setKeywords(entry.getEtag());
		post.setUploaded(true);

		if (entry.getHtmlLink() != null) {
			post.setUrl(entry.getHtmlLink().getHref());
		} else {
			post.setUrl(entry.getEditLink().getHref());
		}

		post.setProviderSpecificObject(entry);

		return post;
	}

	/**
	 * Constructing feed url for the specified blog
	 * 
	 * @param blog
	 *            Blog to use
	 * @return constructed url
	 * @throws BlogServiceException
	 *             If construction fails
	 */
	private URL constructFeedUrlForBlog(Blog blog) throws BlogServiceException {
		URL feedUrl;
		try {
			feedUrl = new URL(FEED_URI_BASE + "/" + blog.getId()
					+ POSTS_FEED_URI_SUFFIX);
		} catch (MalformedURLException e1) {
			throw new BlogServiceException(e1);
		}
		return feedUrl;
	}

	/**
	 * Getting feed using blogger service from account
	 * 
	 * @param account
	 *            Account where blogger service is stored
	 * @param feedUrl
	 *            Feed url to get
	 * @return Feed of the account
	 * @throws BlogServiceException
	 *             If error occurs
	 */
	private Feed getFeedFromAccount(Account account, URL feedUrl)
			throws BlogServiceException, ProviderIOException {
		BloggerService myService = (BloggerService) account.getProviderObject();

		Feed resultFeed;
		try {
			resultFeed = myService.getFeed(feedUrl, Feed.class);
		} catch (IOException e) {
			throw new ProviderIOException(e);
		} catch (ServiceException e) {
			throw new BlogServiceException(e);
		}

		return resultFeed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#publishNewPost(ua.jdesktopblogger
	 * .domain.Account, ua.jdesktopblogger.domain.Blog,
	 * ua.jdesktopblogger.domain.Post)
	 */
	@Override
	public Post publishNewPost(Account account, Blog blog, Post newPost)
			throws BlogServiceException, ProviderIOException,
			IllegalArgumentException {

		checkAccountAndBlogForValidity(account, blog);

		// Create the entry to insert
		Entry myEntry = new Entry();
		updateEntryValuesFromPost(newPost, myEntry);

		// Ask the service to insert the new entry
		URL postUrl = constructFeedUrlForBlog(blog);

		BloggerService myService = (BloggerService) account.getProviderObject();

		try {
			Entry entry = myService.insert(postUrl, myEntry);

			Post publishedPost = constructPostFromEntry(entry);
			publishedPost.setUploaded(true);

			blog.getPosts().add(publishedPost);

			return publishedPost;
		} catch (IOException e) {
			throw new ProviderIOException(e);
		} catch (ServiceException e) {
			throw new BlogServiceException(e);
		}
	}

	/**
	 * Setting values from post object to the entry object
	 * 
	 * @param post
	 *            Post to take values from
	 * @param entry
	 *            entry to update
	 */
	private void updateEntryValuesFromPost(Post post, Entry entry) {
		entry.setTitle(new PlainTextConstruct(post.getTitle()));
		entry.setContent(new PlainTextConstruct(post.getBody()));
		entry.setDraft(post.isDraft());
	}

	/**
	 * Checking blog and account for validity
	 * 
	 * @param account
	 *            Account to check
	 * @param blog
	 *            Blog to check
	 * @throws BlogServiceException
	 *             If if error occurs
	 * @throws AccountAuthenticateException
	 *             If login fails for not-logged on account
	 * @throws IllegalArgumentException
	 *             if blog is invalid
	 */
	private void checkAccountAndBlogForValidity(Account account, Blog blog)
			throws BlogServiceException, AccountAuthenticateException,
			IllegalArgumentException {
		if (account.getProviderObject() == null) {
			login(account);
		}

		if ((blog.getId() == null) || (blog.getId().isEmpty())) {
			throw new IllegalArgumentException("Blog id is not valid");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#editPost(ua.jdesktopblogger
	 * .domain.Account, ua.jdesktopblogger.domain.Blog,
	 * ua.jdesktopblogger.domain.Post)
	 */
	@Override
	public Post editPost(Account account, Blog blog, Post post)
			throws BlogServiceException, ProviderIOException,
			IllegalArgumentException {

		checkAccountAndBlogForValidity(account, blog);

		// Publishing new entry if it was not published before
		if (post.getProviderSpecificObject() == null) {
			return publishNewPost(account, blog, post);
		}

		// Create the entry to insert
		Entry myEntry = (Entry) post.getProviderSpecificObject();
		updateEntryValuesFromPost(post, myEntry);

		BloggerService myService = (BloggerService) account.getProviderObject();

		try {
			URL editUrl = new URL(myEntry.getEditLink().getHref());
			Entry entry = myService.update(editUrl, myEntry);

			Post publishedPost = constructPostFromEntry(entry);
			publishedPost.setUploaded(true);

			blog.getPosts().remove(post);
			blog.getPosts().add(publishedPost);

			return publishedPost;
		} catch (IOException e) {
			throw new ProviderIOException(e);
		} catch (ServiceException e) {
			throw new BlogServiceException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ua.jdesktopblogger.providers.IBlogProvider#deletePost(ua.jdesktopblogger
	 * .domain.Account, ua.jdesktopblogger.domain.Blog,
	 * ua.jdesktopblogger.domain.Post)
	 */
	@Override
	public void deletePost(Account account, Blog blog, Post post)
			throws BlogServiceException, ProviderIOException,
			IllegalArgumentException {
		checkAccountAndBlogForValidity(account, blog);

		if (post.getProviderSpecificObject() != null) {

			// Create the entry to insert
			Entry myEntry = (Entry) post.getProviderSpecificObject();
			updateEntryValuesFromPost(post, myEntry);

			BloggerService myService = (BloggerService) account
					.getProviderObject();

			try {
				URL editUrl = new URL(myEntry.getEditLink().getHref());
				myService.delete(editUrl);
			} catch (IOException e) {
				throw new ProviderIOException(e);
			} catch (ServiceException e) {
				throw new BlogServiceException(e);
			}

		}
		blog.getPosts().remove(post);
	}

}
