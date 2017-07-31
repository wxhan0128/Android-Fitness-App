package com.ynu.healthyfriendv05.fragments;

import java.io.Serializable;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.Sports;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.service.SportsService;

public class PlanFragment extends Fragment {
	private Context context;
	private ExpandableListView packages;
	private Spinner package_spinner;
	private ImageView finish_bn;
	private ImageView back_bn;

	private ExpandableListAdapter adapter;
	private ArrayAdapter<?> array_adapter;
	private OnDetailsListener mListener;

	private User user;
	public SportsService sportService;
	private ArrayList<Sports> sport_list_A = new ArrayList<Sports>();
	private ArrayList<Sports> sport_list_B = new ArrayList<Sports>();
	private ArrayList<Sports> sport_list_C = new ArrayList<Sports>();
	private ArrayList<Sports> sport_list_D = new ArrayList<Sports>();
	private ArrayList<ArrayList<Sports>> all_list;
	private Boolean finish_flag = false;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mListener = (OnDetailsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDetailsListener");
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.planfragment, container, false);
		this.context = getActivity();
		packages = (ExpandableListView) view.findViewById(R.id.all_packages);
		package_spinner = (Spinner) view.findViewById(R.id.package_values);
		finish_bn = (ImageView) view.findViewById(R.id.finished);
		back_bn = (ImageView) view.findViewById(R.id.backtostate);

		Bundle state_data = getArguments();// 获得从activity中传递过来的值
		this.user = (User) state_data.getSerializable("state_inform");

		Log.e("tag",
				"" + user.getName() + " " + user.getBirthday() + " "
						+ user.getDes_weight() + " " + user.getGender() + " "
						+ user.getPlanday() + " " + user.getArmlength() + " "
						+ user.getSteplength() + " " + user.getExercise_type());

		packages.setVerticalScrollBarEnabled(false);
		packages.setCacheColorHint(Color.TRANSPARENT);
		packages.setGroupIndicator(null);
		ColorDrawable drawable_tranparent = new ColorDrawable(Color.TRANSPARENT);
		packages.setSelector(drawable_tranparent);

		sportService = new SportsService(context);
		sport_list_A = sportService.findAPackage();
		sport_list_B = sportService.findBPackage();
		sport_list_C = sportService.findCPackage();
		sport_list_D = sportService.findDPackage();
		adapter = new ExpandInfoAdapter();
		packages.setAdapter(adapter);

		array_adapter = ArrayAdapter
				.createFromResource(getActivity(), R.array.package_type_list,
						android.R.layout.simple_spinner_item);
		array_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		package_spinner.setAdapter(array_adapter);

		finish_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish_flag = true;
				ArrayList<Sports> temp_list = new ArrayList<Sports>();
				if (package_spinner.getSelectedItem().toString()
						.equals("健身套餐A")) {
					temp_list = sport_list_A;
				} else if (package_spinner.getSelectedItem().toString()
						.equals("健身套餐B")) {
					temp_list = sport_list_B;
				} else if (package_spinner.getSelectedItem().toString()
						.equals("健身套餐C")) {
					temp_list = sport_list_C;
				} else if (package_spinner.getSelectedItem().toString()
						.equals("健身套餐D")) {
					temp_list = sport_list_D;
				}

				Bundle all_data = new Bundle();
				all_data.putSerializable("all_inform", (Serializable) user);

				mListener.onFinished(all_data, finish_flag, temp_list);
			}
		});

		back_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onBackFragment("backtostate");
			}
		});

		packages.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					if (groupPosition != i) {
						packages.collapseGroup(i);
					}
				}
			}
		});

		return view;
	}

	public class ExpandInfoAdapter extends BaseExpandableListAdapter {
		private String[] packageTypes;

		public ExpandInfoAdapter() {
			// TODO Auto-generated constructor stub
			packageTypes = new String[] { "健身套餐A", "健身套餐B", "健身套餐C", "健身套餐D" };
			all_list = new ArrayList<ArrayList<Sports>>();
			all_list.add(sport_list_A);
			all_list.add(sport_list_B);
			all_list.add(sport_list_C);
			all_list.add(sport_list_D);
		}

		// 获取指定组位置、指定子列表项处的子列表项数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return all_list.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return all_list.get(groupPosition).size();
		}

		// 该方法决定每个子选项的外观
		@SuppressLint("InflateParams")
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView_name;
			TextView textView_calories;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.packagelist_child, null);
			}
			textView_name = (TextView) convertView
					.findViewById(R.id.package_child_name);
			textView_calories = (TextView) convertView
					.findViewById(R.id.package_child_calories_value);
			textView_name.setText(((Sports) getChild(groupPosition,
					childPosition)).getName().toString());
			textView_calories.setText(((Sports) getChild(groupPosition,
					childPosition)).getCalories() + " 大卡");

			return convertView;
		}

		// 获取指定组位置处的组数据
		@Override
		public Object getGroup(int groupPosition) {
			return packageTypes[groupPosition];
		}

		@Override
		public int getGroupCount() {
			return packageTypes.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 该方法决定每个组选项的外观
		@SuppressLint("InflateParams")
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView;
			if (null == convertView) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.packagelist_group, null);
			}
			textView = (TextView) convertView.findViewById(R.id.package_name);
			textView.setText(getGroup(groupPosition).toString());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}