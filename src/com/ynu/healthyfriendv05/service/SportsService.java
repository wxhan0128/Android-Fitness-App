package com.ynu.healthyfriendv05.service;

import java.util.ArrayList;

import android.content.Context;

import com.ynu.healthyfriendv05.dao.SportsDAO;
import com.ynu.healthyfriendv05.po.Sports;

public class SportsService {
	private SportsDAO sportDAO;

	public SportsService(Context context) {
		this.sportDAO = new SportsDAO(context);
	}

	public ArrayList<Sports> findAllSports() {
		return sportDAO.selectAll();
	}

	public ArrayList<Sports> findAPackage() {
		return sportDAO.selectPackage("A");
	}

	public ArrayList<Sports> findBPackage() {
		return sportDAO.selectPackage("B");
	}

	public ArrayList<Sports> findCPackage() {
		return sportDAO.selectPackage("C");
	}

	public ArrayList<Sports> findDPackage() {
		return sportDAO.selectPackage("D");
	}
}
