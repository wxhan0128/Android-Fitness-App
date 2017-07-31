package com.ynu.healthyfriendv05.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.po.Food;
import com.ynu.healthyfriendv05.po.Slim;
import com.ynu.healthyfriendv05.service.FoodService;

public class DietFragment extends Fragment {
	private Context context;
	private ListView food_list;
	private LinearLayout title;

	private FoodService foodService;
	private OnSlimListener mListener;

	private Slim food;
	private ArrayList<Food> list = new ArrayList<Food>();
	private int food_id = 0;
	private String food_name = "";
	private String food_introduce = "";
	private String food_description = "";
	private double calories = 0;
	private byte[] food_pic;

	private String flag = "food";

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			mListener = (OnSlimListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSlimListener");
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dietfragment, container, false);
		this.context = getActivity();
		food_list = (ListView) view.findViewById(R.id.food_list);
		title = (LinearLayout) view.findViewById(R.id.food_title_panel);
		title.getBackground().setAlpha(100);

		foodService = new FoodService(context);
		list = foodService.findAllFood();

		food_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				food = new Food();

				food_id = list.get(position).getId();
				food_name = list.get(position).getName().trim();
				food_introduce = list.get(position).getIntroduce().trim();
				food_description = list.get(position).getDescription().trim();
				calories = list.get(position).getCalories();
				food_pic = list.get(position).getPic();

				food.setId(food_id);
				food.setName(food_name);
				food.setIntroduce(food_introduce);
				food.setDescription(food_description);
				food.setCalories(calories);
				food.setPic(food_pic);

				Bundle food_data = new Bundle();
				food_data.putSerializable("food_info", (Serializable) food);
				mListener.onSportOrFoodSelect(food_data, flag);
			}
		});

		refreshList();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("生命周期检查", "重新启动");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("生命周期检查", "暂停");
	}

	private void refreshList() {
		inflateList(convertCursorToList(list));
	}

	private ArrayList<Map<String, Object>> convertCursorToList(
			ArrayList<Food> list) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		byte[] imgArr = null;
		Bitmap pic = null;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			imgArr = list.get(i).getPic();
			pic = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);

			map.put("food_pic", pic);
			map.put("food_name", list.get(i).getName());
			map.put("food_introduce", list.get(i).getIntroduce());
			result.add(map);
		}
		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList) {
		// 填充SimpleCursorAdapter
		SimpleAdapter adapter = new SimpleAdapter(context, arrayList,
				R.layout.foodlist_item, new String[] { "food_pic", "food_name",
						"food_introduce" }, new int[] { R.id.food_pic,
						R.id.food_name, R.id.food_introduce }); // ③

		// 处理imageview不能显示图片的问题
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// TODO Auto-generated method stub
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView imageView = (ImageView) view;
					imageView.setImageBitmap((Bitmap) data);
					return true;
				} else
					return false;
			}
		});

		// 显示数据
		food_list.setAdapter(adapter);
	}
}
