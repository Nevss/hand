/**
 * 上午11:17:51
 * @author zhangyh2
 * $
 * SlideActivity.java
 * TODO
 */
package com.darly.oop.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.darly.oop.R;
import com.darly.oop.adapter.MainAdapter;
import com.darly.oop.adapter.MainDrawAdapter;
import com.darly.oop.base.APPEnum;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.common.ToastOOP;
import com.darly.oop.db.DBUtilsHelper;
import com.darly.oop.model.DarlyTableModel;
import com.darly.oop.model.Menu;
import com.darly.oop.model.Menu_Top;
import com.darly.oop.model.SimplePazzle;
import com.darly.oop.widget.slide.SlideView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zhangyh2 SlideActivity $ 上午11:17:51 TODO
 */
@ContentView(R.layout.activity_slide)
public class SlideActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	@ViewInject(R.id.slide_view)
	private SlideView slideView;
	@ViewInject(R.id.slide_list_content)
	private ListView lv;

	@ViewInject(R.id.slide_list_menu)
	private ListView drawerList;

	private MainAdapter adapter;

	private ArrayList<DarlyTableModel> data;

	private ArrayList<Menu> drawData;

	private MainDrawAdapter drawAdapter;
	@ViewInject(R.id.slide_view_group)
	private RadioGroup radioGroup;
	@ViewInject(R.id.slide_view_one)
	private RadioButton one;
	@ViewInject(R.id.slide_view_two)
	private RadioButton two;
	@ViewInject(R.id.slide_view_thr)
	private RadioButton thr;
	@ViewInject(R.id.slide_view_fou)
	private RadioButton fou;
	@ViewInject(R.id.slide_search)
	private AutoCompleteTextView autoText;
	@ViewInject(R.id.slide_search_btn)
	private Button btn;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		data = new ArrayList<DarlyTableModel>();

		data.add(new DarlyTableModel());

		adapter = new MainAdapter(data, R.layout.main_item, this);

		lv.setAdapter(adapter);

		drawerList.addHeaderView(LayoutInflater.from(this).inflate(
				R.layout.header_menu, null));

		drawListData();

		drawAdapter = new MainDrawAdapter(drawData, R.layout.item_drawer_view,
				this);

		drawerList.setAdapter(drawAdapter);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		radioGroup.setOnCheckedChangeListener(this);
		btn.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#loadData()
	 */
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		autoText.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, DBUtilsHelper
						.getInstance().findSimplePazzle()));
	}

	/**
	 * 
	 * 上午11:24:21
	 * 
	 * @author zhangyh2 MainActivity.java TODO 菜单列表数据
	 */
	private void drawListData() {
		// TODO Auto-generated method stub
		drawData = new ArrayList<Menu>();
		// 中间选项
		drawData.add(new Menu("", new Menu_Top(1, R.drawable.ic_index, "首页"),
				APPEnum.ITEMVIEW, APPEnum.ITEMSELECTVIEW, false));
		drawData.add(new Menu("", new Menu_Top(2, R.drawable.ic_me, "消息"),
				APPEnum.ITEMVIEW, APPEnum.ITEMSELECTVIEW, false));
		drawData.add(new Menu("",
				new Menu_Top(3, R.drawable.ic_set_press, "列表"),
				APPEnum.ITEMVIEW, APPEnum.ITEMSELECTVIEW, false));
		drawData.add(new Menu("",
				new Menu_Top(4, R.drawable.ic_me_press, "其他"),
				APPEnum.ITEMVIEW, APPEnum.ITEMSELECTVIEW, false));
		// 底部选项
		drawData.add(new Menu("个人设置", null, APPEnum.ITEMTITLE,
				APPEnum.ITEMSELECTITLE, false));
		drawData.add(new Menu("", new Menu_Top(5, R.drawable.ic_set, "设置"),
				APPEnum.ITEMVIEW, APPEnum.ITEMSELECTITLE, false));
		drawData.add(new Menu("", new Menu_Top(6, R.drawable.ic_loacl, "关于"),
				APPEnum.ITEMVIEW, APPEnum.ITEMSELECTITLE, false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		String msg = null;
		switch (checkedId) {
		case R.id.slide_view_one:
			msg = "slide_view_one";
			break;
		case R.id.slide_view_two:
			msg = "slide_view_two";
			break;
		case R.id.slide_view_thr:
			msg = "slide_view_thr";
			break;
		case R.id.slide_view_fou:
			msg = "slide_view_fou";
			break;

		default:
			break;
		}
		ToastOOP.showToast(this, msg);
		slideView.closeMenu();
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
		case R.id.slide_search_btn:
			String test = autoText.getText().toString().trim();
			SimplePazzle z = new SimplePazzle(test);
			if (test != null && !"".equals(test)) {
				if (!DBUtilsHelper.getInstance().findOne(z)) {
					DBUtilsHelper.getInstance().save(z);
					autoText.setAdapter(new ArrayAdapter<String>(this,
							android.R.layout.simple_list_item_1, DBUtilsHelper
									.getInstance().findSimplePazzle()));
				}
			}

			break;

		default:
			break;
		}
	}

}
