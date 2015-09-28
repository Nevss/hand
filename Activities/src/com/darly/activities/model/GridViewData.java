package com.darly.activities.model;

import java.io.Serializable;

public class GridViewData implements Serializable {

	/**
	 * 下午5:35:15
	 * @author Zhangyuhui
	 * GridViewData.java
	 * TODO
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String url;

	public GridViewData(int id, String url) {
		super();
		this.id = id;
		this.url = url;
	}

}
