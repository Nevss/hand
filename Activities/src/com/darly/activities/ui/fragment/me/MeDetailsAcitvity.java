package com.darly.activities.ui.fragment.me;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.darly.activities.app.Constract;
import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.LogFileHelper;
import com.darly.activities.model.GridViewData;
import com.darly.activities.ui.R;
import com.darly.activities.widget.carousel.Carousel;
import com.darly.activities.widget.carousel.ImageHandler;
import com.darly.activities.widget.load.ProgressDialogUtil;
import com.darly.activities.widget.pop.PhotoPop;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author Zhangyuhui MeDetailsAcitvity 下午2:57:21 TODO预览图片详情页面。
 */
@ContentView(R.layout.activity_me_details)
public class MeDetailsAcitvity extends BaseActivity {

	private static final String TAG = "MeDetailsAcitvity";
	private ArrayList<GridViewData> data;
	/**
	 * 下午5:38:14
	 * 
	 * @author Zhangyuhui MeDetailsAcitvity.java TODO展示图片
	 */
	/*
	 * @ViewInject(R.id.me_details_iv) private ImageView iv;
	 */

	/**
	 * 上午10:14:52 TODO 多图展示。
	 */
	@ViewInject(R.id.me_details_relative)
	private RelativeLayout relative;
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
	/*
	 * private int imageW;
	 *//**
	 * 上午9:29:49 TODO 首图的高度。
	 */
	/*
	 * private int imageH;
	 */

	/**
	 * 上午9:30:17 TODO 加载动画。
	 */
	private ProgressDialogUtil loading;

	/**
	 * TODO轮播开始循环使用的Handler
	 */
	WeakReference<MeDetailsAcitvity> weak = new WeakReference<MeDetailsAcitvity>(
			this);
	public ImageHandler<MeDetailsAcitvity> imagehandler = new ImageHandler<MeDetailsAcitvity>(
			weak);

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
		if (savedInstanceState == null) {
			data = getIntent().getParcelableArrayListExtra("GridViewData");
		} else {
			data = savedInstanceState.getParcelableArrayList("GridViewData");
		}

		pop = new PhotoPop(this);

		title.setText(R.string.detail_msg);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);

		more.setOnClickListener(this);
		loading = new ProgressDialogUtil(this);
		loading.setMessage(R.string.xlistview_header_hint_loading);

		ArrayList<String> images = new ArrayList<String>();
		for (GridViewData gridViewData : data) {
			images.add(gridViewData.url);
		}
		// 添加轮播效果。以及轮播点击效果。
		Carousel<MeDetailsAcitvity> carousel = new Carousel<MeDetailsAcitvity>(
				this, images, imageLoader, options, imagehandler);
		LayoutParams lp = new LayoutParams(Constract.width, Constract.width / 3);
		relative.setLayoutParams(lp);
		relative.addView(carousel.view);
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
		/*
		 * if (object == null) { return; }
		 * 
		 * Bitmap bitmap = (Bitmap) object; LayoutParams lp = new
		 * LayoutParams(Literal.width, Literal.width imageH / imageW);
		 * iv.setLayoutParams(lp); iv.setImageBitmap(bitmap);
		 */
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
		if (requestCode == Constract.REQUESTCODE_CUT) {
			// 裁剪
			if (data != null) {
				Bundle extras = data.getExtras();
				Bitmap head = extras.getParcelable("data");
				ImageView imageView = new ImageView(MeDetailsAcitvity.this);
				LayoutParams lp = new LayoutParams(Constract.width / 5,
						Constract.width / 5);
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
					head_path = Constract.capUri;
				} else {
					head_path = pop.PopStringActivityResult(null,
							Constract.REQUESTCODE_CAP);
				}
			} else {
				head_path = pop.PopStringActivityResult(data,
						Constract.REQUESTCODE_CAM);

			}
			LogFileHelper.getInstance().i(TAG, head_path);
			File temp = new File(head_path);
			pop.cropPhoto(Uri.fromFile(temp));// 裁剪图片
		}
	}

}
