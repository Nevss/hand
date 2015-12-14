/**
 * 下午1:51:45
 * @author Zhangyuhui
 * $
 * HorizontalListViewAdapter.java
 * TODO
 */
package com.darly.activities.widget.horilistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.darly.activities.R;
import com.darly.activities.app.Constract;

public class HorizontalListViewAdapter extends BaseAdapter {
	private int[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(Context context, int[] titles) {
		this.mContext = context;
		this.mTitles = titles;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.horizontal_list_item, null);
			LayoutParams lp = new LayoutParams(Constract.width / 4,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			convertView.setLayoutParams(lp);
			holder.mTitle = (Button) convertView
					.findViewById(R.id.text_list_item);
			holder.mTitle.setLayoutParams(lp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == selectIndex) {
			holder.mTitle.setTextColor(mContext.getResources().getColor(
					R.color.set_list_line));
			holder.mTitle
					.setBackgroundResource(R.drawable.app_main_header_backer);
			convertView.setSelected(true);
		} else {
			holder.mTitle.setTextColor(mContext.getResources().getColor(
					R.color.main_bottom_text));
			holder.mTitle
					.setBackgroundResource(R.drawable.app_main_header_normal);
			convertView.setSelected(false);
		}

		holder.mTitle.setText(mTitles[position]);

		return convertView;
	}

	private static class ViewHolder {
		private Button mTitle;
	}

	public void setSelectIndex(int i) {
		selectIndex = i;
		notifyDataSetChanged();
	}
}
