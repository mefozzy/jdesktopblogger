package ua.jdesktopblogger.services.impl;

import ua.jdesktopblogger.services.IAccountService;
import ua.jdesktopblogger.services.ServiceFactory;

public class DefaultServiceFactory extends ServiceFactory {

	@Override
	public IAccountService getAccountService() {
		return new AccountServiceImpl();
	}

}
