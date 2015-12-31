/**
 * 下午3:21:36
 * @author zhangyh2
 * $
 * SharePreferEnum.java
 * TODO
 */
package com.darly.oop.base;

/**
 * @author zhangyh2 SharePreferEnum $ 下午3:21:36 TODO 缓存文件枚举类
 */
public enum APPEnum {
	// 方案一
	/**
	 * 下午3:30:24 TODO 是否首次安装程序，判断是否进入引导页面。
	 */
	ISFIRSTCOME("checkisupdate", 0), /**
	 * 下午3:39:32 TODO 分享定义变量
	 */
	DESCRIPTOR("com.umeng.share", 0), LOADPATH("PATHLOAD", 0), /**
	 * 上午10:11:46
	 * TODO 下载文件的文件地址。
	 */
	DOWNLOADPATH(
			"http://dlsw.baidu.com/sw-search-sp/soft/2e/10849/winrar521scp.1432703245.exe",
			0), WIDTH("screen width", 0), HEIGHT("screen height", 0);

	public static final int DB_SELECT = 0x0001;

	public static final int DB_INSERT = 0x0002;

	public static final int DB_UPDATA = 0x0003;

	public static final int DB_DELETE = 0x0004;

	public static final int DB_REQUST = 0x0005;

	/**
	 * 下午1:45:21 TODO 抽屉菜单样式，标题样式
	 */
	public static final int ITEMTITLE = 0x0006;

	/**
	 * 下午1:45:25 TODO 抽屉菜单样式，条目样式
	 */
	public static final int ITEMVIEW = 0x0007;

	/**
	 * 下午1:46:52
	 * TODO 抽屉菜单样式，条目样式(点击选择的效果)
	 */
	public static final int ITEMSELECTVIEW = 0x0010;
	/**
	 * 下午1:46:52
	 * TODO 抽屉菜单样式，条目样式(点击选择的效果)
	 */
	public static final int ITEMSELECTITLE = 0x0011;
	private String dec;

	private int len;

	private APPEnum(String dec, int len) {
		// TODO Auto-generated constructor stub
		this.dec = dec;
		this.len = len;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	// 方案二：
	// ISFIRSTCOME {
	// @Override
	// public String getName() {
	// // TODO Auto-generated method stub
	// return "checkisupdate";
	// }
	// },
	// DESCRIPTOR {
	// @Override
	// public String getName() {
	// // TODO Auto-generated method stub
	// return "com.umeng.share";
	// }
	// };
	// public abstract String getName();

}
