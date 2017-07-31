package com.ynu.healthyfriendv05.service;

import java.util.ArrayList;

import android.content.Context;

import com.ynu.healthyfriendv05.dao.UserSlimDAO;
import com.ynu.healthyfriendv05.po.UserSlim;

public class UserSlimService {
	private UserSlimDAO userSlimDAO;

	public UserSlimService(Context context) {
		this.userSlimDAO = new UserSlimDAO(context);
	}

	public boolean isExist(UserSlim userSlim) {
		return userSlimDAO.selectByIdAndDate(userSlim);
	}

	public boolean isTask(UserSlim userSlim) {
		return userSlimDAO.selectByTask(userSlim);
	}

	public void choose(UserSlim userSlim) {
		userSlimDAO.add(userSlim);
	}

	public ArrayList<UserSlim> findAllUserSlim() {
		return userSlimDAO.selectAll();
	}

	public ArrayList<UserSlim> findFoodByDate(String date) {
		return userSlimDAO.selectDietByDate(date);
	}

	public ArrayList<UserSlim> findSportsTask() {
		return userSlimDAO.selectSportsTask();
	}

	public ArrayList<UserSlim> findSportsByDate(String date) {
		return userSlimDAO.selectSportsByDate(date);
	}

	public ArrayList<UserSlim> findSportsByDateOrTask(String date) {
		return userSlimDAO.selectSportsByDateOrTask(date);
	}

	public void deleteByItem(UserSlim userSlim) {
		userSlimDAO.deleteItem(userSlim);
	}

	public double calculateTodayCalories(String type, String date) {
		return userSlimDAO.sumCaloriesByDate(type, date);
	}

	public double calculateCaloriesByMonth(String type, String month) {
		return userSlimDAO.sumCaloriesByMonth(type, month);
	}

	public double calculateTaskCalories() {
		return userSlimDAO.sumTaskCalories();
	}
}