package com.ynu.healthyfriendv05.po;

import java.io.Serializable;

import com.ynu.healthyfriendv05.tools.Decimaltool;

import android.database.Cursor;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id = 0;
	private String name = "";
	private String birthday = "";
	private String gender = "";
	private double weight = 0;
	private double height = 0;
	private double armlength = 0;
	private double steplength = 0;
	private String exercise_type = "";
	private double des_weight = 0;
	private int planday = 0;

	public User() {

	}

	public User(String name, String birthday, String gender, double weight,
			double height, double armlength, double steplength, String type) {
		this.name = name;
		this.birthday = birthday;
		this.gender = gender;
		this.weight = weight;
		this.height = height;
		this.armlength = armlength;
		this.steplength = steplength;
		this.exercise_type = type;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getWeight() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(weight));
	}

	public void setWeight(double weight) {
		this.weight = Double
				.parseDouble(Decimaltool.getDoublePrecision(weight));

	}

	public double getHeight() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(height));
	}

	public void setHeight(double height) {
		this.height = Double
				.parseDouble(Decimaltool.getDoublePrecision(height));
	}

	public double getArmlength() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(armlength));
	}

	public void setArmlength(double armlength) {
		this.armlength = Double.parseDouble(Decimaltool
				.getDoublePrecision(armlength));
	}

	public double getSteplength() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(steplength));
	}

	public void setSteplength(double steplength) {
		this.steplength = Double.parseDouble(Decimaltool
				.getDoublePrecision(steplength));
	}

	public String getExercise_type() {
		return exercise_type;
	}

	public void setExercise_type(String exercise_type) {
		this.exercise_type = exercise_type;
	}

	public double getDes_weight() {
		return Double.parseDouble(Decimaltool.getDoublePrecision(des_weight));
	}

	public void setDes_weight(double des_weight) {
		this.des_weight = Double.parseDouble(Decimaltool
				.getDoublePrecision(des_weight));
	}

	public int getPlanday() {
		return planday;
	}

	public void setPlanday(int planday) {
		this.planday = planday;
	}

	public User userInfo(Cursor cursor) {
		User user = new User();

		while (cursor.moveToNext()) {
			user.setId(cursor.getInt(cursor.getColumnIndex("user_id")));
			user.setName(cursor.getString(cursor.getColumnIndex("user_name"))
					.trim());
			user.setBirthday(cursor.getString(
					cursor.getColumnIndex("user_birthday")).trim());
			user.setGender(cursor.getString(
					cursor.getColumnIndex("user_gender")).trim());
			user.setWeight(cursor.getDouble(cursor
					.getColumnIndex("user_weight")));
			user.setHeight(cursor.getDouble(cursor
					.getColumnIndex("user_height")));
			user.setArmlength(cursor.getDouble(cursor
					.getColumnIndex("arm_length")));
			user.setSteplength(cursor.getDouble(cursor
					.getColumnIndex("step_length")));
			user.setExercise_type(cursor.getString(
					cursor.getColumnIndex("exercise_type")).trim());
			user.setDes_weight(cursor.getDouble(cursor
					.getColumnIndex("user_desweight")));
			user.setPlanday(cursor.getInt(cursor.getColumnIndex("days")));
		}

		return user;
	}
}