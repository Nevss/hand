/**
 * 上午9:29:33
 * @author Zhangyuhui
 * a.java
 * TODO
 */
package com.darly.activities.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.darly.activities.common.LogFileHelper;

/**
 * @author Zhangyuhui a 上午9:29:33 TODO
 */
public class DisplayActivity extends Activity {
	private static final String TAG = "DisplayActivity";
	private SurfaceView videoView;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private boolean flag = true;
	private int screenW, screenH;
	private Bitmap bmpIcon;
	// 记录两个触屏点的坐标
	private int x1, x2, y1, y2;
	// 倍率
	private float rate = 1;
	// 记录上次的�?�?
	private float oldRate = 1;
	// 记录第一次触屏时线段的长�?
	private float oldLineDistance;
	// 判定是否头次多指触点屏幕
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_play);
		videoView = (SurfaceView) findViewById(R.id.videoView);
		sfh = videoView.getHolder();
		sfh.addCallback(new DisplaySurfaceView());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;
		screenH = (dm.heightPixels) / 2;
		LogFileHelper.getInstance().i(TAG, "onCreate is run");
	}

	class DisplaySurfaceView implements SurfaceHolder.Callback {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			new ImageThread().start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			flag = false;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isFirst = true;
			oldRate = rate;
		} else {
			if (event.getPointerCount() > 1) {
				x1 = (int) event.getX(0);
				y1 = (int) event.getY(0);
				x2 = (int) event.getX(1);
				y2 = (int) event.getY(1);
				if (event.getPointerCount() == 2) {
					if (isFirst) {
						// 得到第一次触屏时线段的长�?
						oldLineDistance = (float) Math.sqrt(Math.pow(
								event.getX(1) - event.getX(0), 2)
								+ Math.pow(event.getY(1) - event.getY(0), 2));
						isFirst = false;
					} else {
						// 得到非第�?��触屏时线段的长度
						float newLineDistance = (float) Math.sqrt(Math.pow(
								event.getX(1) - event.getX(0), 2)
								+ Math.pow(event.getY(1) - event.getY(0), 2));
						// 获取本次的缩放比�?
						rate = oldRate * newLineDistance / oldLineDistance;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	class ImageThread extends Thread {
		@Override
		public void run() {
			while (flag) {
				long start = System.currentTimeMillis();
				myDraw();
				long end = System.currentTimeMillis();
				try {
					if (end - start < 50) {
						Thread.sleep(50 - (end - start));
					}
				} catch (InterruptedException e) {
					LogFileHelper.getInstance().e(TAG, e.getMessage());
				}
			}
		}
	}

	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				canvas.save();
				// 缩放画布(以图片中心点进行缩放，XY轴缩放比例相�?
				bmpIcon = BitmapFactory.decodeResource(this.getResources(),
						R.drawable.add);
				canvas.scale(rate, rate, screenW / 2, screenH / 2);
				int width = screenW / 2 - bmpIcon.getWidth() / 2;
				int height = screenH / 2 - bmpIcon.getHeight() / 2;
				// 绘制位图icon
				canvas.drawBitmap(bmpIcon, width, height, paint);
				canvas.restore();
				// 便于观察，这里绘制两个触点时形成的线�?
				canvas.drawLine(x1, y1, x2, y2, paint);
				sfh.unlockCanvasAndPost(canvas);
			}
		} catch (Exception e) {
			LogFileHelper.getInstance().e(TAG, e.getMessage());
		} finally {
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}