/**
 * 上午10:58:40
 * @author zhangyh2
 * $
 * LuckDrawView.java
 * TODO
 */
package com.darly.oop.ui.luckdraw;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.darly.oop.R;
import com.darly.oop.base.APPEnum;
import com.darly.oop.model.LuckDraw;

/**
 * @author zhangyh2 LuckDrawView $ 上午10:58:40 TODO
 */
public class LuckDrawView extends View {

	public LuckDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LuckDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 上午11:06:12 TODO 奖品信息
	 */
	private List<LuckDraw> prize;

	/**
	 * 上午11:09:29 TODO 每个奖品的角度
	 */
	private float degree;

	private Paint paint;

	/**
	 * 下午1:38:27 TODO 文字的位置
	 */
	private StaticLayout layout;

	/**
	 * 下午1:40:21 TODO 文字画笔
	 */
	private TextPaint textPaints;

	public LuckDrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * 
	 * 上午11:10:03
	 * 
	 * @author zhangyh2 LuckDrawView.java TODO
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		paint = new Paint();
		paint.setTextSize(22);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);

		textPaints = new TextPaint();
		/* 设置paint的外框宽度 */
		textPaints.setStrokeWidth(1);
		textPaints.setColor(Color.RED);
		textPaints.setTextSize(24);
		if (prize == null) {
			return;
		}
		degree = 360 / prize.size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (prize == null) {
			return;
		}
		for (int i = 0; i < prize.size(); i++) {
			// 画文字
			canvas.save();
			canvas.translate(APPEnum.WIDTH.getLen() / 4 - 50, 0);
			layout = new StaticLayout(prize.get(i).getName(), textPaints, 100,
					Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
			layout.draw(canvas);
			// 画线
			canvas.drawLine(APPEnum.WIDTH.getLen() / 8, 5,
					APPEnum.WIDTH.getLen() / 8 - 40,
					APPEnum.WIDTH.getLen() / 4, paint);
			canvas.restore();
			canvas.rotate(degree, APPEnum.WIDTH.getLen() / 4,
					APPEnum.WIDTH.getLen() / 4);
		}
		// 画图
		src = new Rect(0, 0, APPEnum.WIDTH.getLen() / 4,
				APPEnum.WIDTH.getLen() / 4);
		dst = new Rect(APPEnum.WIDTH.getLen() / 8, APPEnum.WIDTH.getLen() / 8,
				APPEnum.WIDTH.getLen() / 4 + APPEnum.WIDTH.getLen() / 8,
				APPEnum.WIDTH.getLen() / 4 + APPEnum.WIDTH.getLen() / 8);
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_luck_draw_circle), src, dst, paint);
		// 画圆
		canvas.drawCircle(APPEnum.WIDTH.getLen() / 4,
				APPEnum.WIDTH.getLen() / 4, APPEnum.WIDTH.getLen() / 4, paint);

		super.onDraw(canvas);
	}

	private Rect src, dst;

	public void setPrize(List<LuckDraw> prize) {
		this.prize = prize;
		degree = 360 / prize.size();
		invalidate();
	}

}
