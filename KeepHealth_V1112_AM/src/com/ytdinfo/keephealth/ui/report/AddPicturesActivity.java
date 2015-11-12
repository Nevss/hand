package com.ytdinfo.keephealth.ui.report;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.voice.demo.group.GroupChatActivity;
import com.voice.demo.ui.CCPHelper;
import com.ytdinfo.keephealth.R;
import com.ytdinfo.keephealth.adapter.PhotoGridViewAdapter;
import com.ytdinfo.keephealth.adapter.PhotoGridViewAdapter.Callback;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.HttpClient;
import com.ytdinfo.keephealth.model.ChatInfoBean;
import com.ytdinfo.keephealth.model.DocInfoBean;
import com.ytdinfo.keephealth.model.UserModel;
import com.ytdinfo.keephealth.service.TimerService;
import com.ytdinfo.keephealth.ui.BaseActivity;
import com.ytdinfo.keephealth.ui.MainActivity;
import com.ytdinfo.keephealth.ui.view.CommonActivityTopView;
import com.ytdinfo.keephealth.ui.view.MyPopWindow;
import com.ytdinfo.keephealth.ui.view.MyProgressDialog;
import com.ytdinfo.keephealth.utils.DBUtilsHelper;
import com.ytdinfo.keephealth.utils.ImageTools;
import com.ytdinfo.keephealth.utils.JsonUtil;
import com.ytdinfo.keephealth.utils.LogUtil;
import com.ytdinfo.keephealth.utils.SharedPrefsUtil;
import com.ytdinfo.keephealth.utils.ToastUtil;

