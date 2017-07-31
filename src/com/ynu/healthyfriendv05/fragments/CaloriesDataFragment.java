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
	private String[] titles = { "��·��" };

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
			// �½�һ��ϵ�У�������
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
		renderer.setXLabels(0);// ��x��ɼ������Զ���Ϊn��

		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setYLabels(20);

		renderer.setAxesColor(axesColor);

		for (int i = 1; i < xMax; i++) {
			renderer.addXTextLabel(i, i + "��");
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
		renderer.setZoomEnabled(false, false);// �����Ƿ��������
		renderer.setPanEnabled(false, false);
		renderer.setFitLegend(true);
		renderer.setMargins(new int[] { 30, 25, 35, 25 });// ��,��,��,��

		renderer.setChartTitleTextSize(title_size); // ����ͼ������ı���С
		renderer.setLabelsTextSize(value_size); // �������ǩ�ı���С
		renderer.setLegendTextSize(legend_size); // ����ͼ���ı���С
		renderer.setPointSize(point_size);// ���õ�Ĵ�С
		renderer.setMarginsColor(Color.BLACK);

		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();// (������һ���߶���)
			renderer.setChartTitle(titles[i] + "ÿ������");
			r.setChartValuesTextAlign(Align.RIGHT);
			r.setChartValuesTextSize(value_size);
			r.setDisplayChartValues(true);
			r.setColor(color);
			r.setPointStyle(PointStyle.DIAMOND);// ���õ����ʽ
			r.setFillPoints(true);// ���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�
			r.setChartValuesSpacing(distance);// ��ʾ�ĵ��ֵ��ͼ�ľ���
			r.setLineWidth(width);// �����߿�
			renderer.addSeriesRenderer(r);
		}

		return renderer;
	}
}