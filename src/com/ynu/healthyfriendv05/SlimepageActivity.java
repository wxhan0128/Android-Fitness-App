package com.ynu.healthyfriendv05;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ynu.healthyfriendv05.R.drawable;
import com.ynu.healthyfriendv05.fragments.DietFragment;
import com.ynu.healthyfriendv05.fragments.OnSlimListener;
import com.ynu.healthyfriendv05.fragments.SportsFragment;
import com.ynu.healthyfriendv05.po.Slim;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.po.UserSlim;
import com.ynu.healthyfriendv05.service.UserSlimService;
import com.ynu.healthyfriendv05.tools.Datetool;

public class SlimepageActivity extends Activity implements OnSlimListener {
	private Context context;
	private ImageView sports_bn;
	private ImageView diet_bn;
	private ListView diet_list;
	private ListView sport_list;
	private LinearLayout slim_table;
	private TextView input_calories;
	private TextView output_calories;

	private FragmentManager fragmentManager;
	private Fragment sportsFragment;
	private Fragment dietFragment;
	private Fragment currentFragment;

	private Slim slim;
	private User user;
	private UserSlim userSlim;
	private UserSlimService userslimService;
	private int userSlim_sid = 0;
	private String userSlim_date = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slimepage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.context = this;
		sports_bn = (ImageView) findViewById(R.id.sports);
		diet_bn = (ImageView) findViewById(R.id.diet);
		sport_list = (ListView) findViewById(R.id.today_sport_listview);
		diet_list = (ListView) findViewById(R.id.today_diet_listview);
		slim_table = (LinearLayout) findViewById(R.id.slimtable_panel);
		input_calories = (TextView) findViewById(R.id.my_incalories_value);
		output_calories = (TextView) findViewById(R.id.my_outcalories_value);

		userslimService = new UserSlimService(this);
		userslimService.findAllUserSlim();
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");
		Log.e("tag", "" + user.getId());

		fragmentManager = getFragmentManager();
		sportsFragment = new SportsFragment();
		dietFragment = new DietFragment();
		currentFragment = sportsFragment;

		refreshList();

		sport_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				userSlim = new UserSlim();

				userSlim_sid = userslimService
						.findSportsByDate(Datetool.getCurrentTime())
						.get(position).getSlimId();
				userSlim_date = userslimService
						.findSportsByDate(Datetool.getCurrentTime())
						.get(position).getDate();

				userSlim.setSlimId(userSlim_sid);
				userSlim.setDate(userSlim_date);

				Log.e("����", "�˶�����" + position + " " + userSlim.getSlimId()
						+ " " + userSlim.getDate());
				simpleAlert();

