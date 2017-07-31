package com.ynu.healthyfriendv05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ynu.healthyfriendv05.po.Recorder;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.service.RecorderService;
import com.ynu.healthyfriendv05.service.UserService;
import com.ynu.healthyfriendv05.tools.Datetool;
import com.ynu.healthyfriendv05.tools.Decimaltool;
import com.ynu.healthyfriendv05.tools.RoundProgressBar2;

public class HostpageActivity extends Activity {
	private Context context;
	private ImageView exercise_bn;
	private ImageView slim_bn;
	private ImageView setting_bn;
	private ImageView assistant_bn;
	private ListView details_list;
	private TextView index;
	private TextView exercise_type;
	private TextView tips;
	private RoundProgressBar2 r;

	private User user;
	private UserService userService;
	private Recorder recorder;
	private RecorderService recorderService;
	private double bmi = 0;
	private double weight_rate = 0;
	private long age = 0;

	private long exitTime = 0;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hostpage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.context = this;
		r = (RoundProgressBar2) findViewById(R.id.weight_index);
		index = (TextView) findViewById(R.id.my_weighttype_value);
		exercise_type = (TextView) findViewById(R.id.my_exercisetype_value);
		tips = (TextView) findViewById(R.id.healthy_tips);
		details_list = (ListView) findViewById(R.id.personaldetail);
		exercise_bn = (ImageView) findViewById(R.id.exercise);
		slim_bn = (ImageView) findViewById(R.id.slim);
		assistant_bn = (ImageView) findViewById(R.id.assistant);
		setting_bn = (ImageView) findViewById(R.id.setting);
		details_list.setEnabled(false);

		refreshList();
		initComponent();

		exercise_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putSerializable("user", user);
				Intent intent = new Intent(HostpageActivity.this,
						ExercisepageActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		});

		slim_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putSerializable("user", user);
				Intent intent = new Intent(HostpageActivity.this,
						SlimepageActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		});

		assistant_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putSerializable("user", user);
				Intent intent = new Intent(HostpageActivity.this,
						AssistantpageActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		});

		setting_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putSerializable("user", user);
				Intent intent = new Intent(HostpageActivity.this,
						SettingspageActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("tag", "-------onPause-------");
		// userService.closeDB();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("tag", "-------onRestart-------");
		// 刷新listview
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("tag", "-------onResume-------");
		refreshList();// 刷新listview
		initComponent();
	}

	@SuppressWarnings("deprecation")
	private void initComponent() {
		// TODO Auto-generated method stub
		String[] str = null;
		String state = null;
		StringBuffer advices = new StringBuffer();
		bmi = Double.parseDouble(Decimaltool.getDoublePrecision(user
				.getWeight() / Math.pow(user.getHeight() / 100, 2)));
		age = Datetool.getDateYear(Datetool.getCurrentTime()).getYear()
				- Datetool.getDateYear(user.getBirthday()).getYear();
		if (user.getGender().equals("男")) {
			weight_rate = Double.parseDouble(Decimaltool.getSinglePrecision(1.2
					* bmi + 0.23 * age - 5.4 - 10.8 * 1));
			if (weight_rate <= 5) {
				state = "低脂型";
			} else if (weight_rate <= 9 && weight_rate > 5) {
				state = "竞技型";
			} else if (weight_rate > 9 && weight_rate <= 12) {
				state = "健美型";
			} else if (weight_rate > 12 && weight_rate <= 15) {
				state = "运动型";
			} else if (weight_rate > 15 && weight_rate <= 18) {
				state = "标准型";
			} else if (weight_rate > 18 && weight_rate <= 25) {
				state = "偏胖型";
			} else if (weight_rate > 25) {
				state = "肥胖型";
			}
			advices.append("理想指数在15~18%之间");
		} else if (user.getGender().equals("女")) {
			weight_rate = Double.parseDouble(Decimaltool.getSinglePrecision(1.2
					* bmi + 0.23 * age - 5.4 - 10.8 * 0));
			if (weight_rate < 11) {
				state = "低脂型";
			} else if (weight_rate >= 11 && weight_rate < 17) {
				state = "竞技型";
			} else if (weight_rate >= 17 && weight_rate < 19) {
				state = "健美型";
			} else if (weight_rate >= 19 && weight_rate < 25) {
				state = "苗条型";
			} else if (weight_rate >= 25 && weight_rate <= 28) {
				state = "标准型";
			} else if (weight_rate > 28 && weight_rate <= 30) {
				state = "偏胖型";
			} else if (weight_rate > 30) {
				state = "肥胖型";
			}
			advices.append("理想指数在25~28%之间");
		}
		str = user.getExercise_type().toString().split("[ ]");
		r.setProgress((float) (weight_rate));
		r.setExplanation("体脂率");

		index.setText(state);
		exercise_type.setText(str[0]);
		advices.append("\n" + str[1]);
		tips.setText(advices);
	}

	private void refreshList() {
		userService = new UserService(context);
		recorderService = new RecorderService(context);
		user = userService.findUser();
		recorder = recorderService
				.findRecorderByDate(Datetool.getCurrentTime());

		inflateList(convertCursorToList(user, recorder));
	}

	private ArrayList<Map<String, Object>> convertCursorToList(User user,
			Recorder recorder) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_name", user.getName());
		map.put("user_weight", user.getWeight() + " kg");
		map.put("user_desweight", user.getDes_weight() + " kg");

		map.put("user_step", recorder.getStepcount() + " 步");
		map.put("user_pushup", recorder.getPushupcount() + " 个");
		result.add(map);

		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList) {
		// 填充SimpleCursorAdapter
		SimpleAdapter adapter = new SimpleAdapter(context, arrayList,
				R.layout.hostpagelist_item, new String[] { "user_name",
						"user_weight", "user_desweight", "user_step",
						"user_pushup" }, new int[] { R.id.my_name,
						R.id.my_weight, R.id.my_planweight, R.id.my_step,
						R.id.my_pushup }); // ③
		// 显示数据
		details_list.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("应用关闭消息", "关闭");
		userService.closeDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hostpage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}