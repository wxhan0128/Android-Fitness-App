package com.ynu.healthyfriendv05.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.Recorder;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.service.RunFragmentService;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class RunFragment extends Fragment implements OnClickListener {
	private Context context;
	private ImageView background;
	private ImageView start_bn;
	private ImageView pause_bn;
	private ImageView stop_bn;
	private TextView run_count;
	private TextView run_calories;
	private TextView distance_count;
	private TextView time_count;
	private TextView goal_rest;
	private RelativeLayout run_panel;

	private User user;
	private Recorder recorder;

	private int count = 0;
	private int rest = 0;
	private double distance = 0;
	private String timer;
	private double calories = 0;
	private boolean time_work = false;
	private OnExerciseListener mListener;

	private IntentFilter stepFilter = new IntentFilter(
			"com.healthyfriend.service.stepReceiver");
	private StepReceiver stepReceiver = new StepReceiver();
	private Intent host_intent;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnExerciseListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnExerciseListener");
		}
	}

	@SuppressLint("InlinedApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.runfragment, container, false);
		this.context = getActivity();
		background = (ImageView) view.findViewById(R.id.run_bg);
		run_count = (TextView) view.findViewById(R.id.run_num);
		distance_count = (TextView) view.findViewById(R.id.distance_values);
		run_calories = (TextView) view.findViewById(R.id.run_calorie_burn);
		time_count = (TextView) view.findViewById(R.id.run_time_count);
		goal_rest = (TextView) view.findViewById(R.id.run_goal_value);
		start_bn = (ImageView) view.findViewById(R.id.run_start);
		pause_bn = (ImageView) view.findViewById(R.id.run_pause);
		stop_bn = (ImageView) view.findViewById(R.id.run_exit);
		run_panel = (RelativeLayout) view.findViewById(R.id.run_panel);
		pause_bn.setEnabled(false);
		time_count.setText("00:00");

		background.getBackground().setAlpha(100);
		run_panel.getBackground().setAlpha(170);
		run_count.setText(String.valueOf(count));
		run_calories.setText(String.valueOf(calories) + " 大卡");

		Bundle run_data = getArguments();
		this.user = (User) run_data.getSerializable("user_inform");
		distance_count.setText(distance + " m");
		this.rest = run_data.getInt("goal_num");
		if ((rest - count) > 0)
			goal_rest.setText(String.valueOf(rest - count));
		else
			goal_rest.setText(String.valueOf(0));

		host_intent = new Intent(getActivity(), RunFragmentService.class);
		getActivity().startService(host_intent);

		start_bn.setOnClickListener(this);
		pause_bn.setOnClickListener(this);
		stop_bn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getActivity().registerReceiver(stepReceiver, stepFilter);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("fragment消息", "暂停");
		try {
			getActivity().unregisterReceiver(stepReceiver);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		connectService(2);
		Log.e("fragment消息", "销毁");
		try {
			getActivity().unregisterReceiver(stepReceiver);
		} catch (IllegalArgumentException e) {
		}
		mListener.onServiceDestroy(host_intent);
	}

	public class StepReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();// 获得 Bundle
			timer = bundle.getString("time_count");
			count = bundle.getInt("step_num");
			time_work = bundle.getBoolean("working");
			distance = Double.parseDouble(Decimaltool.getDoublePrecision(count
					* user.getSteplength()));
			calories = Double.parseDouble(Decimaltool
					.getDoublePrecision(0.04 * count));

			time_count.setText(timer);
			if (count == Integer.parseInt(run_count.getText().toString()) + 1) {
				if ((rest - count) >= 0) {
					goal_rest.setText(String.valueOf(rest - count));
					if ((rest - count) < 5 && (rest - count) > 0) {
						Toast.makeText(getActivity(),
								"距目标还剩" + (rest - count) + "步",
								Toast.LENGTH_SHORT).show();
					} else if ((rest - count) == 0)
						Toast.makeText(getActivity(), "目标达成",
								Toast.LENGTH_SHORT).show();
				}
			}
			run_count.setText(String.valueOf(count));
			distance_count.setText(distance + " m");
			run_calories.setText(String.valueOf(calories) + " 大卡");

			if (time_work) {
				start_bn.setEnabled(false);
				pause_bn.setEnabled(true);
			} else {
				pause_bn.setEnabled(false);
				start_bn.setEnabled(true);
			}
		}
	}

	private void simpleAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setIcon(R.drawable.warning);
		builder.setMessage("您走了" + count + "步，确定要离开吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				connectService(3);
				try {
					getActivity().unregisterReceiver(stepReceiver);
				} catch (IllegalArgumentException e) {
				}
				getActivity().stopService(host_intent);

				recorder = new Recorder();
				recorder.setStepcount(count);
				recorder.setDistance(distance);
				recorder.setCalories(calories);
				Log.e("距离", "" + distance);

				Bundle run_data = new Bundle();
				run_data.putSerializable("run", recorder);
				mListener.onFragmentExit(run_data, "run");
				count = 0;
				rest = 0;
				distance = 0;
				calories = 0;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (!start_bn.isEnabled()) {
					connectService(1);
				} else {

				}
			}
		});

		builder.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.run_start:
			connectService(1);
			mListener.onButtonSpeak("开始跑步！");
			start_bn.setEnabled(false);
			pause_bn.setEnabled(true);
			break;
		case R.id.run_pause:
			connectService(2);
			start_bn.setEnabled(true);
			pause_bn.setEnabled(false);
			break;
		case R.id.run_exit:
			connectService(2);
			start_bn.setEnabled(true);
			pause_bn.setEnabled(false);
			simpleAlert();
			break;
		}
	}

	private void connectService(int value) {
		Intent send_intent = new Intent();
		send_intent.setAction("com.healthyfriend.service.runControlReceiver");
		send_intent.putExtra("control", value);
		getActivity().sendBroadcast(send_intent);
	}
}