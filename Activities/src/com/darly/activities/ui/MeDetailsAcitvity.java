package com.darly.activities.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
	 * 上午9:27:39 TODO测试拍照功能模块的ImageView
	 */
	@ViewInject(R.id.me_details_test)
	private ImageView test;

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
	 * 上午9:28:45 TODO 测试照相功能的按钮
	 */
	@ViewInject(R.id.me_details_btn)
	private Button btn;

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
		case R.id.me_details_btn:
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

		pop = PhotoPop.getPhotoPop(this);

		title.setText("详细页面");
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		btn.setOnClickListener(this);
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
				test.setImageBitmap(head);
				LogApp.i(head.toString());
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
			int degree = getBitmapDegree(head_path);
			if (degree == 0) {

				rotateBitmapByDegree(head_path, 90);
				LogApp.i("返回的文件路径旋转图片" + 90 + head_path);
			}

			LogApp.i("返回的文件路径" + degree + head_path);
			File temp = new File(head_path);
			pop.cropPhoto(Uri.fromFile(temp));// 裁剪图片

		}

	}

	/**
	 * @param path
	 * @return 上午10:42:09
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO 获取图片的旋转角度。
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * @param bm需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片 上午10:44:08
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO将图片按照某个角度进行旋转
	 */
	private void rotateBitmapByDegree(String url, int degree) {
		Bitmap bitmap = BitmapFactory.decodeFile(url);

		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			if (returnBm == null) {
				returnBm = bitmap;
			}
			saveBitmap(url, returnBm);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		if (bitmap != returnBm) {
			bitmap.recycle();
		}

	}

	/**
	 * @param url
	 * @param bitmap
	 *            上午10:53:39
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO 将Bitmap保存到文件。
	 */
	public void saveBitmap(String url, Bitmap bitmap) {
		LogApp.i("保存图片");
		File f = new File(url);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			LogApp.i("已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
