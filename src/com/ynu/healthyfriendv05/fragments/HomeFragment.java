package com.ynu.healthyfriendv05.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.UserSlim;
import com.ynu.healthyfriendv05.service.UserSlimService;
import com.ynu.healthyfriendv05.tools.Datetool;

public class HomeFragment extends Fragment {
	private Context context;
	private ListView fix_tasklist;
	private ListView new_tasklist;
	private ImageView searchdata_bn;
	private ImageView cancel_bn;

	private UserSlimService userslimService;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.homefragment, container, false);
		this.context = getActivity();
		fix_tasklist = (ListView) view
				.findViewById(R.id.fix_sportstask_listview);
		new_tasklist = (ListView) view
				.findViewById(R.id.new_sportstask_listview);
		searchdata_bn = (ImageView) view.findViewById(R.id.searchdata);
		cancel_bn = (ImageView) view.findViewById(R.id.home_cancel);
		fix_tasklist.setEnabled(false);
		new_tasklist.setEnabled(false);

		searchdata_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onSearchData();
			}
		});

		cancel_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onFragmentBack();
			}
		});

		refreshList();
		return view;
	}

	private void refreshList() {
		// TODO Auto-generated method stub
		userslimService = new UserSlimService(context);
		inflateList(convertCursorToList(userslimService.findSportsTask()),
				fix_tasklist);
		inflateList(
				convertCursorToList(userslimService.findSportsByDate(Datetool
						.getCurrentTime())), new_tasklist);
	}

	private ArrayList<Map<String, Object>> convertCursorToList(
			ArrayList<UserSlim> userslimlist) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < userslimlist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("task_sport_name", userslimlist.get(i).getSlimName().trim());
			map.put("task_sport_calories", userslimlist.get(i)
					.getSlimCalories() + " ´ó¿¨");
			result.add(map);
		}

		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList,
			ListView listview) {
		SimpleAdapter adapter = new SimpleAdapter(context, arrayList,
				R.layout.mysportstasklist_item, new String[] {
						"task_sport_name", "task_sport_calories" }, new int[] {
						R.id.task_sport_name, R.id.task_sport_calories }); // ¢Û
		listview.setAdapter(adapter);
	}
}