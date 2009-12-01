package ua.jdesktopblogger.services.impl;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;
import ua.jdesktopblogger.domain.IAccountListener;
import ua.jdesktopblogger.domain.IPostListener;
import ua.jdesktopblogger.domain.Post;
import ua.jdesktopblogger.excetions.BlogServiceException;
import ua.jdesktopblogger.providers.IBlogProvider;
import ua.jdesktopblogger.providers.ProviderFactory;
import ua.jdesktopblogger.services.IBlogService;

/**
 * Implementation of the blog service
 * @author Yuriy Tkach
 */
public class BlogServiceImpl implements IBlogService {

	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.services.IBlogService#refreshAccount(ua.jdesktopblogger.domain.Account)
	 */
	@Override
	public void refreshAccount(Account account, IAccountListener accountListener) throws BlogServiceException {
		IBlogProvider pr = ProviderFactory.getBlogProvider(account);
		
		if (account.getProviderObject() == null) {
			pr.login(account);
		}
		
		account.setBlogs(pr.loadListOfBlogs(account));
		
		if (accountListener != null) {
			accountListener.accountRefreshed(account);
		}
	}

	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.services.IBlogService#loadPosts(ua.jdesktopblogger.domain.Account, ua.jdesktopblogger.domain.Blog)
	 */
	@Override
	public void loadPosts(Account account, Blog blog, IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException {
		
		IBlogProvider pr = ProviderFactory.getBlogProvider(account);
		
		if (account.getProviderObject() == null) {
			refreshAccount(account, accountListener);
			throw new BlogServiceException("Please, select blog to load posts for.");
		}
		
		blog.setPosts(pr.loadListOfPosts(account, blog));
		
		if (postListener != null) {
			postListener.postsLoaded(blog);
		}
	}

	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.services.IBlogService#publishPost(ua.jdesktopblogger.domain.Account, ua.jdesktopblogger.domain.Blog, ua.jdesktopblogger.domain.Post, ua.jdesktopblogger.domain.IAccountListener, ua.jdesktopblogger.domain.IPostListener)
	 */
	@Override
	public void publishPost(Account account, Blog blog, Post newPost,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException {
		
		IBlogProvider pr = ProviderFactory.getBlogProvider(account);
		
		if (account.getProviderObject() == null) {
			refreshAccount(account, accountListener);
			throw new BlogServiceException("Please, select blog to load posts for.");
		}
		
		Post publishedPost = pr.publishNewPost(account, blog, newPost);
		
		if (postListener != null) {
			postListener.postPublished(blog, publishedPost);
		}
	}

	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.services.IBlogService#editPost(ua.jdesktopblogger.domain.Account, ua.jdesktopblogger.domain.Blog, ua.jdesktopblogger.domain.Post, ua.jdesktopblogger.domain.IAccountListener, ua.jdesktopblogger.domain.IPostListener)
	 */
	@Override
	public void editPost(Account account, Blog blog, Post post,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException {
		IBlogProvider pr = ProviderFactory.getBlogProvider(account);
		
		if (account.getProviderObject() == null) {
			refreshAccount(account, accountListener);
			throw new BlogServiceException("Please, select blog to load posts for.");
		}
		
		Post publishedPost = pr.editPost(account, blog, post);
		
		if (postListener != null) {
			postListener.postUpdated(blog, publishedPost);
		}		
	}

	/* (non-Javadoc)
	 * @see ua.jdesktopblogger.services.IBlogService#deletePost(ua.jdesktopblogger.domain.Account, ua.jdesktopblogger.domain.Blog, ua.jdesktopblogger.domain.Post, ua.jdesktopblogger.domain.IAccountListener, ua.jdesktopblogger.domain.IPostListener)
	 */
	@Override
	public void deletePost(Account account, Blog blog, Post post,
			IAccountListener accountListener, IPostListener postListener)
			throws BlogServiceException {
		IBlogProvider pr = ProviderFactory.getBlogProvider(account);
		
		if (account.getProviderObject() == null) {
			refreshAccount(account, accountListener);
			throw new BlogServiceException("Please, select blog to load posts for.");
		}
		
		pr.deletePost(account, blog, post);
		
		if (postListener != null) {
			postListener.postDeleted(blog);
		}
	}
}
