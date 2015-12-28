package com.darly.oop.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.darly.oop.R;
import com.darly.oop.adapter.MainAdapter;
import com.darly.oop.adapter.MainDrawAdapter;
import com.darly.oop.base.APPEnum;
import com.darly.oop.base.BaseActivity;
import com.darly.oop.db.DBMongo;
import com.darly.oop.model.DarlyTableModel;
import com.darly.oop.model.Menu;
import com.darly.oop.model.Menu_Top;
import com.darly.oop.ui.inditorviewpager.InditorViewpage;
import com.darly.oop.widget.share.CustomShareBoard;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnItemClickListener {
	/**
	 * 下午2:46:54 TODO 测试ListView
	 */
	@ViewInject(R.id.main_list)
	protected ListView lv;
	@ViewInject(R.id.main_plugs)
	protected Button plugs;
	@ViewInject(R.id.header_back)
	protected ImageView back;
	@ViewInject(R.id.header_title)
	protected TextView title;
	@ViewInject(R.id.header_other)
	protected ImageView other;
	@ViewInject(R.id.drawer_drawer)
	protected DrawerLayout drawer;
	@ViewInject(R.id.drawer_list)
	protected ListView drawerList;

	private MainController controller;

	protected MainAdapter adapter;

	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService(APPEnum.DESCRIPTOR.getDec());

	public CustomShareBoard shareBoard;

	protected ArrayList<DarlyTableModel> data;

	protected ArrayList<Menu> drawData;

	private MainDrawAdapter drawAdapter;

	private long firstime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	protected void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		title.setText(R.string.header_main);

		back.setImageResource(R.drawable.ic_menu_select);

		other.setImageResource(R.drawable.ic_menu_select);
		other.setVisibility(View.INVISIBLE);

		drawer.closeDrawers();

		controller = new MainController(this);
		shareBoard = new CustomShareBoard(this);
		shareBoard.setWXCallBack(controller);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#initListener()
	 */
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(controller);

		lv.setOnItemClickListener(controller);
		plugs.setOnClickListener(controller);

		drawerList.setOnItemClickListener(this);
		DBMongo.getInstance().setOnMongoListener(controller);
		String lastesVersion = 12 + "";
		String versionCode = 9 + "";
		Log.i("版本", lastesVersion.compareTo(versionCode + "") + "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.oop.base.BaseActivity#loadData()
	 */
	@Override
	protected void loadData() {
		// TODO Auto-generated method stub
		data = new ArrayList<DarlyTableModel>();

		data.add(new DarlyTableModel());

		adapter = new MainAdapter(data, R.layout.main_item, this);

		lv.setAdapter(adapter);

		drawerList.addHeaderView(LayoutInflater.from(this).inflate(
				R.layout.header_menu, null));

		drawListData();

		drawAdapter = new MainDrawAdapter(drawData, R.layout.item_drawer_view,
				this);

		drawerList.setAdapter(drawAdapter);

		addWXPlatform();

		addQQQZonePlatform();

		setShareContent();
	}

	/**
	 * 
	 * 上午11:24:21
	 * 
	 * @author zhangyh2 MainActivity.java TODO 菜单列表数据
	 */
	private void drawListData() {
		// TODO Auto-generated method stub
		drawData = new ArrayList<Menu>();
		// 中间选项
		drawData.add(new Menu("", new Menu_Top(R.drawable.ic_index, "首页"),
				APPEnum.ITEMVIEW, false));
		drawData.add(new Menu("", new Menu_Top(R.drawable.ic_me, "消息"),
				APPEnum.ITEMVIEW, false));
		drawData.add(new Menu("", new Menu_Top(R.drawable.ic_set_press, "列表"),
				APPEnum.ITEMVIEW, false));
		drawData.add(new Menu("", new Menu_Top(R.drawable.ic_me_press, "其他"),
				APPEnum.ITEMVIEW, false));
		// 底部选项
		drawData.add(new Menu("个人设置", null, APPEnum.ITEMTITLE, false));
		drawData.add(new Menu("", new Menu_Top(R.drawable.ic_set, "设置"),
				APPEnum.ITEMVIEW, false));
		drawData.add(new Menu("", new Menu_Top(R.drawable.ic_loacl, "关于"),
				APPEnum.ITEMVIEW, false));
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx967daebe835fbeac";
		String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "1104513231";
		String appKey = "VFVBeqWa7Rv2ZeDf";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {
		//
		// // 配置SSO
		// mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		// qq空间分享
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"1104513231", "VFVBeqWa7Rv2ZeDf");
		qZoneSsoHandler.addToSocialSDK();
		mController
				.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能。http://www.umeng.com/social");

		UMImage urlImage = new UMImage(this,
				"http://www.umeng.com/images/pic/social/integrated_3.png");

		// 视频分享
		UMVideo video = new UMVideo(
				"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		video.setTitle("友盟社会化组件视频");
		video.setThumb(urlImage);

		UMusic uMusic = new UMusic(
				"http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
		uMusic.setAuthor("umeng");
		uMusic.setTitle("天籁之音");
		uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

		// 微信分享
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent
				.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-微信。http://www.umeng.com/social");
		weixinContent.setTitle("友盟社会化分享组件-微信");
		weixinContent.setTargetUrl("http://www.umeng.com/social");
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);

		// 设置微信圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia
				.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-朋友圈。http://www.umeng.com/social");
		circleMedia.setTitle("友盟社会化分享组件-朋友圈");
		circleMedia.setShareMedia(urlImage);
		circleMedia.setTargetUrl("http://www.umeng.com/social");
		mController.setShareMedia(circleMedia);

		UMImage image = new UMImage(this, BitmapFactory.decodeResource(
				getResources(), R.drawable.divider));
		image.setTitle("thumb title");
		image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");

		UMImage qzoneImage = new UMImage(this,
				"http://www.umeng.com/images/pic/social/integrated_3.png");
		qzoneImage
				.setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("share test");
		qzone.setTargetUrl("http://www.umeng.com");
		qzone.setTitle("QZone title");
		qzone.setShareMedia(urlImage);
		// qzone.setShareMedia(uMusic);
		mController.setShareMedia(qzone);

		video.setThumb(new UMImage(this, BitmapFactory.decodeResource(
				getResources(), R.drawable.divider)));

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
		qqShareContent.setTitle("hello, title");
		qqShareContent.setShareMedia(image);
		qqShareContent.setTargetUrl("http://www.umeng.com/social");
		mController.setShareMedia(qqShareContent);

		// 视频分享
		UMVideo umVideo = new UMVideo(
				"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
		umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
		umVideo.setTitle("友盟社会化组件视频");

		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能-腾讯微博。http://www.umeng.com/social");
		// 设置tencent分享内容
		mController.setShareMedia(tencent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requst, int result, Intent data) {
		// TODO Auto-generated method stub
		controller.onActivityResult(requst, result, data);
		super.onActivityResult(requst, result, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - firstime > 3000) {
				Toast.makeText(MainActivity.this, "再按一次,退出程序",
						Toast.LENGTH_SHORT).show();
				firstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
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
		for (Menu menu : drawData) {
			menu.isSelect = false;
		}
		Menu menu = (Menu) parent.getItemAtPosition(position);
		menu.isSelect = true;
		drawAdapter.setData(drawData);
		startActivity(new Intent(this, InditorViewpage.class));
		drawer.closeDrawers();

	}

}
