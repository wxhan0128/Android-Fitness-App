package com.ynu.healthyfriendv05.fragments;

import android.content.Intent;
import android.os.Bundle;

public interface OnExerciseListener {
	public void onFragmentExit(Bundle bundle, String flag);

	public void onButtonSpeak(String sentence);

	public void onServiceDestroy(Intent intent);
}
