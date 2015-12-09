package com.darly.activities.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.darly.activities.R;
import com.darly.activities.adapter.CityAdapter;
import com.darly.activities.adapter.LocalAdapter;
import com.darly.activities.app.App;
import com.darly.activities.app.Constract;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.IAPoisDataConfig;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.common.PreferencesJsonCach;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.BaseCityInfo;
import com.darly.activities.model.BaseOrgInfo;
import com.darly.activities.model.IARoomName;
import com.darly.activities.model.IARoomNameHttp;
import com.darly.activities.model.IARoomPoint;
import com.darly.activities.model.OrgBase;
import com.darly.activities.model.OrgBaseData;
import com.darly.activities.model.RoomInfor;
import com.darly.activities.poll.HttpTaskerForString;
import com.darly.activities.poll.ThreadPoolManager;
import com.darly.activities.widget.intel.DepFloorRoom;
import com.darly.activities.widget.intel.DepInfo;
import com.darly.activities.widget.intel.DepInfoFloor;
import com.darly.activities.widget.intel.DepInterlgent;
import com.darly.activities.widget.intel.InterlgentUtil;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.darly.activities.widget.spinner.BaseSpinner;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author Zhangyuhui IndexShowViewActivity 上午9:01:37 TODO
 *         展示机构平面图页面，取自帮忙医项目的智能导检。耗时操作放到了主进程里面，所以现在要将耗时操作放到线程里面进行测试。是否可以进行对应调节。
 *         OnCreate里面操作完成后才进行页面展示。页面并没有进行展示的原因就是因为OnCreate没有执行完毕。(失败)
 */
@ContentView(R.layout.activity_inter_vtoo)
public class InterlActivityVtoo extends BaseActivity {
	private static final String TAG = "IndexShowViewActivity";
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	/**
	 * 上午9:28:13 TODO 标题栏返回按钮
	 */
	@ViewInject(R.id.main_header_back)
	private ImageView back;

	/**
	 * TODO下拉菜单选择列表
	 */
	@ViewInject(R.id.main_city_spinner_too)
	private BaseSpinner city_spinner;
	/**
	 * TODO下拉菜单选择列表
	 */
	@ViewInject(R.id.main_org_spinner_too)
	private BaseSpinner org_spinner;
	/**
	 * TODO线程管理
	 */
	private ThreadPoolManager manager;
	/**
	 * TODO生成图层
	 */
	@ViewInject(R.id.main_container_intel_too)
	public DepInterlgent interlgent;
	/**
	 * TODO图层容器
	 */
	@ViewInject(R.id.main_container_too)
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
	private String infoUrl = "http://172.3.207.15/APIAccount/GetOrganizationInfos";
	/**
	 * 数据链接
	 */
	private String dataUrl = "http://test.rayelink.com/APIQueuingSystem/GetDatas";

	private final int KEEP = 1000;
	private int NEXTKEEP = 10000;

	/**
	 * TODO 加载过场动画类
	 */
	private ProgressDialogUtil loading;

	/**
	 * 下午2:58:17
	 * 
	 * @author Zhangyuhui IndexShowViewActivity.java TODO 城市机构信息列表。
	 */
	private ArrayList<BaseCityInfo> city_Info;

	/**
	 * 下午3:39:31
	 * 
	 * @author Zhangyuhui IndexShowViewActivity.java TODO 选中的机构信息。
	 */
	private BaseOrgInfo selectOrg;

	private boolean isClick;

	private boolean isUpDataCache;

	/**
	 * 上午11:38:31 TODO 机构信息。
	 */
	private IARoomNameHttp roomOrgpari;

