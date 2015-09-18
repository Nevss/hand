package com.darly.activities.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.darly.activities.common.LogApp;
import com.darly.activities.model.SNote;

public class SnoteTable extends BaseTable<SNote> implements SnoteDAO {
	public SnoteTable(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private static final String TAG = SnoteTable.class.getSimpleName();

	@Override
	public long add(SNote note) {
		long i = -1;
		if (note == null) {
			return i;
		}
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(PRODUCT_ID, note.product_id);
		values.put(PRODUCT_NUM, note.product_num);
		values.put(PRODUCT_URL, note.product_url);
		values.put(PRODUCT_NAME, note.product_name);
		values.put(PRODUCT_PRICE, note.product_price);
		values.put(PRODUCT_ORPRICE, note.product_orprice);
		values.put(PRODUCT_IMAGE, note.product_image);
		values.put(PRODUCT_SPEC, note.product_spec);
		values.put(PRODUCT_RELESS, note.product_reless);
		i = db.insert(TABLE_NAME, null, values);
		Log.i(TAG, "db.insert(TABLE_NAME, null, values);");
		db.setTransactionSuccessful();
		db.endTransaction();
		return i;
	}

	public void delete(int product_id) {
		String whereClause = String.format("%s = '%s'", PRODUCT_ID, product_id);
		db.delete(TABLE_NAME, whereClause, null);
	}

	@Override
	public int deleteAll() {
		int i = db.delete(TABLE_NAME, null, null);
		return i;
	}

	@Override
	public SNote select(SNote note) {
		if (note == null) {
			return null;
		}

		SNote newnote = null;
		String selection = String.format("%s = '%s'", PRODUCT_ID,
				note.product_id);
		Cursor c = db
				.query(TABLE_NAME, null, selection, null, null, null, null);
		if (c != null) {

			while (c.moveToNext()) {
				int id = c.getInt(_ID);
				int product_id = c.getInt(_PRODUCT_ID);
				int product_num = c.getInt(_PRODUCT_NUM);
				String product_url = c.getString(_PRODUCT_URL);
				String product_name = c.getString(_PRODUCT_NAME);
				String product_price = c.getString(_PRODUCT_PRICE);
				String product_orprice = c.getString(_PRODUCT_ORPRICE);
				String product_image = c.getString(_PRODUCT_IMAGE);
				String product_spec = c.getString(_PRODUCT_SPEC);
				int product_reless = c.getInt(_PRODUCT_RELESS);
				newnote = new SNote(id, product_id, product_num, product_url,
						product_name, product_price, product_orprice,
						product_image, product_spec, product_reless);
			}
		}
		return newnote;
	}

	public void check(SNote note) {
		if (note == null) {
			return;
		}
		String selection = String.format("%s = '%s'", PRODUCT_ID,
				note.product_id);
		Cursor c = db
				.query(TABLE_NAME, null, selection, null, null, null, null);
		LogApp.i(c.getColumnName(_PRODUCT_ID) + c.getColumnCount()
				+ c.getCount());
		if (c.getCount() != 0) {
			// 已经存在商品
			int num = 0;
			while (c.moveToNext()) {
				int product_num = c.getInt(_PRODUCT_NUM);
				num += product_num;
			}

			update(note, note.product_num + num);
		} else {
			add(note);
		}
	}

	@Override
	public List<SNote> select() {
		List<SNote> list = new ArrayList<SNote>();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (c.moveToNext()) {
			int id = c.getInt(_ID);
			int product_id = c.getInt(_PRODUCT_ID);
			int product_num = c.getInt(_PRODUCT_NUM);
			String product_url = c.getString(_PRODUCT_URL);
			String product_name = c.getString(_PRODUCT_NAME);
			String product_price = c.getString(_PRODUCT_PRICE);
			String product_orprice = c.getString(_PRODUCT_ORPRICE);
			String product_image = c.getString(_PRODUCT_IMAGE);
			String product_spec = c.getString(_PRODUCT_SPEC);
			int product_reless = c.getInt(_PRODUCT_RELESS);
			SNote note = new SNote(id, product_id, product_num, product_url,
					product_name, product_price, product_orprice,
					product_image, product_spec, product_reless);
			list.add(note);
		}
		return list;
	}

	public int getCount() {
		int tem = 0;
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (c.moveToNext()) {
			int product_num = c.getInt(_PRODUCT_NUM);
			tem += product_num;
			LogApp.i(tem + "");
		}
		return tem;
	}

	public String getPrice() {
		double tem = 0;
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (c.moveToNext()) {
			int product_num = c.getInt(_PRODUCT_NUM);
			String product_price = c.getString(_PRODUCT_PRICE);
			tem += product_num * Double.parseDouble(product_price);
			LogApp.i(tem + "");
		}
		return tem + "";
	}

	public int update(SNote note, int product_num) {
		int i = -1;
		if (note == null) {
			return i;
		}
		ContentValues values = new ContentValues();
		values.put(PRODUCT_NUM, product_num);

		String whereClause = String.format("%s = '%s'", PRODUCT_ID,
				note.product_id);
		i = db.update(TABLE_NAME, values, whereClause, null);
		return i;
	}

	@Override
	public int update(SNote note) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(SNote note) {
		// TODO Auto-generated method stub
		return 0;
	}

}
