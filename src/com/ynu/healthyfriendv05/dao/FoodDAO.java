package com.ynu.healthyfriendv05.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ynu.healthyfriendv05.po.Food;
import com.ynu.healthyfriendv05.tools.DatabaseHelper;

public class FoodDAO {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public FoodDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		dbHelper.openDatabase();
	}

	// 查询所有食物
	public ArrayList<Food> selectAll() {
		Food food = new Food();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from slim where slim_type=" + "'food'";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return food.foodInfo(cursor);
	}
}