	/**
	 * 上午10:28:04 TODO 整个机构信息的汇总Model，所有信息都包含在这个Model中。
	 */
	private DepInfo info;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_container:

			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			if (interlgent != null) {
				interlgent.setFlag(false);
			}
			Intent intent = new Intent(this, IndexZoomViewActivity.class);
			intent.putExtra("selectOrg", selectOrg.org_id);
			startActivity(intent);
			isClick = true;
			break;
		case R.id.main_header_back:
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
		LogFileHelper.getInstance().i(TAG, "OnCreate还没加载interlgent。");
		main_container.setLayoutParams(new LinearLayout.LayoutParams(
				Constract.width, Constract.width * IAPoisDataConfig.babaibanh
						/ IAPoisDataConfig.babaibanw));
		interlgent.setLayoutParams(new LayoutParams(Constract.width,
				Constract.width * IAPoisDataConfig.babaibanh
						/ IAPoisDataConfig.babaibanw));

		initImageAndThread();
		setSpinner();
		// 初始化从第一项开始
		city_spinner.getSpinner().setSelection(0);
		org_spinner.getSpinner().setSelection(0);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		title.setText(getClass().getSimpleName());
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		/*
		 * main_container_test.addView(new RectRestoreSurfaceView(this));
		 * main_container_test.addView(new RotateClockView(this));
		 */
		LogFileHelper.getInstance().i(TAG, "OnCreate已经跑完了。该展示页面了。");
	}

	/**
	 * 
	 * 下午2:44:51
	 * 
	 * @author Zhangyuhui IndexShowViewActivity.java TODO 设置下拉列表。
	 */
	private void setSpinner() {
		// 由于服务端没有城市机构信息。故而虚拟一下数据
		city_Info = new ArrayList<BaseCityInfo>();

		ArrayList<BaseOrgInfo> org = new ArrayList<BaseOrgInfo>();
		org.add(new BaseOrgInfo(12, "静安", 1));
		org.add(new BaseOrgInfo(24, "徐汇", 1));
		org.add(new BaseOrgInfo(31, "八百", 1));

		city_Info.add(new BaseCityInfo(2001, "上海", org));
		city_Info.add(new BaseCityInfo(2002, "北京", org));

		// TODO Auto-generated method stub
		city_spinner.getSpinner().setAdapter(
				new CityAdapter(city_Info, R.layout.ia_guide_item_city, this));

		city_spinner.getSpinner().setOnItemSelectedListener(
				new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						BaseCityInfo info = (BaseCityInfo) parent
								.getItemAtPosition(position);
						org_spinner.getSpinner().setAdapter(
								new LocalAdapter(info.city_org,
										R.layout.ia_guide_item_city,
										InterlActivityVtoo.this));
						org_spinner.getSpinner().setOnItemSelectedListener(
								new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> parent, View view,
											int position, long id) {
										// TODO Auto-generated method stub
										// 选择正确的机构。
										selectOrg = (BaseOrgInfo) parent
												.getItemAtPosition(position);
										firstStep();
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> parent) {
										// TODO Auto-generated method stub

									}
								});
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});
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
		String info = PreferencesJsonCach.getInfo("GETINFO" + selectOrg.org_id,
				this);
		// 初次没有缓存则直接跳过,没有缓存才去请求数据。有缓存则直接访问缓存数据。
		if (info != null) {
			roomOrgpari = new Gson().fromJson(info, IARoomNameHttp.class);
			getOrgAndPoint(roomOrgpari);
			firstSetSurface();
			getDataFHttp();
			isUpDataCache = true;
		} else {
			if (!App.isNetworkConnected(this)) {
				if (loading != null) {
					loading.dismiss();
				}
				Toast.makeText(this, "网络异常，请检查网络！", KEEP).show();
			} else {
				// 请求服务器平面图数据。
				isUpDataCache = false;
				JSONObject object = new JSONObject();
				try {
					object.put("OrganizationID", "" + selectOrg.org_id);
				} catch (Exception e) {
					// TODO: handle exception
					LogFileHelper.getInstance().e(TAG, e.getMessage());
				}
				ArrayList<BasicNameValuePair> par = new ArrayList<BasicNameValuePair>();
				par.add(new BasicNameValuePair("param", object.toString()));
				manager.start();
				// 获取屏幕的宽高。这几
				manager.addAsyncTask(new HttpTaskerForString(
						InterlActivityVtoo.this, par, infoUrl, handler, true,
						Constract.GET_HANDLER, null));
				// 请求服务器平面图数据。
			}
		}
	}

	/**
	 * @param roomOrgpari
	 * @auther Darly Fronch 2015 下午2:20:34 TODO获取机构信息类，即获取房间功能对应表格roomOrgpari
	 */
	public void getOrgAndPoint(IARoomNameHttp roomOrgpari) {
		// -----------如何建立关系----------
		int[] arg = null;
		switch (selectOrg.org_id) {
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
				selectOrg.org_id);
		roomInfo = getRoomInfr(roomOrgpari.model, point/*
														 * roomOrgpari.point
														 * 由于服务器暂时还未传递，制造假数据
														 */);
		// ---------------------------修改整个Model信息------------------------
		/**
		 * 现在已经获取到了。机构房间编号，机构房间和科室关系，房间点阵，以及机构楼层的背景图片。通过这些信息，合成需要使用的后续版本JSON。
		 */
		ArrayList<DepFloorRoom> rooms = new ArrayList<DepFloorRoom>();
		for (RoomInfor iaRoomPoint : roomInfo) {
			int[] depId = { iaRoomPoint.getDepartId() };
			rooms.add(new DepFloorRoom(iaRoomPoint.getRoomPoint(), depId,
					iaRoomPoint.getRoomNum() + "", iaRoomPoint.getDepartId()
							+ "", "", 0));
		}
		ArrayList<DepInfoFloor> floors = new ArrayList<DepInfoFloor>();
		floors.add(new DepInfoFloor(1, rooms, roomOrgpari.Organizationplan));
		info = new DepInfo(200, "", floors);
		/**
		 * 这里的info就是以后需要使用的数据Model.也就是要保存到本地的Model原型。
		 */

		// ---------------------------修改整个Model信息------------------------
	}

	/**
	 * @param roomOrgpari
	 *            上午11:24:08
	 * @author Zhangyuhui IndexShowViewActivity.java TODO
	 */
	private void firstSetSurface() {
		interlgent.ReDraw(info.getFloor().get(0).getRooms());
		main_container.setOnClickListener(this);
		String url = info.getFloor().get(0).getFloorBackground();
		final String name = url.substring(url.lastIndexOf("/") + 1,
				url.length());
		File file = new File(Constract.SROOT + name);
		if (file.exists()) {
			Bitmap tempBitmap = BitmapFactory.decodeFile(Constract.SROOT + name);
			Bitmap back = InterlgentUtil.zoomImage(tempBitmap, Constract.width,
					Constract.width * IAPoisDataConfig.babaibanh
							/ IAPoisDataConfig.babaibanw);
			interlgent.setBackGroud(back);
			interlgent.setNextImage(null, 0, 0);
		} else {

			// 获取到背景图片后进行Bitmap缓存。
			imageLoader.loadImage(url, new ImageLoadingListener() {

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

					Bitmap back = InterlgentUtil.zoomImage(arg2, Constract.width,
							Constract.width * IAPoisDataConfig.babaibanh
									/ IAPoisDataConfig.babaibanw);
					interlgent.setBackGroud(back);
					// 将Bitmap进行数据保存到文件。
					PreferencesJsonCach.saveBitmap(Constract.SROOT + name, arg2,
							TAG);
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
		if (loading != null) {
			loading.dismiss();
		}
		if (object != null) {
			String jsonInfo = (String) object;
			PreferencesJsonCach.putValue("GETINFO" + selectOrg.org_id,
					jsonInfo, this);
			if (!isUpDataCache) {// 非直接更新缓存则需要走这里。
				roomOrgpari = new Gson().fromJson(jsonInfo,
						IARoomNameHttp.class);
				if (roomOrgpari != null && roomOrgpari.model != null) {
					getOrgAndPoint(roomOrgpari);
					firstSetSurface();
					getDataFHttp();
				}
			}
		} else {
			ToastApp.showToast(this, R.string.neterror);
		}
	}

	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub
		if (loading != null) {
			loading.dismiss();
		}
		if (object != null) {
			String jsonData = (String) object;
			// 不需要保存上次状态。
			/*
			 * PreferencesJsonCach.putValue("GETDATA" + selectOrg.org_id,
			 * jsonData, this);
			 */
			OrgBase base = new Gson().fromJson(jsonData, OrgBase.class);
			startTimer();
			setInfoRoom(base.getModel());
			interlgent.ReDraw(info.getFloor().get(0).getRooms());// ---------------------------------------------
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
	public void setInfoRoom(OrgBaseData models) {
		for (int i = 0, len = info.getFloor().get(0).getRooms().size(); i < len; i++) {
			// 判断获取到的数据假如没有此字段，则展示原始页面。
			// 剔除不用体检的项目
			DepFloorRoom room = info.getFloor().get(0).getRooms().get(i);
			if (loading != null) {
				loading.dismiss();
			}
			if (models.getAll() != null) {
				for (int s = 0, lent = models.getAll().length; s < lent; s++) {
					for (int j = 0; j < room.getDeNo().length; j++) {
						if (room.getDeNo()[j] == Integer.parseInt(models
								.getAll()[s] + "")) {
							room.setStatus(0);
							break;
						}
					}
				}
			}
			// 剔除已经体检完成的项目
			if (models.getDone() != null) {
				for (int a = 0, lent = models.getDone().size(); a < lent; a++) {

					for (int j = 0; j < room.getDeNo().length; j++) {
						if (room.getDeNo()[j] != models.getDone().get(a)
								.getDepartmentID()) {
							room.setStatus(0);
							break;
						} else {
							room.setStatus(1);
						}
					}
				}
			}
			if (models.getNext() != null) {
				for (int b = 0, lent = models.getNext().size(); b < lent; b++) {

					String num = models.getNext().get(b).getRoomID();
					if (num.contains("-")) {
						num = num.substring(0, num.indexOf("-"));
					}
					String roomid = room.getRoomId();
					if (roomid.contains("-")) {
						roomid = num.substring(0, num.indexOf("-"));
					}
					if (roomid.equals(num)) {
						room.setStatus(2);
						int X = 0;
						int Y = 0;
						ArrayList<Point> pos = null;
						for (DepFloorRoom roomInfor : info.getFloor().get(0)
								.getRooms()) {
							if (roomInfor.getRoomId().equals(roomid)) {
								pos = roomInfor.getPoints();
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
							if (interlgent != null) {
								interlgent.setNextImage(nextImage, X / lenth
										- width / 2, Y / lenth - heighe);
							}
						}

					}
				}
			} else {
				if (interlgent != null) {
					// 没有下一项。
					interlgent.setNextImage(null, 0, 0);
				}
			}
		}
	}

	/**
	 * @auther Darly Fronch 2015 下午2:21:12 TODO获取机构刷新数据。
	 */
	public void getDataFHttp() {
		JSONObject object = new JSONObject();
		try {
			// 测试数据
			object.put("UserMobile", "18321127312");
			object.put("OrganizationID", "" + selectOrg.org_id);
		} catch (Exception e) {
			LogFileHelper.getInstance().e(TAG, e.getMessage());
		}
		ArrayList<BasicNameValuePair> par = new ArrayList<BasicNameValuePair>();
		par.add(new BasicNameValuePair("param", object.toString()));
		manager.start();
		manager.addAsyncTask(new HttpTaskerForString(InterlActivityVtoo.this,
				par, dataUrl, handler, true, Constract.POST_HANDLER, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isClick) {
			startTimer();
			if (interlgent != null) {
				interlgent.setFlag(true);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
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

}
