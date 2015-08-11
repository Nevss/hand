package com.darly.common;

import android.os.Environment;

/**
* @ClassName: BaseData
* @Description: TODO(基础数据类)
* @author 张宇辉 zhangyuhui@octmami.com
* @date 2014年11月26日 上午9:00:23
*
*/ 
public class BaseData {
	// 一堆图片链接
	public static final String[] IMAGES = new String[] {
			"http://pic15.nipic.com/20110803/8069468_223633682177_2.jpg",
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

	public static boolean isUpdateNote;

	public static final String CREATE_SNOTE = "create table snote (id integer primary key autoincrement,title varchar,message varchar,date varchar)";
	public static final String CREATE_IMAGE = "create table image (id integer primary key autoincrement,date varchar,message varchar,imagepath varchar)";

	public static final String ROOT = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/study";
	public static final String SROOT = ROOT + "/darly";
	public static final String TROOT = SROOT + "/books";
	public static final String IMAGE = SROOT + "/images";
	public static final int REQUESTCODE_CAM = 1001;
	public static final int REQUESTCODE_CAP = 1002;
}
