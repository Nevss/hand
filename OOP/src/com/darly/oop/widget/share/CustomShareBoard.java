package com.darly.oop.widget.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public class CustomShareBoard extends PopupWindow implements OnClickListener {

	public WXCallBack wxCallBack = null;

	public void setWXCallBack(WXCallBack mwxCallBack) {
		this.wxCallBack = mwxCallBack;
	}

	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(APPEnum.DESCRIPTOR.getDec());
	private Activity mActivity;

	public CustomShareBoard(Activity activity) {
		super(activity);
		this.mActivity = activity;
		initView(activity);
	}

	/**
	 * 
	 * 上午10:29:01
	 * 
	 * @author zhangyh2 CustomShareBoard.java TODO
	 */
	public CustomShareBoard(Context context, AttributeSet set) {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.custom_board, null);
		rootView.findViewById(R.id.wechat).setOnClickListener(this);
		rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
		rootView.findViewById(R.id.qq).setOnClickListener(this);
		rootView.findViewById(R.id.qzone).setOnClickListener(this);
		rootView.findViewById(R.id.consel).setOnClickListener(this);
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.wechat:
			performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.wechat_circle:
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.qq:
			performShare(SHARE_MEDIA.QQ);
			break;
		case R.id.qzone:
			performShare(SHARE_MEDIA.QZONE);
			break;
		case R.id.consel:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {

			@Override
			public void onStart() {
				LogUtils.i("onStart share");
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					if (wxCallBack != null)
						wxCallBack.shareComplete(true);
					// showText += "平台分享成功";
				} else {
					if (wxCallBack != null)
						wxCallBack.shareComplete(false);
					// showText += "平台分享失败";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}

}
