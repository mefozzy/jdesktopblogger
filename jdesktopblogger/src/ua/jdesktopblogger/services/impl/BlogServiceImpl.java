package ua.jdesktopblogger.services.impl;

import ua.jdesktopblogger.domain.Account;
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
	public void refreshAccount(Account account) throws BlogServiceException {
		IBlogProvider pr = ProviderFactory.getBlogProvider(account);
		
		if (account.getProviderObject() == null) {
			pr.login(account);
		}
		
		account.setBlogs(pr.loadListOfBlogs(account));
	}

}
