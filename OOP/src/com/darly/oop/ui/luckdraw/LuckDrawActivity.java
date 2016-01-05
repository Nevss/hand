/**
 * 上午10:19:40
 * @author zhangyh2
 * $
 * LuckDrawActivity.java
 * TODO
 */
package com.darly.oop.ui.luckdraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.adapter.LuckDrawAdapter;
import com.darly.oop.base.APPEnum;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.model.LuckDraw;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zhangyh2 LuckDrawActivity $ 上午10:19:40 TODO
 */
@ContentView(R.layout.activity_luckdraw)
public class LuckDrawActivity extends BaseActivity implements OnClickListener {

	/**
	 * 上午10:25:05 TODO 奖品集合（包括奖品数量和奖品详细参数）
	 */
	private List<LuckDraw> data;
	@ViewInject(R.id.header_back)
	private ImageView back;
	@ViewInject(R.id.header_title)
	private TextView title;
	@ViewInject(R.id.header_other)
	private ImageView other;
	@ViewInject(R.id.luck_success)
	private TextView tv;
	@ViewInject(R.id.luck_list)
	private ListView lv;
	@ViewInject(R.id.luck_draw)
	private LuckDrawView view;

	/**
	 * 上午10:53:22 TODO 已经获取的奖品
	 */
	private List<LuckDraw> drawData;

	private LuckDrawAdapter adapter;

	private int result;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		title.setText("抽奖活动");
		tv.setText("获奖结果");
		view.setLayoutParams(new LayoutParams(APPEnum.WIDTH.getLen() / 2,
				APPEnum.WIDTH.getLen() / 2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		view.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#loadData()
	 */
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		getData();
		view.setPrize(data);
		adapter = new LuckDrawAdapter(drawData, R.layout.item_luckdraw_view,
				this);
		lv.setAdapter(adapter);

	}

	/**
	 * @return the data
	 */
	public List<LuckDraw> getData() {
		data = new ArrayList<LuckDraw>();
		drawData = new ArrayList<LuckDraw>();
		for (int i = 0; i < 12; i++) {
			data.add(new LuckDraw(i, "" + i, i + "hello"));
		}
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.header_back:
			finish();
			break;
		case R.id.luck_draw:
			result = new Random().nextInt(data.size()) + 1;
			float degree = result * (360 / data.size() + 360);
			RotateAnimation animation = new RotateAnimation(0, degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(300);
			animation.setFillAfter(true);
			animation.setRepeatCount(5);
			animation.setInterpolator(new AccelerateDecelerateInterpolator());
			animation.setRepeatMode(Animation.INFINITE);
			view.startAnimation(animation);

			drawData.add(data.get(data.size() - result));
			adapter.setData(drawData);
			break;
		default:
			break;
		}
	}

}
