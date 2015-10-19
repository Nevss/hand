/**
 * 上午11:52:26
 * @author Zhangyuhui
 * TestRestore.java
 * TODO
 */
package com.darly.activities.widget.intel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.darly.activities.common.Literal;

/**
 * @author Zhangyuhui TestRestore 上午11:52:26 TODO 正方形回形图
 */
public class RectRestoreSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {
	/**
	 * TODO绘画使用的画笔
	 */
	private Paint paint;

	private SurfaceHolder holder;

	private boolean flag = true;

	private int time = 300;

	private Rect rect;

	private int cout;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 *            下午12:01:43
	 * @author Zhangyuhui RectRestoreSurfaceView.java TODO
	 */
	public RectRestoreSurfaceView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 *            下午12:01:36
	 * @author Zhangyuhui RectRestoreSurfaceView.java TODO
	 */
	public RectRestoreSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 *            上午11:52:29
	 * @author Zhangyuhui TestRestore.java TODO
	 */
	public RectRestoreSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	/**
	 * @param context
	 *            上午11:53:43
	 * @author Zhangyuhui TestRestore.java TODO
	 */
	private void init(Context context) {
		// TODO Auto-generated method stub
		paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		/* 设置paint的　style　为STROKE：空心 */
		paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽度 */
		paint.setStrokeWidth(1);
		holder = this.getHolder();
		holder.addCallback(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		rect = new Rect(0, 0, Literal.width, Literal.width);
		new Thread(this).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
	 * SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag = false;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
		invalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			Draw();
		}
	}

	/**
	 * 
	 * 上午11:57:44
	 * 
	 * @author Zhangyuhui TestRestore.java TODO
	 */
	private void Draw() {
		// TODO Auto-generated method stub
		Canvas canvas = null;
		synchronized (holder) {
			try {
				canvas = holder.lockCanvas(rect);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
				for (int i = 0; i < time; i++) {
					if (i == time - 1) {
						cout++;
					}
					if (cout > time) {
						cout = 0;
					}
					switch ((cout + i) % 5) {
					// 色彩进行替换状态，则Key值则要不断变化。
					case 0:
						paint.setColor(Color.WHITE);
						break;
					case 1:
						paint.setColor(Color.GRAY);
						break;
					case 2:
						paint.setColor(Color.RED);
						break;
					case 3:
						paint.setColor(Color.GREEN);
						break;
					case 4:
						paint.setColor(Color.BLUE);
						break;

					default:
						break;
					}
					canvas.save();
					float fraction = (float) i * 10 / time;
					// 将画布以正方形中心进行缩放
					canvas.scale(fraction, fraction, Literal.width / 2,
							Literal.width / 2);
					canvas.drawRect(rect, paint);
					// 画布回滚
					canvas.restore();
				}
				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(time);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
