package com.ynu.healthyfriendv05.tools;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleBar extends View {

	private RectF mColorWheelRectangle = new RectF();
	private Paint mDefaultWheelPaint;
	private Paint mColorWheelPaint;
	private Paint mColorWheelPaintCentre;
	private Paint mTextP, mTextnum, mTextch;
	private float circleStrokeWidth;
	private float mSweepAnglePer;
	private float mPercent;
	private int index, indexnow;
	private float pressExtraStrokeWidth;
	private BarAnimation anim;
	private int maxIndex = 100;// Ĭ�������
	private float mPercent_y, index_y, Text_y;
	private DecimalFormat fnum = new DecimalFormat("#.0");// ��ʽΪ����С�����һλ

	public CircleBar(Context context) {
		super(context);
		init(null, 0);
	}

	public CircleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public CircleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {

		mColorWheelPaint = new Paint();
		mColorWheelPaint.setColor(Color.rgb(249, 135, 49));
		mColorWheelPaint.setStyle(Paint.Style.STROKE);// ����
		mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);// Բ�ǻ���
		mColorWheelPaint.setAntiAlias(true);// ȥ���

		mColorWheelPaintCentre = new Paint();
		mColorWheelPaintCentre.setColor(Color.rgb(250, 250, 250));
		mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
		mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
		mColorWheelPaintCentre.setAntiAlias(true);

		mDefaultWheelPaint = new Paint();
		mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
		mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
		mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
		mDefaultWheelPaint.setAntiAlias(true);

		mTextP = new Paint();
		mTextP.setAntiAlias(true);
		mTextP.setColor(Color.rgb(249, 135, 49));

		mTextnum = new Paint();
		mTextnum.setAntiAlias(true);
		mTextnum.setColor(Color.BLACK);

		mTextch = new Paint();
		mTextch.setAntiAlias(true);
		mTextch.setColor(Color.BLACK);

		anim = new BarAnimation();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(mColorWheelRectangle, 0, 359, false, mDefaultWheelPaint);
		canvas.drawArc(mColorWheelRectangle, 0, 359, false,
				mColorWheelPaintCentre);
		canvas.drawArc(mColorWheelRectangle, 90, mSweepAnglePer, false,
				mColorWheelPaint);
		canvas.drawText(mPercent + "%", mColorWheelRectangle.centerX()
				- (mTextP.measureText(String.valueOf(mPercent) + "%") / 2),
				mPercent_y, mTextP);
		canvas.drawText(indexnow + "", mColorWheelRectangle.centerX()
				- (mTextnum.measureText(String.valueOf(indexnow)) / 2),
				index_y, mTextnum);
		canvas.drawText(
				"����ָ��",
				mColorWheelRectangle.centerX()
						- (mTextch.measureText(String.valueOf("����ָ��")) / 2),
				Text_y, mTextch);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int min = Math.min(width, height);// ��ȡView��̱ߵĳ���
		setMeasuredDimension(min, min);// ǿ�Ƹ�ViewΪ����̱�Ϊ���ȵ�������
		circleStrokeWidth = Textscale(40, min);// Բ���Ŀ��
		pressExtraStrokeWidth = Textscale(2, min);// Բ������εľ���
		mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth,
				circleStrokeWidth + pressExtraStrokeWidth, min
						- circleStrokeWidth - pressExtraStrokeWidth, min
						- circleStrokeWidth - pressExtraStrokeWidth);// ���þ���
		mTextP.setTextSize(Textscale(80, min));
		mTextnum.setTextSize(Textscale(160, min));
		mTextch.setTextSize(Textscale(50, min));
		mPercent_y = Textscale(190, min);
		index_y = Textscale(330, min);
		Text_y = Textscale(400, min);
		mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
		mColorWheelPaintCentre.setStrokeWidth(circleStrokeWidth);
		mDefaultWheelPaint
				.setStrokeWidth(circleStrokeWidth - Textscale(2, min));
		mDefaultWheelPaint.setShadowLayer(Textscale(10, min), 0, 0,
				Color.rgb(127, 127, 127));// ������Ӱ
	}

	/**
	 * ����������
	 * 
	 * @author Administrator
	 * 
	 */
	public class BarAnimation extends Animation {
		public BarAnimation() {

		}

		/**
		 * ÿ��ϵͳ�����������ʱ�� �ı�mSweepAnglePer��mPercent��stepnumbernow��ֵ��
		 * Ȼ�����postInvalidate()��ͣ�Ļ���view��
		 */
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				mPercent = Float.parseFloat(fnum.format(interpolatedTime
						* index * 100f / maxIndex));// ������ֵ�������뱣��һλС��
				mSweepAnglePer = interpolatedTime * index * 360 / maxIndex;
				indexnow = (int) (interpolatedTime * index);
			} else {
				mPercent = Float.parseFloat(fnum
						.format(index * 100f / maxIndex));// ������ֵ�������뱣��һλС��
				mSweepAnglePer = index * 360 / maxIndex;
				indexnow = index;
			}
			postInvalidate();
		}
	}

	/**
	 * ���ݿؼ��Ĵ�С�ı����λ�õı���
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public float Textscale(float n, float m) {
		return n / 500 * m;
	}

	/**
	 * ����ָ��������һȦ����ʱ��
	 * 
	 * @param stepnumber
	 * @param time
	 */
	public void update(int index, int time) {
		this.index = index;
		anim.setDuration(time);
		// setAnimationTime(time);
		this.startAnimation(anim);
	}

	/**
	 * ����ÿ��������
	 * 
	 * @param Maxstepnumber
	 */
	public void setMaxstepnumber(int maxIndex) {
		this.maxIndex = maxIndex;
	}

	/**
	 * ���ý�������ɫ
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setColor(int red, int green, int blue) {
		mColorWheelPaint.setColor(Color.rgb(red, green, blue));
	}

	/**
	 * ���ö���ʱ��
	 * 
	 * @param time
	 */
	public void setAnimationTime(int time) {
		anim.setDuration(time * index / maxIndex);// ���ձ������ö���ִ��ʱ��
	}
}