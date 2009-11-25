package ua.jdesktopblogger.domain;

/**
 * Interface that defines methods that will be called on 
 * implementor after different account actions (creation, refreshing...)
 * 
 * @author Yuriy Tkach
 */
public interface IAccountListener {
	
	public void accountCreated(Account account);
	
	public void accountEdited(Account account);
	
	public void accountRefreshed(Account account);
	

}
