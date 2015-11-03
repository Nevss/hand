/**
 * 上午11:28:52
 * @author Zhangyuhui
 * $
 * RotateView.java
 * TODO
 */
package com.darly.activities.widget.rotateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.darly.activities.common.Literal;
import com.darly.activities.common.LogFileHelper;

/**
 * @author Zhangyuhui RotateView $ 上午11:28:52 TODO 屏幕自定义组件旋转功能。
 */
public class RotateView extends View {
	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 *            上午11:41:42
	 * @author Zhangyuhui RotateView.java TODO
	 */
	public RotateView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 *            上午11:41:36
	 * @author Zhangyuhui RotateView.java TODO
	 */
	public RotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 *            上午11:41:29
	 * @author Zhangyuhui RotateView.java TODO
	 */
	public RotateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private String TAG = "RotateView";

	private Paint paint;

	private Paint rectPaint;

	private Rect rect;

	private String text = "世界很左右漆黑当爱已成往事闹吸说服力恨消费者公安局卡拉季奇";
	// 矩形框的中心位置。
	private int xline;
	private int yline;
	// 计算Layout的中心位置
	private int layoutw;
	private int layouth;

	private TextPaint textPaints;

	private StaticLayout layout;

	/**
	 * @param context
	 *            上午9:45:09
	 * @author Zhangyuhui RotateAcitvity.java TODO
	 */
	private void init(Context context) {
		// TODO Auto-generated method stub
		paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		/* 设置paint的　style　为STROKE：空心 */
		paint.setStyle(Paint.Style.FILL);
		/* 设置paint的外框宽度 */
		paint.setStrokeWidth(2);
		paint.setColor(Color.DKGRAY);

		rectPaint = new Paint();
		/* 去锯齿 */
		rectPaint.setAntiAlias(true);
		/* 设置paint的　style　为STROKE：空心 */
		rectPaint.setStyle(Paint.Style.FILL);
		/* 设置paint的外框宽度 */
		rectPaint.setStrokeWidth(2);
		rectPaint.setColor(Color.WHITE);

		rect = new Rect(10, 10, 350, 200);
		// 矩形框的中心位置。
		xline = rect.width() / 2 + rect.left;
		yline = rect.height() / 2 + rect.top;
		LogFileHelper.getInstance().i(TAG, xline + "-" + yline);
		textPaints = new TextPaint();
		/* 设置paint的外框宽度 */
		textPaints.setStrokeWidth(1);
		textPaints.setColor(Color.RED);
		textPaints.setTextSize(24);
		// 带换行操作的绘制文字。
		layout = new StaticLayout(text, textPaints, rect.width(),
				Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
		// 计算Layout的中心位置
		layoutw = layout.getWidth() / 2;
		layouth = layout.getHeight() / 2;
		LogFileHelper.getInstance().i(TAG, layoutw + "+" + layouth);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.save();
		drawView(canvas);
		canvas.restore();
		canvas.rotate(90, Literal.width / 2, Literal.width / 2);
		canvas.save();
		drawView(canvas);
		canvas.restore();
		canvas.rotate(90, Literal.height / 2, Literal.height / 2);
		canvas.save();
		drawView(canvas);
		canvas.restore();
		canvas.rotate(190, Literal.width / 2, Literal.width / 2);
		drawView(canvas);
	}

	/**
	 * @param canvas
	 *            上午11:32:10
	 * @author Zhangyuhui RotateAcitvity.java TODO
	 */
	private void drawView(Canvas canvas) {
		canvas.drawRect(rect, paint);
		canvas.drawLine(185, 10, 185, 200, textPaints);
		canvas.drawLine(10, 105, 350, 105, textPaints);
		canvas.translate(xline - layoutw, yline - layouth);
		layout.draw(canvas);
	}
}
