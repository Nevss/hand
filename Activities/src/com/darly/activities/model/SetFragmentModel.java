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
	private int name;
	private int resid;

	public SetFragmentModel(int name, int resid) {
		super();
		this.name = name;
		this.resid = resid;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getResid() {
		return resid;
	}

	public void setResid(int resid) {
		this.resid = resid;
	}

}
