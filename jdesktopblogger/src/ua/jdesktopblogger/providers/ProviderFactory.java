package ua.jdesktopblogger.providers;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.providers.blogger.GoogleBloggerProvider;

/**
 * Factory for blog providers
 * 
 * @author Yuriy Tkach
 */
public final class ProviderFactory {

	private ProviderFactory() {
	}

	/**
	 * Getting provider that is suitable for specified account
	 * 
	 * @param account
	 *            Account to get provider for
	 * @return Provider for blog service
	 */
	public static IBlogProvider getBlogProvider(Account account) {
		return new GoogleBloggerProvider();
	}

}
