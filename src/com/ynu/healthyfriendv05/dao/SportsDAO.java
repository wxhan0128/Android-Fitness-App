package com.ynu.healthyfriendv05.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ynu.healthyfriendv05.po.Sports;
import com.ynu.healthyfriendv05.tools.DatabaseHelper;

public class SportsDAO {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public SportsDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		dbHelper.openDatabase();
	}

	// 查询所有运动
	public ArrayList<Sports> selectAll() {
		Sports sport = new Sports();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from slim where slim_type=" + "'sport'";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return sport.sportInfo(cursor);
	}

	public ArrayList<Sports> selectPackage(String package_type) {
		Sports sport = new Sports();
		String sql = null;
		sqliteDatabase = dbHelper.getWritableDatabase();
		if (package_type.equals("A"))
			sql = "select * from slim where slim_id=1 or slim_id=11 or slim_id=13";
		else if (package_type.equals("B"))
			sql = "select * from slim where slim_id=7 or slim_id=24 or slim_id=19";
		else if (package_type.equals("C"))
			sql = "select * from slim where slim_id=3 or slim_id=16 or slim_id=17";
		else if (package_type.equals("D"))
			sql = "select * from slim where slim_id=12 or slim_id=21 or slim_id=28";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return sport.sportInfo(cursor);
	}
}
