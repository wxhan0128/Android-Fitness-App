package com.ynu.healthyfriendv05.po;

import java.io.Serializable;
import java.util.ArrayList;

import com.ynu.healthyfriendv05.tools.Decimaltool;

import android.database.Cursor;
import android.util.Log;

public class Sports implements Serializable, Slim {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id = 0;
	private String name = "";
	private String introduce = "";
	private String description = "";
	private double calories = 0;
	private byte[] pic;

	public Sports() {

	}

	public Sports(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCalories() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(calories));
	}

	public void setCalories(double calories) {
		this.calories = Double.parseDouble(Decimaltool
				.getDoublePrecision(calories));
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public ArrayList<Sports> sportInfo(Cursor cursor) {
		ArrayList<Sports> list = new ArrayList<Sports>();

		while (cursor.moveToNext()) {
			Sports sport = new Sports();

			sport.setId(cursor.getInt(cursor.getColumnIndex("slim_id")));
			sport.setName(cursor.getString(cursor.getColumnIndex("slim_name"))
					.trim());
			sport.setIntroduce(cursor.getString(
					cursor.getColumnIndex("slim_introduce")).trim());
			sport.setDescription(cursor.getString(
					cursor.getColumnIndex("slim_description")).trim());
			sport.setCalories(cursor.getDouble(cursor
					.getColumnIndex("slim_calories")));
			sport.setPic(cursor.getBlob(cursor.getColumnIndex("slim_pic")));

			list.add(sport);
		}

		for (int i = 0; i < list.size(); i++) {
			Log.e("tag", list.get(i).getId() + " " + list.get(i).getName());
		}

		return list;
	}
}
