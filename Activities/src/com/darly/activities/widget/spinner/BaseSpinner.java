package com.darly.activities.widget.spinner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.darly.activities.ui.R;

public class BaseSpinner extends RelativeLayout {

	public BaseSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public BaseSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public BaseSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private Spinner spinner;

	private ImageView tip;

	private void init(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.spinner_base, this);
		spinner = (Spinner) findViewById(R.id.spinner_sp);
		tip = (ImageView) findViewById(R.id.spinner_iv);
	}

	public Spinner getSpinner() {
		return spinner;
	}

	public ImageView getTip() {
		return tip;
	}

}
