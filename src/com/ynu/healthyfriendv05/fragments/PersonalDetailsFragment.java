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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.User;

public class PersonalDetailsFragment extends Fragment {
	private EditText name_input;
	private EditText birthday_input;
	private RadioGroup gender_input;
	private EditText weight_input;
	private EditText height_input;
	private EditText armlength_input;
	private EditText steplength_input;
	private Spinner type;
	private ImageView nextstep_bn;
	private ArrayList<EditText> edittext_list;

	private ArrayAdapter<?> adapter;
	private OnDetailsListener mListener;
	private boolean complete = false;

	private User user;
	private String user_name = "";
	private String user_birthday = "";
	private String user_gender = "男";
	private double user_weight = 0;
	private double user_height = 0;
	private double armlength = 0;
	private double steplength = 0;
	private String exercise_type = "";

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
		View view = inflater.inflate(R.layout.personaldetailsfragment,
				container, false);

		name_input = (EditText) view.findViewById(R.id.username_input);
		birthday_input = (EditText) view.findViewById(R.id.birthday_input);
		gender_input = (RadioGroup) view.findViewById(R.id.gender_rg);
		weight_input = (EditText) view.findViewById(R.id.weight_input);
		height_input = (EditText) view.findViewById(R.id.height_input);
		armlength_input = (EditText) view.findViewById(R.id.armlength_input);
		steplength_input = (EditText) view.findViewById(R.id.steplength_input);
		type = (Spinner) view.findViewById(R.id.type_choose);
		nextstep_bn = (ImageView) view.findViewById(R.id.nextstep);

		edittext_list = new ArrayList<EditText>();
		edittext_list.add(name_input);
		edittext_list.add(birthday_input);
		edittext_list.add(weight_input);
		edittext_list.add(height_input);
		edittext_list.add(armlength_input);
		edittext_list.add(steplength_input);

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
				user_gender = checkedId == R.id.male ? "男" : "女";
			}
		});

		nextstep_bn.setOnClickListener(new OnClickListener() {

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

					user = new User(user_name, user_birthday, user_gender,
							user_weight, user_height, armlength, steplength,
							exercise_type);

					Bundle base_data = new Bundle();
					base_data.putSerializable("base_inform",
							(Serializable) user);
					mListener.onBaseDetailSubmit(base_data);
				} else {
					Log.e("消息", "有未填的信息");
				}
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
		default:
			break;
		}

		Toast.makeText(getActivity(), "请输入" + message, Toast.LENGTH_SHORT)
				.show();
	}
}