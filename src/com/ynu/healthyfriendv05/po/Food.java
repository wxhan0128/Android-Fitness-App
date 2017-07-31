package com.ynu.healthyfriendv05.po;

import java.io.Serializable;
import java.util.ArrayList;

import com.ynu.healthyfriendv05.tools.Decimaltool;

import android.database.Cursor;
import android.util.Log;

public class Food implements Serializable, Slim {
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

	public Food() {

	}

	public Food(String name) {
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

	public ArrayList<Food> foodInfo(Cursor cursor) {
		ArrayList<Food> list = new ArrayList<Food>();
		while (cursor.moveToNext()) {
			Food food = new Food();

			food.setId(cursor.getInt(cursor.getColumnIndex("slim_id")));
			food.setName(cursor.getString(cursor.getColumnIndex("slim_name"))
					.trim());
			food.setIntroduce(cursor.getString(
					cursor.getColumnIndex("slim_introduce")).trim());
			food.setDescription(cursor.getString(
					cursor.getColumnIndex("slim_description")).trim());
			food.setCalories(cursor.getDouble(cursor
					.getColumnIndex("slim_calories")));
			food.setPic(cursor.getBlob(cursor.getColumnIndex("slim_pic")));

			list.add(food);
		}

		for (int i = 0; i < list.size(); i++) {
			Log.e("tag", list.get(i).getId() + " " + list.get(i).getName());
		}

		return list;
	}
}
