package com.darly.common;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * @ClassName: SDCardUtils
 * @Description: TODO(SD卡相关的辅助类,1.判断SDCard是否可用,2.获取SD卡路径,3.获取SD卡的剩余容量
 *               单位byte,4.获取指定路径所在空间的剩余可用容量字节数，单位byte,5.获取系统存储路径)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年10月24日 下午3:06:46
 *
 */
public class SDCardUtils {
	private SDCardUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:59:44 Project_Name:DFram
	 * Description:TODO(判断SDCard是否可用) Throws Return:boolean
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午9:59:59 Project_Name:DFram
	 * Description:TODO(获取SD卡路径) Throws Return:String
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:00:11 Project_Name:DFram
	 * Description:TODO(获取SD卡的剩余容量 单位byte) Throws Return:long
	 */
	public static long getSDCardAllSize() {
		if (isSDCardEnable()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数量
			@SuppressWarnings("deprecation")
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// 获取单个数据块的大小（byte）
			@SuppressWarnings("deprecation")
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:00:21 Project_Name:DFram
	 * Description:TODO(获取指定路径所在空间的剩余可用容量字节数，单位byte) Throws Return:long容量字节
	 * SDCard可用空间，内部存储可用空间
	 */
	@SuppressWarnings("deprecation")
	public static long getFreeBytes(String filePath) {
		// 如果是sd卡的下的路径，则获取sd卡可用容量
		if (filePath.startsWith(getSDCardPath())) {
			filePath = getSDCardPath();
		} else {// 如果是内部存储的路径，则获取内存存储的可用容量
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}

	/**
	 * Auther:张宇辉 User:zhangyuhui 2015年1月5日 上午10:00:41 Project_Name:DFram
	 * Description:TODO(获取系统存储路径) Throws Return:String
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

}
