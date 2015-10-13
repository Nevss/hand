/**
 * 上午9:40:51
 * @author Zhangyuhui
 * RotateAcitvity.java
 * TODO
 */
package com.darly.activities.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.darly.activities.base.BaseActivity;
import com.darly.activities.common.Literal;

/**
 * @author Zhangyuhui
 * RotateAcitvity
 * 上午9:40:51
 * TODO
 */
public class RotateAcitvity extends BaseActivity{

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#initView(android.os.Bundle)
	 */
	@Override
	public void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(new RotateView(this));
	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#refreshGet(java.lang.Object)
	 */
	@Override
	public void refreshGet(Object object) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.darly.activities.base.BaseActivity#refreshPost(java.lang.Object)
	 */
	@Override
	public void refreshPost(Object object) {
		// TODO Auto-generated method stub
		
	}

	
	class RotateView extends View{

		private Paint paint;
		
		Rect rect;
		
		/**
		 * @param context
		 * 上午9:43:57
		 * @author Zhangyuhui
		 * RotateAcitvity.java
		 * TODO
		 */
		public RotateView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			init(context);
		}

		/**
		 * @param context
		 * 上午9:45:09
		 * @author Zhangyuhui
		 * RotateAcitvity.java
		 * TODO
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
			
			
			rect = new Rect(10, 10, 350, 200);
		}
		
		
		/* (non-Javadoc)
		 * @see android.view.View#onDraw(android.graphics.Canvas)
		 */
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			canvas.save();
			canvas.drawRect(rect, paint);
			canvas.restore();
			canvas.rotate(90, Literal.width/2, Literal.width/2);
			canvas.save();
			canvas.drawRect(rect, paint);
			canvas.restore();
			canvas.rotate(90, Literal.height/2, Literal.height/2);
			canvas.save();
			canvas.drawRect(rect, paint);
			canvas.restore();
			canvas.rotate(90, Literal.width/2, Literal.width/2);
			canvas.drawRect(rect, paint);
		}
	}
}
