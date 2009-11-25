package ua.jdesktopblogger.domain;

import java.util.Calendar;

/**
 * Class that represents single blog post that 
 * implements {@link Comparable} and compares by date.
 * 
 * Blog has indicator field that defines if it was uploaded to server.
 * All non-uploaded posts are saved on local machine.
 * 
 * @author Yuriy Tkach
 */
public class Post {
	
	/** Post title */
	private String title;
	
	/** Post body */
	private String body;
	
	/** Comma separated list of keywords */
	private String keywords;
	
	/** Creation date */
	private Calendar date;
	
	/** Specifies, if blog is a draft and not published yet */
	private boolean draft;
	
	/** Specifies, if blog is uploaded to server */
	private boolean uploaded;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the draft
	 */
	public boolean isDraft() {
		return draft;
	}

	/**
	 * @param draft the draft to set
	 */
	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	/**
	 * @return the uploaded
	 */
	public boolean isUploaded() {
		return uploaded;
	}

	/**
	 * @param uploaded the uploaded to set
	 */
	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}
	
}