				return true;
			}
		});

		diet_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				userSlim = new UserSlim();

				userSlim_sid = userslimService
						.findFoodByDate(Datetool.getCurrentTime())
						.get(position).getSlimId();
				userSlim_date = userslimService
						.findFoodByDate(Datetool.getCurrentTime())
						.get(position).getDate();

				userSlim.setSlimId(userSlim_sid);
				userSlim.setDate(userSlim_date);

				Log.e("����", "ʳ�׳���" + position + " " + userSlim.getSlimId()
						+ " " + userSlim.getDate());
				simpleAlert();
				return true;
			}
		});

		sports_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchFragment(sportsFragment);
				hideComponents();
			}
		});

		diet_bn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchFragment(dietFragment);
				hideComponents();
			}
		});
	}

	private void refreshList() {
		inflateList(
				convertCursorToList(userslimService.findSportsByDate(Datetool
						.getCurrentTime())), sport_list);
		inflateList(convertCursorToList(userslimService.findFoodByDate(Datetool
				.getCurrentTime())), diet_list);
		input_calories.setText(String.valueOf(userslimService
				.calculateTodayCalories("food", Datetool.getCurrentTime()))
				+ " ��");
		output_calories.setText(String.valueOf(userslimService
				.calculateTodayCalories("sport", Datetool.getCurrentTime()))
				+ " ��");
	}

	private ArrayList<Map<String, Object>> convertCursorToList(
			ArrayList<UserSlim> userslimlist) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < userslimlist.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("slim_name", userslimlist.get(i).getSlimName().trim());
			map.put("slim_calories", userslimlist.get(i).getSlimCalories()
					+ " ��");
			result.add(map);
		}

		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList,
			ListView listview) {
		// ���SimpleCursorAdapter
		SimpleAdapter adapter = new SimpleAdapter(context, arrayList,
				R.layout.myslimtablelist_item, new String[] { "slim_name",
						"slim_calories" }, new int[] { R.id.slim_name,
						R.id.slim_calories }); // ��
		listview.setAdapter(adapter);
	}

	private void switchFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		int stackcount = fragmentManager.getBackStackEntryCount();
		Log.e("Fragmentջ", "" + stackcount);
		if (stackcount == 0) {
			fragmentTransaction.replace(R.id.slim_fragment_place, fragment);
			// fragmentTransaction.add(R.id.slim_fragment_place, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
		} else {
			if (currentFragment != fragment) {
				fragmentManager.popBackStack();
				fragmentTransaction.replace(R.id.slim_fragment_place, fragment);
				// fragmentTransaction.add(R.id.slim_fragment_place, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commitAllowingStateLoss();
			} else {
			}
		}
		currentFragment = fragment;
	}

	@Override
	public void onSportOrFoodSelect(Bundle bundle, String flag) {
		// TODO Auto-generated method stub
		if (flag.equals("sport"))
			this.slim = (Slim) bundle.getSerializable("sport_info");
		else if (flag.equals("food"))
			this.slim = (Slim) bundle.getSerializable("food_info");
		slimInformDialog();
	}

	private void simpleAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("��ʾ");
		builder.setIcon(drawable.warning);
		builder.setMessage("�ף�ȷ��Ҫɾ���ü�¼��");
		builder.setPositiveButton("ȷ��ɾ��",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						userslimService.deleteByItem(userSlim);
						refreshList();
					}
				});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		builder.show();
	}

	@SuppressLint("InflateParams")
	private void slimInformDialog() {
		// װ��/res/layout/login.xml���沼��
		RelativeLayout inform_check = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.slimdialog, null);

		ByteArrayInputStream stream = new ByteArrayInputStream(slim.getPic());
		Drawable drawable = Drawable.createFromResourceStream(
				this.getResources(), null, stream, "src", null);

		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		// ���öԻ����ͼ��
		ad.setIcon(drawable);
		// ���öԻ���ı���
		ad.setTitle(slim.getName());
		// ���öԻ�����ʾ��View����
		ad.setView(inform_check);
		// Ϊ�Ի�������һ����ȷ������ť
		ad.setMessage(slim.getDescription() + "\n\n" + "��·�"
				+ slim.getCalories() + " ��");
		ad.setPositiveButton("����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("tag", "" + slim.getId());
				userSlim = new UserSlim();
				userSlim.setUserId(user.getId());
				userSlim.setSlimId(slim.getId());
				userSlim.setDate(Datetool.getCurrentTime());

				if (userslimService.isTask(userSlim)) {
					userslimService.findAllUserSlim();
					Toast.makeText(SlimepageActivity.this, "��¼�Ѵ����������б���",
							Toast.LENGTH_SHORT).show();
				} else {
					if (userslimService.isExist(userSlim)) {
						userslimService.findAllUserSlim();
						Toast.makeText(SlimepageActivity.this, "�Ѿ�ѡ����",
								Toast.LENGTH_SHORT).show();
					} else {
						userSlim.setTask(0);
						userslimService.choose(userSlim);
						Toast.makeText(SlimepageActivity.this, "�¼�¼��ӳɹ�",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		ad.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				userslimService.findAllUserSlim();
			}
		});
		ad.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		refreshList();
		displayComponents();
	}

	private void hideComponents() {
		slim_table.setVisibility(View.INVISIBLE);
	}

	private void displayComponents() {
		slim_table.setVisibility(View.VISIBLE);
	}
}