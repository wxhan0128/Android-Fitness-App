package com.ynu.healthyfriendv05.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.User;

public class ChangeInformationFragment extends Fragment {
	private EditText name_input;
	private EditText birthday_input;
	private RadioGroup gender_input;
	private EditText weight_input;
	private EditText height_input;
	private EditText armlength_input;
	private EditText steplength_input;
	private Spinner type;
	private EditText desweight_input;
	private EditText plandays_input;
	private ImageView save_bn;
	private ImageView cancel_bn;
	private ImageView background;
	private ArrayList<EditText> edittext_list;

	private ArrayAdapter<?> adapter;
	private OnSettingListener mListener;
	private boolean complete = false;

	private User user;
	private int user_id = 0;
	private String user_name = "";
	private String user_birthday = "";
	private String user_gender = "男";
	private double user_weight = 0;
	private double user_height = 0;
	private double armlength = 0;
	private double steplength = 0;
	private String exercise_type = "";
	private double des_weight = 0;
	private int plan_days = 0;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnSettingListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSettingListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.setting_informationfragment,
				container, false);
		name_input = (EditText) view.findViewById(R.id.change_username_input);
		birthday_input = (EditText) view
				.findViewById(R.id.change_birthday_input);
		gender_input = (RadioGroup) view.findViewById(R.id.change_gender_rg);
		weight_input = (EditText) view.findViewById(R.id.change_weight_input);
		height_input = (EditText) view.findViewById(R.id.change_height_input);
		armlength_input = (EditText) view
				.findViewById(R.id.change_armlength_input);
		steplength_input = (EditText) view
				.findViewById(R.id.change_steplength_input);
		type = (Spinner) view.findViewById(R.id.change_type_choose);
		desweight_input = (EditText) view
				.findViewById(R.id.change_desweight_input);
		plandays_input = (EditText) view.findViewById(R.id.change_timeinput);
		save_bn = (ImageView) view.findViewById(R.id.save_change);
		cancel_bn = (ImageView) view.findViewById(R.id.cancel_change);
		background = (ImageView) view.findViewById(R.id.setting_update_bg);
		background.getBackground().setAlpha(130);

		edittext_list = new ArrayList<EditText>();
		edittext_list.add(name_input);
		edittext_list.add(birthday_input);
		edittext_list.add(weight_input);
		edittext_list.add(height_input);
		edittext_list.add(armlength_input);
		edittext_list.add(steplength_input);
		edittext_list.add(desweight_input);
		edittext_list.add(plandays_input);

		Bundle my_data = getArguments();
		this.user = (User) my_data.getSerializable("user_inform");
		initComponent();

		// 第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
		adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.exercise_type_list,
				android.R.layout.simple_spinner_item);

		// 第三步：为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 第四步：将适配器添加到下拉列表上
		type.setAdapter(adapter);

		birthday_input.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Calendar c = Calendar.getInstance();
					new DatePickerDialog(getActivity(),
							new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									// TODO Auto-generated method stub
									birthday_input.setText(year + "-"
											+ (monthOfYear + 1) + "-"
											+ dayOfMonth);

								}
							}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
									.get(Calendar.DAY_OF_MONTH)).show();
					String str = birthday_input.getText().toString();
					System.out.println(str);

					Log.e("日期", str);

					return true;
				}

				return false;
			}
		});

		gender_input.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				user_gender = checkedId == R.id.change_male ? "男" : "女";
				Log.e("性别", user_gender);
			}
		});

		save_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initList();

				for (int i = 0; i < edittext_list.size(); i++) {
					if (edittext_list.get(i).getText().toString().equals("")) {
						complete = false;
						edittext_list.get(i).setBackgroundResource(
								R.drawable.warning_edittext_border);
						checkInput(i);
						break;
					} else {
						complete = true;
						edittext_list.get(i).setBackgroundResource(
								R.drawable.edittext_border);
					}
				}

				if (complete) {
					user_id = user.getId();
					user_name = name_input.getText().toString().trim();
					user_birthday = birthday_input.getText().toString().trim();
					user_weight = Double.parseDouble(weight_input.getText()
							.toString());
					user_height = Double.parseDouble(height_input.getText()
							.toString());
					armlength = Double.parseDouble(armlength_input.getText()
							.toString());
					steplength = Double.parseDouble(steplength_input.getText()
							.toString());
					exercise_type = type.getSelectedItem().toString();
					des_weight = Double.parseDouble(desweight_input.getText()
							.toString());
					plan_days = Integer.parseInt(plandays_input.getText()
							.toString());

					user = new User(user_name, user_birthday, user_gender,
							user_weight, user_height, armlength, steplength,
							exercise_type);
					user.setId(user_id);
					user.setDes_weight(des_weight);
					user.setPlanday(plan_days);

					Bundle new_data = new Bundle();
					new_data.putSerializable("new_inform", (Serializable) user);
					mListener.onSave(new_data);
				} else {

				}
			}
		});

		cancel_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onFragmentBack();
			}
		});

		return view;
	}

	private void initList() {
		for (int i = 0; i < edittext_list.size(); i++) {
			edittext_list.get(i).setBackgroundResource(
					R.drawable.edittext_border);
		}
	}

	private void checkInput(int i) {
		String message = "";
		switch (i) {
		case 0:
			message = "姓名";
			break;
		case 1:
			message = "生日";
			break;
		case 2:
			message = "当前体重";
			break;
		case 3:
			message = "身高";
			break;
		case 4:
			message = "臂长";
			break;
		case 5:
			message = "步长";
			break;
		case 6:
			message = "目标体重";
			break;
		case 7:
			message = "计划天数";
			break;
		default:
			break;
		}

		Toast.makeText(getActivity(), message + "不能为空", Toast.LENGTH_SHORT)
				.show();
	}

	private void initComponent() {
		// TODO Auto-generated method stub
		name_input.setText(user.getName().toString().trim());
		birthday_input.setText(user.getBirthday().toString().trim());
		weight_input.setText(String.valueOf(user.getWeight()));
		height_input.setText(String.valueOf(user.getHeight()));
		armlength_input.setText(String.valueOf(user.getArmlength()));
		steplength_input.setText(String.valueOf(user.getSteplength()));
		desweight_input.setText(String.valueOf(user.getDes_weight()));
		plandays_input.setText(String.valueOf(user.getPlanday()));
	}
}