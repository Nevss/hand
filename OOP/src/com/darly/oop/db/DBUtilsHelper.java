package com.darly.oop.db;

import java.util.ArrayList;
import java.util.List;

import com.darly.oop.base.APP;
import com.darly.oop.model.SimplePazzle;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class DBUtilsHelper {
	private static String dbName = "hellen";

	private DbUtils db;

	public DbUtils getDb() {
		if (db == null) {
			db = DbUtils.create(APP.getInstance(), dbName);
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
	 * @param SimplePazzle
	 *            下午2:15:20
	 * @author Zhangyuhui DBUtilsHelper.java TODO 生成并保存用户信息到数据库
	 */
	public void save(SimplePazzle chatInfoBean) {
		// TODO Auto-generated method stub

		try {
			db.createTableIfNotExist(SimplePazzle.class);
			db.saveOrUpdate(chatInfoBean);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 上午10:33:46
	 * 
	 * @author zhangyh2 DBUtilsHelper.java TODO
	 */
	public List<String> findSimplePazzle() {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>();
		
		try {
			List<SimplePazzle> pazzles = db.findAll(SimplePazzle.class);
			if (pazzles == null) {
				return data;
			}
			for (SimplePazzle string : pazzles) {
				data.add(string.getName());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 
	 * 上午11:05:20
	 * 
	 * @author zhangyh2 DBUtilsHelper.java TODO
	 */
	public boolean findOne(SimplePazzle chatInfoBean) {
		// TODO Auto-generated method stub
		try {
			db.findFirst(SimplePazzle.class);
			
			SimplePazzle z = db.findFirst(Selector.from(SimplePazzle.class)
					.where("name", "=", chatInfoBean.getName()));
			if (z != null) {
				return true;
			} else {
				return false;
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
