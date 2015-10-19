package com.darly.activities.model;

import java.util.ArrayList;

/**
 * @author Administrator 体检城市
 */
public class BaseCityInfo {
	/**
	 * 下午2:55:36
	 * 
	 * @author Zhangyuhui BaseCityInfo.java TODO 城市ID
	 */
	public int city_id;
	/**
	 * 下午2:55:44
	 * 
	 * @author Zhangyuhui BaseCityInfo.java TODO 城市名称
	 */
	public String city_name;

	/**
	 * 下午2:59:15
	 * 
	 * @author Zhangyuhui BaseCityInfo.java TODO 城市里的机构集合。
	 */
	public ArrayList<BaseOrgInfo> city_org;

	public BaseCityInfo(int city_id, String city_name,
			ArrayList<BaseOrgInfo> city_org) {
		super();
		this.city_id = city_id;
		this.city_name = city_name;
		this.city_org = city_org;
	}

}
