package com.darly.activities.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.darly.activities.app.AppStack;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.IAPoisDataConfig;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.common.PreferencesJsonCach;
import com.darly.activities.model.IARoomName;
import com.darly.activities.model.IARoomNameHttp;
import com.darly.activities.model.IARoomPoint;
import com.darly.activities.model.OrgBase;
import com.darly.activities.model.OrgBaseData;
import com.darly.activities.model.RoomInfor;
import com.darly.activities.poll.HttpTasker;
import com.darly.activities.poll.ThreadPoolManager;
import com.darly.activities.widget.intel.BaseInterlgent;
import com.darly.activities.widget.intel.InterlgentUtil;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author Zhangyuhui IndexShowViewActivity 上午9:01:37 TODO
 *         展示机构平面图页面，取自帮忙医项目的智能导检。
 */
@ContentView(R.layout.activity_index_zoom_view)
public class IndexZoomViewActivity extends BaseActivity {

	/**
	 * TODO线程管理
	 */
	private ThreadPoolManager manager;
	/**
	 * TODO生成图层
	 */
	public BaseInterlgent interlgent;
	/**
	 * TODO图层容器
	 */
	@ViewInject(R.id.index_zoom_container)
	private RelativeLayout main_container;
	/**
	 * TODO计时器
	 */
	private Timer timer;

	/**
	 * TODO房间全部信息
	 */
	public ArrayList<RoomInfor> roomInfo;

	/**
	 * 咨询链接
	 */
	private String infoUrl = "http://test.rayelink.com/APIAccount/GetOrganizationInfo";
	/**
	 * 数据链接
	 */
	private String dataUrl = "http://test.rayelink.com/APIQueuingSystem/GetData";

	private final int KEEP = 1000;
	private final int NEXTKEEP = 10000;

	/**
	 * TODO 加载过场动画类
	 */
	private ProgressDialogUtil loading;

	/**
	 * 下午2:07:13
	 * 
	 * @author Zhangyuhui IndexZoomViewActivity.java TODO关闭按钮
	 */
	@ViewInject(R.id.ia_show_image_consel)
	private ImageView consel;

	private int selectOrgID;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.index_zoom_container:
			break;
		case R.id.ia_show_image_consel:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		loading = new ProgressDialogUtil(this);
		loading.setMessage("加载中...");
		loading.show();

		selectOrgID = getIntent().getIntExtra("selectOrg", 0);

