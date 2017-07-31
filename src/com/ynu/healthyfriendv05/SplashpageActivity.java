package com.ynu.healthyfriendv05;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ynu.healthyfriendv05.service.UserService;

public class SplashpageActivity extends Activity {
	private boolean isFirstIn = false;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	private UserService userService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashpage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		userService = new UserService(SplashpageActivity.this);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				boolean flag = userService.isExist();
				if (flag) {
					Log.i("check", "��¼�ɹ�");

					Intent intent = new Intent(SplashpageActivity.this,
							HostpageActivity.class); // ����������ui��ת����ui
					startActivity(intent);
					SplashpageActivity.this.finish(); // ����������������

					Toast.makeText(SplashpageActivity.this, "��ӭ",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.i("check", "��¼ʧ��");
					if (isFirstIn) {
						Intent intent = new Intent(SplashpageActivity.this,
								GuidepageActivity.class); // ����������ui��ת����ui
						startActivity(intent);
						SplashpageActivity.this.finish(); // ����������������
						Toast.makeText(SplashpageActivity.this, "��ע��",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(SplashpageActivity.this,
								InformationpageActivity.class); // ����������ui��ת����ui
						startActivity(intent);
						SplashpageActivity.this.finish(); // ����������������
						Toast.makeText(SplashpageActivity.this, "��ע��",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}, 2000); // ������������1.5����
	}
}