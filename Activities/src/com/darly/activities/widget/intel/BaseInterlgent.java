package com.darly.activities.widget.intel;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.darly.activities.common.IAPoisDataConfig;
import com.darly.activities.common.Literal;
import com.darly.activities.model.RoomInfor;
import com.darly.activities.ui.R;

/**
 * 2015年9月15日 BaseInterlgent.java com.darly.interlgent.widget
 * 
 * @auther Darly Fronch 上午9:24:49 BaseInterlgent TODO
 *         基础类，主要功能为绘制底部图层，并根据传输的数据变更色彩。
 */
public class BaseInterlgent extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	/**
	 * TODO点阵集合。
	 */
	private ArrayList<RoomInfor> pointList;

	/**
	 * TODO绘画使用的画笔
	 */
	private Paint paint;

	private SurfaceHolder holder;

	private boolean flag = true;

	private int cout;

	/**
	 * 上午8:53:52
	 * 
	 * @author Zhangyuhui BaseInterlgent.java TODO 背景图片
	 */
	private Bitmap backGroud;

	/**
	 * 上午8:54:22
	 * 
	 * @author Zhangyuhui BaseInterlgent.java TODO 下一项图片。
	 */
	private Bitmap nextImage;

	private int left;
	private int top;

	/**
	 * 下午4:15:37 TODO 缩放比率
	 */
	private float rate = 1;

	private int sleepTime = 5;

	private float trasX;
	private float trasY;

	public BaseInterlgent(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}

	public BaseInterlgent(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public BaseInterlgent(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * @auther Darly Fronch 2015 上午9:28:18 TODO初始化方法
	 */
	private void init() {
		// TODO Auto-generated method stub
		paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		/* 设置paint的　style　为STROKE：空心 */
		paint.setStyle(Paint.Style.FILL);
		/* 设置paint的外框宽度 */
		paint.setStrokeWidth(2);
		holder = this.getHolder();
		holder.addCallback(this);
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
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		new Thread(this).start();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			
			if (pointList != null) {
				onDraws();
			}
			
		}
	}

	/**
	 * 
	 * 下午2:39:48
	 * 
	 * @author Zhangyuhui BaseInterlgent.java TODO 绘制方法主题。
	 */
	private void onDraws() {
		synchronized (holder) {
			try {
				Canvas canvas = holder.lockCanvas();
				if (canvas == null) {
					holder.unlockCanvasAndPost(canvas);
					return;
				}
				canvas.translate(trasX, trasY);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
				// 保存画布状态（图片问题之所以没有解决的根本问题就是没有深入研究Canvas，canvas.save();方法可以保存当前画布状态。通过回滚，回滚到保存的状态。）
				canvas.save();
				canvas.scale(rate, rate);
				for (int i = 0, length = pointList.size(); i < length; i++) {
					paintView(pointList.get(i).getRoomPoint(), canvas, paint,
							pointList.get(i).getRoomStauts());
				}
				// 画布状态回滚
				canvas.restore();
				canvas.save();
				if (backGroud != null) {
					Bitmap back = InterlgentUtil.zoomImage(backGroud,
							Literal.width * rate, Literal.width
									* IAPoisDataConfig.babaibanh * rate
									/ IAPoisDataConfig.babaibanw);
					canvas.drawBitmap(back, 0, 0, null);
				}
				canvas.restore();
				if (nextImage != null) {
					canvas.scale(rate, rate);
					canvas.drawBitmap(nextImage, left, top, null);
				}
				// 画布状态回滚
				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * @param canvas
	 * @param _paint
	 * @param integer
	 * @auther Darly Fronch 2015 上午9:33:27 TODO开始绘画一个一个的图像，调用一次，绘制一个
	 */
	private void paintView(ArrayList<Point> points, Canvas canvas,
			Paint _paint, int integer) {
		// TODO Auto-generated method stub
		if (points == null || canvas == null || _paint == null) {
			return;
		}
		switch (integer) {
		case 0:
			// 为体检
			_paint.setColor(Color.YELLOW);
			break;
		case 1:
			// 已体检
			_paint.setColor(Color.GREEN);
			break;
		case 2:
			// 下一项
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (cout < 100) {
				cout++;
			} else {
				cout = 0;
			}
			int alpa = cout % 3;
			switch (alpa) {
			case 0:
				_paint.setColor(getResources().getColor(R.color.color_1));
				break;
			case 1:
				_paint.setColor(getResources().getColor(R.color.color_2));
				break;
			case 2:
				_paint.setColor(getResources().getColor(R.color.color_3));
				break;
			}
			break;
		default:
			_paint.setColor(Color.GREEN);
			break;
		}
		if (points != null && points.size() > 0) {

			/**
			 * @auther Darly Fronch TODO
			 *         由于以前使用的是一个Path，考虑到使用同一个Paint进行渲染。但没有考虑到同一个Path
			 *         ，变更为各自的Path后。可以满足需求。进行不同的色彩渲染。
			 */
			Path path = new Path();
			path.moveTo(points.get(0).x, points.get(0).y);
			for (int i = 0, length = points.size(); i < length; i++) {
				path.lineTo(points.get(i).x, points.get(i).y);
			}
			path.close();
			canvas.drawPath(path, _paint);
		}
	}

	/**
	 * @auther Darly Fronch 2015 上午11:19:26 TODO 重绘刷新方法
	 */
	public void ReDraw(ArrayList<RoomInfor> pointList) {
		this.pointList = pointList;
	}

	public void setBackGroud(Bitmap backGroud) {
		this.backGroud = backGroud;
	}

	public void setNextImage(Bitmap nextImage, int left, int top) {
		this.left = left;
		this.top = top;
		this.nextImage = nextImage;
	}

	/**
	 * @param rate
	 *            the rate to set 设置缩放比率。
	 */
	public void setRate(float rate) {
		this.rate = rate;
	}

	/**
	 * @param flag
	 *            the flag to set 关闭SurfaceView
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * @param trasX
	 * @param trasY
	 *            上午10:17:39
	 * @author Zhangyuhui BaseInterlgent.java TODO 设置平移距离。
	 */
	public void setTranslation(float trasX, float trasY) {
		this.trasX = trasX;
		this.trasY = trasY;
	}

}
