/**
 * 下午2:11:00
 * @author zhangyh2
 * $
 * PlugActivity.java
 * TODO
 */
package com.darly.oop.ui.plug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apkplug.Bundle.InstallBundler;
import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.BundleContext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.darly.oop.R;
import com.darly.oop.adapter.PlugBundleAdapter;
import com.darly.oop.base.APP;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.common.ToastOOP;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zhangyh2 PlugActivity $ 下午2:11:00 TODO 插件页面展示类。
 */
@ContentView(R.layout.activity_plug)
public class PlugActivity extends BaseActivity implements OnItemClickListener,
		OnItemLongClickListener {

	private FrameworkInstance frame = null;

	@ViewInject(R.id.plug_list)
	private GridView bundlelist;
	private PlugBundleAdapter adapter = null;
	public Handler mHandler;

	private List<PlugsModel> data;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		frame = APP.getInstance().getFrame();

		data = new ArrayList<PlugsModel>();

		String[] filStrings = null;
		try {
			filStrings = getResources().getAssets().list("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < filStrings.length; i++) {
			if (filStrings[i].endsWith(".apk")) {
				data.add(new PlugsModel(false, null, filStrings[i]));
			}
		}
	}

	/**
	 * @param context
	 * @return 上午11:47:39
	 * @author zhangyh2 PlugActivity.java TODO
	 */
	private void getData() {
		BundleContext context = frame.getSystemBundleContext();
		for (int i = 0; i < context.getBundles().length; i++) {
			// 获取已安装插件
			if (context.getBundles()[i].getState() == 2) {
				for (PlugsModel plug : data) {
					if (context.getBundles()[i].getLocation().contains(
							plug.name)) {
						plug.bundle = context.getBundles()[i];
						plug.install = true;
					}
				}
			}
		}
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
		adapter = new PlugBundleAdapter(data, R.layout.item_bundle_listview,
				this);
		bundlelist.setAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		bundlelist.setOnItemClickListener(this);
		bundlelist.setOnItemLongClickListener(this);
	}

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
		// TODO Auto-generated method stub
		final PlugsModel t = (PlugsModel) parent.getItemAtPosition(position);
		if (t.install && t.bundle != null) {
			AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
			alertbBuilder
					.setMessage("是否卸载此插件？")
					.setNegativeButton("卸载",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 直接使用 Bundle.uninstall()卸载
									try {
										t.bundle.uninstall();
										// 卸载完成刷新数据
										for (PlugsModel plug : data) {
											Log.i("PlugsModel", plug.name
													+ plug.bundle
													+ plug.install);
											if (t.bundle.getLocation().equals(
													plug.bundle.getLocation())) {
												plug.bundle = null;
												plug.install = false;
												break;
											}
										}
										adapter.setData(data);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										ToastOOP.showToast(PlugActivity.this,
												"卸载出现问题。请重新启动应用！");
									}

									dialog.cancel();
								}
							}).create();
			alertbBuilder.show();
		}

		return false;
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

		PlugsModel t = (PlugsModel) parent.getItemAtPosition(position);
		if (t.install && t.bundle != null) {
			if (t.bundle.getState() != org.osgi.framework.Bundle.ACTIVE) {
				// 判断插件是否已启动
				try {
					t.bundle.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (t.bundle.getBundleActivity() != null) {
				Toast.makeText(this,
						"启动" + t.bundle.getBundleActivity().split(",")[0],
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent();
				i.setClassName(this, t.bundle.getBundleActivity().split(",")[0]);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.startActivity(i);
			} else {
				Toast.makeText(this, "该插件没有配置BundleActivity",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			installs(t.name);
		}

	}

	private void installs(String name) {
		BundleContext context = frame.getSystemBundleContext();
		InstallBundler ib = new InstallBundler(context);
		ib.installForAssets(name, null, null, null);
		getData();
		adapter.setData(data);
	}
}
