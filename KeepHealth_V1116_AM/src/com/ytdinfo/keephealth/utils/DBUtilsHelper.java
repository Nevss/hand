package com.ytdinfo.keephealth.utils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.ytdinfo.keephealth.app.Constants;
import com.ytdinfo.keephealth.app.MyApp;
import com.ytdinfo.keephealth.model.ChatInfoBean;
import com.ytdinfo.keephealth.model.GroupUserInfoBean;

public class DBUtilsHelper {
	 private 	static DbUtils db ;
	 public DbUtils getDb() {
		return db;
	}
	public void setDb(DbUtils db) {
		DBUtilsHelper.db = db;
	}
	private static DBUtilsHelper instance;

		/**
		 * 单例，返回一个实例
		 * 
		 * @return
		 */
		public static DBUtilsHelper getInstance() {
			
				instance = new DBUtilsHelper();
				if (null!=SharedPrefsUtil.getValue(Constants.USERID, null)) {
				
			db = DbUtils.create(MyApp.getInstance(), SharedPrefsUtil.getValue(Constants.USERID, null));
			
				}	
			
			return instance;
		}
	 public   void saveChatinfo(ChatInfoBean chatInfoBean) {
		// TODO Auto-generated method stub
		
		 try {
				db.createTableIfNotExist(ChatInfoBean.class);
				db.saveOrUpdate(chatInfoBean);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	 public   void saveGroupUserInfo(GroupUserInfoBean groupUserInfoBean) {
		 // TODO Auto-generated method stub
		
		 try {
			 db.createTableIfNotExist(GroupUserInfoBean.class);
			 db.save(groupUserInfoBean);
		 } catch (DbException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	 }
	    public   boolean isOnline() {
			// TODO Auto-generated method stub
	    	 
	    	try {
	    		//List<ChatInfoBean> l= db.findAll(ChatInfoBean.class);
	    		//l.toString();
				ChatInfoBean chatInfoBean = db.findFirst(Selector.from(ChatInfoBean.class).where("status","=",true));
			if (null !=chatInfoBean) 
			return true;
	    	
	    	} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
return false;
		}
	
}