		main_container.setLayoutParams(new LayoutParams(Literal.height,
				Literal.width));
		Literal.bitmapheight = Literal.width * IAPoisDataConfig.babaibanh
				/ IAPoisDataConfig.babaibanw;
		Literal.bitmapwidth = Literal.width;
		initImageAndThread();
		firstStep();
	}

	/**
	 * 上午11:00:34
	 * 
	 * @author Zhangyuhui MainActivity.java TODO初始化ImageLoader和线程池选项
	 */
	private void initImageAndThread() {
		manager = ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_FIFO, 5);
	}

	/**
	 * 
	 * 上午11:03:04
	 * 
	 * @author Zhangyuhui MainActivity.java
	 *         TODO判断网络是否正常。正常则继续请求数据，异常状态使用上次缓存下来资料
	 */
	public void firstStep() {
		String info = PreferencesJsonCach.getInfo("GETINFO" + selectOrgID, this);

		// 初次没有缓存则直接跳过
		if (info != null) {
			getOrgAndPoint(new Gson().fromJson(info, IARoomNameHttp.class));
		}
		// 初次没有缓存则直接跳过

		if (!AppStack.isNetworkConnected(this)) {
			if (loading != null) {
				loading.dismiss();
			}
			Toast.makeText(this, "网络异常，请检查网络！", KEEP).show();
			String data = PreferencesJsonCach.getInfo("GETDATA" + selectOrgID, this);
			if (data != null) {
				interlgent.ReDraw(setInfoRoom(
						new Gson().fromJson(data, OrgBase.class).getModel(),
						roomInfo));
			}
		} else {
			// 请求服务器平面图数据。
			JSONObject object = new JSONObject();
			try {
				object.put("OrganizationID", selectOrgID);
			} catch (Exception e) {
				// TODO: handle exception
			}
			ArrayList<BasicNameValuePair> par = new ArrayList<BasicNameValuePair>();
			par.add(new BasicNameValuePair("param", object.toString()));
			manager.start();
			// 获取屏幕的宽高。这几
			manager.addAsyncTask(new HttpTasker(IndexZoomViewActivity.this,
					par, infoUrl, null, handler, true, Literal.GET_HANDLER,
					true));
			// 请求服务器平面图数据。
		}
	}

	/**
	 * @param roomOrgpari
	 * @auther Darly Fronch 2015 下午2:20:34 TODO获取机构信息类，即获取房间功能对应表格roomOrgpari
	 */
	public void getOrgAndPoint(IARoomNameHttp roomOrgpari) {
		// -----------如何建立关系----------
		int[] arg = null;
		switch (selectOrgID) {
		case 12:
			arg = getResources().getIntArray(R.array.jingan_roomnub);
			break;
		case 24:
			arg = getResources().getIntArray(R.array.xuhui_roomnub);
			break;
		case 31:
			arg = getResources().getIntArray(R.array.babaiban_roomnub);
			break;
		default:
			break;
		}

		ArrayList<IARoomPoint> point = IAPoisDataConfig.getModelTest(arg,
				selectOrgID);
		roomInfo = getRoomInfr(roomOrgpari.model, point/*
														 * roomOrgpari.point
														 * 由于服务器暂时还未传递，制造假数据
														 */);
		// -----------如何建立关系----------
		if (interlgent == null) {
			main_container.removeAllViews();
			interlgent = new BaseInterlgent(this, roomInfo);
			main_container.addView(interlgent);
			setViewFullScreen();
		}
		String url = roomOrgpari.Organizationplan;
		final String name = url.substring(url.lastIndexOf("/") + 1,
				url.length());
		LogApp.i(TAG, name);
		File file = new File(Literal.SROOT + name);
		if (file.exists()) {
			Bitmap tempBitmap = BitmapFactory.decodeFile(Literal.SROOT + name);
			interlgent.setBackGroud(tempBitmap);
		} else {

			// 获取到背景图片后进行Bitmap缓存。
			imageLoader.loadImage(roomOrgpari.Organizationplan,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							// TODO Auto-generated method stub

							Bitmap back = InterlgentUtil.zoomImage(arg2,
									Literal.width, Literal.width
											* IAPoisDataConfig.babaibanh
											/ IAPoisDataConfig.babaibanw);
							LogApp.i(back.toString());
							interlgent.setBackGroud(back);
							// 将Bitmap进行数据保存到文件。
							PreferencesJsonCach.saveBitmap(
									Literal.SROOT + name, back);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});

		}
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		consel.setOnClickListener(this);
	}

	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
		Log.i("handler", "Literal.GETINFO");
		if (object != null) {
			String jsonInfo = (String) object;
			PreferencesJsonCach.putValue("GETINFO" + selectOrgID, jsonInfo, this);
			IARoomNameHttp roomOrgpari = new Gson().fromJson(jsonInfo,
					IARoomNameHttp.class);
			if (roomOrgpari != null && roomOrgpari.model != null) {
				getOrgAndPoint(roomOrgpari);
				getDataFHttp();
			}
		}
	}

	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub
		Log.i("handler", "Literal.GETDATA");
		if (object != null) {
			String jsonData = (String) object;
			PreferencesJsonCach.putValue("GETDATA" + selectOrgID, jsonData, this);
			OrgBase base = new Gson().fromJson(jsonData, OrgBase.class);
			startTimer();
			interlgent.ReDraw(setInfoRoom(base.getModel(), roomInfo));
		}
	}

	/**
	 * @auther Darly Fronch 2015 下午2:12:53 TODO启动计时，每隔10s进行一次数据刷新
	 */
	public void startTimer() {
		// TODO Auto-generated method stub
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub\
					getDataFHttp();
				}
			}, KEEP, NEXTKEEP);
		}
	}

	/**
	 * @auther Darly Fronch 2015 下午2:54:39 TODO生成全部信息
	 */
	private ArrayList<RoomInfor> getRoomInfr(ArrayList<IARoomName> model,
			ArrayList<IARoomPoint> point) {
		ArrayList<RoomInfor> room = new ArrayList<RoomInfor>();
		// 获取所有对应关系表格。
		for (int i = 0, len = point.size(); i < len; i++) {
			for (int j = 0, dat = model.size(); j < dat; j++) {
				if (model.get(j).RoomID.equals(point.get(i).getRoomNum())) {
					room.add(new RoomInfor(point.get(i).getRoomNum(), Integer
							.parseInt(model.get(j).DepartmentID), -1, point
							.get(i).getRoomPoint()));
				}
			}
		}
		return room;
	}

	/**
	 * @auther Darly Fronch 2015 下午3:01:05 TODO 对原始数据进行变更。替换状态
	 */
	public ArrayList<RoomInfor> setInfoRoom(OrgBaseData models,
			ArrayList<RoomInfor> roomIn) {
		for (int i = 0, len = roomIn.size(); i < len; i++) {
			// 判断获取到的数据假如没有此字段，则展示原始页面。
			// 剔除不用体检的项目
			if (loading != null) {
				loading.dismiss();
			}
			if (models.getAll() != null) {
				for (int s = 0, lent = models.getAll().length; s < lent; s++) {
					if (roomIn.get(i).getDepartId() == Integer.parseInt(models
							.getAll()[s] + "")) {
						roomIn.get(i).setRoomStauts(0);
						break;
					}
				}
			}
			// 剔除已经体检完成的项目
			if (models.getDone() != null) {
				for (int a = 0, lent = models.getDone().size(); a < lent; a++) {
					if (roomIn.get(i).getDepartId() == models.getDone().get(a)
							.getDepartmentID()) {
						roomIn.get(i).setRoomStauts(1);
					}
				}
			}
			if (models.getNext() != null) {
				for (int b = 0, lent = models.getNext().size(); b < lent; b++) {
					String num = models.getNext().get(b).getRoomID();
					if (num.contains("-")) {
						num = num.substring(0, num.indexOf("-"));
					}
					String room = roomIn.get(i).getRoomNum();
					if (room.contains("-")) {
						room = num.substring(0, num.indexOf("-"));
					}
					if (room.equals(num)) {
						roomIn.get(i).setRoomStauts(2);

						int X = 0;
						int Y = 0;
						ArrayList<Point> pos = null;
						for (RoomInfor roomInfor : roomIn) {
							if (roomInfor.getRoomNum().equals(room)) {
								pos = roomInfor.getRoomPoint();
								break;
							}
						}
						if (pos != null) {
							int lenth = pos.size();
							for (int s = 0; s < lenth; s++) {
								// 无论多少个点。现在获取所有的X轴和与Y轴和。
								Point p = pos.get(s);
								X += p.x;
								Y += p.y;
							}
							// 获取到背景图片后进行Bitmap缓存。
							Drawable drawable = getResources().getDrawable(
									R.drawable.next_check);
							Bitmap nextImage = ((BitmapDrawable) drawable)
									.getBitmap();
							int heighe = nextImage.getHeight();
							int width = nextImage.getWidth();
							LogApp.i(nextImage.toString() + heighe + width);
							interlgent.setNextImage(nextImage, X / lenth
									- width / 2, Y / lenth - heighe);
						}

					}
				}
			}
		}

		return roomIn;
	}

	/**
	 * @auther Darly Fronch 2015 下午2:21:12 TODO获取机构刷新数据。
	 */
	public void getDataFHttp() {
		JSONObject object = new JSONObject();
		try {
			// 测试数据
			object.put("UserMobile", "18321127312");
			object.put("OrganizationID", ""+selectOrgID);
		} catch (Exception e) {
			Log.i("getDataFHttp", e.getMessage().toString());
		}
		ArrayList<BasicNameValuePair> par = new ArrayList<BasicNameValuePair>();
		par.add(new BasicNameValuePair("param", object.toString()));
		manager.addAsyncTask(new HttpTasker(IndexZoomViewActivity.this, par,
				dataUrl, null, handler, true, Literal.POST_HANDLER, true));
	}

	/**
	 * @auther Darly Fronch 2015 上午9:22:51 TODO起始进来后放大到全屏状态。
	 */
	private void setViewFullScreen() {

		// 横屏：width<height

		double screen = (double) Literal.height / (double) Literal.width;
		// 图片 if(width>height)
		double image = (double) IAPoisDataConfig.babaibanw
				/ (double) IAPoisDataConfig.babaibanh;

		double a = 0;
		if (screen > image) {
			// 按照手机高度放大
			a = Literal.bitmapheight / Literal.width;
			if (a < 1) {
				a = 1 / a;
			}

		} else if (screen < image) {
			// 按照手机宽度放大
			a = Literal.bitmapwidth / Literal.height;
			if (a < 1) {
				a = 1 / a;
			}
		} else {
			// 等比放大
			a = Literal.bitmapwidth / Literal.width;
			if (a < 1) {
				a = 1 / a;
			}
		}
		interlgent.setRate((float) a);
	};

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		setResult(Literal.CA_HANDLER);
		if (timer != null) {
			timer.cancel(); 
			timer = null;
		}
		if (interlgent != null) {
			interlgent.setFlag(false);
			interlgent = null;
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
