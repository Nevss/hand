package com.darly.oop.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.darly.oop.R;
import com.darly.oop.adapter.MainAdapter;
import com.darly.oop.adapter.MainDrawAdapter;
import com.darly.oop.base.APPEnum;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.db.DBMongo;
import com.darly.oop.model.DarlyTableModel;
import com.darly.oop.model.Menu;
import com.darly.oop.model.Menu_Top;
import com.darly.oop.ui.fragment.HomeFragment;
import com.darly.oop.ui.fragment.MainFragment;
import com.darly.oop.ui.fragment.MeFragment;
import com.darly.oop.ui.fragment.SetFragment;
import com.darly.oop.ui.inditorviewpager.InditorViewpage;
import com.darly.oop.widget.share.CustomShareBoard;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnItemClickListener {
	/**
	 * 下午2:46:54 TODO 测试ListView
	 */
	@ViewInject(R.id.main_list)
	protected ListView lv;
	@ViewInject(R.id.main_plugs)
	protected Button plugs;
	@ViewInject(R.id.header_back)
	protected ImageView back;
	@ViewInject(R.id.header_title)
	protected TextView title;
	@ViewInject(R.id.header_other)
	protected ImageView other;
	@ViewInject(R.id.drawer_drawer)
	protected DrawerLayout drawer;
	@ViewInject(R.id.drawer_list)
	protected ListView drawerList;

	private MainController controller;

	protected MainAdapter adapter;


	public CustomShareBoard shareBoard;

	protected ArrayList<DarlyTableModel> data;

	protected ArrayList<Menu> drawData;

	private MainDrawAdapter drawAdapter;

	private long firstime;

	/**
	 * TODO首页展示效果哦Fragment
	 */
	private MainFragment index;
	/**
	 * TODO用户自己页面展示Fragment
	 */
	private MeFragment me;

	/**
	 * 下午6:03:37 TODO美女列表Fragment
	 */
	private HomeFragment home;
	/**
	 * TODO用户信息设置Fragment
	 */
	private SetFragment set;
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		title.setText(R.string.header_main);

		back.setImageResource(R.drawable.ic_menu_select);

		other.setImageResource(R.drawable.ic_menu_select);
		other.setVisibility(View.INVISIBLE);

		drawer.closeDrawers();

		controller = new MainController(this);
		shareBoard = new CustomShareBoard(this);
		shareBoard.setWXCallBack(controller);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(controller);

		lv.setOnItemClickListener(controller);
		plugs.setOnClickListener(controller);

		drawerList.setOnItemClickListener(this);
		DBMongo.getInstance().setOnMongoListener(controller);
		String lastesVersion = 12 + "";
		String versionCode = 9 + "";
		Log.i("版本", lastesVersion.compareTo(versionCode + "") + "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#loadData()
	 */
	@Override
	protected void loadData() {
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
		
		drawerList.setItemChecked(1, true);

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
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requst, int result, Intent data) {
		// TODO Auto-generated method stub
		controller.onActivityResult(requst, result, data);
		super.onActivityResult(requst, result, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - firstime > 3000) {
				Toast.makeText(MainActivity.this, "再按一次,退出程序",
						Toast.LENGTH_SHORT).show();
				firstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
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
		Menu menu = (Menu) parent.getItemAtPosition(position);
		if (menu == null) {
			return;
		}
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (menu.selectType == APPEnum.ITEMSELECTVIEW) {
			for (Menu menus : drawData) {
				menus.isSelect = false;
			}
			menu.isSelect = true;
		}
		// 个人中心设置列表。跳转各自Activity。
		switch (menu.tops.typeID) {
		case 1:
			hideFragments(ft);
			if (index != null) {
				if (index.isVisible())
					return;
				ft.show(index);
			} else {
				index = new MainFragment();
				ft.add(R.id.main_frame, index);
			}
			ft.commit();
			drawAdapter.setData(drawData);
			break;
		case 2:
			hideFragments(ft);
			if (me != null) {
				if (me.isVisible())
					return;
				ft.show(me);
			} else {
				me = new MeFragment();
				ft.add(R.id.main_frame, me);
			}
			ft.commit();
			drawAdapter.setData(drawData);
			break;
		case 3:
			hideFragments(ft);
			if (home != null) {
				if (home.isVisible())
					return;
				ft.show(home);
			} else {
				home = new HomeFragment();
				ft.add(R.id.main_frame, home);
			}
			ft.commit();
			drawAdapter.setData(drawData);
			break;
		case 4:
			hideFragments(ft);
			if (set != null) {
				if (set.isVisible())
					return;
				ft.show(set);
			} else {
				set = new SetFragment();
				ft.add(R.id.main_frame, set);
			}
			ft.commit();
			drawAdapter.setData(drawData);
			break;
		case 5:
			startActivity(new Intent(this, InditorViewpage.class));
			break;
		case 6:
			startActivity(new Intent(this, InditorViewpage.class));
			break;

		default:
			break;
		}
		drawer.closeDrawers();
	}
	
	
	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (index != null) {
			transaction.hide(index);
		}
		if (me != null) {
			transaction.hide(me);
		}
		if (set != null) {
			transaction.hide(set);
		}
		if (home != null) {
			transaction.hide(home);
		}
	}

}
