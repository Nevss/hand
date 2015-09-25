/**
 * 2015年9月15日
 * RoomInfor.java
 * com.darly.interlgent.model
 * @auther Darly Fronch
 * 下午2:28:20
 * RoomInfor
 * TODO
 */
package com.darly.activities.model;

import java.util.ArrayList;

import android.graphics.Point;

/**
 * 2015年9月15日 RoomInfor.java com.darly.interlgent.model
 * 
 * @auther Darly Fronch 下午2:28:20 RoomInfor
 *         TODO一个中间类，将房间对应信息，以及房间状态，房间的点阵全部记录下来。
 */
public class RoomInfor {
	/**
	 * TODO房间号码
	 */
	private String roomNum;

	/**
	 * TODO对应科室ID
	 */
	private int departId;

	/**
	 * TODO对应房间的状态（已检，未检，下一项）；
	 */
	private int roomStauts;

	/**
	 * TODO房间点阵
	 */
	private ArrayList<Point> roomPoint;

	public RoomInfor(String roomNum, int departId, int roomStauts,
			ArrayList<Point> roomPoint) {
		super();
		this.roomNum = roomNum;
		this.departId = departId;
		this.roomStauts = roomStauts;
		this.roomPoint = roomPoint;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public int getDepartId() {
		return departId;
	}

	public void setDepartId(int departId) {
		this.departId = departId;
	}

	public int getRoomStauts() {
		return roomStauts;
	}

	public void setRoomStauts(int roomStauts) {
		this.roomStauts = roomStauts;
	}

	public ArrayList<Point> getRoomPoint() {
		return roomPoint;
	}

	public void setRoomPoint(ArrayList<Point> roomPoint) {
		this.roomPoint = roomPoint;
	}

}
