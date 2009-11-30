package ua.jdesktopblogger.ui;

import ua.jdesktopblogger.domain.Blog;

/**
 * Wrapper that holds blog and returns it name in the toString method
 * @author Yuriy Tkach
 */
public class BlogComboWrapper {

	private Blog blog;

	public BlogComboWrapper(Blog blog) {
		this.blog = blog;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return blog.getName();
	}

	/**
	 * @return the blog
	 */
	public Blog getBlog() {
		return blog;
	}

	/**
	 * @param blog the blog to set
	 */
	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	

}
