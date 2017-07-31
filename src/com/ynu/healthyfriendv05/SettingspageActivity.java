package com.ynu.healthyfriendv05;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ynu.healthyfriendv05.fragments.ChangeInformationFragment;
import com.ynu.healthyfriendv05.fragments.OnSettingListener;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.service.UserService;

public class SettingspageActivity extends Activity implements OnSettingListener {
	private Context context;
	private ListView informsetting_list;
	private ListView appsetting_list;
	private LinearLayout setting_panel;

	private String[] informsetting_titles = new String[] { "更新个人信息", "更新任务列表" };
	private String[] appsetting_titles = new String[] { "关于应用", "反馈" };
	private String[] informsetting_descs = { "重新设置自己的资料", "向自己的任务列表中添加新任务" };
	private String[] appsetting_descs = new String[] { "查看应用的基本信息", "提交个人评价和建议" };
	private int inform_imageIds[] = new int[] { R.drawable.update_inform,
			R.drawable.update_task };
	private int app_imageIds[] = new int[] { R.drawable.about,
			R.drawable.feedback };

	private FragmentManager fragmentManager;
	private Fragment changeInformationFragment;
	private Fragment currentFragment;

	private User user;
	private UserService userService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingspage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.context = this;
		informsetting_list = (ListView) findViewById(R.id.informsetting_listview);
		appsetting_list = (ListView) findViewById(R.id.appsetting_listview);
		setting_panel = (LinearLayout) findViewById(R.id.settingtable_panel);

		Intent intent = getIntent();
		this.user = (User) intent.getSerializableExtra("user");

		fragmentManager = getFragmentManager();
		changeInformationFragment = new ChangeInformationFragment();
		currentFragment = changeInformationFragment;

		refreshList();

		informsetting_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (informsetting_titles[position].equals("更新个人信息")) {
					switchFragment(changeInformationFragment);
					hideComponents();
					Bundle user_data = new Bundle();
					user_data.putSerializable("user_inform",
							(Serializable) user);
					changeInformationFragment.setArguments(user_data);
				}
			}
		});

		appsetting_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void refreshList() {
		inflateList(
				convertCursorToList(informsetting_titles, informsetting_descs,
						inform_imageIds), informsetting_list);
		inflateList(
				convertCursorToList(appsetting_titles, appsetting_descs,
						app_imageIds), appsetting_list);
	}

	private List<Map<String, Object>> convertCursorToList(String[] list_title,
			String[] list_descs, int[] list_imageIds) {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < list_title.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", list_imageIds[i]);
			listItem.put("title", list_title[i]);
			listItem.put("descs", list_descs[i]);
			listItems.add(listItem);
		}

		return listItems;
	}

	private void inflateList(List<Map<String, Object>> arrayList,
			ListView listview) {
		// 填充SimpleCursorAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, arrayList,
				R.layout.settinglist_item, new String[] { "image", "title",
						"descs" }, new int[] { R.id.setting_header,
						R.id.setting_title, R.id.setting_summary });
		listview.setAdapter(simpleAdapter);
	}

	private void switchFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		int stackcount = fragmentManager.getBackStackEntryCount();
		Log.e("Fragment栈", "" + stackcount);
		if (stackcount == 0) {
			fragmentTransaction.replace(R.id.setting_fragment_place, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commitAllowingStateLoss();
		} else {
			if (currentFragment != fragment) {
				fragmentManager.popBackStack();
				fragmentTransaction.replace(R.id.setting_fragment_place,
						fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commitAllowingStateLoss();
			} else {
			}
		}
		currentFragment = fragment;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		displayComponents();
	}

	private void hideComponents() {
		setting_panel.setVisibility(View.INVISIBLE);
	}

	private void displayComponents() {
		setting_panel.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSave(Bundle bundle) {
		// TODO Auto-generated method stub
		userService = new UserService(context);
		fragmentManager.popBackStack();
		this.user = (User) bundle.getSerializable("new_inform");
		userService.updateInform(user);
		displayComponents();
		refreshList();
	}

	@Override
	public void onFragmentBack() {
		// TODO Auto-generated method stub
		fragmentManager.popBackStack();
		// 显示
		displayComponents();
	}
}