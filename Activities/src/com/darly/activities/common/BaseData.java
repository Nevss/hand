package com.darly.activities.common;

import java.util.ArrayList;

import com.darly.activities.model.BottomModel;
import com.darly.activities.model.UserInformation;
import com.darly.activities.ui.R;
import com.gotye.api.GotyeUser;

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

	/**
	 * @return the data
	 */
	public static ArrayList<BottomModel> getData() {
		ArrayList<BottomModel> data = new ArrayList<BottomModel>();
		data.add(new BottomModel(R.drawable.ic_index_press, "扫一扫",
				"MipcaActivityCapture"));
		data.add(new BottomModel(R.drawable.ic_set_press, "机构",
				"IndexShowViewActivity"));
		data.add(new BottomModel(R.drawable.ic_me_press, "详情",
				"MeDetailsAcitvity"));
		data.add(new BottomModel(R.drawable.ic_search, "旋转", "RotateAcitvity"));
		return data;
	}

	/**
	 * @return 上午9:38:20
	 * @author Zhangyuhui BaseData.java TODO 测试数据，获取的是当前所有用户信息。当然，使用的是UserID而已。
	 */
	public static ArrayList<UserInformation> getUsers() {
		ArrayList<UserInformation> infos = new ArrayList<UserInformation>();
		UserInformation user1 = new UserInformation();
		user1.setUserAge(28);
		user1.setUserSex(1);
		user1.setUsername("18321127312");
		user1.setUserTrueName("darly");
		user1.setUserID("18321127312");
		user1.setUserToken("+9SIwce7WWdenAPV68stCbEzg2PO5RfFjDpIMZ3SH5pI+C8Qo/55ds2uaBIsRNDUA/G+ZMIfJO8SRi2C78cM3snEM7o3kRwf");
		user1.setUser(new GotyeUser("18321127312"));
		infos.add(user1);

		UserInformation user2 = new UserInformation();
		user2.setUserAge(26);
		user2.setUserSex(0);
		user2.setUsername("13891431454");
		user2.setUserTrueName("hellen");
		user2.setUserID("13891431454");
		user2.setUserToken("0IJdBjvLpZ3E62ivp+VZxTpfCOjKborXvgZ7VwhenilYR5KmRB6ja9rjep8KOoP2w0DfbHgvoCeWDps/MXaiQgYHpk+t6P1J");
		user2.setUser(new GotyeUser("13891431454"));
		infos.add(user2);

		return infos;
	}

}
