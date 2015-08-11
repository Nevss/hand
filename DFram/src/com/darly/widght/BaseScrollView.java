package com.darly.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.darly.listen.ScrollViewListener;

/**
 * @ClassName: BaseListView
 * @Description: TODO(ListView 上下拉动出现间距)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月24日 下午5:13:52
 *
 */
public class BaseScrollView extends ScrollView {
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

	private ScrollViewListener scrollViewListener = null;

	private boolean isScrollable = true;

	private Context mContext;
	public int mMaxYOverscrollDistance;

	public BaseScrollView(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}

	public BaseScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	public BaseScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView() {

		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;
		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	// @Override
	// protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
	// int scrollY, int scrollRangeX, int scrollRangeY,
	// int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
	// return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
	// scrollRangeX, scrollRangeY, maxOverScrollX,
	// mMaxYOverscrollDistance, isTouchEvent);
	// }

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public void setScrollable(boolean isScrollable) {
		this.isScrollable = isScrollable;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (isScrollable) {
			return super.onTouchEvent(ev);
		} else {
			return false;
		}

	}

}
