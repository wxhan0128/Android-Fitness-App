package com.ynu.healthyfriendv05;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.ynu.healthyfriendv05.fragments.OnDetailsListener;
import com.ynu.healthyfriendv05.fragments.PersonalDetailsFragment;
import com.ynu.healthyfriendv05.fragments.PlanFragment;
import com.ynu.healthyfriendv05.fragments.StateFragment;
import com.ynu.healthyfriendv05.po.Sports;
import com.ynu.healthyfriendv05.po.User;
import com.ynu.healthyfriendv05.po.UserSlim;
import com.ynu.healthyfriendv05.service.UserService;
import com.ynu.healthyfriendv05.service.UserSlimService;

public class InformationpageActivity extends Activity implements
		OnDetailsListener {
	private Context context;
	private ImageView background;

	private FragmentManager fragmentManager;
	private PersonalDetailsFragment personalDetailsFragment;
	private StateFragment stateFragment;
	private PlanFragment planFragment;

	private User user;
	private UserSlim userSlim;
	private UserSlimService userslimService;
	private UserService userService;

	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informationpage);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		this.context = this;
		background = (ImageView) findViewById(R.id.register_bg);
		background.getBackground().setAlpha(130);

		fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		personalDetailsFragment = new PersonalDetailsFragment();
		fragmentTransaction.add(R.id.inform_fragment_place,
				personalDetailsFragment);
		fragmentTransaction.commit();
	}

	private void switchFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.remove(getFragmentManager().findFragmentById(
				R.id.inform_fragment_place));
		fragmentTransaction.add(R.id.inform_fragment_place, fragment).commit();
	}

	@Override
	public void onBaseDetailSubmit(Bundle bundle) {
		// TODO Auto-generated method stub
		stateFragment = new StateFragment();
		Bundle base_data = bundle;
		this.user = (User) bundle.getSerializable("base_inform");
		stateFragment.setArguments(base_data);
		switchFragment(stateFragment);
	}

	@Override
	public void onStateDetailSubmit(Bundle bundle) {
		// TODO Auto-generated method stub
		planFragment = new PlanFragment();
		Bundle state_data = bundle;
		this.user = (User) bundle.getSerializable("state_inform");
		planFragment.setArguments(state_data);
		switchFragment(planFragment);
	}

	@Override
	public void onFinished(Bundle bundle, Boolean flag, ArrayList<Sports> list) {
		// TODO Auto-generated method stub
		if (flag) {
			userService = new UserService(context);
			userslimService = new UserSlimService(context);
			userService.register(this.user);
			Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();

			this.user = userService.findUser();
			for (int i = 0; i < list.size(); i++) {
				userSlim = new UserSlim();
				userSlim.setUserId(user.getId());
				userSlim.setSlimId(list.get(i).getId());
				userSlim.setTask(1);
				userslimService.choose(userSlim);
			}

			Intent intent = new Intent(this, HostpageActivity.class);
			startActivity(intent);
			this.finish();
		} else {

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		userService.closeDB();
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

	@Override
	public void onBackFragment(String flag) {
		// TODO Auto-generated method stub
		if (flag.equals("backtoinform")) {
			switchFragment(personalDetailsFragment);
		} else if (flag.equals("backtostate")) {
			switchFragment(stateFragment);
		}
	}
}