package com.darly.activities.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.darly.activities.common.Literal;
import com.darly.activities.common.NetUtils;
import com.darly.activities.common.ToastApp;
import com.darly.activities.poll.ThreadPoolManager;
import com.darly.activities.ui.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: BaseFragment
 * @Description: TODO(Fragment基础类，所有Fragment类都需要继承此类)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年12月15日 上午9:03:29
 *
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options;
	protected DisplayImageOptions option_big;
	protected AQuery aq;
	/**
	 * TODO线程管理
	 */
	protected ThreadPoolManager manager;

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

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
		// 初始化LOG
		manager = ThreadPoolManager.getInstance(ThreadPoolManager.TYPE_FIFO,
				Thread.MAX_PRIORITY);
		// 设置ImageLoader初始化参数。设置线程，设置保存文件名等。
		aq = new AQuery(getActivity());
		// 设置参数，加载每个图片的详细参数和是否存储、缓存的问题。
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.bitmapConfig(Config.RGB_565).cacheOnDisc(true).build();
		option_big = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.bitmapConfig(Config.RGB_565).cacheOnDisc(true).build();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initView();
		initData();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		if (!NetUtils.isConnected(getActivity())) {
			ToastApp.showToast(getActivity(), "网络连接异常，请检查网络！");
		}
		super.onResume();
		MobclickAgent.onPageStart("FragmentScreen"); // 统计页面
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FragmentScreen");
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:48:56 Project_Name:DFram
	 * Description:初始化界面控件，获取界面XML的方法体，可以在这里对所有XML中的控件进行实例。 Throws
	 */
	public abstract void initView();

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:49:13 Project_Name:DFram
	 * Description:加载数据方法体，所有的参数数据都可以放到这里进行实现，当然和InitView功能差不多。 Throws
	 */
	public abstract void initData();

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:49:34 Project_Name:DFram
	 * Description
	 * :通过Literal.GET_HANDLER调取Handler，由Handler进行调用的一个方法体，可以实现界面更改，界面刷新等功能。
	 * Throws
	 */
	public abstract void refreshGet(Object object);

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:49:37 Project_Name:DFram
	 * Description
	 * :通过Literal.POST_HANDLER调取Handler，由Handler进行调用的一个方法体，可以实现界面更改，界面刷新等功能。
	 * Throws
	 */
	public abstract void refreshPost(Object object);

}
