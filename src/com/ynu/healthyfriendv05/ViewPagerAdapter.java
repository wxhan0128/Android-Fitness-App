package com.ynu.healthyfriendv05;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {
	private List<View> views;
	private Activity activity;
	private ImageView startApp_bn;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	public ViewPagerAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	public void finishUpdate(View arg0) {

	}

	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			startApp_bn = (ImageView) arg0.findViewById(R.id.guide_pic04);

			startApp_bn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setGuided();
					goHome();
				}
			});
		}

		return views.get(arg1);
	}

	private void goHome() {
		Intent intent = new Intent(activity, InformationpageActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
	}

	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	public Parcelable saveState() {
		return null;
	}

	public void startUpdate(View arg0) {

	}
}