public class AddPicturesActivity extends BaseActivity implements Callback {
	private String TAG = "AddPicturesActivity";
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				if (croped_bitmap != null) {

					requestImagesUrl();

				}
			}
		};
	};

	private CommonActivityTopView commonActivityTopView;
	private GridView gridView;
	private List<String> listData = new ArrayList<String>();
	private PhotoGridViewAdapter photoGridViewAdapter;

	private MyPopWindow mypop;
	private PopupWindow pop;
	private LinearLayout ll_parent;

	private List<String> list_imagesUrl = new ArrayList<String>();

	private Bitmap croped_bitmap;
	private String image_path;
	DbUtils db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pictures);
		db = DBUtilsHelper.getInstance().getDb();
		initView();
		initListener();
		initPop();
	}

	private void initView() {
		commonActivityTopView = (CommonActivityTopView) findViewById(R.id.id_CommonActivityTopView);
		commonActivityTopView.tv_title.setText("选择体检报告");
		commonActivityTopView.bt_save.setText("提交");
		commonActivityTopView.bt_save
				.setBackgroundResource(R.drawable.circle_white_selector);

		ll_parent = (LinearLayout) findViewById(R.id.id_ll_parent);

		gridView = (GridView) findViewById(R.id.aor_gridview);
		listData.add("file:///android_asset/add.png");

		photoGridViewAdapter = new PhotoGridViewAdapter(this, listData, this,"");
		gridView.setAdapter(photoGridViewAdapter);

	}

	private void initListener() {
		commonActivityTopView.ibt_back
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});
		commonActivityTopView.bt_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list_imagesUrl.size() > 0) {
					if (!DBUtilsHelper.getInstance().isOnline()) {
						requestAddReportPic();
					} else {
						ToastUtil.showMessage("您当前正在进行在线咨询，结束后才能进行报告解读哦");
						SharedPrefsUtil.putValue(Constants.CHECKEDID_RADIOBT, 1);
						Intent intent = new Intent(AddPicturesActivity.this,
								MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("news", "news");
						startActivity(intent);
						finish();
					}

				} else {
					ToastUtil.showMessage("请先添加体检照片");
				}

			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == (listData.size() - 1)) {
					// 点击最后一个，添加照片
					pop.showAtLocation(ll_parent, Gravity.BOTTOM, 0, 0);
					
//					Intent intent = new Intent(AddPicturesActivity.this, MyPopWindow.class);
//					startActivityForResult(intent, 0x123);
				}

			}
		});
	}

	private void requestAddReportPic() {

		try {
			String userStr = SharedPrefsUtil.getValue(Constants.USERMODEL, "");
			UserModel userModel = new Gson().fromJson(userStr, UserModel.class);

			// 向服务器发送请求
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("SubjectType", 2);
			jsonParam.put("UserID", userModel.getID());
			jsonParam.put("UserSex", userModel.getUserSex());
			jsonParam.put("UserName", userModel.getUserName());
			jsonParam.put("Age", userModel.getAge());
			jsonParam.put("HeadPicture", userModel.getHeadPicture());
			jsonParam.put("RelationShip", "");
			jsonParam.put("StudyID", "1000");
			jsonParam.put("AttachPics",
					new JSONArray(list_imagesUrl).toString());
			// jsonParam.put("BodyContent", "");

			HttpClient.post(Constants.ONLINE_QUESTION_SUBMIT_URl,
					jsonParam.toString(), new RequestCallBack<String>() {

						@Override
						public void onStart() {
							Log.i("HttpUtil", "onStart");

						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							Log.i("HttpUtil", "onLoading");
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Log.i("HttpUtil", "onSuccess");

							Log.i("HttpUtil", "onSuccess==="
									+ responseInfo.result.toString());
							parseJson(responseInfo.result.toString());

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.i("HttpUtil", "onFailure===" + msg);
							LogUtil.i("===============", error.toString());
							ToastUtil.showMessage("网络获取失败");
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private MyProgressDialog myProgressDialog;
	ChatInfoBean chatInfoBean;

	private void parseJson(String jsonStr) {
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject jsonData = jsonObject.getJSONObject("Data");
			final int subjectId = jsonData.getInt("SubjectID");
			// SharedPrefsUtil.putValue(Constants.SUBJECTID, subjectId + "");
			String docInfoBeanStr = jsonData.getString("responser");
			if (null == docInfoBeanStr || docInfoBeanStr.equals("")
					|| docInfoBeanStr.equals("null")) {
				ToastUtil.showMessage("当前没有医生在线...");
				return;
			}
			TimerService.count = 0;
			final DocInfoBean docInfoBean = new Gson().fromJson(docInfoBeanStr,
					DocInfoBean.class);
			saveDoc(docInfoBean);
			try {
				chatInfoBean = db.findFirst(Selector.from(ChatInfoBean.class)
						.where("docInfoBeanId", "=",
								docInfoBean.getVoipAccount()));
				if (null == chatInfoBean) {
					chatInfoBean = new ChatInfoBean();
					chatInfoBean.setSubjectID(subjectId + "");
					chatInfoBean.setDocInfoBeanId(docInfoBean.getVoipAccount());
					chatInfoBean.setComment(false);
				} else {
					chatInfoBean.setSubjectID(subjectId + "");
					chatInfoBean.setComment(false);
				}
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * chatInfoBean = new ChatInfoBean();
			 * chatInfoBean.setSubjectID(subjectId + ""); //
			 * chatInfoBean.setDocInfoBean(docInfoBean);
			 * chatInfoBean.setDocInfoBeanId(docInfoBean.getVoipAccount());
			 */
			// SharedPrefsUtil.putValue(Constants.CHATINFO,
			// chatInfoBean.toString());
			if (CCPHelper.getInstance().getDevice() == null) {
				myProgressDialog = new MyProgressDialog(
						AddPicturesActivity.this);
				myProgressDialog.setMessage("正在连接对话....");
				myProgressDialog.show();
				CCPHelper.getInstance().registerCCP(
						new CCPHelper.RegistCallBack() {

							@Override
							public void onRegistResult(int reason, String msg) {
								// Log.i("XXX", String.format("%d, %s",
								// reason, msg));
								if (reason == 8192) {
									LogUtil.i(TAG, "通讯云登录成功");
									myProgressDialog.dismiss();
									goIntent(chatInfoBean);
								} else {
									LogUtil.i(TAG, "通讯云登录失败");
									myProgressDialog.dismiss();
									ToastUtil.showMessage("对话连接失败....");
								}
							}
						});
			} else {
				goIntent(chatInfoBean);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void goIntent(ChatInfoBean chatInfoBean) {
		// TODO Auto-generated method stub
		chatInfoBean.setSubjectType("2");
		chatInfoBean.setStatus(true);
		DBUtilsHelper.getInstance().saveChatinfo(chatInfoBean);
		Intent i = new Intent();
		i.setClass(AddPicturesActivity.this, GroupChatActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("chatInfoBean", chatInfoBean);
		bundle.putStringArrayList("imageList", (ArrayList<String>) list_imagesUrl);
		i.putExtras(bundle);
		startActivity(i);
	}

	private void saveDoc(DocInfoBean docInfoBean) {
		// TODO Auto-generated method stub
		// DbUtils dbUtils = new DbUtils(null);

		try {
			db.createTableIfNotExist(DocInfoBean.class);
			db.save(docInfoBean);
			// List<DocInfoBean> docInfoBeans = db.findAll(DocInfoBean.class);
			// DocInfoBean docInfoBean2 =
			// db.findFirst(Selector.from(DocInfoBean.class).where("voipAccount","=","89077100000003"));
			// DocInfoBean docInfoBean2 = db.findById(DocInfoBean.class,
			// docInfoBean.getVoipAccount());
			// System.out.println("doc..........."+docInfoBean2.toString());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initPop() {
		mypop = new MyPopWindow(this);
		pop = mypop.getPop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//		if(resultCode==1001){
//			Bundle bundle = data.getExtras();
//			image_path = bundle.getString("imageUrl");
//		}
		LogUtil.i(TAG, "onActivityResult");

		image_path = mypop.INonActivityResult(requestCode, data, 0);
		if(image_path==null){
			return;
		}

		myProgressDialog2 = new MyProgressDialog(AddPicturesActivity.this);
		myProgressDialog2.setMessage("上传照片....");
		myProgressDialog2.show();

		new Thread(new Runnable() {

			@Override
			public void run() {
				cropImage(image_path);
				Message msg = Message.obtain();
				msg.what = 0x123;
				handler.sendMessage(msg);
			}
		}).start();

	}

	@SuppressLint("NewApi")
	private void cropImage(String image_path) {

		croped_bitmap = ImageTools.cropBitmap(image_path);

	}

	// private void crop_upload(Bitmap bitmap) {
	// Bitmap cropedBitmap = ImageTools.cropBitmap(bitmap);
	// // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
	// if (bitmap != null && !bitmap.isRecycled()) {
	//
	// bitmap.recycle();
	// bitmap = null;
	// }
	// System.gc();
	//
	// if (cropedBitmap != null) {
	//
	// requestImagesUrl(cropedBitmap);
	//
	// }
	// }

	private MyProgressDialog myProgressDialog2;

	/**
	 * 上传服务器
	 */
	public void requestImagesUrl() {

		try {
			// 向服务器发送请求
			JSONArray jsonArray = JsonUtil.bitmapTOjsonArray(croped_bitmap);
			LogUtil.i(TAG, jsonArray.toString());

			HttpClient.post(Constants.REPORT_IMAGES_URl, jsonArray.toString(),
					new RequestCallBack<String>() {

						@Override
						public void onStart() {
							Log.i("HttpUtil", "onStart");
							// myProgressDialog2 = new
							// MyProgressDialog(AddPicturesActivity.this);
							// myProgressDialog2.setMessage("上传照片....");
							// myProgressDialog2.show();
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							Log.i("HttpUtil", "onLoading");
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Log.i("HttpUtil", "onSuccess");
							Log.i("HttpUtil", "onSuccess==="
									+ responseInfo.result.toString());

							try {
								JSONObject jsonObject = new JSONObject(
										responseInfo.result.toString());
								JSONArray jsonArray = jsonObject
										.getJSONArray("path");
								for (int i = 0; i < jsonArray.length(); i++) {
									list_imagesUrl.add(jsonArray.get(i)
											.toString());
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								myProgressDialog2.dismiss();
							} catch (Exception e) {
								// TODO: handle exception
							}

							updateAdapter();

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Log.i("HttpUtil", "onFailure===" + msg);
							LogUtil.i("===============", error.toString());

							myProgressDialog2.dismiss();
							ToastUtil.showMessage("照片上传失败");
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将newbitmap加入list中，并刷新adapter
	 * 
	 * @param newBitmap
	 */
	public void updateAdapter() {
		if (croped_bitmap != null) {
			listData.add(listData.size() - 1, image_path);
//			gridView.setAdapter(new PhotoGridViewAdapter(this, listData, this));
			photoGridViewAdapter.notifyDataSetChanged();
			ImageTools.recycleBitmap(croped_bitmap);
		}
	}

	/**
	 * 接口方法，响应ListView按钮点击事件
	 */
	@Override
	public void click(View v, int position) {
		listData.remove(position);
//		gridView.setAdapter(new PhotoGridViewAdapter(this, listData, this));
		photoGridViewAdapter.notifyDataSetChanged();
		// 将list_imagesUrl中第position位置的图片URl删除
		list_imagesUrl.remove(position);
	}
	@Override
	public void onResume() {
		super.onResume();
		
		MobclickAgent.onPageStart("AddPicturesActivity");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		MobclickAgent.onPageEnd("AddPicturesActivity");
		MobclickAgent.onPause(this);
	}
}
