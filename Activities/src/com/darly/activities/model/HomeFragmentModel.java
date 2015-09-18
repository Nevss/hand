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

import java.util.List;

/**
 * 2015年9月17日 HomtFragmentModel.java com.darly.activities.model
 * 
 * @auther Darly Fronch 下午6:00:12 HomtFragmentModel TODO
 */
public class HomeFragmentModel {

	private HomeSingleOne single;

	private List<HomeSingleOne> more;

	public HomeFragmentModel(HomeSingleOne single, List<HomeSingleOne> more) {
		super();
		this.single = single;
		this.more = more;
	}

	public HomeSingleOne getSingle() {
		return single;
	}

	public void setSingle(HomeSingleOne single) {
		this.single = single;
	}

	public List<HomeSingleOne> getmore() {
		return more;
	}

	public void setmore(List<HomeSingleOne> more) {
		this.more = more;
	}

}
