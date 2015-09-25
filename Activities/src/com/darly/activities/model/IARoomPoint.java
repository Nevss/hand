/**
 * 2015年9月15日
 * IARoomPoint.java
 * com.darly.interlgent.model
 * @auther Darly Fronch
 * 下午2:36:22
 * IARoomPoint
 * TODO
 */
package com.darly.activities.model;

import java.util.ArrayList;

import android.graphics.Point;

/**
 * 2015年9月15日 IARoomPoint.java com.darly.interlgent.model
 * 
 * @auther Darly Fronch 下午2:36:22 IARoomPoint TODO 房间号对应的点阵
 */
public class IARoomPoint {
	private String roomNum;

	private ArrayList<Point> roomPoint;

	public IARoomPoint(String roomNum, ArrayList<Point> roomPoint) {
		super();
		this.roomNum = roomNum;
		this.roomPoint = roomPoint;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public ArrayList<Point> getRoomPoint() {
		return roomPoint;
	}

	public void setRoomPoint(ArrayList<Point> roomPoint) {
		this.roomPoint = roomPoint;
	}

}
