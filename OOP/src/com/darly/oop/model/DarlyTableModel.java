/**
 * 上午9:46:05
 * @author zhangyh2
 * $
 * DarlyTableModel.java
 * TODO
 */
package com.darly.oop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhangyh2 DarlyTableModel $ 上午9:46:05 TODO [{ \"_id\" : { \"$oid\" :
 *         \"565d6af07086c30b60eab1dd\"} , \"name\" : \"小偷\" , \"age\" : 24 ,
 *         \"sex\" : \"男\" , \"time\" : \"1986-10-03\"}
 */
public class DarlyTableModel implements Parcelable {

	public String name;

	public int age;

	public String sex;

	public String time;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeInt(age);
		dest.writeString(sex);
		dest.writeString(time);
	}

	// 用来创建自定义的Parcelable的对象
	public static final Parcelable.Creator<DarlyTableModel> CREATOR = new Creator<DarlyTableModel>() {

		@Override
		public DarlyTableModel createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			DarlyTableModel model = new DarlyTableModel();
			model.name = source.readString();
			model.age = source.readInt();
			model.sex = source.readString();
			model.time = source.readString();
			return model;
		}

		@Override
		public DarlyTableModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DarlyTableModel[size];
		}
	};
}
