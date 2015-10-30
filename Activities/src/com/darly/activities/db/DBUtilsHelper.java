package com.darly.activities.db;

import com.darly.activities.app.AppStack;
import com.darly.activities.common.LogFileHelper;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class DBUtilsHelper {
	private static String dbName;

	private DbUtils db;

	public DbUtils getDb() {
		if (db == null) {
			db = DbUtils.create(AppStack.getInstance(), dbName);
		}
		return db;
	}

	public void setDb(DbUtils db) {
		this.db = db;
	}

	private static DBUtilsHelper instance;

	/**
	 * @return 下午2:14:50
	 * @author Zhangyuhui DBUtilsHelper.java TODO 单例模式
	 */
	public static DBUtilsHelper getInstance() {
		if (instance == null) {
			instance = new DBUtilsHelper();
		}
		return instance;
	}

	/**
	 * @param chatInfoBean
	 *            下午2:15:20
	 * @author Zhangyuhui DBUtilsHelper.java TODO 生成并保存用户信息到数据库
	 */
	public void saveChatinfo(ChatInfoBean chatInfoBean) {
		// TODO Auto-generated method stub

		try {
			db.createTableIfNotExist(ChatInfoBean.class);
			db.saveOrUpdate(chatInfoBean);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e("DBUtilsHelper", e.getMessage());
		}
	}

	/**
	 * @param docInfoBean
	 *            下午2:15:47
	 * @author Zhangyuhui DBUtilsHelper.java TODO 生成并保存医生信息到数据库
	 */
	public void saveDocinfo(DocInfoBean docInfoBean) {
		// TODO Auto-generated method stub

		try {
			db.createTableIfNotExist(DocInfoBean.class);
			db.saveOrUpdate(docInfoBean);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e("DBUtilsHelper", e.getMessage());
		}
	}

	/**
	 * @param groupUserInfoBean
	 *            下午2:16:16
	 * @author Zhangyuhui DBUtilsHelper.java TODO 生成并保存用户群组信息到数据库
	 */
	public void saveGroupUserInfo(GroupUserInfoBean groupUserInfoBean) {
		// TODO Auto-generated method stub

		try {
			db.createTableIfNotExist(GroupUserInfoBean.class);
			db.save(groupUserInfoBean);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e("DBUtilsHelper", e.getMessage());
		}
	}

	/**
	 * @return 下午2:16:42
	 * @author Zhangyuhui DBUtilsHelper.java TODO 查詢是否正在聊天。
	 */
	public boolean isOnline() {
		// TODO Auto-generated method stub

		try {
			// List<ChatInfoBean> l= db.findAll(ChatInfoBean.class);
			// l.toString();
			ChatInfoBean chatInfoBean = db.findFirst(Selector.from(
					ChatInfoBean.class).where("status", "=", true));
			if (null != chatInfoBean)
				return true;

		} catch (DbException e) {
			// TODO Auto-generated catch block
			LogFileHelper.getInstance().e("DBUtilsHelper", e.getMessage());
		}
		return false;
	}

}
