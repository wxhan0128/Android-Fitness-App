package com.ynu.healthyfriendv05.service;

import java.util.ArrayList;

import android.content.Context;

import com.ynu.healthyfriendv05.dao.FoodDAO;
import com.ynu.healthyfriendv05.po.Food;

public class FoodService {
	private FoodDAO foodDAO;

	public FoodService(Context context) {
		this.foodDAO = new FoodDAO(context);
	}

	public ArrayList<Food> findAllFood() {
		return foodDAO.selectAll();
	}
}
