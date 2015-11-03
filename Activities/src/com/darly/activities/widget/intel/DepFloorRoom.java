/**
 * 上午9:55:05
 * @author Zhangyuhui
 * $
 * DepFloorRoom.java
 * TODO
 */
package com.darly.activities.widget.intel;

import java.util.ArrayList;

import android.graphics.Point;

/**
 * @author Zhangyuhui DepFloorRoom $ 上午9:55:05 TODO 楼层中每个房间的具体信息。
 */
public class DepFloorRoom {
	private ArrayList<Point> points;
	private int[] deNo;
	private String roomId;
	private String deName;
	private String namePoi;
	private int status;

	public DepFloorRoom(ArrayList<Point> points, int[] deNo, String roomId,
			String deName, String namePoi, int status) {
		this.points = points;
		this.deNo = deNo;
		this.roomId = roomId;
		this.deName = deName;
		this.namePoi = namePoi;
		this.status = status;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public int[] getDeNo() {
		return deNo;
	}

	public void setDeNo(int[] deNo) {
		this.deNo = deNo;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getDeName() {
		return deName;
	}

	public void setDeName(String deName) {
		this.deName = deName;
	}

	public String getNamePoi() {
		return namePoi;
	}

	public void setNamePoi(String namePoi) {
		this.namePoi = namePoi;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
