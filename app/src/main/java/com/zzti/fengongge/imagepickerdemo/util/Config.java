package com.zzti.fengongge.imagepickerdemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;


public class Config {
	public static HashMap<String, Integer> ScreenMap;
	public static int VersionCode = 0;

	/**
	 * 获取屏幕尺寸
	 * @param context
	 * @return
	 */
	public static HashMap<String, Integer> getScreenSize(Activity activity,
			Context context) {
		int width, height;
		if (Config.VersionCode > 13) {
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		} else {
			Display display = activity.getWindowManager().getDefaultDisplay();
			width = display.getWidth();
			height = display.getHeight();
		}
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		hashMap.put("width", width);
		hashMap.put("height", height);
		return hashMap;
	}
	
}
