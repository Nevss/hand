package com.darly.activities.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseTable<T> {

	protected SQLiteDatabase db;

	public BaseTable(Context context) {
		if (db == null) {
			db = DB.getmInstance(context).getDb();
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public void closeDB() {
		db.close();
	}

	public abstract long add(T note);

	public abstract int delete(T note);

	public abstract int deleteAll();

	public abstract T select(T note);

	public abstract List<T> select();

	public abstract int update(T note);
}
