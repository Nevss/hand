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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.darly.activities.R;
import com.darly.activities.app.App;
import com.darly.activities.app.Constract;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.IAPoisDataConfig;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.PreferencesJsonCach;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.IARoomName;
import com.darly.activities.model.IARoomNameHttp;
import com.darly.activities.model.IARoomPoint;
import com.darly.activities.model.OrgBase;
import com.darly.activities.model.OrgBaseData;
import com.darly.activities.model.RoomInfor;
import com.darly.activities.poll.HttpTaskerForString;
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
 *         由于参数是上个页面加载完成后点击进入此页面，故而初始数据已经缓存完成。没有必要重新加载。只用加载Timer即可。
 */
@ContentView(R.layout.activity_index_zoom_view)
public class IndexZoomViewActivity extends BaseActivity implements
		OnTouchListener {
	private static final String TAG = "IndexZoomViewActivity";

	/**
	 * TODO线程管理
	 */
	private ThreadPoolManager manager;
	/**
	 * TODO生成图层
	 */
	@ViewInject(R.id.index_zoom_container_inter)
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
	/**
	 * 上午9:37:00 TODO 全屏比例。
	 */
	private double radtio;

	/**
	 * 上午9:35:10 TODO 全屏过后放大缩小记录的上次比例。
	 */
	private double oldRate = 1;
	/**
	 * 上午9:35:42 TODO记录第一次触屏时线段的长度
	 */
	private float oldLineDistance;
	/**
	 * 上午9:35:56 TODO判定是否头次多指触点屏幕
	 */
	private boolean isFirst = true;

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

		main_container.setLayoutParams(new LayoutParams(Constract.height,
				Constract.width));

		Constract.bitmapheight = Constract.width * IAPoisDataConfig.babaibanh
				/ IAPoisDataConfig.babaibanw;
		Constract.bitmapwidth = Constract.width;
		initImageAndThread();
		firstStep();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		consel.setOnClickListener(this);
		interlgent.setOnTouchListener(this);

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
		String info = PreferencesJsonCach
				.getInfo("GETINFO" + selectOrgID, this);
		// 由于是上个页面直接跳入此页面，则缓存是已经存在的。不需要进行再次更新。
		if (info != null) {
			getOrgAndPoint(new Gson().fromJson(info, IARoomNameHttp.class));
		}
		// 初次没有缓存则直接跳过
		if (!App.isNetworkConnected(this)) {
			loading.dismiss();
			ToastApp.showToast(this, R.string.neterror);
		} else {
			getDataFHttp();
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
		radtio = setViewFullScreen();
		interlgent.setRate((float) radtio);
		interlgent.ReDraw(roomInfo);
		String url = roomOrgpari.Organizationplan;
		final String name = url.substring(url.lastIndexOf("/") + 1,
				url.length());
		File file = new File(Constract.SROOT + name);
		if (file.exists()) {
			Bitmap tempBitmap = BitmapFactory.decodeFile(Constract.SROOT + name);
			interlgent.setBackGroud(tempBitmap);
			interlgent.setNextImage(null, 0, 0);
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
									Constract.width, Constract.width
											* IAPoisDataConfig.babaibanh
											/ IAPoisDataConfig.babaibanw);
							interlgent.setBackGroud(back);
							// 将Bitmap进行数据保存到文件。
							PreferencesJsonCach.saveBitmap(
									Constract.SROOT + name, arg2, TAG);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});

		}
	}

	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
	}

	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub
		loading.dismiss();
		if (object != null) {
			String jsonData = (String) object;
			LogFileHelper.getInstance().i(TAG, jsonData);
			PreferencesJsonCach.putValue("GETDATA" + selectOrgID, jsonData,
					this);
			OrgBase base = new Gson().fromJson(jsonData, OrgBase.class);
			startTimer();
			interlgent.ReDraw(setInfoRoom(base.getModel(), roomInfo));
		} else {
			ToastApp.showToast(this, R.string.neterror);
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
							Bitmap nextImage = null;
							if (nextImage == null) {
								Drawable drawable = getResources().getDrawable(
										R.drawable.next_check);
								nextImage = ((BitmapDrawable) drawable)
										.getBitmap();
							}
							int heighe = nextImage.getHeight();
							int width = nextImage.getWidth();
							LogFileHelper.getInstance().i(
									nextImage.toString() + heighe + width);
							if (interlgent != null) {
								interlgent.setNextImage(nextImage, X / lenth
										- width / 2, Y / lenth - heighe);
							}
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
			object.put("OrganizationID", "" + selectOrgID);
		} catch (Exception e) {
			LogFileHelper.getInstance().e(TAG, e.getMessage());
		}
		ArrayList<BasicNameValuePair> par = new ArrayList<BasicNameValuePair>();
		par.add(new BasicNameValuePair("param", object.toString()));
		manager.start();
		manager.addAsyncTask(new HttpTaskerForString(
				IndexZoomViewActivity.this, par, dataUrl, handler, true,
				Constract.POST_HANDLER, null));
	}

	/**
	 * @auther Darly Fronch 2015 上午9:22:51 TODO起始进来后放大到全屏状态。
	 */
	private double setViewFullScreen() {

		// 横屏：width<height

		double screen = (double) Constract.height / (double) Constract.width;
		// 图片 if(width>height)
		double image = (double) IAPoisDataConfig.babaibanw
				/ (double) IAPoisDataConfig.babaibanh;

		double a = 0;
		if (screen > image) {
			// 按照手机高度放大
			a = Constract.bitmapheight / Constract.width;
			if (a < 1) {
				a = 1 / a;
			}

		} else if (screen < image) {
			// 按照手机宽度放大
			a = Constract.bitmapwidth / Constract.height;
			if (a < 1) {
				a = 1 / a;
			}
		} else {
			// 等比放大
			a = Constract.bitmapwidth / Constract.width;
			if (a < 1) {
				a = 1 / a;
			}
		}
		return a;
	};

	@Override
	public void finish() {
		// TODO Auto-generated method stub

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (interlgent != null) {
			interlgent.setFlag(false);
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private float startMoveX;
	private float startMoveY;
	private float endMoveX;
	private float endMoveY;
	private boolean isSingleFig = true;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isFirst = true;
			isSingleFig = true;
			oldRate = radtio;
		} else {
			if (event.getPointerCount() > 1) {
				if (event.getPointerCount() == 2) {
					isSingleFig = false;
					if (isFirst) {
						oldLineDistance = (float) Math.sqrt(Math.pow(
								event.getX(1) - event.getX(0), 2)
								+ Math.pow(event.getY(1) - event.getY(0), 2));
						isFirst = false;
					} else {
						float newLineDistance = (float) Math.sqrt(Math.pow(
								event.getX(1) - event.getX(0), 2)
								+ Math.pow(event.getY(1) - event.getY(0), 2));
						float line = newLineDistance / oldLineDistance;

						if (line > 1) {
							// 放大。则比例变大。
							if (oldRate * line <= 3) {
								radtio = oldRate * line;
							}

						} else {
							// 缩小。比例变小。
							if (oldRate * line >= 1) {
								// 最小比例不能再小。
								radtio = oldRate * line;
							}
						}
					}
				}
			} else {
				if (isSingleFig) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						startMoveX = event.getX();
						startMoveY = event.getY();
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						endMoveX = event.getX();
						endMoveY = event.getY();
						interlgent.setTranslation(endMoveX - startMoveX,
								endMoveY - startMoveY);
					}
				}
			}
		}
		interlgent.setRate((float) radtio);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return onTouchEvent(event);
	}

}
