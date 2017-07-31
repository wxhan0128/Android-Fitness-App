package com.ynu.healthyfriendv05.service;

import java.util.ArrayList;

import android.content.Context;

import com.ynu.healthyfriendv05.dao.RecorderDAO;
import com.ynu.healthyfriendv05.po.Recorder;

public class RecorderService {
	private RecorderDAO recorderDAO;

	public RecorderService(Context context) {
		this.recorderDAO = new RecorderDAO(context);
	}

	public void insert(Recorder recorder) {
		recorderDAO.add(recorder);
	}

	public ArrayList<Recorder> findAllRecorders() {
		return recorderDAO.selectAll();
	}

	public Recorder findRecorderByDate(String date) {
		return recorderDAO.selectByDate(date);
	}

	public boolean isExist(String date) {
		return recorderDAO.checkByDate(date);
	}

	public void changeRecorderByDate(Recorder recorder) {
		recorderDAO.modifyByDate(recorder);
	}

	public Recorder findBestPushupGrade() {
		return recorderDAO.selectMaxPushup();
	}

	public Recorder findBestRunGrade() {
		return recorderDAO.selectMaxStep();
	}

	public Object[] findTotalGrade() {
		return recorderDAO.sumValues();
	}

	public int[] findTotalGradeByMonth(String month) {
		return recorderDAO.sumValuesByMonth(month);
	}

	public double findTotalCaloriesByDate(String date) {
		return recorderDAO.sumCaloriesByDate(date);
	}

	public double findTotalCaloriesByMonth(String month) {
		return recorderDAO.sumCaloriesByMonth(month);
	}

	public void closeDB() {
		recorderDAO.closeDBHelper();
	}
}
