package com.ynu.healthyfriendv05.fragments;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

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
import com.ynu.healthyfriendv05.tools.Datetool;

public class StepDataFragment extends Fragment {
	private Context context;
	private LinearLayout chart;

	private RecorderService recorderService;
	private String[] titles = { "步行" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.stepdatafragment, container,
				false);
		this.context = getActivity();
		chart = (LinearLayout) view.findViewById(R.id.run_chart);

		GraphicalView chart_view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, 0),
				buildBarRenderer(titles, Color.GREEN), Type.DEFAULT);
		chart.addView(chart_view);

		return view;
	}

	private XYMultipleSeriesDataset buildBarDataset(String[] titles, int index) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		recorderService = new RecorderService(context);
		int length = titles.length;

		for (int i = 0; i < length; i++) {
			// 新建一个系列（线条）
			CategorySeries series = new CategorySeries(titles[i]);
			for (int x = 1; x <= 12; x++) {
				int y = 0;
				if (x < 10) {
					y = recorderService.findTotalGradeByMonth(Datetool
							.getCurrentYear() + "-0" + x)[index];
				} else {
					y = recorderService.findTotalGradeByMonth(Datetool
							.getCurrentYear() + "-" + x)[index];
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

	private XYMultipleSeriesRenderer buildBarRenderer(String[] titles, int color) {
		int title_size = 45, value_size = 25, legend_size = 40;
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
		renderer.setBarSpacing(1f);// 设置柱状的间距
		renderer.setMarginsColor(Color.BLACK);

		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setChartValuesTextAlign(Align.RIGHT);
			r.setChartValuesTextSize(value_size);
			r.setDisplayChartValues(true);
			r.setColor(color);
			renderer.addSeriesRenderer(r);
			renderer.setChartTitle(titles[i] + "每月数据");
		}

		return renderer;
	}
}