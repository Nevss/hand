/**
 * 下午2:51:49
 * @author zhangyh2
 * $
 * MainAdapter.java
 * TODO
 */
package com.darly.oop.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.darly.oop.model.DarlyTableModel;
import com.darly.oop.ui.download.DownLoadPop;

/**
 * @author zhangyh2 MainAdapter $ 下午2:51:49 TODO
 */
public class MainAdapter extends ParentAdapter<DarlyTableModel> {

	/**
	 * @param data
	 * @param resID
	 * @param context
	 *            下午2:52:10
	 * @author zhangyh2 MainAdapter.java TODO
	 */
	public MainAdapter(List<DarlyTableModel> data, int resID, Context context) {
		super(data, resID, context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.adapter.ParentAdapter#HockView(int, android.view.View,
	 * android.view.ViewGroup, int, android.content.Context, java.lang.Object)
	 */
	@Override
	public View HockView(int position, View view, ViewGroup parent, int resID,
			Context context, DarlyTableModel t) {
		// TODO Auto-generated method stub
		ViewHocker hocker = null;
		if (view == null) {
			hocker = new ViewHocker();
			view = LayoutInflater.from(context).inflate(resID, null);
			hocker.tv = (TextView) view.findViewById(R.id.main_item_tv);
			hocker.bt = (Button) view.findViewById(R.id.main_item_anim);
			view.setTag(hocker);
		} else {
			hocker = (ViewHocker) view.getTag();
		}
		hocker.bt.setOnClickListener(new Click());
		if (getCount() - 1 == position) {
			hocker.tv.setText("点击查找已存在用户");
			hocker.bt.setText("断点续传");
			hocker.bt.setClickable(true);
		} else {
			hocker.bt.setClickable(false);
			hocker.tv.setText("姓名" + t.name + "年龄" + t.age + "性别" + t.sex
					+ "生日" + t.time);
			switch (position % 3) {
			case 0:
				hocker.bt.setText("delete");
				hocker.tv.setTextColor(Color.RED);
				break;
			case 1:
				hocker.bt.setText("updata");
				hocker.tv.setTextColor(Color.BLUE);
				break;
			case 2:
				hocker.bt.setText("insert");
				hocker.tv.setTextColor(Color.GREEN);
				break;
			default:
				break;
			}

		}
		return view;
	}

	class Click implements OnClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 进行断点续传代码混合。弹出一个窗口，进行文件下载。
			DownLoadPop downLoadPop = new DownLoadPop(context,
					APPEnum.DOWNLOADPATH.getDec(), v,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			downLoadPop.showAtLocation(v, Gravity.CENTER, 0, 0);
		}

	}

	class ViewHocker {
		TextView tv;
		Button bt;
	}

}
