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
					Log.i("check", "登录成功");

					Intent intent = new Intent(SplashpageActivity.this,
							HostpageActivity.class); // 从启动动画ui跳转到主ui
					startActivity(intent);
					SplashpageActivity.this.finish(); // 结束启动动画界面

					Toast.makeText(SplashpageActivity.this, "欢迎",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.i("check", "登录失败");
					if (isFirstIn) {
						Intent intent = new Intent(SplashpageActivity.this,
								GuidepageActivity.class); // 从启动动画ui跳转到主ui
						startActivity(intent);
						SplashpageActivity.this.finish(); // 结束启动动画界面
						Toast.makeText(SplashpageActivity.this, "请注册",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(SplashpageActivity.this,
								InformationpageActivity.class); // 从启动动画ui跳转到主ui
						startActivity(intent);
						SplashpageActivity.this.finish(); // 结束启动动画界面
						Toast.makeText(SplashpageActivity.this, "请注册",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}, 2000); // 启动动画持续1.5秒钟
	}
}