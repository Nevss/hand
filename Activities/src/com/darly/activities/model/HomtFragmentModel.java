/**
 * 2015年9月17日
 * HomtFragmentModel.java
 * com.darly.activities.model
 * @auther Darly Fronch
 * 下午6:00:12
 * HomtFragmentModel
 * TODO
 */
package com.darly.activities.model;

/**
 * 2015年9月17日 HomtFragmentModel.java com.darly.activities.model
 * 
 * @auther Darly Fronch 下午6:00:12 HomtFragmentModel TODO
 */
public class HomtFragmentModel {
	private String name;
	private String url;

	public HomtFragmentModel(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
