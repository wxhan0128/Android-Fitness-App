package com.ynu.healthyfriendv05.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ynu.healthyfriendv05.po.Recorder;
import com.ynu.healthyfriendv05.tools.DatabaseHelper;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class RecorderDAO {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public RecorderDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		dbHelper.openDatabase();
	}

	public boolean checkByDate(String date) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from recorder where recorder_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	public void add(Recorder recorder) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "insert into recorder("
				+ "user_id,step_count,distance,pushup_count,burn_calories,recorder_date)"
				+ "values(?,?,?,?,?,?)";
		Object obj[] = { recorder.getUserId(), recorder.getStepcount(),
				recorder.getDistance(), recorder.getPushupcount(),
				recorder.getCalories(), recorder.getDate() };
		sqliteDatabase.execSQL(sql, obj);
	}

	public void modifyByDate(Recorder recorder) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "update recorder set step_count=step_count+?,distance=distance+?,"
				+ "pushup_count=pushup_count+?,burn_calories=burn_calories+? "
				+ "where recorder_date=?";
		Object obj[] = { recorder.getStepcount(), recorder.getDistance(),
				recorder.getPushupcount(), recorder.getCalories(),
				recorder.getDate() };
		sqliteDatabase.execSQL(sql, obj);
	}

	public ArrayList<Recorder> selectAll() {
		Recorder recorder = new Recorder();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from recorder";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return recorder.allRecorderInfo(cursor);
	}

	public Recorder selectByDate(String date) {
		Recorder recorder = new Recorder();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from recorder where recorder_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });

		return recorder.recorderInfo(cursor);
	}

	public Recorder selectMaxPushup() {
		Recorder recorder = new Recorder();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from recorder where "
				+ "pushup_count=(select max(pushup_count) from recorder)";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return recorder.recorderInfo(cursor);
	}

	public Recorder selectMaxStep() {
		Recorder recorder = new Recorder();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from recorder where "
				+ "step_count=(select max(step_count) from recorder)";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return recorder.recorderInfo(cursor);
	}

	public Object[] sumValues() {
		Object a[] = new Object[3];
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select sum(step_count),sum(distance),sum(pushup_count) from recorder";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			a[0] = cursor.getInt(0);
			a[1] = Decimaltool.getDoublePrecision(cursor.getDouble(1));
			a[2] = cursor.getInt(2);
			cursor.close();
		}
		for (int i = 0; i < a.length; i++) {
			Log.e("×ÜÊý", "" + a[i]);
		}

		return a;
	}

	public int[] sumValuesByMonth(String month) {
		int a[] = new int[2];
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select sum(step_count),sum(pushup_count) from recorder "
				+ "where recorder_date like ''||?||'%'";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { month });
		if (cursor.moveToFirst()) {
			a[0] = cursor.getInt(0);
			a[1] = cursor.getInt(1);
			cursor.close();
		}

		return a;
	}

	public double sumCaloriesByDate(String date) {
		double c = 0;
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select sum(burn_calories) from recorder where recorder_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });
		if (cursor.moveToFirst()) {
			c = Double.parseDouble(Decimaltool.getDoublePrecision(cursor
					.getDouble(0)));
			cursor.close();
		}

		return c;
	}

	public double sumCaloriesByMonth(String month) {
		double c = 0;
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select sum(burn_calories) from recorder "
				+ "where recorder_date like ''||?||'%'";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { month });
		if (cursor.moveToFirst()) {
			c = Double.parseDouble(Decimaltool.getDoublePrecision(cursor
					.getDouble(0)));
			cursor.close();
		}

		return c;
	}

	public void closeDBHelper() {
		// TODO Auto-generated method stub
		if (dbHelper != null)
			dbHelper.closeDatabase();
	}
}