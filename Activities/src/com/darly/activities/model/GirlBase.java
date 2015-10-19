/**
 * 下午3:14:21
 * @author Zhangyuhui
 * GirlBase.java
 * TODO
 */
package com.darly.activities.model;

/**
 * @author Zhangyuhui GirlBase 下午3:14:21 TODO
 */
public class GirlBase {
	private String title;
	private String description;
	private String picUrl;
	private String url;

	public GirlBase(String title, String description, String picUrl, String url) {
		super();
		this.title = title;
		this.description = description;
		this.picUrl = picUrl;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
