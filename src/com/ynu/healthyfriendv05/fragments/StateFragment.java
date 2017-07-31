package com.ynu.healthyfriendv05.fragments;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class StateFragment extends Fragment {
	private EditText des_weight_input;
	private EditText day_input;
	private TextView current_weight;
	private TextView bmi_value;
	private TextView health_describe;
	private ImageView level;
	private ImageView nextstep_bn;
	private ImageView backstep_bn;
	private ArrayList<EditText> edittext_list;

	private double des_weight = 0;
	private int day = 0;
	private double bmi = 0;
	private User user;

	private boolean complete = false;
	private OnDetailsListener mListener;

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
		View view = inflater.inflate(R.layout.statefragment, container, false);
		level = (ImageView) view.findViewById(R.id.level_pic);
		des_weight_input = (EditText) view.findViewById(R.id.des_weight_input);
		day_input = (EditText) view.findViewById(R.id.time_input);
		current_weight = (TextView) view
				.findViewById(R.id.current_weight_index);
		bmi_value = (TextView) view.findViewById(R.id.bmi_index);
		health_describe = (TextView) view.findViewById(R.id.state_explain);
		nextstep_bn = (ImageView) view.findViewById(R.id.nextstep);
		backstep_bn = (ImageView) view.findViewById(R.id.backtoinfrom);

		edittext_list = new ArrayList<EditText>();
		edittext_list.add(des_weight_input);
		edittext_list.add(day_input);

		Bundle base_data = getArguments();// ��ô�activity�д��ݹ�����ֵ
		user = (User) base_data.getSerializable("base_inform");
		current_weight.setText(String.valueOf(user.getWeight()) + " kg");

		bmi = Double.parseDouble(Decimaltool.getDoublePrecision(user
				.getWeight() / Math.pow(user.getHeight() / 100, 2)));
		bmi_value.setText(String.valueOf(bmi));

		if (bmi >= 18.5 && bmi < 24.99) {
			level.setImageResource(R.drawable.good);
			health_describe.setText("����bmiָ��Ϊ��"
					+ bmi
					+ "\n\n����ǰ�Ľ���״�����ã���ؼ�������Σ����Ϊƽ��ˮƽ����������֡�"
					+ "\n\nΪ���Ƽ������׼�Ľ��������ǣ�"
					+ Decimaltool.getDoublePrecision(22 * Math.pow(
							user.getHeight() / 100, 2)) + "kg");
		} else if (bmi < 18.5) {
			level.setImageResource(R.drawable.common);
			health_describe.setText("����bmiָ��Ϊ��"
					+ bmi
					+ "\n\n�����������ڱ���ƫ�����ͣ���ǰ�Ľ���״��һ�㣬��ؼ�������Σ������Ȼ�ϵ�"
					+ "������������Σ�������ӣ���Ҫע�⡣"
					+ "\n\nΪ���Ƽ��Ľ������������ǣ�"
					+ Decimaltool.getDoublePrecision(18.5 * Math.pow(
							user.getHeight() / 100, 2))
					+ "kg~"
					+ Decimaltool.getDoublePrecision(24.99 * Math.pow(
							user.getHeight() / 100, 2)) + "kg");
		} else if (bmi >= 25 && bmi < 28) {
			level.setImageResource(R.drawable.bad);
			health_describe.setText("����bmiָ��Ϊ��"
					+ bmi
					+ "\n\n������������ƫ�����ͣ���ǰ�Ľ���״�����Ǻ����룬��ؼ�������Σ���Կ�ʼ���ӣ���Ҫ���ע�⡣"
					+ "\n\nΪ���Ƽ��Ľ������������ǣ�"
					+ Decimaltool.getDoublePrecision(18.5 * Math.pow(
							user.getHeight() / 100, 2))
					+ "kg~"
					+ Decimaltool.getDoublePrecision(24.99 * Math.pow(
							user.getHeight() / 100, 2)) + "kg");
		} else {
			level.setImageResource(R.drawable.verybad);
			health_describe.setText("����bmiָ��Ϊ��"
					+ bmi
					+ "\n\n�����������ڷ������ϵ����ͣ���ǰ�Ľ���״���ܲ����룬��ؼ�������Σ�����������ӣ���Ҫ���ע�⡣"
					+ "\n\nΪ���Ƽ��Ľ������������ǣ�"
					+ Decimaltool.getDoublePrecision(18.5 * Math.pow(
							user.getHeight() / 100, 2))
					+ "kg~"
					+ Decimaltool.getDoublePrecision(24.99 * Math.pow(
							user.getHeight() / 100, 2)) + "kg");
		}

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
					des_weight = Double.parseDouble(des_weight_input.getText()
							.toString());
					day = Integer.parseInt(day_input.getText().toString());

					user.setDes_weight(des_weight);
					user.setPlanday(day);

					Bundle state_data = new Bundle();
					state_data.putSerializable("state_inform",
							(Serializable) user);
					mListener.onStateDetailSubmit(state_data);
				} else {
					Log.e("��Ϣ", "��δ�����Ϣ");
				}
			}
		});

		backstep_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				des_weight = 0;
				day = 0;
				bmi = 0;
				mListener.onBackFragment("backtoinform");
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
			message = "Ŀ������";
			break;
		case 1:
			message = "�ƻ�����";
			break;
		default:
			break;
		}

		Toast.makeText(getActivity(), "������" + message, Toast.LENGTH_SHORT)
				.show();
	}
}