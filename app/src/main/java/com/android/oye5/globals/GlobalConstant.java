package com.android.oye5.globals;

import android.os.Environment;

import java.io.File;

public class GlobalConstant {
	/** System Common Constants **/
	public static final String TAG = "OYE5";

	public static final boolean isDebug = true;

	public static final String SENDER_ID = "100934099222";

	public static final int MENU_BUY = 1;
	public static final int MENU_CATEGORIES = 2;
	public static final int MENU_SELL = 3;
	public static final int MENU_CHAT = 4;
	public static final int MENU_PROFILE = 5;

	public static String getHomeDirPath() {
		File extStore = Environment.getExternalStorageDirectory();
		String result = String.format("%s/%s", new Object[]{extStore.getPath(), ".Oye5"});
		return result;
	}

	public static String getTempDirpath() {
		return String.format("%s/%s", new Object[]{getHomeDirPath(), "temp"});
	}

	public static String getCameraTempFilePath() {
		return String.format("%s/%s", new Object[]{getTempDirpath(), "camera_temp.jpg"});
	}

	public static String getCropTempFilePath() {
		return String.format("%s/%s", new Object[]{getTempDirpath(), "crop_temp.jpg"});
	}

	public static String getSignatureTempFilePath() {
		return String.format("%s/%s", new Object[]{getTempDirpath(), "signature_temp.jpg"});
	}

	public static String getLargeImageTempFilePath() {
		String result = String.format("%s/%s", new Object[]{getTempDirpath(), "temp_large_image.jpg"});
		return result;
	}
}