package com.ynu.healthyfriendv05.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ynu.healthyfriendv05.po.UserSlim;
import com.ynu.healthyfriendv05.tools.DatabaseHelper;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class UserSlimDAO {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public UserSlimDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		dbHelper.openDatabase();
	}

	public boolean selectByIdAndDate(UserSlim userSlim) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim where user_id=? "
				+ "and slim_id=? and choose_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(
				sql,
				new String[] { String.valueOf(userSlim.getUserId()),
						String.valueOf(userSlim.getSlimId()),
						userSlim.getDate() });
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	public boolean selectByTask(UserSlim userSlim) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim where user_id=? "
				+ "and slim_id=? and task_flag=1";
		Cursor cursor = sqliteDatabase.rawQuery(
				sql,
				new String[] { String.valueOf(userSlim.getUserId()),
						String.valueOf(userSlim.getSlimId()) });
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	public void add(UserSlim userSlim) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "insert into user_slim(user_id,slim_id,task_flag,choose_date)"
				+ "values(?,?,?,?)";
		Object obj[] = { userSlim.getUserId(), userSlim.getSlimId(),
				userSlim.getTask(), userSlim.getDate() };
		sqliteDatabase.execSQL(sql, obj);
	}

	public ArrayList<UserSlim> selectAll() {
		UserSlim userSlim = new UserSlim();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return userSlim.userSlimInfo(cursor);
	}

	public ArrayList<UserSlim> selectSportsTask() {
		UserSlim userSlim = new UserSlim();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim,slim "
				+ "where user_slim.slim_id=slim.slim_id " + "and task_flag=1";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return userSlim.certainUserSlimAllInfo(cursor);
	}

	public ArrayList<UserSlim> selectSportsByDate(String date) {
		UserSlim userSlim = new UserSlim();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim,slim "
				+ "where user_slim.slim_id=slim.slim_id "
				+ "and slim_type='sport' and choose_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });

		return userSlim.certainUserSlimAllInfo(cursor);
	}

	public ArrayList<UserSlim> selectSportsByDateOrTask(String date) {
		UserSlim userSlim = new UserSlim();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim,slim "
				+ "where user_slim.slim_id=slim.slim_id "
				+ "and slim_type='sport' and (choose_date=? or task_flag=1)";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });

		return userSlim.certainUserSlimAllInfo(cursor);
	}

	public ArrayList<UserSlim> selectDietByDate(String date) {
		UserSlim userSlim = new UserSlim();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from user_slim,slim "
				+ "where user_slim.slim_id=slim.slim_id "
				+ "and slim_type='food' and choose_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });

		return userSlim.certainUserSlimAllInfo(cursor);
	}

	public double sumCaloriesByDate(String type, String date) {
		double c = 0;
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select sum(slim_calories) from slim,user_slim "
				+ "where user_slim.slim_id=slim.slim_id and "
				+ "slim.slim_type=? and user_slim.choose_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql,
				new String[] { type, date });
		if (cursor.moveToFirst()) {
			c = Double.parseDouble(Decimaltool.getDoublePrecision(cursor
					.getDouble(0)));
			// c = cursor.getDouble(0);
			cursor.close();
		}

		return c;
	}

	public double sumCaloriesByMonth(String type, String month) {
		double c = 0;
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select sum(slim_calories) from slim,user_slim "
				+ "where user_slim.slim_id=slim.slim_id and "
				+ "slim.slim_type=? and user_slim.choose_date like ''||?||'%'";
		Cursor cursor = sqliteDatabase.rawQuery(sql,
				new String[] { type, month });
		if (cursor.moveToFirst()) {
			c = cursor.getDouble(0);
			cursor.close();
		}

		return c;
	}

	public double sumTaskCalories() {
		double c = 0;
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select sum(slim_calories) from slim,user_slim "
				+ "where user_slim.slim_id=slim.slim_id and user_slim.task_flag=1";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			c = Double.parseDouble(Decimaltool.getDoublePrecision(cursor
					.getDouble(0)));
			// c = cursor.getDouble(0);
			cursor.close();
		}

		return c;
	}

	public void deleteItem(UserSlim userSlim) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "delete from user_slim where slim_id=? and choose_date=?";
		Object obj[] = { userSlim.getSlimId(), userSlim.getDate() };
		sqliteDatabase.execSQL(sql, obj);
	}
}