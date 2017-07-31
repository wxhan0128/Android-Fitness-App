package com.ynu.healthyfriendv05.po;

import java.io.Serializable;
import java.util.ArrayList;

import com.ynu.healthyfriendv05.tools.Decimaltool;

import android.database.Cursor;
import android.util.Log;

public class Recorder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int recorderId = 0;
	private int userId = 0;
	private int stepcount = 0;
	private double distance = 0;
	private int pushupcount = 0;
	private double calories = 0;
	private String date = "";

	public Recorder() {

	}

	public int getRecorderId() {
		return recorderId;
	}

	public void setRecorderId(int recorderId) {
		this.recorderId = recorderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStepcount() {
		return stepcount;
	}

	public void setStepcount(int stepcount) {
		this.stepcount = stepcount;
	}

	public double getDistance() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(distance));
	}

	public void setDistance(double distance) {
		this.distance = Double.parseDouble(Decimaltool
				.getDoublePrecision(distance));
	}

	public int getPushupcount() {
		return pushupcount;
	}

	public void setPushupcount(int pushupcount) {
		this.pushupcount = pushupcount;
	}

	public double getCalories() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(calories));
	}

	public void setCalories(double calories) {
		this.calories = Double.parseDouble(Decimaltool
				.getDoublePrecision(calories));
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Recorder> allRecorderInfo(Cursor cursor) {
		ArrayList<Recorder> list = new ArrayList<Recorder>();
		while (cursor.moveToNext()) {
			Recorder recorder = new Recorder();

			recorder.setRecorderId(cursor.getInt(cursor
					.getColumnIndex("recorder_id")));
			recorder.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			recorder.setStepcount(cursor.getInt(cursor
					.getColumnIndex("step_count")));
			recorder.setDistance(cursor.getDouble(cursor
					.getColumnIndex("distance")));
			recorder.setPushupcount(cursor.getInt(cursor
					.getColumnIndex("pushup_count")));
			recorder.setCalories(cursor.getDouble(cursor
					.getColumnIndex("burn_calories")));
			recorder.setDate(cursor.getString(cursor
					.getColumnIndex("recorder_date")));

			list.add(recorder);
		}

		for (int i = 0; i < list.size(); i++) {
			Log.e("ÅÜ²½Óë¸©ÎÔ³Å¼ÇÂ¼", list.get(i).getRecorderId() + " "
					+ list.get(i).getUserId() + " "
					+ list.get(i).getStepcount() + " "
					+ list.get(i).getDistance() + " "
					+ list.get(i).getPushupcount() + " "
					+ list.get(i).getCalories() + " " + list.get(i).getDate());
		}

		return list;
	}

	public Recorder recorderInfo(Cursor cursor) {
		Recorder recorder = new Recorder();

		while (cursor.moveToNext()) {
			recorder.setRecorderId(cursor.getInt(cursor
					.getColumnIndex("recorder_id")));
			recorder.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
			recorder.setStepcount(cursor.getInt(cursor
					.getColumnIndex("step_count")));
			recorder.setDistance(cursor.getDouble(cursor
					.getColumnIndex("distance")));
			recorder.setPushupcount(cursor.getInt(cursor
					.getColumnIndex("pushup_count")));
			recorder.setCalories(cursor.getDouble(cursor
					.getColumnIndex("burn_calories")));
			recorder.setDate(cursor.getString(cursor
					.getColumnIndex("recorder_date")));
		}
		return recorder;
	}
}
