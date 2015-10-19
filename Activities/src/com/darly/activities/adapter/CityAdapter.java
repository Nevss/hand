package com.darly.activities.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darly.activities.model.BaseCityInfo;
import com.darly.activities.ui.R;

public class CityAdapter extends ParentAdapter<BaseCityInfo> {

	public CityAdapter(List<BaseCityInfo> data, int resID, Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			Context context, BaseCityInfo t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			hocker = new ViewHocker();
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker.tv = (TextView) view.findViewById(R.id.ia_guide_item_city);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		hocker.tv.setText(t.city_name);
		return view;
	}

	class ViewHocker {
		TextView tv;
	}
}
