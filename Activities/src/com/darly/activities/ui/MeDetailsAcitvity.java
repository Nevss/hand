package com.darly.activities.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.Literal;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.GridViewData;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@ContentView(R.layout.activity_me_details)
public class MeDetailsAcitvity extends BaseActivity {

	/**
	 * 下午5:38:14
	 * 
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO展示图片
	 */
	@ViewInject(R.id.me_details_iv)
	private ImageView iv;

	@ViewInject(R.id.main_header_back)
	private ImageView back;
	@ViewInject(R.id.main_header_text)
	private TextView title;

	private int imageW;
	private int imageH;

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

		default:
			break;
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this); // 注入view和事件
		GridViewData data = (GridViewData) getIntent().getSerializableExtra(
				"GridViewData");

		title.setText("详细页面");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
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

}
