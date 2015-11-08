package com.fanhl.doujinMoe.util;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class Utility {

	public static boolean isChrome() {
		return Build.BRAND.equals("chromium") || Build.BRAND.equals("chrome");
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static String getFirstCharacter(String sentence) {
		for (int i = 0; i < sentence.length(); i++) {
			String s = sentence.substring(i, i+1);
			if (s.equals("[") || s.equals("]")) continue;
			if (s.equals("{") || s.equals("}")) continue;
			if (s.equals("(") || s.equals(")")) continue;
			if (s.equals(",") || s.equals(".")) continue;
			if (s.equals("<") || s.equals(">")) continue;
			if (s.equals("《") || s.equals("》")) continue;
			if (s.equals("【") || s.equals("】")) continue;
			if (s.equals("｛") || s.equals("｝")) continue;
			return s;
		}
		return null;
	}

	public static String getSystemProperties(String key) {
		try {
			Class c = Class.forName("android.os.SystemProperties");
			Method m = c.getDeclaredMethod("get", String.class);
			m.setAccessible(true);
			return (String) m.invoke(null, key);
		} catch (Throwable e) {
			return "";
		}
	}

	public static int getTrueScreenHeight(Context context) {
		int dpi = 0;
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= 17) {
			display.getRealMetrics(dm);
			dpi = dm.heightPixels;
		} else {
			try {
				Class c = Class.forName("android.view.Display");
				Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
				method.invoke(display, dm);
				dpi = dm.heightPixels;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dpi;
	}

	public static int getNavigationBarHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		return getTrueScreenHeight(context) - dm.heightPixels;
	}

	public static void saveStringToFile(Context context, String name, String text) throws IOException {
		FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
		fos.write(text.getBytes());
		fos.close();
	}

	public static String readStringFromFile(Context context, String name) throws IOException{
		File file = context.getFileStreamPath(name);
		InputStream is = new FileInputStream(file);

		byte b[] = new byte[(int) file.length()];

		is.read(b);
		is.close();

		String string = new String(b);

		return string;
	}

	public static int calcProgress(int progress, int max) {
		return (int) (((float) progress)/((float) max) * 100);
	}

}
