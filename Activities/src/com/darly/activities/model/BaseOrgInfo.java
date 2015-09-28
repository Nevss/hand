package com.darly.activities.model;

/**
 * @author Administrator 体检机构
 */
public class BaseOrgInfo {
	/**
	 * 下午2:55:57
	 * 
	 * @author Zhangyuhui BaseOrgInfo.java TODO 机构ID
	 */
	public int org_id;
	/**
	 * 下午2:56:08
	 * 
	 * @author Zhangyuhui BaseOrgInfo.java TODO 机构名称
	 */
	public String org_name;
	/**
	 * 下午2:56:16
	 * 
	 * @author Zhangyuhui BaseOrgInfo.java TODO 机构楼层
	 */
	public int org_floor;

	public BaseOrgInfo(int org_id, String org_name, int org_floor) {
		super();
		this.org_id = org_id;
		this.org_name = org_name;
		this.org_floor = org_floor;
	}

}
