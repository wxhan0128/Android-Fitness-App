package com.ynu.healthyfriendv05.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ynu.healthyfriendv05.R;

public class RoundProgressBar1 extends View {
	private Paint paint;
	private int roundColor;
	private int roundProgressColor;
	private int textColor;
	private float textSize;
	private float roundWidth;
	private int max;
	private int progress;
	private boolean textIsDisplayable;
	private int style;
	private String explanation;
	public static final int STROKE = 0;
	public static final int FILL = 1;

	public RoundProgressBar1(Context context) {
		this(context, null);
	}

	public RoundProgressBar1(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundProgressBar1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		paint = new Paint();

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar1);

		// ��ȡ�Զ������Ժ�Ĭ��ֵ
		roundColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar1_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar1_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar1_textColor, Color.GREEN);
		textSize = mTypedArray.getDimension(
				R.styleable.RoundProgressBar1_textSize, 15);
		roundWidth = mTypedArray.getDimension(
				R.styleable.RoundProgressBar1_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar1_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.RoundProgressBar1_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar1_style, 0);

		mTypedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
		int radius = (int) (centre - roundWidth / 2); // Բ���İ뾶
		paint.setColor(roundColor); // ����Բ������ɫ
		paint.setStyle(Paint.Style.STROKE); // ���ÿ���
		paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
		paint.setAntiAlias(true); // �������
		canvas.drawCircle(centre, centre, radius, paint); // ����Բ��

		Log.e("log", centre + "");

		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // ��������
		int percent = (int) (((float) progress / (float) max) * 100); // �м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0
		float textWidth = paint.measureText(percent + "%"); // ���������ȣ�������Ҫ��������Ŀ��������Բ���м�
		float explanationWidth = paint.measureText(explanation);

		if (textIsDisplayable && percent != 0 && style == STROKE) {
			canvas.drawText(percent + "%", centre - textWidth / 2, centre
					+ textSize / 2, paint); // �������Ȱٷֱ�
			canvas.drawText(explanation, centre - explanationWidth / 2, centre
					- textSize / 2, paint); // �������Ȱٷֱ�
		}

		// ���ý�����ʵ�Ļ��ǿ���
		paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
		paint.setColor(roundProgressColor); // ���ý��ȵ���ɫ
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // ���ݽ��Ȼ�Բ��
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // ���ݽ��Ȼ�Բ��
			break;
		}
		}
	}

	public synchronized int getMax() {
		return max;
	}

	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	public synchronized int getProgress() {
		return progress;
	}

	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
	}

	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}
}