/**
 * 下午5:59:52
 * @author zhangyh2
 * $
 * DBMongo.java
 * TODO
 */
package com.darly.oop.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.darly.oop.model.DarlyTableModel;
import com.lidroid.xutils.util.LogUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

/**
 * @author zhangyh2 DBMongo $ 下午5:59:52 TODO
 */
public class DBMongo {

	public interface MongoListener {
		public void selectDB(Object object);

		public void updateDB(Object object);

		public void insertDB(Object object);

		public void deleteDB(Object object);
	}

	private static DBMongo instance;

	private MongoListener onMongoListener;

	private MongoClient client;

	private DB db;

	public static boolean isMongoOnline;

	/**
	 * 
	 * 下午6:00:12
	 * 
	 * @author zhangyh2 DBMongo.java TODO
	 */
	private DBMongo() {
		// TODO Auto-generated constructor stub
		initDB();
	}

	/**
	 * 
	 * 下午6:00:49
	 * 
	 * @author zhangyh2 DBMongo.java TODO
	 */
	@SuppressWarnings("deprecation")
	private void initDB() {
		// TODO Auto-generated method stub
		try {
			client = new MongoClient("10.0.2.2", 27017);
			db = client.getDB("local");
			isMongoOnline = true;
		} catch (Exception e) {
			// TODO: handle exception
			isMongoOnline = false;
			LogUtils.i("Mongo数据库连接失败。" + e.getMessage());
		}

	}

	/**
	 * @return the instance
	 */
	public static DBMongo getInstance() {
		if (instance == null) {
			instance = new DBMongo();
		}
		return instance;
	}

	/**
	 * 
	 * 下午6:08:30
	 * 
	 * @author zhangyh2 DBMongo.java TODO
	 */
	public void closeMongo() {
		// TODO Auto-generated method stub
		if (client != null) {
			client.close();
		}
	}

	/**
	 * @param onMongoListener
	 *            the onMongoListener to set
	 */
	public void setOnMongoListener(MongoListener onMongoListener) {
		this.onMongoListener = onMongoListener;
	}

	public DB getDB() {
		return db;
	}

	public MongoClient getClient() {
		return client;
	}

	/**
	 * 
	 * 上午11:27:09
	 * 
	 * @author zhangyh2 DBMongo.java TODO
	 */
	public void select(final String tablename) {
		// TODO Auto-generated method stub
		final DBCollection collection = db.getCollection(tablename);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 增删查改必须放到线程中进行。否则报错。
					DBCursor dbObject = collection.find();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("table", tablename);
					jsonObject.put("code", 200);

					JSONArray array = new JSONArray();
					while (dbObject.hasNext()) {
						DBObject ob = dbObject.next();
						array.put(ob);
					}
					jsonObject.put("data", array);
					jsonObject.put("msg", "数据查询成功");
					onMongoListener.selectDB(jsonObject);
				} catch (Exception e) {
					// TODO: handle exception
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("table", tablename);
						jsonObject.put("code", 400);
						jsonObject.put("data", null);
						jsonObject.put("success", false);
						jsonObject.put("msg", "数据查询失败");
						onMongoListener.selectDB(jsonObject);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
					}
				}
			}
		}).start();
	}

	public void update(final String tablename, final DarlyTableModel query,
			final DarlyTableModel updata) {
		final DBCollection collection = db.getCollection(tablename);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 增删查改必须放到线程中进行。否则报错。
					BasicDBObject object = new BasicDBObject();
					object.put("name", query.name);
					object.put("age", query.age);
					object.put("sex", query.sex);
					object.put("time", query.time);
					BasicDBObject des = new BasicDBObject();
					des.put("name", updata.name);
					des.put("age", updata.age);
					des.put("sex", updata.sex);
					des.put("time", updata.time);
					WriteResult dbObject = collection.update(object, des);
					boolean isgood = dbObject.isUpdateOfExisting();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("table", tablename);
					jsonObject.put("code", 200);
					jsonObject.put("success", isgood);
					jsonObject.put("msg", "数据更新成功");
					onMongoListener.updateDB(jsonObject);
				} catch (Exception e) {
					// TODO: handle exception
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("table", tablename);
						jsonObject.put("code", 400);
						jsonObject.put("success", false);
						jsonObject.put("msg", "数据更新失败");
						onMongoListener.updateDB(jsonObject);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
					}
				}
			}
		}).start();
	}

	public void insert(final String tablename, final DarlyTableModel model) {
		final DBCollection collection = db.getCollection(tablename);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 增删查改必须放到线程中进行。否则报错。
					BasicDBObject object = new BasicDBObject();
					object.put("name", model.name);
					object.put("age", model.age);
					object.put("sex", model.sex);
					object.put("time", model.time);
					WriteResult dbObject = collection.insert(object);
					boolean isgood = dbObject.isUpdateOfExisting();
					int num = dbObject.getN();
					LogUtils.i("insert方法进行执行" + num);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("table", tablename);
					jsonObject.put("code", 200);
					jsonObject.put("success", isgood);
					jsonObject.put("msg", "数据插入成功");
					onMongoListener.insertDB(jsonObject);
				} catch (Exception e) {
					// TODO: handle exception
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("table", tablename);
						jsonObject.put("code", 400);
						jsonObject.put("success", false);
						jsonObject.put("msg", "数据插入失败");
						onMongoListener.insertDB(jsonObject);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
					}
				}
			}
		}).start();
	}

	public void delete(final String tablename, final DarlyTableModel model) {
		final DBCollection collection = db.getCollection(tablename);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 增删查改必须放到线程中进行。否则报错。
					BasicDBObject object = new BasicDBObject();
					object.put("name", model.name);
					object.put("age", model.age);
					object.put("sex", model.sex);
					object.put("time", model.time);
					WriteResult dbObject = collection.remove(object);
					boolean isgood = dbObject.isUpdateOfExisting();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("table", tablename);
					jsonObject.put("code", 200);
					jsonObject.put("success", isgood);
					jsonObject.put("msg", "数据删除成功");
					onMongoListener.deleteDB(jsonObject);
				} catch (Exception e) {
					// TODO: handle exception
					try {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("table", tablename);
						jsonObject.put("code", 400);
						jsonObject.put("success", false);
						jsonObject.put("msg", "数据删除失败");
						onMongoListener.deleteDB(jsonObject);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
					}

				}
			}
		}).start();
	}

}
