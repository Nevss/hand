/**
 * 上午10:25:29
 * @author zhangyh2
 * $
 * LuckDraw.java
 * TODO
 */
package com.darly.oop.model;


/**
 * @author zhangyh2 LuckDraw $ 上午10:25:29 TODO
 */
public class LuckDraw {

	private int id;

	private String name;

	private String url;

	public LuckDraw(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
