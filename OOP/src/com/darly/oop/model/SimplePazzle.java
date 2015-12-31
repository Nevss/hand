/**
 * 上午10:23:43
 * @author zhangyh2
 * $
 * SimplePazzle.java
 * TODO
 */
package com.darly.oop.model;

/**
 * @author zhangyh2 SimplePazzle $ 上午10:23:43 TODO
 */
public class SimplePazzle {

	private int id;
	private String name;

	/**
	 * 
	 * 上午11:53:30
	 * 
	 * @author zhangyh2 SimplePazzle.java TODO
	 */
	public SimplePazzle() {
		// TODO Auto-generated constructor stub
	}

	public SimplePazzle(String name) {
		this.name = name;
	}

	public SimplePazzle(int id, String name) {
		this.id = id;
		this.name = name;
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

}
