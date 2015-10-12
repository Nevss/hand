package com.darly.activities.widget.intel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class InterlgentUtil {
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * @param options
	 * @param reqW
	 * @param reqH
	 * @return 上午9:15:45
	 * @author Zhangyuhui InterlgentUtil.java TODO 获取缩放比例值
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqW, int reqH) {
		final int height = options.outHeight;
		final int width = options.outWidth;

		int inSampleSize = 1;
		if (height > reqH || width > reqW) {
			final int hRatio = Math.round((float) height / (float) reqH);
			final int wRatio = Math.round((float) width / (float) reqW);
			inSampleSize = hRatio < wRatio ? hRatio : wRatio;

		}
		return inSampleSize;
	}

	/**
	 * @param filePath
	 * @param reqW
	 * @param reqH
	 * @return
	 * 上午9:20:09
	 * @author Zhangyuhui
	 * InterlgentUtil.java
	 * TODO 获取小图的方法
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqW, int reqH) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, reqW, reqH);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

}
