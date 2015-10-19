package com.darly.activities.widget.pop;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.darly.activities.adapter.BottomGridViewAdapter;
import com.darly.activities.common.BaseData;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.BottomModel;
import com.darly.activities.model.GridViewData;
import com.darly.activities.ui.IndexShowViewActivity;
import com.darly.activities.ui.MeDetailsAcitvity;
import com.darly.activities.ui.R;
import com.darly.activities.ui.RotateAcitvity;
import com.darly.activities.ui.qrcode.MipcaActivityCapture;
import com.darly.activities.widget.pop.BottomPopSec.Backer;

public class BottomPop extends PopupWindow implements OnItemClickListener,
		OnItemLongClickListener {

	private final String TAG = getClass().getSimpleName();

	/**
	 * 下午1:29:10 TODO 系统参数。
	 */
	private Context context;

	private GridView grid;

	private ArrayList<BottomModel> data;

	private BottomGridViewAdapter adapter;

	private View v;

	private Backer backer;

	private ArrayList<BottomModel> secData;

	/**
	 * 
	 * 上午9:32:39
	 * 
	 * @author Zhangyuhui BottomPop.java TODO
	 */
	public BottomPop(Context context) {
		super();
		this.context = context;
		init();
	}

	public BottomPop(Context context, View v) {
		super();
		this.context = context;
		this.v = v;
		init();
	}

	/**
	 * 
	 * 下午1:29:54
	 * 
	 * @author Zhangyuhui PhotoPop.java TODO 初始化控件集合。
	 */
	private void init() {
		// TODO Auto-generated method stub

		View view = LayoutInflater.from(context).inflate(R.layout.pop_bottom,
				null);

		data = new ArrayList<BottomModel>();
		data.add(new BottomModel(R.drawable.ic_add, null, null));

		secData = BaseData.getData();
		grid = (GridView) view.findViewById(R.id.pop_grid_view);
		adapter = new BottomGridViewAdapter(data,
				R.layout.item_bottom_gridview, context);
		grid.setAdapter(adapter);

		grid.setOnItemClickListener(this);
		grid.setOnItemLongClickListener(this);

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (isShowing()) {
					dismiss();
				}
				return false;
			}
		});

		backer = new Backer() {

			@Override
			public void getBacker(BottomModel checked) {
				// TODO Auto-generated method stub
				secData.remove(checked);
				reDataList(checked);
			}
		};
		setHeight(Literal.height - v.getHeight());
		setWidth(Literal.width);
		setContentView(view);
	}

	BottomModel model;
	int pos = -100;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub长按，不仅仅添加新选项，而且还要删除此选项，替换成新选择的选项。

		model = (BottomModel) parent.getItemAtPosition(position);
		if (model.getT() != null) {
			secData.add(model);
			BottomPopSec popSec = new BottomPopSec(context, grid.getHeight(),
					secData);
			pos = position;
			popSec.setSetOnbacker(backer);
			if (popSec.isShowing()) {
				popSec.dismiss();
			} else {
				popSec.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BottomModel model = (BottomModel) parent.getItemAtPosition(position);
		LogApp.i(TAG, model.toString());
		if (model.getT() == null) {
			ToastApp.showToast(context, "需要添加项目了。");
			BottomPopSec popSec = new BottomPopSec(context, grid.getHeight(),
					secData);
			popSec.setSetOnbacker(backer);
			if (popSec.isShowing()) {
				popSec.dismiss();
			} else {
				popSec.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}

		} else {
			intentTo(model);

		}
	}

	/**
	 * 
	 * 下午1:26:43
	 * 
	 * @author Zhangyuhui BottomPop.java TODO
	 * @param model
	 */
	private void intentTo(BottomModel model) {
		// TODO Auto-generated method stub
		switch (model.getResid()) {
		case R.drawable.ic_index_press:
			context.startActivity(new Intent(context,
					MipcaActivityCapture.class));
			break;
		case R.drawable.ic_set_press:
			context.startActivity(new Intent(context,
					IndexShowViewActivity.class));
			break;
		case R.drawable.ic_me_press:
			ArrayList<GridViewData> gridData = new ArrayList<GridViewData>();
			for (int i = 0; i < BaseData.IMAGES.length; i++) {
				gridData.add(new GridViewData(i, BaseData.IMAGES[i]));
			}
			Intent intent = new Intent(context, MeDetailsAcitvity.class);
			intent.putParcelableArrayListExtra("GridViewData", gridData);
			context.startActivity(intent);
			break;
		case R.drawable.ic_search:
			context.startActivity(new Intent(context, RotateAcitvity.class));
			break;
		default:
			break;
		}

		this.dismiss();
	}

	/**
	 * 
	 * 下午5:15:10
	 * 
	 * @author Zhangyuhui BottomPop.java TODO
	 */
	public void reDataList(BottomModel checked) {
		// TODO Auto-generated method stub
		if (pos != -100) {
			data.remove(pos);
			data.add(pos, checked);
			pos = -100;
		} else {
			data.add(data.size() - 1, checked);
		}
		adapter.setData(data);
	}

}
