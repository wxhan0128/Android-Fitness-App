package com.ynu.healthyfriendv05.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class Datetool {
	public static String getCurrentTime() {
		String time = null;
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		time = dateFormat.format(today);
		return time;
	}

	public static String getCurrentYear() {
		String time = null;
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		time = dateFormat.format(today);
		return time;
	}

	public static String getCurrentMonth() {
		String time = null;
		Date today = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		time = dateFormat.format(today);
		return time;
	}

	public static Date getDateYear(String date) {
		Date time = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		try {
			time = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
}