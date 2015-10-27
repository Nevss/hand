/**
 * 下午1:42:31
 * @author Zhangyuhui
 * $
 * CaiModel.java
 * TODO
 */
package com.darly.activities.model;

/**
 * @author Zhangyuhui CaiModel $ 下午1:42:31 TODO
 */
public class CaiModel {

	private int id;
	private String Title;
	private String Answer;

	public CaiModel(int id, String title, String answer) {
		super();
		this.id = id;
		Title = title;
		Answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

}
