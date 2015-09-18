/**
 * 2015年9月17日
 * SetFragmentModel.java
 * com.darly.activities.model
 * @auther Darly Fronch
 * 下午1:36:37
 * SetFragmentModel
 * TODO
 */
package com.darly.activities.model;

/**
 * 2015年9月17日 SetFragmentModel.java com.darly.activities.model
 * 
 * @auther Darly Fronch 下午1:36:37 SetFragmentModel TODO
 */
public class SetFragmentModel {
	private String name;
	private int resid;

	public SetFragmentModel(String name, int resid) {
		super();
		this.name = name;
		this.resid = resid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResid() {
		return resid;
	}

	public void setResid(int resid) {
		this.resid = resid;
	}

}
