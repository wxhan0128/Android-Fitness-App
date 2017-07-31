package com.ynu.healthyfriendv05.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ynu.healthyfriendv05.po.Diary;
import com.ynu.healthyfriendv05.tools.DatabaseHelper;

public class DiaryDAO {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public DiaryDAO(Context context) {
		dbHelper = new DatabaseHelper(context);
		dbHelper.openDatabase();
	}

	public ArrayList<Diary> selectAll() {
		Diary diary = new Diary();
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "select * from diary";
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		return diary.allDiaryInfo(cursor);
	}

	public boolean selectByDate(String date) {
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from diary where create_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	public Diary selectDiaryByDate(String date) {
		Diary diary = new Diary();
		sqliteDatabase = dbHelper.getReadableDatabase();
		String sql = "select * from diary where create_date=?";
		Cursor cursor = sqliteDatabase.rawQuery(sql, new String[] { date });

		return diary.diaryInfo(cursor);
	}

	public void add(Diary diary) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "insert into diary("
				+ "user_id,diary_title,diary_content,create_date)"
				+ "values(?,?,?,?)";
		Object obj[] = { diary.getUserId(), diary.getTitle(),
				diary.getContent(), diary.getCreateDate() };
		sqliteDatabase.execSQL(sql, obj);
	}

	public void modifyByDate(Diary diary) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "update diary set diary_title=?,diary_content=? "
				+ "where create_date=?";
		Object obj[] = { diary.getTitle(), diary.getContent(),
				diary.getCreateDate() };
		sqliteDatabase.execSQL(sql, obj);
	}

	public void deleteItem(Diary diary) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String sql = "delete from diary where diary_id=?";
		Object obj[] = { diary.getDiaryId() };
		sqliteDatabase.execSQL(sql, obj);
	}
}
