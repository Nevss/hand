/**
 * 上午9:40:51
 * @author Zhangyuhui
 * RotateAcitvity.java
 * TODO
 */
package com.darly.activities.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;

/**
 * @author Zhangyuhui RotateAcitvity 上午9:40:51 TODO 测试canvas.save();
 *         canvas.restore(); canvas.rotate(90, Literal.width/2,
 *         Literal.width/2);功能是否强悍
 */
public class RotateAcitvity extends BaseActivity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(new RotateView(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.darly.activities.base.BaseActivity#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub

	}

	class RotateView extends View {

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
		 *            上午9:43:57
		 * @author Zhangyuhui RotateAcitvity.java TODO
		 */
		public RotateView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			init(context);
		}

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
			LogApp.i(xline + "-" + yline);

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
			LogApp.i(layoutw + "+" + layouth);

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
}
