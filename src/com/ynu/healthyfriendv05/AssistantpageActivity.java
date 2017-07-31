package com.ynu.healthyfriendv05;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ynu.healthyfriendv05.fragments.HealthyDiaryFragment;
import com.ynu.healthyfriendv05.fragments.HomeFragment;
import com.ynu.healthyfriendv05.fragments.OnAssistantListener;
import com.ynu.healthyfriendv05.po.Diary;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.service.DiaryService;
import com.ynu.healthyfriendv05.service.RecorderService;
import com.ynu.healthyfriendv05.service.UserSlimService;
import com.ynu.healthyfriendv05.tools.Datetool;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class AssistantpageActivity extends Activity implements
		SensorEventListener, OnAssistantListener {
	private Context context;
	private TextView temperature;
	private TextView humidity;
	private TextView comfortable;
	private TextView suitable_sport;
	private TextView plan_days;
	private TextView cut_weight;
	private TextView aver_calories;
	private TextView today_calories;
	private ImageView diary_bn;
	private ImageView home_bn;
	private LinearLayout assistant_panel;
	private LinearLayout assistant_options;
	private ListView diary_list;
	private TextView emptyText;

	private FragmentManager fragmentManager;
	private Fragment diaryFragment;
	private Fragment homeFragment;
	private Fragment currentFragment;

	private User user;
	private Diary diary;
	private DiaryService diaryService;
	private RecorderService recorderService;
	private UserSlimService userslimService;
	private ArrayList<Diary> list = new ArrayList<Diary>();

	private SensorManager sensorManager;

	private Bundle diary_bundle;
	private boolean isEdit = false;
	private double task_calories = 0;
	private double recorder_calories = 0;
	private double sport_calories = 0;
	private double food_calories = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assistantpage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.context = this;
		temperature = (TextView) findViewById(R.id.temperature_value);
		humidity = (TextView) findViewById(R.id.humidity_value);
		comfortable = (TextView) findViewById(R.id.comfortable_value);
		suitable_sport = (TextView) findViewById(R.id.suitable_sport_value);
		plan_days = (TextView) findViewById(R.id.days_value);
		cut_weight = (TextView) findViewById(R.id.aver_weight_value);
		aver_calories = (TextView) findViewById(R.id.aver_calories_value);
		today_calories = (TextView) findViewById(R.id.totalcalories_value);
		diary_bn = (ImageView) findViewById(R.id.diary);
		home_bn = (ImageView) findViewById(R.id.home);
		diary_list = (ListView) findViewById(R.id.diary_list);
		assistant_panel = (LinearLayout) findViewById(R.id.whole_assistant_panel);
		assistant_options = (LinearLayout) findViewById(R.id.assistant_options);
		emptyText = (TextView) this.findViewById(R.id.emptyShow);

		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");

		fragmentManager = getFragmentManager();
		diaryFragment = new HealthyDiaryFragment();
		homeFragment = new HomeFragment();
		currentFragment = diaryFragment;

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		diary_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshBundle();
				switchFragment(diaryFragment);
				hideComponents();
			}
		});

		home_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshBundle();
				switchFragment(homeFragment);
				hideComponents();
			}
		});

		initComponent();
		refreshList();
		refreshBundle();

		diary_list.setEmptyView(emptyText);
		this.registerForContextMenu(diary_list);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initComponent();
		refreshList();
		refreshBundle();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	private void initComponent() {
		recorderService = new RecorderService(context);
		userslimService = new UserSlimService(context);
		plan_days.setText(String.valueOf(user.getPlanday()) + " 天");

		recorder_calories = recorderService.findTotalCaloriesByDate(Datetool
				.getCurrentTime());
		task_calories = userslimService.calculateTaskCalories();
		sport_calories = userslimService.calculateTodayCalories("sport",
				Datetool.getCurrentTime());
		food_calories = userslimService.calculateTodayCalories("food",
				Datetool.getCurrentTime());

		cut_weight
				.setText(Decimaltool.getDoublePrecision((user.getWeight() - user
						.getDes_weight()) / user.getPlanday())
						+ " KG");
		aver_calories
				.setText(Decimaltool.getDoublePrecision((user.getWeight() - user
						.getDes_weight()) / user.getPlanday() * 7700)
						+ " 大卡");
		today_calories.setText(Decimaltool.getDoublePrecision(recorder_calories
				+ task_calories + sport_calories - food_calories)
				+ " 大卡");
	}

	private void refreshBundle() {
		diary_bundle = new Bundle();
		diary_bundle.putBoolean("edit_flag", isEdit);
		diaryFragment.setArguments(diary_bundle);
	}

	private void refreshList() {
		diaryService = new DiaryService(context);
		list = diaryService.findAllDiary();
		inflateList(convertCursorToList(list));
	}

	private ArrayList<Map<String, Object>> convertCursorToList(
			ArrayList<Diary> diarylist) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < diarylist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("diary_name", diarylist.get(i).getTitle().trim());
			map.put("diary_date", diarylist.get(i).getCreateDate().trim());
			result.add(map);
		}

		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList) {
		// 填充SimpleCursorAdapter
		SimpleAdapter adapter = new SimpleAdapter(context, arrayList,
				R.layout.diarylist_item, new String[] { "diary_name",
						"diary_date" }, new int[] { R.id.my_diary_name,
						R.id.my_diary_date }); // ③
		diary_list.setAdapter(adapter);
	}

	private void switchFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		int stackcount = fragmentManager.getBackStackEntryCount();
		Log.e("tag", "" + stackcount);
		if (stackcount == 0) {
			fragmentTransaction.add(R.id.assistant_fragment_place, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
		} else {
			if (currentFragment != fragment) {
				fragmentManager.popBackStack();
				fragmentTransaction
						.add(R.id.assistant_fragment_place, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commitAllowingStateLoss();
			} else {
			}
		}
		currentFragment = fragment;
	}

	@Override
	public void onDiarySave(Bundle bundle) {
		// TODO Auto-generated method stub
		fragmentManager.popBackStack();
		// 显示
		displayComponents();

		diaryService = new DiaryService(context);
		this.diary = (Diary) bundle.getSerializable("diary_info");
		diary.setUserId(user.getId());
		if (diaryService.isExist(diary.getCreateDate())) {
			diaryService.editDiary(diary);
		} else
			diaryService.writeDiary(diary);
		refreshList();
	}

	@Override
	public void onFragmentBack() {
		// TODO Auto-generated method stub
		fragmentManager.popBackStack();
		// 显示
		displayComponents();
		refreshList();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initComponent();
		refreshList();
		refreshBundle();

		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
						SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		displayComponents();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflator = new MenuInflater(context);
		inflator.inflate(R.menu.diary_context_menu, menu);
		menu.setHeaderTitle("请选择对日志的操作");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Log.v("单击ID", "context item seleted ID=" + menuInfo.id);

		diary = new Diary();
		diary.setDiaryId(list.get((int) menuInfo.id).getDiaryId());
		diary.setUserId(list.get((int) menuInfo.id).getUserId());
		diary.setTitle(list.get((int) menuInfo.id).getTitle());
		diary.setContent(list.get((int) menuInfo.id).getContent());
		diary.setCreateDate(list.get((int) menuInfo.id).getCreateDate());

		switch (item.getItemId()) {
		case R.id.edit_diary:
			isEdit = true;
			diary_bundle = new Bundle();
			diary_bundle.putSerializable("my_diary_data", (Serializable) diary);
			diary_bundle.putBoolean("edit_flag", isEdit);
			diaryFragment.setArguments(diary_bundle);

			switchFragment(diaryFragment);
			hideComponents();

			isEdit = false;
			break;
		case R.id.delete_diary:
			diaryService.deleteDiary(diary);
			refreshList();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		double tem = 0, hum = 0;
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		switch (sensorType) {
		case Sensor.TYPE_AMBIENT_TEMPERATURE:
			temperature.setText(Decimaltool.getSinglePrecision(values[0])
					+ " ℃");
			tem = values[0];
			break;
		case Sensor.TYPE_RELATIVE_HUMIDITY:
			humidity.setText(Decimaltool.getSinglePrecision(values[0]) + " %RH");
			hum = values[0];
			break;
		default:
			break;
		}

		if (tem >= 17 && tem <= 24) {
			if (hum >= 50 && hum <= 60) {
				comfortable.setText("舒适");
				suitable_sport.setText("任何运动");
			} else if (hum < 50) {
				comfortable.setText("干燥");
				suitable_sport.setText("室外运动");
			} else if (hum > 60) {
				comfortable.setText("潮湿");
				suitable_sport.setText("室内运动");
			}
		} else if (tem > 24) {
			if (hum >= 50 && hum <= 60) {
				if (tem > 37) {
					comfortable.setText("酷热");
					suitable_sport.setText("室内轻微运动");
				} else {
					comfortable.setText("较热");
					suitable_sport.setText("室内外轻微运动");
				}
			} else if (hum < 50) {
				comfortable.setText("干热");
				suitable_sport.setText("室内外轻微运动");
			} else if (hum > 60) {
				comfortable.setText("闷热");
				suitable_sport.setText("室内轻微运动");
			}
		} else if (tem < 17) {
			if (hum >= 50 && hum <= 60) {
				if (tem < 0) {
					comfortable.setText("严寒");
					suitable_sport.setText("不宜运动");
				} else {
					comfortable.setText("干爽");
					suitable_sport.setText("任何运动");
				}
			} else if (hum < 50) {
				comfortable.setText("干冷");
				suitable_sport.setText("室内运动");
			} else if (hum > 60) {
				comfortable.setText("湿冷");
				suitable_sport.setText("室内运动");
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private void hideComponents() {
		// TODO Auto-generated method stub
		assistant_panel.setVisibility(View.INVISIBLE);
		assistant_options.setVisibility(View.INVISIBLE);
	}

	private void displayComponents() {
		// TODO Auto-generated method stub
		assistant_panel.setVisibility(View.VISIBLE);
		assistant_options.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSearchData() {
		Intent intent = new Intent(AssistantpageActivity.this,
				SearchDatapageActivity.class);
		startActivity(intent);
	}
}