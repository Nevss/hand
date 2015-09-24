/**
 * 2015年9月16日
 * IndexFragment.java
 * com.darly.activities.ui.fragment
 * @auther Darly Fronch
 * 下午4:59:37
 * IndexFragment
 * TODO
 */
package com.darly.activities.ui.fragment;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.darly.activities.adapter.XAdapter;
import com.darly.activities.base.BaseFragment;
import com.darly.activities.common.ToastApp;
import com.darly.activities.model.HomtFragmentBase;
import com.darly.activities.model.HomtFragmentModel;
import com.darly.activities.ui.R;
import com.darly.activities.widget.carousel.Carousel;
import com.darly.activities.widget.carousel.ImageHandler;
import com.darly.activities.widget.xlistview.XListView;
import com.darly.activities.widget.xlistview.XListView.IXListViewListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 2015年9月16日 IndexFragment.java com.darly.activities.ui.fragment
 * 
 * @auther Darly Fronch 下午4:59:37 IndexFragment TODO娱乐首页
 */
public class IndexFragment extends BaseFragment implements OnItemClickListener,
		IXListViewListener {
	/**
	 * TODO根View
	 */
	private View rootView;
	/**
	 * TODO顶部标签卡
	 */
	@ViewInject(R.id.main_header_text)
	private TextView title;
	/**
	 * TODO首页轮播图片集合
	 */
	private static final String[] IMAGES = new String[] {
			"http://pic2.ooopic.com/01/01/17/53bOOOPIC4e.jpg",
			"http://pic2.ooopic.com/01/01/17/39bOOOPICe8.jpg",
			"http://pic13.nipic.com/20110424/818468_090858462000_2.jpg",
			"http://thumbs.dreamstime.com/z/%C9%BD%C2%B7%BE%B6-20729104.jpg",
			"http://image.72xuan.com/cases/100305/600_600/1003051017041241.jpg",
			"http://pica.nipic.com/2007-11-14/20071114114452315_2.jpg",
			"http://md.cuncun8.com/media/cc8/upload/68192031/0c67e362be347607a877697f46c5f773/101104142242_2026.jpg",
			"http://pic16.nipic.com/20110824/8169416_135754121000_2.jpg",
			"http://b.hiphotos.bdimg.com/album/w%3D2048/sign=79f7b0c594cad1c8d0bbfb274b066509/5366d0160924ab18de9241dd34fae6cd7a890b57.jpg",
			"http://pic2.ooopic.com/01/01/18/42bOOOPIC6c.jpg" };
	/**
	 * TODO轮播开始循环使用的Handler
	 */
	public ImageHandler imagehandler = new ImageHandler(
			new WeakReference<IndexFragment>(this));
	/**
	 * TODO轮播数据集合
	 */
	private ArrayList<String> datas;
	private ArrayList<HomtFragmentBase> data;
	/**
	 * TODO轮播控件
	 */
	private View header;
	@ViewInject(R.id.index_fragment_xlist)
	private XListView xlist;
	private XAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_index, container, false);// 关联布局文件
		ViewUtils.inject(this, rootView); // 注入view和事件
		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/**
	 * @auther Darly Fronch 2015 下午4:45:38 TODO集合添加数据
	 */
	private void setLoader() {
		// TODO Auto-generated method stub
		ArrayList<HomtFragmentModel> ic = new ArrayList<HomtFragmentModel>();
		for (int j = 0; j < IMAGES.length; j++) {
			ic.add(new HomtFragmentModel(IMAGES[j], IMAGES[j]));
		}
		for (int i = 0; i < IMAGES.length; i++) {
			data.add(new HomtFragmentBase(null, null, ic));
			data.add(new HomtFragmentBase(IMAGES[i], IMAGES[i], null));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		title.setText(getClass().getSimpleName());
		datas = new ArrayList<String>();
		data = new ArrayList<HomtFragmentBase>();

		for (int i = 0; i < IMAGES.length; i++) {
			datas.add(IMAGES[i]);
		}

		// 添加轮播效果。以及轮播点击效果。
		Carousel carousel = new Carousel(getActivity(), datas, imageLoader,
				options, imagehandler);
		header = carousel.view;
		xlist.addHeaderView(header);
		// 设置xlistview可以加载、刷新
		xlist.setPullLoadEnable(true);
		xlist.setPullRefreshEnable(true);
		setLoader();
		adapter = new XAdapter(data, 0, getActivity(), imageLoader, options);
		xlist.setAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		xlist.setOnItemClickListener(this);
		xlist.setXListViewListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseFragment#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		HomtFragmentBase base = (HomtFragmentBase) parent
				.getItemAtPosition(position);
		if (base.getData() == null) {
			ToastApp.showToast(getActivity(), base.getName());
		}
		
	}

	/*
	 * (non-Javadoc)下拉刷新
	 * 
	 * @see
	 * com.darly.activities.widget.xlistview.XListView.IXListViewListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				data.clear();
				setLoader();
				adapter.setData(data);
				xlist.stopRefresh();
				xlist.setPullLoadEnable(true);
				onLoad();
			}
		}, 2000);
	}

	/**
	 * @auther Darly Fronch 2015 下午4:46:07 TODO加载动画
	 */
	private void onLoad() {
		xlist.stopRefresh();
		xlist.stopLoadMore();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		xlist.setRefreshTime(format.format(new Date()));
	}

	/*
	 * (non-Javadoc)上拉加载
	 * 
	 * @see
	 * com.darly.activities.widget.xlistview.XListView.IXListViewListener#onLoadMore
	 * ()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setLoader();
				adapter.setData(data);
				xlist.stopLoadMore();
				xlist.setPullLoadEnable(false);
			}
		}, 2000);
	}
}
