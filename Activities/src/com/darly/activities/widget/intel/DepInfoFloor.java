/**
 * 上午9:53:19
 * @author Zhangyuhui
 * $
 * DepInfoFloor.java
 * TODO
 */
package com.darly.activities.widget.intel;

import java.util.ArrayList;

/**
 * @author Zhangyuhui DepInfoFloor $ 上午9:53:19 TODO 机构分多层。以楼层概念进行区分。
 *         本类中包含楼层的所有信息。
 */
public class DepInfoFloor {
	private int floorId;
	private ArrayList<DepFloorRoom> rooms;
	private String floorBackground;

	public DepInfoFloor(int floorId, ArrayList<DepFloorRoom> rooms,
			String floorBackground) {
		this.floorId = floorId;
		this.rooms = rooms;
		this.floorBackground = floorBackground;
	}

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	public ArrayList<DepFloorRoom> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<DepFloorRoom> rooms) {
		this.rooms = rooms;
	}

	public String getFloorBackground() {
		return floorBackground;
	}

	public void setFloorBackground(String floorBackground) {
		this.floorBackground = floorBackground;
	}

}
