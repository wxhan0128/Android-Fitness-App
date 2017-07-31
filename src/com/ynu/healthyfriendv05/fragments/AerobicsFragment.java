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
import com.ynu.healthyfriendv05.service.AerobicsFragmentService;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class AerobicsFragment extends Fragment implements OnClickListener {
	private Context context;
	private TextView pushupCount;
	private TextView pushup_calories;
	private TextView goal_rest;
	private ImageView background;
	private ImageView start_bn;
	private ImageView pause_bn;
	private ImageView stop_bn;
	private RelativeLayout pushup_panel;
	private TextView time_count;

	private User user;
	private Recorder recorder;

	private int count = 0;
	private int rest = 0;
	private String timer;
	private double calories = 0;
	private boolean time_work = false;
	private OnExerciseListener mListener;

	private IntentFilter proximityFilter = new IntentFilter(
			"com.healthyfriend.service.pushupReceiver");
	private ProximityReceiver proximityReceiver = new ProximityReceiver();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("SimpleDateFormat")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.aerobicsfragment, container,
				false);
		this.context = getActivity();
		background = (ImageView) view.findViewById(R.id.aerobics_bg);
		pushup_panel = (RelativeLayout) view.findViewById(R.id.pushup_panel);
		pushupCount = (TextView) view.findViewById(R.id.pushup_num);
		pushup_calories = (TextView) view
				.findViewById(R.id.pushup_calorie_burn);
		goal_rest = (TextView) view.findViewById(R.id.pushup_goal_value);
		time_count = (TextView) view.findViewById(R.id.aerobics_time_count);
		start_bn = (ImageView) view.findViewById(R.id.aerobics_start);
		pause_bn = (ImageView) view.findViewById(R.id.aerobics_pause);
		stop_bn = (ImageView) view.findViewById(R.id.aerobics_exit);
		pause_bn.setEnabled(false);
		time_count.setText("00:00");

		background.getBackground().setAlpha(100);
		pushup_panel.getBackground().setAlpha(170);
		pushupCount.setText(String.valueOf(count));
		pushup_calories.setText(String.valueOf(calories) + " 大卡");

		Bundle aerobics_data = getArguments();
		this.user = (User) aerobics_data.getSerializable("user_inform");
		Log.e("我的臂展", "" + this.user.getArmlength());
		this.rest = aerobics_data.getInt("goal_num");
		if ((rest - count) > 0)
			goal_rest.setText(String.valueOf(rest - count));
		else
			goal_rest.setText(String.valueOf(0));

		host_intent = new Intent(getActivity(), AerobicsFragmentService.class);
		getActivity().startService(host_intent);

		start_bn.setOnClickListener(this);
		pause_bn.setOnClickListener(this);
		stop_bn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(proximityReceiver, proximityFilter);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("fragment消息", "暂停");
		try {
			getActivity().unregisterReceiver(proximityReceiver);
		} catch (IllegalArgumentException e) {
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		connectService(2);
		Log.e("fragment消息", "销毁");
		try {
			getActivity().unregisterReceiver(proximityReceiver);
		} catch (IllegalArgumentException e) {
		}
		mListener.onServiceDestroy(host_intent);
	}

	public class ProximityReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();// 获得 Bundle
			timer = bundle.getString("time_count");
			count = bundle.getInt("pushup_num");
			time_work = bundle.getBoolean("working");
			calories = Double.parseDouble(Decimaltool.getDoublePrecision(user
					.getWeight() * user.getArmlength() * count / 1000));

			time_count.setText(timer);
			if (count == Integer.parseInt(pushupCount.getText().toString()) + 1) {
				if ((rest - count) >= 0) {
					goal_rest.setText(String.valueOf(rest - count));
					if ((rest - count) < 5 && (rest - count) > 0) {
						Toast.makeText(getActivity(),
								"距目标还剩" + (rest - count) + "个",
								Toast.LENGTH_SHORT).show();
					} else if ((rest - count) == 0)
						Toast.makeText(getActivity(), "目标达成",
								Toast.LENGTH_SHORT).show();
				}
			}
			pushupCount.setText(String.valueOf(count));
			pushup_calories.setText(String.valueOf(calories) + " 大卡");

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
		builder.setMessage("您做了" + count + "个俯卧撑，确定要离开吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				connectService(3);
				try {
					getActivity().unregisterReceiver(proximityReceiver);
				} catch (IllegalArgumentException e) {
				}
				getActivity().stopService(host_intent);

				recorder = new Recorder();
				recorder.setPushupcount(count);
				recorder.setCalories(calories);

				Bundle pushup_data = new Bundle();
				pushup_data.putSerializable("pushup", recorder);
				mListener.onFragmentExit(pushup_data, "aerobics");
				count = 0;
				rest = 0;
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
		case R.id.aerobics_start:
			connectService(1);
			mListener.onButtonSpeak("开始做俯卧撑！");
			start_bn.setEnabled(false);
			pause_bn.setEnabled(true);
			break;
		case R.id.aerobics_pause:
			connectService(2);
			start_bn.setEnabled(true);
			pause_bn.setEnabled(false);
			break;
		case R.id.aerobics_exit:
			connectService(2);
			start_bn.setEnabled(true);
			pause_bn.setEnabled(false);
			simpleAlert();
			break;
		}
	}

	private void connectService(int value) {
		Intent send_intent = new Intent();
		send_intent
				.setAction("com.healthyfriend.service.pushupControlReceiver");
		send_intent.putExtra("control", value);
		getActivity().sendBroadcast(send_intent);
	}
}