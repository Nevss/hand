package com.darly.activities.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GridViewData implements Parcelable {

	public int id;
	public String url;

	public GridViewData(int id, String url) {
		super();
		this.id = id;
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(url);
	}
	
	public static final Creator<GridViewData> CREATOR = new Creator<GridViewData>() {
		
		@Override
		public GridViewData[] newArray(int size) {
			// TODO Auto-generated method stub
			
			return new GridViewData[size];
		}
		
		@Override
		public GridViewData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			
			return new GridViewData(source.readInt(), source.readString());
		}
	};

}
