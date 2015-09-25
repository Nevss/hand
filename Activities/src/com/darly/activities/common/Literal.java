package com.darly.activities.common;

import android.content.Context;
import android.view.WindowManager;

import com.darly.activities.app.Declare;

/**
 * @ClassName: Literal
 * @Description: TODO(Handler的常量类)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月26日 上午9:03:57
 *
 */
public class Literal {
	public static final int EA_HANDLER = 0x00012;
	/*
	 * CA_HANDLER handler for goto MainActivity
	 */
	public static final int CA_HANDLER = 0x00022;
	public static final int TA_HANDLER = 0x00011;
	/*
	 * DA_HANDLER handler for msg
	 */
	public static final int DA_HANDLER = 0x00021;
	public static final int GET_HANDLER = 0x00121;
	public static final int POST_HANDLER = 0x00101;
	public static final int SHOW_HANDLER = 0x00102;
	public static final int HINT_HANDLER = 0x00110;

	public static boolean isLogin;
	public static boolean isCardEmpty = true;
	public static boolean CardUpdate;
	@Declare(NAME = "isTaxExsit", TODO = "发票信息是否存在，TRUE为存在，FALSE为不存在，默认不存在")
	public static boolean isTaxExsit;

	/**
	 * VERSION參數，当参数为0的时候，展示最老的首页版本，当参数为1的时候，展示现在的每周精选版本。
	 */
	public static final int VERSION = 1;
	public static final int TOASTTIME = 10;

	public static int width = 0;

	public static int height = 0;

	@SuppressWarnings("deprecation")
	public static void getWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
	}

	public static boolean isShowMktprice = false;
	public static boolean FragmentMeisShow;
	public static double bitmapheight;
	public static double bitmapwidth;
}
