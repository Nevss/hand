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

	public int type;

	public boolean isSelect;

	public Menu(String title, Menu_Top tops, int type, boolean isSelect) {
		this.title = title;
		this.tops = tops;
		this.type = type;
		this.isSelect = isSelect;
	}

}
