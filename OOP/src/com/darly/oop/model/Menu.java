/**
 * 上午11:19:35
 * @author zhangyh2
 * $
 * Menu.java
 * TODO
 */
package com.darly.oop.model;

/**
 * @author zhangyh2 Menu $ 上午11:19:35 TODO
 */
public class Menu {

	public String title;

	public Menu_Top tops;

	/**
	 * 下午1:44:02 TODO 展示样式
	 */
	public int type;

	/**
	 * 下午1:44:29 TODO 点击选取的样式
	 */
	public int selectType;

	public boolean isSelect;

	public Menu(String title, Menu_Top tops, int type, int selectType,
			boolean isSelect) {
		this.title = title;
		this.tops = tops;
		this.type = type;
		this.selectType = selectType;
		this.isSelect = isSelect;
	}

}
