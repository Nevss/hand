package com.darly.activities.widget.pop;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.darly.activities.adapter.BottomGridViewAdapter;
import com.darly.activities.common.Literal;
import com.darly.activities.model.BottomModel;
import com.darly.activities.ui.R;

public class BottomPopSec extends PopupWindow implements OnItemClickListener {

	/**
	 * 下午1:29:10 TODO 系统参数。
	 */
	private Context context;

	private GridView grid;

	private ArrayList<BottomModel> data;

	private BottomGridViewAdapter adapter;

	private int height;

	private BottomModel checkedmodel;

	private Backer setOnbacker;

	/**
	 * 
	 * 上午9:32:39
	 * 
	 * @author Zhangyuhui BottomPop.java TODO
	 */
	public BottomPopSec(Context context) {
		super();
		this.context = context;
		init();
	}

	public BottomPopSec(Context context, int height, ArrayList<BottomModel> data) {
		super();
		this.context = context;
		this.height = height;
		this.data = data;
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

		grid = (GridView) view.findViewById(R.id.pop_grid_view);

		adapter = new BottomGridViewAdapter(data,
				R.layout.item_bottom_gridview, context);
		grid.setAdapter(adapter);

		grid.setOnItemClickListener(this);

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
		setHeight(Literal.height - height);
		setWidth(Literal.width);
		setContentView(view);
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
		checkedmodel = (BottomModel) parent.getItemAtPosition(position);
		dismiss();
		setOnbacker.getBacker(checkedmodel);
	}

	/**
	 * @return the checkedmodel
	 */
	public BottomModel getCheckedmodel() {
		return checkedmodel;
	}

	public interface Backer {
		public void getBacker(BottomModel checked);
	}

	public void setSetOnbacker(Backer setOnbacker) {
		this.setOnbacker = setOnbacker;
	}

}
