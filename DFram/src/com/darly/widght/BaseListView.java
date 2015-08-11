package com.darly.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * @ClassName: BaseListView
 * @Description: TODO(ListView 上下拉动出现间距)
 * @author 张宇辉 zhangyuhui@octmami.com
 * @date 2014年11月24日 下午5:13:52
 *
 */
public class BaseListView extends ListView {
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 150;

	private Context mContext;
	public int mMaxYOverscrollDistance;

	public BaseListView(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	public BaseListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView() {

		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;
		if (density > 0) {
			mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
		} else {
			mMaxYOverscrollDistance = 2 * MAX_Y_OVERSCROLL_DISTANCE;
		}

	}

//	@Override
//	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
//			int scrollY, int scrollRangeX, int scrollRangeY,
//			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
//				scrollRangeX, scrollRangeY, maxOverScrollX,
//				mMaxYOverscrollDistance, isTouchEvent);
//	}

}
