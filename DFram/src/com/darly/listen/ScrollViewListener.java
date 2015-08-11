package com.darly.listen;

import com.darly.widght.BaseScrollView;

public interface ScrollViewListener {
	void onScrollChanged(BaseScrollView scrollView, int x, int y, int oldx,
			int oldy);
}
