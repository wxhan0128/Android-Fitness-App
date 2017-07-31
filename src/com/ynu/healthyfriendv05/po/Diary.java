package com.ynu.healthyfriendv05.po;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

public class Diary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int diaryId = 0;
	private int userId = 0;
	private String title = "";
	private String content = "";
	private String createDate = "";

	public int getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(int diaryId) {
		this.diaryId = diaryId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public ArrayList<Diary> allDiaryInfo(Cursor cursor) {
		ArrayList<Diary> list = new ArrayList<Diary>();
		while (cursor.moveToNext()) {
			Diary diary = new Diary();

			diary.setDiaryId(cursor.getInt(cursor.getColumnIndex("diary_id")));
			diary.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			diary.setTitle(cursor.getString(
					cursor.getColumnIndex("diary_title")).trim());
			diary.setContent(cursor.getString(
					cursor.getColumnIndex("diary_content")).trim());
			diary.setCreateDate(cursor.getString(
					cursor.getColumnIndex("create_date")).trim());

			list.add(diary);
		}

		for (int i = 0; i < list.size(); i++) {
			Log.e("日记数据", list.get(i).getDiaryId() + " "
					+ list.get(i).getTitle());
		}

		return list;
	}

	public Diary diaryInfo(Cursor cursor) {
		Diary diary = new Diary();
		while (cursor.moveToNext()) {
			diary.setDiaryId(cursor.getInt(cursor.getColumnIndex("diary_id")));
			diary.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			diary.setTitle(cursor.getString(
					cursor.getColumnIndex("diary_title")).trim());
			diary.setContent(cursor.getString(
					cursor.getColumnIndex("diary_content")).trim());
			diary.setCreateDate(cursor.getString(
					cursor.getColumnIndex("create_date")).trim());
		}

		return diary;
	}
}