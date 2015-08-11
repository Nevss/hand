package com.darly.widght;



import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
* @ClassName: MyListView
* @Description: TODO(重写ListView，主要功能重写点击事件)
* @author 张宇辉 zhangyuhui@octmami.com
* @date 2014年11月15日 上午10:54:13
*
*/ 
public class MyListView extends BaseListView {
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	public MyListView(Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (distanceY != 0 && distanceX != 0) {

			}
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
}