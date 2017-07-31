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
import com.ynu.healthyfriendv05.po.Slim;
import com.ynu.healthyfriendv05.po.Sports;
import com.ynu.healthyfriendv05.service.SportsService;

public class SportsFragment extends Fragment {
	private Context context;
	private ListView sports_list;
	private LinearLayout title;

	private SportsService sportsService;
	private OnSlimListener mListener;

	private Slim sport;
	private ArrayList<Sports> list = new ArrayList<Sports>();
	private int sport_id = 0;
	private String sport_name = "";
	private String sport_introduce = "";
	private String sport_description = "";
	private double calories = 0;
	private byte[] sport_pic;

	private String flag = "sport";

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
		View view = inflater.inflate(R.layout.sportsfragment, container, false);
		this.context = getActivity();
		sports_list = (ListView) view.findViewById(R.id.sports_list);
		title = (LinearLayout) view.findViewById(R.id.sports_title_panel);
		title.getBackground().setAlpha(130);

		sportsService = new SportsService(context);
		list = sportsService.findAllSports();

		sports_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sport = new Sports();

				sport_id = list.get(position).getId();
				sport_name = list.get(position).getName().trim();
				sport_introduce = list.get(position).getIntroduce().trim();
				sport_description = list.get(position).getDescription().trim();
				calories = list.get(position).getCalories();
				sport_pic = list.get(position).getPic();

				sport.setId(sport_id);
				sport.setName(sport_name);
				sport.setIntroduce(sport_introduce);
				sport.setDescription(sport_description);
				sport.setCalories(calories);
				sport.setPic(sport_pic);

				Bundle sport_data = new Bundle();
				sport_data.putSerializable("sport_info", (Serializable) sport);
				mListener.onSportOrFoodSelect(sport_data, flag);
			}
		});

		inflateList(convertCursorToList(list));
		return view;
	}

	private ArrayList<Map<String, Object>> convertCursorToList(
			ArrayList<Sports> list) {
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		byte[] imgArr = null;
		Bitmap pic = null;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			imgArr = list.get(i).getPic();
			pic = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);

			map.put("sport_pic", pic);
			map.put("sport_name", list.get(i).getName().trim());
			map.put("sport_introduce", list.get(i).getIntroduce().trim());
			result.add(map);
		}

		return result;
	}

	private void inflateList(ArrayList<Map<String, Object>> arrayList) {
		// 填充SimpleCursorAdapter
		SimpleAdapter adapter = new SimpleAdapter(context, arrayList,
				R.layout.sportslist_item, new String[] { "sport_pic",
						"sport_name", "sport_introduce" }, new int[] {
						R.id.sports_pic, R.id.sports_name,
						R.id.sports_introduce }); // ③

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
		sports_list.setAdapter(adapter);
	}
}
