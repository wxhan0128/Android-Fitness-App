package com.ynu.healthyfriendv05.fragments;

import java.io.Serializable;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.Diary;
import com.ynu.healthyfriendv05.service.DiaryService;
import com.ynu.healthyfriendv05.tools.Datetool;

public class HealthyDiaryFragment extends Fragment {
	private Context context;
	private EditText title_input;
	private EditText content_input;
	private ImageView save_bn;
	private ImageView cancel_bn;

	private DiaryService diaryService;
	private Diary diary;
	private String title = "";
	private String content = "";
	private String date = "";
	private OnAssistantListener mListener;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnAssistantListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnAssistantListener");
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.healthydiaryfragment, container,
				false);
		this.context = getActivity();
		title_input = (EditText) view.findViewById(R.id.title_input);
		content_input = (EditText) view.findViewById(R.id.content_input);
		save_bn = (ImageView) view.findViewById(R.id.save);
		cancel_bn = (ImageView) view.findViewById(R.id.diary_cancel);

		save_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				diary = new Diary();
				title = title_input.getText().toString().trim();
				content = content_input.getText().toString().trim();

				diary.setTitle(title);
				diary.setContent(content);
				diary.setCreateDate(date);

				Bundle diary_data = new Bundle();
				diary_data.putSerializable("diary_info", (Serializable) diary);
				mListener.onDiarySave(diary_data);
			}
		});

		cancel_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onFragmentBack();
			}
		});

		refreshComponent();
		return view;
	}

	private void refreshComponent() {
		this.diaryService = new DiaryService(context);

		Bundle diary_bundle = getArguments();
		if (diary_bundle.getBoolean("edit_flag")) {
			this.diary = (Diary) diary_bundle.getSerializable("my_diary_data");
			Log.e("我的日记", diary.getTitle());
			title_input.setText(diary.getTitle());
			content_input.setText(diary.getContent());
			date = diary.getCreateDate();
		} else {
			if (diaryService.isExist(Datetool.getCurrentTime())) {
				this.diary = diaryService.obtainDiaryByDate(Datetool
						.getCurrentTime());
				title_input.setText(diary.getTitle());
				content_input.setText(diary.getContent());
				date = diary.getCreateDate();
			} else {
				title_input.setText("");
				content_input.setText("");
				date = Datetool.getCurrentTime();
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshComponent();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}