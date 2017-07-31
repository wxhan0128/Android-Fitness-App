package com.ynu.healthyfriendv05.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.tools.DatabaseHelper;

public class UserDAO {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public UserDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		dbHelper.openDatabase();
	}

	// 验证登录
	public boolean checkUser() {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from users";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst() == true) {
			cursor.close();
			return true;
		}
		return false;
	}

	// 注册
	public void add(User user) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "insert into users("
				+ "user_name,user_birthday,user_gender,"
				+ "user_weight,user_height,arm_length,"
				+ "step_length,exercise_type,user_desweight,days) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		Object obj[] = { user.getName(), user.getBirthday(), user.getGender(),
				user.getWeight(), user.getHeight(), user.getArmlength(),
				user.getSteplength(), user.getExercise_type(),
				user.getDes_weight(), user.getPlanday() };
		sqliteDatabase.execSQL(sql, obj);
	}

	public void modify(User user) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "update users set user_name=?,user_birthday=?,"
				+ "user_gender=?,user_weight=?,user_height=?,"
				+ "arm_length=?,step_length=?,exercise_type=?,"
				+ "user_desweight=?,days=? where user_id=?";
		Object obj[] = { user.getName(), user.getBirthday(), user.getGender(),
				user.getWeight(), user.getHeight(), user.getArmlength(),
				user.getSteplength(), user.getExercise_type(),
				user.getDes_weight(), user.getPlanday(), user.getId() };
		sqliteDatabase.execSQL(sql, obj);
	}

	// 简单查询
	public User selectAll() {
		User user = new User();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from users";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return user.userInfo(cursor);
	}

	public void closeDBHelper() {
		// TODO Auto-generated method stub
		if (dbHelper != null)
			dbHelper.closeDatabase();
	}
}
