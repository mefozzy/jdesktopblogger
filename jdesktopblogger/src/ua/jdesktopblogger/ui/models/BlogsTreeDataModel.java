package ua.jdesktopblogger.ui.models;

import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;

public class BlogsTreeDataModel extends TreeModelSupport implements TreeModel{

	private Set<Account> accountsList = new HashSet<Account>();
	
	public int getSize() {
		return accountsList.size();
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof Account){
			Account account = (Account) parent;
			if (index < account.getBlogs().size()){
				return account.getBlogs().toArray()[index];
			}
		} else if (parent instanceof String){
			return accountsList.toArray()[index];
		}
		return accountsList.size();
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof Account){
			Account account = (Account) parent;
			return account.getBlogs().size();
		} else {
			return accountsList.size();
		} 
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof Account){
			Account account = (Account) parent;
			Blog childBlog = (Blog) child;
			int result = 0;
			for (Blog blog: account.getBlogs()){
				if (blog.getName().equals(childBlog.getName())){
					return result;
				}
				result++;
			}
		} else if (parent instanceof String){
			int result = 0;
			Account childAccount = (Account) child;
			for (Account account: accountsList){
				if (account.getLogin().equals(childAccount.getLogin())){
					return result;
				}
				result++;
			}
		}
		return 0;
	}

	@Override
	public Object getRoot() {
//		if (accountsList.size() > 0){
//			return accountsList.get(0);
//		}
		return "Accounts";
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof Blog){
			return true;
		}
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
	}
	
	
	public void addAccount(Account newAccount){
		accountsList.add(newAccount);
	}
	
	public void removeAccount(Account account) {
		accountsList.remove(account);
	}
	
	public void removeAllAccounts() {
		accountsList.clear();
	}
	
	public Account getAccountByLogin(String login) {
		for (Account account : accountsList) {
			if (account.getLogin().equals(login)) {
				return account;
			}
		}
		return null;
	}
	
}
