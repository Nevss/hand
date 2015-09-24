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

import java.util.ArrayList;

/**
 * 2015年9月17日 HomtFragmentModel.java com.darly.activities.model
 * 
 * @auther Darly Fronch 下午6:00:12 HomtFragmentModel TODO
 */
public class HomtFragmentBase {
	private String name;
	private String url;
	private ArrayList<HomtFragmentModel> data;

	public HomtFragmentBase(String name, String url,
			ArrayList<HomtFragmentModel> data) {
		super();
		this.name = name;
		this.url = url;
		this.data = data;
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

	public ArrayList<HomtFragmentModel> getData() {
		return data;
	}

	public void setData(ArrayList<HomtFragmentModel> data) {
		this.data = data;
	}

}
