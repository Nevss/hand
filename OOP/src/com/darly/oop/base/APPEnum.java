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
	ISFIRSTCOME("checkisupdate"), /**
	 * 下午3:39:32 TODO 分享定义变量
	 */
	DESCRIPTOR("com.umeng.share"),LOADPATH("PATHLOAD"), /**
	 * 上午10:11:46
	 * TODO 下载文件的文件地址。
	 */
	DOWNLOADPATH(
			"http://gdown.baidu.com/data/wisegame/35c2db2914b058bc/QQ_312.apk");

	public static final int DB_SELECT = 0x0001;

	public static final int DB_INSERT = 0x0002;

	public static final int DB_UPDATA = 0x0003;

	public static final int DB_DELETE = 0x0004;

	public static final int DB_REQUST = 0x0005;

	private String dec;

	private APPEnum(String dec) {
		// TODO Auto-generated constructor stub
		this.dec = dec;
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
