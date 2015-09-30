package com.darly.activities.widget.pop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.darly.activities.common.Literal;
import com.darly.activities.common.LogApp;
import com.darly.activities.ui.R;

public class PhotoPop implements OnClickListener {

	private final String TAG = getClass().getSimpleName();

	private static PhotoPop photoPop;

	private PhotoPop(Context context) {
		super();
		this.context = context;
		init();
	}

	/**
	 * @return the photoPop
	 */
	public static PhotoPop getPhotoPop(Context context) {
		if (photoPop == null) {
			photoPop = new PhotoPop(context);
		}
		return photoPop;
	}

	/**
	 * 下午1:29:10 TODO 系统参数。
	 */
	private Context context;

	/**
	 * 下午1:30:52 TODO pop窗口主键。
	 */
	private PopupWindow popupWindow;

	private Button item_popupwindows_camera;

	private String capUri;

	private Button item_popupwindows_Photo;

	private Button item_popupwindows_cancel;

	private Uri photoUri;

	/**
	 * 
	 * 下午1:29:54
	 * 
	 * @author Zhangyuhui PhotoPop.java TODO 初始化控件集合。
	 */
	private void init() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.popupwindows,
				null);

		item_popupwindows_camera = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		item_popupwindows_camera.setOnClickListener(this);
		item_popupwindows_Photo = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		item_popupwindows_Photo.setOnClickListener(this);
		item_popupwindows_cancel = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		item_popupwindows_cancel.setOnClickListener(this);

		popupWindow = new PopupWindow();
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setContentView(view);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.item_popupwindows_camera:
			// 照相功能
			capPhoto();
			break;
		case R.id.item_popupwindows_Photo:
			// 相册功能
			albumPhoto();
			break;
		case R.id.item_popupwindows_cancel:
			// 取消
		default:
			break;
		}
		popupWindow.dismiss();
	}

	/**
	 * 
	 * 下午1:56:36
	 * 
	 * @author Zhangyuhui PhotoPop.java TODO 相册内容
	 */
	private void albumPhoto() {
		// TODO Auto-generated method stub
		LogApp.i(TAG, "开始调用相册");
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		((Activity) context).startActivityForResult(intent,
				Literal.REQUESTCODE_CAM);
	}

	/**
	 * 
	 * 下午1:54:52
	 * 
	 * @author Zhangyuhui PhotoPop.java TODO 相机内容
	 */
	private void capPhoto() {
		LogApp.i(TAG, "开始调用照相");
		// 照相
		capUri = Literal.SROOT + System.currentTimeMillis() + ".png";
		Literal.capUri = capUri;
		File destDir = new File(Literal.SROOT);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		File file = new File(capUri);
		if (!file.exists()) {
			try {
				// 在指定的文件夹中创建文件
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(capUri)));
		((Activity) context).startActivityForResult(openCameraIntent,
				Literal.REQUESTCODE_CAP);
		LogApp.i(TAG, "文件路径" + capUri + "文件" /* + imageUri */);

	}

	/**
	 * @param uri
	 *            下午2:07:46
	 * @author Zhangyuhui PhotoPop.java TODO 調用手機裁剪功能。
	 */
	public void cropPhoto(Uri uri) {
		LogApp.i(TAG, "开始调用裁剪");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		((Activity) context).startActivityForResult(intent,
				Literal.REQUESTCODE_CUT);
	}

	/**
	 * @param requestCode
	 * @param data
	 * @return 下午3:14:23
	 * @author Zhangyuhui PhotoPop.java TODO 回调方法。获取BItma
	 */
	public Bitmap PopActivityResult(int requestCode, Intent data) {
		if (data == null) {
			LogApp.i(TAG, "data返回数据错误，图片获取空字段。");
			return null;
		}

		switch (requestCode) {
		case Literal.REQUESTCODE_CAP:
			//
			LogApp.i(TAG, "相机调用完成。返回数据");
			Bitmap tempBitmap = BitmapFactory.decodeFile(Literal.HEAD);
			photoUri = Uri.fromFile(new File(Literal.SROOT));
			return tempBitmap;
		case Literal.REQUESTCODE_CAM:
			//
			LogApp.i(TAG, "相册调用完成。返回数据");
			// 调用Gallery返回的
			try {
				// 照片的原始资源地址
				ContentResolver resolver = context.getContentResolver();
				photoUri = data.getData();
				// 使用ContentProvider通过URI获取原始图片
				Bitmap tempBitmap2 = MediaStore.Images.Media.getBitmap(
						resolver, photoUri);

				return tempBitmap2;
				// crop_upload(tempBitmap2);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case Literal.REQUESTCODE_CUT:
			//
			LogApp.i(TAG, "裁剪调用完成。返回数据");
			Bundle extras = data.getExtras();
			Bitmap cropedBitmap = extras.getParcelable("data");
			return cropedBitmap;
		default:
			break;
		}

		return null;
	}

	/**
	 * @param requestCode
	 * @param data
	 * @param tag
	 * @return 下午3:14:45
	 * @author Zhangyuhui PhotoPop.java TODO 回调方法获取图片路径
	 */
	public String PopStringActivityResult(Intent data, int tag) {
		switch (tag) {
		case Literal.REQUESTCODE_CAP: {
			// 照相机程序返回的
			LogApp.i(TAG, "相机调用完成。返回图片的保存地址");
			LogApp.i(TAG, "相机圖片位置" + capUri);
			return capUri;

		}

		case Literal.REQUESTCODE_CAM: {
			// 照片的原始资源地址
			LogApp.i(TAG, "相册调用完成。返回图片的地址");
			photoUri = data.getData();
			return getImagePath(photoUri);
		}
		default:
			break;
		}

		return null;

	}

	/**
	 * @param originalUri
	 * @return 下午3:15:15
	 * @author Zhangyuhui PhotoPop.java TODO 获取图片路径
	 */
	public String getImagePath(Uri originalUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(originalUri, proj,
					null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			// 将光标移至开头 ，这个很重要，不小心很容易引起越界
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径
			String path = cursor.getString(column_index);
			LogApp.i(TAG, "相册图片位置" + path);
			return path;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * @param v
	 *            下午3:15:27
	 * @author Zhangyuhui PhotoPop.java TODO 展示POP
	 */
	public void show(View v) {
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	/**
	 * @return the capUri
	 */
	public String getCapUri() {
		return capUri;
	}

}
