/**
 * 下午2:47:54
 * @author zhangyh2
 * $
 * MainController.java
 * TODO
 */
package com.darly.oop.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.darly.oop.common.ToastOOP;
import com.darly.oop.db.DBMongo;
import com.darly.oop.model.DarlyModel;
import com.darly.oop.model.DarlyTableModel;
import com.darly.oop.ui.mongo.MainInsertActivity;
import com.darly.oop.ui.mongo.MainUpdataActivity;
import com.darly.oop.ui.plug.PlugActivity;
import com.darly.oop.widget.share.WXCallBack;
import com.google.gson.Gson;

/**
 * @author zhangyh2 MainController $ 下午2:47:54 TODO MainActivity的控制类。
 */
public class MainController implements OnItemClickListener, OnClickListener,
		WXCallBack, DBMongo.MongoListener {

	public MainActivity activity;

	/**
	 * 上午11:00:27 TODO 选中的数据库单挑信息。
	 */
	private DarlyTableModel singleModel;

	public MainController(MainActivity activity) {
		this.activity = activity;
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
		if (!DBMongo.isMongoOnline) {
			return;
		}
		if (position == activity.data.size() - 1) {
			DBMongo.getInstance().select("darly");
		} else {
			singleModel = (DarlyTableModel) parent.getItemAtPosition(position);
			postShare();
			Intent intent = null;
			switch (position % 3) {
			case 0:
				DBMongo.getInstance().delete("darly", singleModel);
				break;
			case 1:
				intent = new Intent(activity, MainUpdataActivity.class);
				intent.putExtra("data", singleModel);
				activity.startActivityForResult(intent, APPEnum.DB_UPDATA);
				break;
			case 2:
				intent = new Intent(activity, MainInsertActivity.class);
				intent.putExtra("data", singleModel);
				activity.startActivityForResult(intent, APPEnum.DB_INSERT);
				break;
			default:
				break;
			}

		}
	}

	/**
	 * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
	 * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
	 */
	private void postShare() {
		// activity.shareBoard.showAtLocation(activity.getWindow().getDecorView(),
		// Gravity.BOTTOM, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.db.DBMongo.MongoListener#selectDB(java.lang.Object)
	 */
	@Override
	public void selectDB(final Object object) {
		// TODO Auto-generated method stub
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				DarlyModel model = parseJson(object.toString());
				if (model != null) {
					activity.data.clear();
					activity.data.add(new DarlyTableModel());
					for (String table : model.data) {
						DarlyTableModel tableModel = parseJsonTable(table);
						activity.data.add(activity.data.size() - 1, tableModel);
					}
					ToastOOP.showToast(activity, model.msg);
					activity.adapter.setData(activity.data);
				}

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.db.DBMongo.MongoListener#updateDB(java.lang.Object)
	 */
	@Override
	public void updateDB(final Object object) {
		// TODO Auto-generated method stub
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DarlyModel model = parseJson(object.toString());
				if (model != null) {
					ToastOOP.showToast(activity, model.msg);
					DBMongo.getInstance().select("darly");
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.db.DBMongo.MongoListener#insertDB(java.lang.Object)
	 */
	@Override
	public void insertDB(final Object object) {
		// TODO Auto-generated method stub
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DarlyModel model = parseJson(object.toString());
				if (model != null) {
					ToastOOP.showToast(activity, model.msg);
					DBMongo.getInstance().select("darly");
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.db.DBMongo.MongoListener#deleteDB(java.lang.Object)
	 */
	@Override
	public void deleteDB(final Object object) {
		// TODO Auto-generated method stub
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DarlyModel model = parseJson(object.toString());
				if (model != null) {
					ToastOOP.showToast(activity, model.msg);
					DBMongo.getInstance().select("darly");
				}
			}
		});

	}

	private DarlyModel parseJson(String json) {
		if (json == null) {
			return null;
		}
		try {
			DarlyModel darlyModel = new Gson().fromJson(json, DarlyModel.class);
			return darlyModel;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private DarlyTableModel parseJsonTable(String json) {
		if (json == null) {
			return null;
		}
		try {
			DarlyTableModel darlyModel = new Gson().fromJson(json,
					DarlyTableModel.class);
			return darlyModel;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param requst
	 * @param result
	 * @param data
	 *            上午10:59:03
	 * @author zhangyh2 MainController.java TODO
	 */
	public void onActivityResult(int requst, int result, Intent data) {
		// TODO Auto-generated method stub
		if (result != Activity.RESULT_OK) {
			return;
		}
		DarlyTableModel model = null;
		if (data != null) {
			model = data.getParcelableExtra("model");
		}

		switch (requst) {
		case APPEnum.DB_INSERT:
			DBMongo.getInstance().insert("darly", model);
			break;
		case APPEnum.DB_UPDATA:
			if (data != null) {
				DBMongo.getInstance().update("darly", singleModel, model);
			}
			break;
		case APPEnum.DB_DELETE:

			break;

		default:
			break;
		}
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
		case R.id.main_plugs:
			activity.startActivity(new Intent(activity, PlugActivity.class));
			break;
		case R.id.header_back:
			activity.drawer.openDrawer(Gravity.LEFT);
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.widget.share.WXCallBack#shareComplete(boolean)
	 */
	@Override
	public void shareComplete(boolean flag) {
		// TODO Auto-generated method stub
		Log.e("flag", "jsdiaoyong");
	}

}
