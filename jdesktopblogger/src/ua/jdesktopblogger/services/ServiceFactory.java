package ua.jdesktopblogger.services;

import ua.jdesktopblogger.services.impl.DefaultServiceFactory;

/**
 * Factory for services
 * 
 * @author Alex Skosyr
 *
 */
public abstract class ServiceFactory {

	protected static ServiceFactory defaultFactory = null;
	
	public static ServiceFactory getDefaultFactory(){
		if (defaultFactory == null){
			defaultFactory = new DefaultServiceFactory(); 
		}
		return defaultFactory;
	}
	
	public abstract IAccountService getAccountService();
	
	public abstract IBlogService getBlogService();
	
}
