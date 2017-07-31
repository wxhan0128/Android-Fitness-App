package com.ynu.healthyfriendv05.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class AerobicsFragmentService extends Service implements
		SensorEventListener {
	private SensorManager sensorManager;
	private Sensor sensor;

	private int control = 2;
	private int count = 0;
	private int recLen = 0;
	private boolean isHigh = false, isLow = false, isHighAgain = false;

	private IntentFilter flagFilter = new IntentFilter(
			"com.healthyfriend.service.pushupControlReceiver");
	private FlagReceiver flagReceiver = new FlagReceiver();
	private Intent intent = new Intent();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		registerReceiver(flagReceiver, flagFilter);

		super.onCreate();
	}

	public class FlagReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();// 获得 Bundle
			control = bundle.getInt("control");
			switch (control) {
			case 1:
				start();
				break;
			case 2:
				exit();
				break;
			case 3:
				exit();
				count = 0;
				recLen = 0;
			default:
				break;
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		exit();
		Log.e("service消息", "死亡");
		AerobicsFragmentService.this.stopSelf();
		unregisterReceiver(flagReceiver);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.e("control", "" + control);
			switch (msg.what) {
			case 1:
				if (control == 1) {
					refreshTime();
					handler.sendEmptyMessageDelayed(1, 1000);
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void refreshTime() {
		recLen++;
		intent.setAction("com.healthyfriend.service.pushupReceiver");
		intent.putExtra("time_count", getMin() + ":" + getSec());
		intent.putExtra("working", true);
		Log.e("时间", getMin() + ":" + getSec());
		sendBroadcast(intent);
	}

	private CharSequence getMin() {
		int minute = (int) (recLen / 60) % 60;
		CharSequence min;
		if (minute < 10)
			min = "0" + minute;
		else
			min = "" + minute;
		return min;
	}

	private CharSequence getSec() {
		int second = (int) (recLen % 60);
		CharSequence sec;
		if (second < 10)
			sec = "0" + second;
		else
			sec = "" + second;
		return sec;
	}

	private void start() {
		handler.sendEmptyMessage(1);
		if (sensorManager != null) {// 注册监听器
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
			// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
		}
	}

	private void exit() {
		if (sensorManager != null) {// 取消监听器
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		intent.setAction("com.healthyfriend.service.pushupReceiver");
		float[] values = event.values;
		if (values[0] >= sensor.getMaximumRange()) {
			isHigh = true;
			Log.e("俯卧撑标记", "高度到达");
		}

		if (isHigh && values[0] <= 5) {
			isLow = true;
			Log.e("俯卧撑标记", "低度到达");
		}

		if (isLow && values[0] >= sensor.getMaximumRange()) {
			isHighAgain = true;
			Log.e("俯卧撑标记", "高度再次到达");
		}

		if (isHighAgain) {
			count++;
			isHigh = isLow = isHighAgain = false;
			Log.e("俯卧撑标记", "俯卧撑加1");
		}
		intent.putExtra("pushup_num", count);
		sendBroadcast(intent);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}