package com.ynu.healthyfriendv05.po;

import java.io.Serializable;
import java.util.ArrayList;

import com.ynu.healthyfriendv05.tools.Decimaltool;

import android.database.Cursor;
import android.util.Log;

public class UserSlim implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int userId = 0;
	private int slimId = 0;
	private int task = 0;
	private String date = "";
	private String slimName = "";
	private String slimType = "";
	private String slimIntroduce = "";
	private String slimDescription = "";
	private double slimCalories = 0;

	public UserSlim() {

	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSlimId() {
		return slimId;
	}

	public void setSlimId(int slimId) {
		this.slimId = slimId;
	}

	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSlimName() {
		return slimName;
	}

	public void setSlimName(String slimName) {
		this.slimName = slimName;
	}

	public String getSlimType() {
		return slimType;
	}

	public void setSlimType(String slimType) {
		this.slimType = slimType;
	}

	public String getSlimIntroduce() {
		return slimIntroduce;
	}

	public void setSlimIntroduce(String slimIntroduce) {
		this.slimIntroduce = slimIntroduce;
	}

	public String getSlimDescription() {
		return slimDescription;
	}

	public void setSlimDescription(String slimDescription) {
		this.slimDescription = slimDescription;
	}

	public double getSlimCalories() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(slimCalories));
	}

	public void setSlimCalories(double slimCalories) {
		this.slimCalories = Double.parseDouble(Decimaltool
				.getDoublePrecision(slimCalories));
	}

	public ArrayList<UserSlim> userSlimInfo(Cursor cursor) {
		ArrayList<UserSlim> list = new ArrayList<UserSlim>();
		while (cursor.moveToNext()) {
			UserSlim userSlim = new UserSlim();

			userSlim.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			userSlim.setSlimId(cursor.getInt(cursor.getColumnIndex("slim_id")));
			userSlim.setTask(cursor.getInt(cursor.getColumnIndex("task_flag")));
			userSlim.setDate(cursor.getString(cursor
					.getColumnIndex("choose_date")));

			list.add(userSlim);
		}

		for (int i = 0; i < list.size(); i++) {
			Log.e("所有记录", list.get(i).getUserId() + " "
					+ list.get(i).getSlimId() + " " + list.get(i).getTask()
					+ " " + list.get(i).getDate());
		}

		return list;
	}

	public ArrayList<UserSlim> certainUserSlimAllInfo(Cursor cursor) {
		ArrayList<UserSlim> list = new ArrayList<UserSlim>();
		while (cursor.moveToNext()) {
			UserSlim userSlim = new UserSlim();

			userSlim.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			userSlim.setSlimId(cursor.getInt(cursor.getColumnIndex("slim_id")));
			userSlim.setTask(cursor.getInt(cursor.getColumnIndex("task_flag")));
			userSlim.setDate(cursor.getString(cursor
					.getColumnIndex("choose_date")));
			userSlim.setSlimName(cursor.getString(
					cursor.getColumnIndex("slim_name")).trim());
			userSlim.setSlimType(cursor.getString(
					cursor.getColumnIndex("slim_type")).trim());
			userSlim.setSlimIntroduce(cursor.getString(
					cursor.getColumnIndex("slim_introduce")).trim());
			userSlim.setSlimDescription(cursor.getString(
					cursor.getColumnIndex("slim_description")).trim());
			userSlim.setSlimCalories(cursor.getDouble(cursor
					.getColumnIndex("slim_calories")));

			list.add(userSlim);
		}

		for (int i = 0; i < list.size(); i++) {
			Log.e("特定记录", list.get(i).getUserId() + " "
					+ list.get(i).getSlimId() + " " + list.get(i).getTask()
					+ " " + list.get(i).getDate());
		}

		return list;
	}
}