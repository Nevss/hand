/**
 * 上午9:55:09
 * @author zhangyh2
 * $
 * a.java
 * TODO
 */
package com.darly.oop.ui.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author zhangyh2
 * a
 * $
 * 上午9:55:09
 * TODO
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "oop.db";
    private static final int VERSION = 1;
   
    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }
   
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, downpath varchar(100), threadid INTEGER, downlength INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS filedownlog");
        onCreate(db);
    }
}
