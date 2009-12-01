package ua.jdesktopblogger.domain;

import java.util.Calendar;

/**
 * Class that represents single blog post that 
 * implements {@link Comparable} and compares by publishDate.
 * 
 * Blog has indicator field that defines if it was uploaded to server.
 * All non-uploaded posts are saved on local machine.
 * 
 * @author Yuriy Tkach
 */
public class Post implements Comparable<Post> {
	
	/** Post title */
	private String title;
	
	/** Post body */
	private String body;
	
	/** Comma separated list of keywords */
	private String keywords;
	
	/** Creation publishDate */
	private Calendar publishDate;

	/** Edit date */
	private Calendar editDate;
	
	/** Specifies, if blog is a draft and not published yet */
	private boolean draft;
	
	/** Specifies, if blog is uploaded to server */
	private boolean uploaded;
	
	/** Url of the post */
	private String url;
	
	/** Object that is specific to provider and that is associated with the post */
	private Object providerSpecificObject;

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

	@Override
	public int compareTo(Post o) {
		int rez = -1;
		if ((o != null) && (publishDate != null)) {
			rez = publishDate.compareTo(o.getPublishDate());
		}
		return rez;
	}

	/**
	 * @return the publishDate
	 */
	public Calendar getPublishDate() {
		return publishDate;
	}

	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(Calendar publishDate) {
		this.publishDate = publishDate;
	}

	/**
	 * @return the editDate
	 */
	public Calendar getEditDate() {
		return editDate;
	}

	/**
	 * @param editDate the editDate to set
	 */
	public void setEditDate(Calendar editDate) {
		this.editDate = editDate;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the providerSpecificObject
	 */
	public Object getProviderSpecificObject() {
		return providerSpecificObject;
	}

	/**
	 * @param providerSpecificObject the providerSpecificObject to set
	 */
	public void setProviderSpecificObject(Object providerSpecificObject) {
		this.providerSpecificObject = providerSpecificObject;
	}
	
}
