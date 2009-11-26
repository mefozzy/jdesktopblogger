package ua.jdesktopblogger.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ua.jdesktopblogger.domain.Account;
import ua.jdesktopblogger.domain.Blog;

public class BlogsTreeDataModel extends TreeModelSupport implements TreeModel{

	private List<Account> accountsList = new ArrayList<Account>();

	@Override
	public Object getChild(Object parent, int index) {
		if (parent instanceof Account){
			Account account = (Account) parent;
			if (index < account.getBlogs().size()){
				return account.getBlogs().toArray()[index];
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent instanceof Account){
			Account account = (Account) parent;
			return account.getBlogs().size();
		} 
		return 0;
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
		}
		return 0;
	}

	@Override
	public Object getRoot() {
		if (accountsList.size() > 0){
			return accountsList.get(0);
		}
		return null;
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
		for (Account account: accountsList){
			if (account.getLogin().equals(newAccount.getLogin())){
				return;
			}
		}
		accountsList.add(newAccount);
	}
	
}
