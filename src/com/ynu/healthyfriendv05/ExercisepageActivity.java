package com.ynu.healthyfriendv05;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ynu.healthyfriendv05.R.id;
import com.ynu.healthyfriendv05.fragments.AerobicsFragment;
import com.ynu.healthyfriendv05.fragments.OnExerciseListener;
import com.ynu.healthyfriendv05.fragments.RunFragment;
import com.ynu.healthyfriendv05.po.Recorder;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.service.RecorderService;
import com.ynu.healthyfriendv05.tools.Datetool;

public class ExercisepageActivity extends Activity implements
		OnExerciseListener, OnInitListener {
	private Context context;
	private ImageView run_bn;
	private ImageView aerobics_bn;
	private ListView run_list;
	private ListView aerobics_list;
	private EditText goalsteps_input;
	private EditText goalpushup_input;
	private LinearLayout exercise_panel;
	private LinearLayout exercise_setting;
	private LinearLayout options;

	private FragmentManager fragmentManager;
	private Fragment runFragment;
	private Fragment aerobicsFragment;
	private Fragment currentFragment;
	private User user;
	private Recorder recorder;
	private ArrayList<Recorder> recorderlist = new ArrayList<Recorder>();
	private RecorderService recorderService;

	private int run_count = 0;;
	private int pushup_count = 0;
	private Intent service_intent;
	private TextToSpeech textToSpeech;
	private int MY_DATA_CHECK_CODE = 0;
	private String speak_text = "您好";

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercisepage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.context = this;
		run_bn = (ImageView) findViewById(R.id.run);
		aerobics_bn = (ImageView) findViewById(R.id.aerobics);
		goalsteps_input = (EditText) findViewById(R.id.step_input);
		goalpushup_input = (EditText) findViewById(R.id.pushup_input);
		run_list = (ListView) findViewById(R.id.rundetail);
		aerobics_list = (ListView) findViewById(id.aerobicsdetail);
		exercise_panel = (LinearLayout) findViewById(R.id.exercise_panel);
		exercise_setting = (LinearLayout) findViewById(R.id.exercise_settings);
		options = (LinearLayout) findViewById(R.id.exercise_options);
		run_list.setEnabled(false);
		aerobics_list.setEnabled(false);

		Intent intent = getIntent();
		this.user = (User) intent.getSerializableExtra("user");

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

		fragmentManager = getFragmentManager();
		runFragment = new RunFragment();
		aerobicsFragment = new AerobicsFragment();
		currentFragment = runFragment;

		refreshList();

		run_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchFragment(runFragment);
				hideComponents();
				if (!goalsteps_input.getText().toString().equals("")) {
					run_count = Integer.parseInt(goalsteps_input.getText()
							.toString());
					goalsteps_input.setEnabled(false);
				} else
					run_count = 0;

				Bundle run_data = new Bundle();
				run_data.putSerializable("user_inform", (Serializable) user);
				run_data.putInt("goal_num", run_count);
				runFragment.setArguments(run_data);
			}
		});

		aerobics_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchFragment(aerobicsFragment);
				hideComponents();
				if (!goalpushup_input.getText().toString().equals("")) {
					pushup_count = Integer.parseInt(goalpushup_input.getText()
							.toString());
					goalpushup_input.setEnabled(false);
				} else
					pushup_count = 0;

				Bundle aerobics_data = new Bundle();
				aerobics_data.putSerializable("user_inform",
						(Serializable) user);
				aerobics_data.putInt("goal_num", pushup_count);
				aerobicsFragment.setArguments(aerobics_data);
			}
		});
	}

	private void refreshList() {
		recorderlist.clear();
		recorderService = new RecorderService(context);
		recorderlist.add(recorderService.findRecorderByDate(Datetool
				.getCurrentTime()));
		recorderlist.add(recorderService.findBestRunGrade());
		recorderlist.add(recorderService.findBestPushupGrade());
		// recorder = recorderService.findRecorderByDate(time);

		inflateList(convertCursorToList(recorderlist));
	}

	private ArrayList<Map<String, Object>> convertCursorToList(
			ArrayList<Recorder> recorderlist) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("my_today_step", recorderlist.get(0).getStepcount() + " 步");
		map.put("my_today_distance", recorderlist.get(0).getDistance() + " m");
		map.put("my_best_step", recorderlist.get(1).getStepcount() + " 步");
		map.put("my_total_step", recorderService.findTotalGrade()[0] + " 步");
		map.put("my_total_distance", recorderService.findTotalGrade()[1] + " m");
		map.put("my_today_pushup", recorderlist.get(0).getPushupcount() + " 个");
		map.put("my_best_pushup", recorderlist.get(2).getPushupcount() + " 个");
		map.put("my_total_pushup", recorderService.findTotalGrade()[2] + " 个");
		result.add(map);

		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList) {
		// 填充SimpleCursorAdapter
		SimpleAdapter run_adapter = new SimpleAdapter(context, arrayList,
				R.layout.runlist_item, new String[] { "my_today_step",
						"my_total_step", "my_today_distance",
						"my_total_distance", "my_best_step" }, new int[] {
						R.id.my_today_steps, R.id.my_total_steps,
						R.id.my_today_distance, R.id.my_total_distance,
						R.id.my_best_steps }); // ③

		SimpleAdapter aerobics_adapter = new SimpleAdapter(context, arrayList,
				R.layout.aerobicslist_item, new String[] { "my_today_pushup",
						"my_total_pushup", "my_best_pushup" }, new int[] {
						R.id.my_today_pushup, R.id.my_total_pushup,
						R.id.my_best_pushup }); // ③
		// 显示数据
		run_list.setAdapter(run_adapter);
		aerobics_list.setAdapter(aerobics_adapter);
	}

	private void switchFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		int stackcount = fragmentManager.getBackStackEntryCount();
		Log.e("tag", "" + stackcount);
		if (stackcount == 0) {
			fragmentTransaction.add(R.id.exercise_fragment_place, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
		} else {
			if (currentFragment != fragment) {
				fragmentManager.popBackStack();
				fragmentTransaction.add(R.id.exercise_fragment_place, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commitAllowingStateLoss();
			} else {
			}
		}
		currentFragment = fragment;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("页面消息", "暂停");
		if (textToSpeech != null)
			textToSpeech.stop();
		// recorderService.closeDB();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("页面消息", "关闭");
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		if (service_intent != null) {
			this.stopService(service_intent);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		displayComponents();
		Log.e("tag", "按返回键了");
	}

	@Override
	public void onFragmentExit(Bundle bundle, String flag) {
		// TODO Auto-generated method stub
		recorderService = new RecorderService(context);
		fragmentManager.popBackStack();
		// 显示
		displayComponents();
		goalsteps_input.setEnabled(true);
		goalpushup_input.setEnabled(true);
		goalsteps_input.setText("");
		goalpushup_input.setText("");

		if (flag.equals("aerobics")) {
			this.recorder = (Recorder) bundle.getSerializable("pushup");
		} else if (flag.equals("run")) {
			this.recorder = (Recorder) bundle.getSerializable("run");
		}
		this.recorder.setUserId(user.getId());
		this.recorder.setDate(Datetool.getCurrentTime());
		Log.e("tag", recorder.getUserId() + " " + recorder.getPushupcount());
		if (recorderService.isExist(recorder.getDate())) {
			recorderService.changeRecorderByDate(recorder);
		} else {
			recorderService.insert(recorder);
		}
		recorderService.findRecorderByDate(recorder.getDate());
		recorderService.findAllRecorders();

		refreshList();
	}

	@Override
	public void onServiceDestroy(Intent intent) {
		// TODO Auto-generated method stub
		this.service_intent = intent;
	}

	private void hideComponents() {
		options.setVisibility(View.INVISIBLE);
		exercise_panel.setVisibility(View.INVISIBLE);
		exercise_setting.setVisibility(View.INVISIBLE);
	}

	private void displayComponents() {
		exercise_panel.setVisibility(View.VISIBLE);
		options.setVisibility(View.VISIBLE);
		exercise_setting.setVisibility(View.VISIBLE);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			int result = textToSpeech.setLanguage(Locale.CHINA);
			// Try this someday for some interesting results.
			// int result mTts.setLanguage(Locale.FRANCE);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Lanuage data is missing or the language is not supported.
				Log.e("语音消息", "Language is not available.");
			} else {
				// Check the documentation for other possible result codes.
				// For example, the language may be available for the locale,
				// but not for the specified country and variant.
				// The TTS engine has been successfully initialized.
				// Allow the user to press the button for the app to speak
				// again.
				// Greet the user.
				Log.e("语音消息",
						"The TTS engine has been successfully initialized.");
			}
		} else {
			// Initialization failed.
			Log.e("语音消息", "Could not initialize TextToSpeech.");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				textToSpeech = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	@Override
	public void onButtonSpeak(String sentence) {
		// TODO Auto-generated method stub
		this.speak_text = sentence;
		textToSpeech.speak(speak_text, TextToSpeech.QUEUE_ADD, null);
	}
}