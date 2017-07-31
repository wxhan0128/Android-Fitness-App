package com.ynu.healthyfriendv05.service;

import java.util.ArrayList;

import android.content.Context;

import com.ynu.healthyfriendv05.dao.DiaryDAO;
import com.ynu.healthyfriendv05.po.Diary;

public class DiaryService {
	private DiaryDAO diaryDAO;

	public DiaryService(Context context) {
		this.diaryDAO = new DiaryDAO(context);
	}

	public ArrayList<Diary> findAllDiary() {
		return diaryDAO.selectAll();
	}

	public boolean isExist(String date) {
		return diaryDAO.selectByDate(date);
	}

	public Diary obtainDiaryByDate(String date) {
		return diaryDAO.selectDiaryByDate(date);
	}

	public void writeDiary(Diary diary) {
		diaryDAO.add(diary);
	}

	public void editDiary(Diary diary) {
		diaryDAO.modifyByDate(diary);
	}

	public void deleteDiary(Diary diary) {
		diaryDAO.deleteItem(diary);
	}
}
