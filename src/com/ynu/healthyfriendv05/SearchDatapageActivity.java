package com.ynu.healthyfriendv05;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.ynu.healthyfriendv05.fragments.CaloriesDataFragment;
import com.ynu.healthyfriendv05.fragments.PushupDataFragment;
import com.ynu.healthyfriendv05.fragments.StepDataFragment;

public class SearchDatapageActivity extends FragmentActivity {
	private ViewPager viewPager;
	private PagerTabStrip title_tab;

	private StepDataFragment stepdataFragment;
	private PushupDataFragment pushupdataFragment;
	private CaloriesDataFragment caloriesdataFragment;
	private ArrayList<Fragment> fragmentList;

	private ArrayList<String> titleList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchdatapage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		title_tab = (PagerTabStrip) findViewById(R.id.tabstrip);
		title_tab.setDrawFullUnderline(false);
		title_tab.setBackgroundColor(getResources()
				.getColor(R.color.light_gray));
		title_tab.setTabIndicatorColor(getResources().getColor(
				R.color.super_dark_gray));
		title_tab.setTextSpacing(200);

		stepdataFragment = new StepDataFragment();
		pushupdataFragment = new PushupDataFragment();
		caloriesdataFragment = new CaloriesDataFragment();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(stepdataFragment);
		fragmentList.add(pushupdataFragment);
		fragmentList.add(caloriesdataFragment);

		titleList.add("步行数据");
		titleList.add("俯卧撑数据");
		titleList.add("卡路里数据");

		viewPager
				.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}