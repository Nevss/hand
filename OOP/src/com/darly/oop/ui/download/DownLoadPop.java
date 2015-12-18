/**
 * 下午2:38:32
 * @author zhangyh2
 * $
 * DownLoadPop.java
 * TODO
 */
package com.darly.oop.ui.download;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darly.oop.R;
import com.darly.oop.common.ToastOOP;

/**
 * @author zhangyh2 DownLoadPop $ 下午2:38:32 TODO
 */
public class DownLoadPop extends PopupWindow {

	private Context context;

	private TextView resultView;
	private ProgressBar progressBar;

	private String url;

	private static int threadNUM = 10;
	/**
	 * 上午10:14:27 TODO 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息
	 * 消息队列中的消息由当前线程内部进行处理
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressBar.setProgress(msg.getData().getInt("size"));
				float num = (float) progressBar.getProgress()
						/ (float) progressBar.getMax();
				int result = (int) (num * 100);
				resultView.setText(result + "%");

				if (progressBar.getProgress() == progressBar.getMax()) {
					ToastOOP.showToast(context, R.string.success);
					if (msg.obj instanceof File) {
						File file = (File) msg.obj;
						Instanll(file, context);
					}
				}
				break;
			case -1:
				ToastOOP.showToast(context, R.string.error);
				break;
			}
		}
	};

	public DownLoadPop(Context activity, String url, View contentView,
			int width, int height, Boolean focusable) {
		super(contentView, width, height, focusable);
		this.context = activity;
		this.url = url;
		initView(activity);
	}

	public DownLoadPop(Context activity, String url) {
		super(activity);
		this.context = activity;
		this.url = url;
		initView(activity);
	}

	public DownLoadPop(Context context, AttributeSet set) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param activity
	 *            下午2:40:37
	 * @author zhangyh2 DownLoadPop.java TODO
	 */
	@SuppressWarnings("deprecation")
	private void initView(Context context) {
		// TODO Auto-generated method stub
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.activity_download, null);
		resultView = (TextView) rootView.findViewById(R.id.download_text);
		progressBar = (ProgressBar) rootView
				.findViewById(R.id.download_progress);

		LinearLayout llt = (LinearLayout) rootView
				.findViewById(R.id.dowload_linear);
		llt.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				} else {
					return false;
				}

			}
		});
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(false);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(false);
		startDownLoad(url);
	}

	/**
	 * 
	 * 上午10:09:53
	 * 
	 * @author zhangyh2 MainDownLoad.java TODO 直接打开此界面即进行下载。
	 * @param url
	 */
	private void startDownLoad(String url) {
		// TODO Auto-generated method stub
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			download(url, Environment.getExternalStorageDirectory());
		} else {
			ToastOOP.showToast(context, R.string.sdcarderror);
		}
	}

	/**
	 * @param dec
	 * @param externalStorageDirectory
	 *            上午10:12:53
	 * @author zhangyh2 MainDownLoad.java TODO 下载的起始方法。
	 */
	private void download(final String path, final File savedir) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileDownloader loader = new FileDownloader(context, path,
						savedir, threadNUM);
				progressBar.setMax(loader.getFileSize());// 设置进度条的最大刻度为文件的长度

				try {
					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(File file, int size) {// 实时获知文件已经下载的数据长度
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							msg.obj = file;
							handler.sendMessage(msg);// 发送消息
						}
					});
				} catch (Exception e) {
					handler.obtainMessage(-1).sendToTarget();
				}
			}
		}).start();
	}

	// 安装下载后的apk文件
	private void Instanll(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
