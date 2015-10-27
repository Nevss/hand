package com.ytdinfo.keephealth.model;

import java.io.Serializable;

import com.google.gson.Gson;


/**
 * @author Zhangyuhui
 * ChatInfoBean
 * $
 * 下午2:11:22
 * TODO 聊天窗口信息Bean
 */
public class ChatInfoBean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	private String SubjectID;
	private String docInfoBeanId;
	/**是否在线*/
	private boolean status= true;
	/**问诊类型*/
	private String subjectType;
	/**是否评价*/
	private boolean isComment  = false;
	/**是否超时*/
	private boolean isTimeout  = false;

	public boolean isTimeout() {
		return isTimeout;
	}

	public void setTimeout(boolean isTimeout) {
		this.isTimeout = isTimeout;
	}

	public boolean isComment() {
		return isComment;
	}

	public void setComment(boolean isComment) {
		this.isComment = isComment;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this);
	}
	
	public String getSubjectID() {
		return SubjectID;
	}

	public void setSubjectID(String subjectID) {
		SubjectID = subjectID;
	}

	public String getDocInfoBeanId() {
		return docInfoBeanId;
	}

	public void setDocInfoBeanId(String docInfoBeanId) {
		this.docInfoBeanId = docInfoBeanId;
	}

	
	
	
}
