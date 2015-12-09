package com.darly.activities.app;

import java.util.ArrayList;

import android.os.Environment;

import com.darly.activities.model.UserInformation;

/**
 * @ClassName: Literal
 * @Description: TODO(Handler的常量类)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月26日 上午9:03:57
 *
 */
public class Constract {
	public static final int GET_HANDLER = 0x00121;
	public static final int POST_HANDLER = 0x00101;

	/**
	 * VERSION參數，当参数为0的时候，展示最老的首页版本，当参数为1的时候，展示现在的每周精选版本。
	 */
	public static final int TOASTTIME = 10;

	public static int width = -1;

	public static int height = -1;

	public static int desty = -1;

	public static double bitmapheight;
	public static double bitmapwidth;

	public static final String ROOT = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/activities/";

	public static final String SROOT = ROOT + "image/";
	/** 头像的路径(所有照片的临时存储位置和名字) */
	public static final String HEAD = SROOT + "head.png";

	public static final String LOG = ROOT + "log/";

	public static String capUri;

	public static final int REQUESTCODE_CAM = 0x1001;
	public static final int REQUESTCODE_CAP = 0x1002;
	public static final int REQUESTCODE_CUT = 0x1003;
	public static final int TOKEN = 0x1004;

	public static final String USERINFO = "USERDATA";

	public static ArrayList<UserInformation> users;

	public static String QJAppKey = "71f48a13-4a2f-4e7e-b4b0-7b1bd961793b";

	public static final String DESCRIPTOR = "com.umeng.share";

}