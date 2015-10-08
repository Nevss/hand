package com.darly.activities.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.GridViewData;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.darly.activities.widget.pop.PhotoPop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author Zhangyuhui MeDetailsAcitvity 下午2:57:21 TODO预览图片详情页面。
 */
@ContentView(R.layout.activity_me_details)
public class MeDetailsAcitvity extends BaseActivity {

	private GridViewData data;
	/**
	 * 下午5:38:14
	 * 
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO展示图片
	 */
	@ViewInject(R.id.me_details_iv)
	private ImageView iv;
	/**
	 * 上午9:27:39 TODO添加更多图片按钮
	 */
	@ViewInject(R.id.me_details_more)
	private ImageView more;
	/**
	 * 下午3:07:33 TODO更多图片横向排列。
	 */
	@ViewInject(R.id.me_details_linear)
	private LinearLayout container;

	/**
	 * 上午9:28:13 TODO 标题栏返回按钮
	 */
	@ViewInject(R.id.main_header_back)
	private ImageView back;
	/**
	 * 上午9:28:30 TODO 标题栏，标题。
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;

	/**
	 * 上午9:29:04 TODO 调出选项的POP窗口，主要为相机，相册，取消
	 */
	private PhotoPop pop;

	/**
	 * 上午9:29:49 TODO 首图的宽度。
	 */
	private int imageW;
	/**
	 * 上午9:29:49 TODO 首图的高度。
	 */
	private int imageH;

	/**
	 * 上午9:30:17 TODO 加载动画。
	 */
	private ProgressDialogUtil loading;

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
		case R.id.main_header_back:
			finish();
			break;
		case R.id.me_details_more:
			pop.show(v);
			break;

		default:
			break;
		}
	}

	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewUtils.inject(this); // 注入view和事件
		if (savedInstanceState == null) {
			data = (GridViewData) getIntent().getSerializableExtra(
					"GridViewData");
		} else {
			data = (GridViewData) savedInstanceState
					.getSerializable("GridViewData");
		}

		pop = new PhotoPop(this);

		title.setText("详细页面");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);

		more.setOnClickListener(this);
		loading = new ProgressDialogUtil(this);
		loading.setMessage("加载中...");

		// 获取到背景图片后进行Bitmap缓存。
		imageLoader.loadImage(data.url, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				loading.show();
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				loading.dismiss();
				ToastApp.showToast(MeDetailsAcitvity.this, "网络异常，请检查网络");
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				loading.dismiss();
				imageW = arg2.getWidth();
				imageH = arg2.getHeight();
				Message message = new Message();
				message.what = Literal.GET_HANDLER;
				message.obj = arg2;
				handler.sendMessage(message);
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				loading.dismiss();
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	 * .Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable("GridViewData", data);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
		if (object == null) {
			return;
		}

		Bitmap bitmap = (Bitmap) object;
		LayoutParams lp = new LayoutParams(Literal.width, Literal.width
				* imageH / imageW);
		iv.setLayoutParams(lp);
		iv.setImageBitmap(bitmap);
	}

	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == Literal.REQUESTCODE_CUT) {
			// 裁剪
			if (data != null) {
				Bundle extras = data.getExtras();
				Bitmap head = extras.getParcelable("data");
				ImageView imageView = new ImageView(MeDetailsAcitvity.this);
				LayoutParams lp = new LayoutParams(Literal.width / 5,
						Literal.width / 5);
				lp.setMargins(2, 2, 2, 2);
				imageView.setLayoutParams(lp);
				
				imageView.setImageBitmap(head);
				container.addView(imageView, container.getChildCount() - 1);

			}
		} else {
			// 拍照或相册
			String head_path = null;
			if (data == null) {
				if (pop == null) {
					head_path = Literal.capUri;
				} else {
					head_path = pop.PopStringActivityResult(null,
							Literal.REQUESTCODE_CAP);
				}
			} else {
				head_path = pop.PopStringActivityResult(data,
						Literal.REQUESTCODE_CAM);

			}
			int degree = pop.getBitmapDegree(head_path);
			if (degree != 0) {
				pop.new ImageDegree(degree, head_path, loading).execute();
			} else {
				LogApp.i("返回的文件路径" + degree + head_path);
				File temp = new File(head_path);
				pop.cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}
		}
	}

}
