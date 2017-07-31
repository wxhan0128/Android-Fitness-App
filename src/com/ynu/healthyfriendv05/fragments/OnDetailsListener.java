package com.ynu.healthyfriendv05.fragments;

import java.util.ArrayList;

import android.os.Bundle;

import com.ynu.healthyfriendv05.po.Sports;

public interface OnDetailsListener {
	public void onBackFragment(String flag);

	public void onBaseDetailSubmit(Bundle bundle);

	public void onStateDetailSubmit(Bundle bundle);

	public void onFinished(Bundle bundle, Boolean flag, ArrayList<Sports> list);
}
