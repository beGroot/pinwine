package com.pin.pinwine.view;

import android.content.Context;
import android.os.Handler;

/**
 * 为了防止内存泄漏，定义外部类，防止内部类对外部类的引�? */
public class CycleViewPagerHandler extends Handler {
	 Context context;

	public CycleViewPagerHandler(Context context) {
		this.context = context;
	}
};