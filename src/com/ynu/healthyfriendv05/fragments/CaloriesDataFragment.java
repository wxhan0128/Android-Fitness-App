package com.ynu.healthyfriendv05.fragments;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ynu.healthyfriendv05.R;
import com.ynu.healthyfriendv05.service.RecorderService;
import com.ynu.healthyfriendv05.service.UserSlimService;
import com.ynu.healthyfriendv05.tools.Datetool;
import com.ynu.healthyfriendv05.tools.Decimaltool;

public class CaloriesDataFragment extends Fragment {
	private Context context;
	private LinearLayout chart;

	private UserSlimService userslimService;
	private RecorderService recorderService;
	private String[] titles = { "卡路里" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.caloriesdatafragment, container,
				false);
		this.context = getActivity();
		chart = (LinearLayout) view.findViewById(R.id.calories_chart);

		GraphicalView chart_view = ChartFactory.getLineChartView(context,
				buildLineDataset(titles), buildLineRenderer(titles, Color.RED));
		chart.addView(chart_view);

		return view;
	}

	private XYMultipleSeriesDataset buildLineDataset(String[] titles) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		userslimService = new UserSlimService(context);
		recorderService = new RecorderService(context);
		int length = titles.length;

		for (int i = 0; i < length; i++) {
			// 新建一个系列（线条）
			CategorySeries series = new CategorySeries(titles[i]);
			for (int x = 1; x <= 12; x++) {
				double y = 0;
				double a = 0, b = 0, c = 0, d = 0;
				int current_month = Integer
						.parseInt(Datetool.getCurrentMonth());
				if (x <= current_month) {
					if (x < 10) {
						a = recorderService.findTotalCaloriesByMonth(Datetool
								.getCurrentYear() + "-0" + x);
						b = userslimService.calculateCaloriesByMonth("sport",
								Datetool.getCurrentYear() + "-0" + x);
						c = userslimService.calculateCaloriesByMonth("food",
								Datetool.getCurrentYear() + "-0" + x);
					} else {
						a = recorderService.findTotalCaloriesByMonth(Datetool
								.getCurrentYear() + "-" + x);
						b = userslimService.calculateCaloriesByMonth("sport",
								Datetool.getCurrentYear() + "-" + x);
						c = userslimService.calculateCaloriesByMonth("food",
								Datetool.getCurrentYear() + "-" + x);
					}
					d = userslimService.calculateTaskCalories();
					y = Double.parseDouble(Decimaltool.getDoublePrecision(a + b
							+ d - c));
				} else {
					y = 0;
				}
				series.add(y);
			}
			dataset.addSeries(series.toXYSeries());
		}

		return dataset;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer,
			double xMin, double xMax, double yMin, double yMax, int axesColor) {
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setXLabels(0);// 将x轴可见区域自动分为n份

		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setYLabels(20);

		renderer.setAxesColor(axesColor);

		for (int i = 1; i < xMax; i++) {
			renderer.addXTextLabel(i, i + "月");
		}
	}

	private XYMultipleSeriesRenderer buildLineRenderer(String[] titles,
			int color) {
		int title_size = 45, value_size = 25, legend_size = 40;
		float point_size = 10f;
		int distance = 10, width = 3;
		int length = titles.length;

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setChartSettings(renderer, 0, 13, 0, 1000, Color.RED);

		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false, false);// 设置是否可以缩放
		renderer.setPanEnabled(false, false);
		renderer.setFitLegend(true);
		renderer.setMargins(new int[] { 30, 25, 35, 25 });// 上,左,下,右

		renderer.setChartTitleTextSize(title_size); // 设置图表标题文本大小
		renderer.setLabelsTextSize(value_size); // 设置轴标签文本大小
		renderer.setLegendTextSize(legend_size); // 设置图例文本大小
		renderer.setPointSize(point_size);// 设置点的大小
		renderer.setMarginsColor(Color.BLACK);

		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();// (类似于一条线对象)
			renderer.setChartTitle(titles[i] + "每月数据");
			r.setChartValuesTextAlign(Align.RIGHT);
			r.setChartValuesTextSize(value_size);
			r.setDisplayChartValues(true);
			r.setColor(color);
			r.setPointStyle(PointStyle.DIAMOND);// 设置点的样式
			r.setFillPoints(true);// 填充点（显示的点是空心还是实心）
			r.setChartValuesSpacing(distance);// 显示的点的值与图的距离
			r.setLineWidth(width);// 设置线宽
			renderer.addSeriesRenderer(r);
		}

		return renderer;
	}
}