package com.darly.activities.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
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
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
	 * 下一项图片
	 */
	private ImageView nextCheck;
	/**
	 * 下一项数据
	 */
	private LayoutParams next;

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

	/**
	 * TODOActivity中使用网络请求，对应的数据返回区。
	 */
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Literal.GET_HANDLER:
				refreshGet(msg.obj);
				break;
			case Literal.POST_HANDLER:
				refreshPost(msg.obj);
				break;
			default:
				break;
			}
		}

	};

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
	public void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);// 注入view和事件
		loading = new ProgressDialogUtil(this);
		loading.setMessage("加载中...");
		loading.show();
		main_container.setLayoutParams(new LayoutParams(Literal.width,
				Literal.width * IAPoisDataConfig.babaibanh
						/ IAPoisDataConfig.babaibanw));
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
		if (!AppStack.isNetworkConnected(this)) {
			if (loading != null) {
				loading.dismiss();
			}
			Toast.makeText(this, "网络异常，请检查网络！", KEEP).show();
			String info = PreferencesJsonCach.getInfo("GETINFO" + 31, this);
			String data = PreferencesJsonCach.getInfo("GETDATA" + 31, this);
			// 初次没有缓存则直接跳过
			if (info != null) {
				getOrgAndPoint(new Gson().fromJson(info, IARoomNameHttp.class));
			}
			// 初次没有缓存则直接跳过
			if (data != null) {
				interlgent.ReDraw(setInfoRoom(
						new Gson().fromJson(data, OrgBase.class).getModel(),
						roomInfo));
			}
		} else {
			// 请求服务器平面图数据。
			JSONObject object = new JSONObject();
			try {
				object.put("OrganizationID", "31");
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
		ArrayList<IARoomPoint> point = IAPoisDataConfig.getModelTest(
				getResources().getIntArray(R.array.babaiban_roomnub), 31);
		roomInfo = getRoomInfr(roomOrgpari.model, point/*
														 * roomOrgpari.point
														 * 由于服务器暂时还未传递，制造假数据
														 */);
		// -----------如何建立关系----------
		main_container.removeAllViews();
		interlgent = new BaseInterlgent(this, roomInfo);
		LayoutParams lp = new LayoutParams(Literal.width, Literal.width
				* IAPoisDataConfig.babaibanh / IAPoisDataConfig.babaibanw);
		ImageView iv = new ImageView(this);
		iv.setLayoutParams(lp);
		DisplayImageOptions options_mo = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.displayer(new RoundedBitmapDisplayer(0)) // 设置显示风格这里是圆角矩形
				.build();
		imageLoader.displayImage(roomOrgpari.Organizationplan, iv, options_mo);
		main_container.addView(interlgent);
		// main_container.addView(iv);
		nextCheck = new ImageView(this);
		next = new LayoutParams(96, 44);
		nextCheck.setLayoutParams(next);
		nextCheck.setImageResource(R.drawable.next_check);
		nextCheck.setVisibility(View.INVISIBLE);
		// main_container.addView(nextCheck);
		setViewFullScreen();
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
			PreferencesJsonCach.putValue("GETINFO" + 31, jsonInfo, this);
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
			PreferencesJsonCach.putValue("GETDATA" + 31, jsonData, this);
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
							next.leftMargin = X / lenth - 48;
							next.topMargin = Y / lenth - 44;
							nextCheck.setVisibility(View.VISIBLE);
							nextCheck.setLayoutParams(next);
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
			object.put("OrganizationID", "31");
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
			a = (double) Literal.bitmapheight / (double) Literal.width;
			if (a < 1) {
				a = 1 / a;
			}

		} else if (screen < image) {
			// 按照手机宽度放大
			a = (double) Literal.bitmapwidth / (double) Literal.height;
			if (a < 1) {
				a = 1 / a;
			}
		} else {
			// 等比放大
			a = (double) Literal.bitmapwidth / (double) Literal.width;
			if (a < 1) {
				a = 1 / a;
			}
		}

		// 整个RelativeLayout布局放置大小
		AnimatorSet conanimSet = new AnimatorSet();// 定义一个AnimatorSet对象
		ObjectAnimator conStart = ObjectAnimator.ofFloat(interlgent, "scaleX",
				1f, (float) (a));
		ObjectAnimator conStop = ObjectAnimator.ofFloat(interlgent, "scaleY",
				1f, (float) (a));
		conanimSet.play(conStart).with(conStop);
		conanimSet.setDuration(0);
		conanimSet.start();
		// 整个RelativeLayout布局放置大小
		AnimatorSet animSet = new AnimatorSet();// 定义一个AnimatorSet对象
		ObjectAnimator Start = ObjectAnimator.ofFloat(main_container, "scaleX",
				1f, (float) (a));
		ObjectAnimator Stop = ObjectAnimator.ofFloat(main_container, "scaleY",
				1f, (float) (a));
		animSet.play(Start).with(Stop);
		animSet.setDuration(0);
		animSet.start();

	};

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}
}
