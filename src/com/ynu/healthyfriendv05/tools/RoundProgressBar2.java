package com.ynu.healthyfriendv05.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RoundProgressBar2 extends View {
	/** 最外围的颜色值 */
	private int outRoundColor = Color.argb(60, 255, 255, 255);
	/** 中心圆的颜色值 */
	private int centerRoundColor = Color.argb(255, 255, 255, 255);
	/** 进度的颜色 */
	private int progressRoundColor = Color.argb(255, 255, 255, 255);
	/** 进度的背景颜色 */
	private int progressRoundBgColor = Color.argb(100, 255, 255, 255);
	/** 进度条的宽度 */
	private int progressWidth = 5;

	private int[] colors = { Color.WHITE, Color.RED, Color.rgb(118, 181, 66) };
	/*** 字体颜色 */
	private String explanation = "index";
	private int textColor = colors[2];
	private float pencentTextSize = 65;

	private int width, height;
	private int paddingX;

	private float progress = 0.5f;
	private float max = 1.0f;

	private Paint paint = new Paint();

	public RoundProgressBar2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RoundProgressBar2(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundProgressBar2(Context context) {
		super(context);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = getWidth();
		height = getHeight();

		if (width > height) {
			paddingX = (width - height) / 2;
			width = height;
		}
		paint.setAntiAlias(true); // 消除锯齿
		paint.setStyle(Style.FILL);
		paint.setColor(outRoundColor);
		RectF oval = new RectF(new Rect(paddingX, 0, width + paddingX, height));
		canvas.drawArc(oval, 0, 360, true, paint);

		int halfWidth = width / 6;
		paint.setStrokeWidth(progressWidth);
		paint.setColor(progressRoundBgColor);
		paint.setStyle(Style.STROKE);
		oval = new RectF(new Rect(halfWidth + paddingX, halfWidth, halfWidth
				* 5 + paddingX, halfWidth * 5));
		canvas.drawArc(oval, 0, 360, false, paint);

		paint.setColor(progressRoundColor);
		canvas.drawArc(oval, 90, 360 * progress / (max * 100), false, paint);

		halfWidth = width / 4;
		paint.setStyle(Style.FILL);
		paint.setColor(centerRoundColor);
		oval = new RectF(new Rect(halfWidth + paddingX, halfWidth, halfWidth
				* 3 + paddingX, halfWidth * 3));
		canvas.drawArc(oval, 0, 360, false, paint);

		paint.reset();
		paint.setTextSize(pencentTextSize);
		paint.setColor(textColor);
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Align.CENTER);
		String number = String.valueOf(progress / max);
		canvas.drawText(number, width / 2 + paddingX, height / 2
				+ pencentTextSize / 3, paint);

		// float textWidth = paint.measureText(number);
		// paint.setTextSize(pencentTextSize / 2);
		// canvas.drawText("", width / 2 + paddingX + textWidth / 2 + 5, height
		// / 2 - pencentTextSize / 8, paint);

		paint.setTextSize(pencentTextSize / 2);
		canvas.drawText(explanation, width / 2 + paddingX, height / 2
				+ halfWidth / 4 * 3, paint);
	}

	public void setMax(float max) {
		this.max = max;
	}

	public void setProgress(float progress) {
		this.progress = progress;
		invalidate();
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public float getMax() {
		return max;
	}

	public float getProgress() {
		return progress;
	}

	public String getExplanation() {
		return explanation;
	}
}