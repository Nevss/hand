package com.darly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {
	private static final String DATABASE_NAME = "snote.db";
	private static final int DATABASE_VERSION = 1;

	public static final String CREATE_SNOTE = "create table shoping (id integer primary key autoincrement,product_id integer,product_num integer,product_url varchar,product_name varchar, product_price varchar,product_orprice varchar,product_image varchar,product_spec varchar,product_reless integer)";

	private SQLiteDatabase db;
	private DatabaseHelper mDbHelper;

	private static DB mInstance;

	private DB(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DatabaseHelper(context);
		}
		if (db == null) {
			db = mDbHelper.getWritableDatabase();
		}
	}

	public static DB getmInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DB(context);
		}
		return mInstance;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_SNOTE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("DB", db.toString());
			db.execSQL(CREATE_SNOTE);
		}

	}

}
