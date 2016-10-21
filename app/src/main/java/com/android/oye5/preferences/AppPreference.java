package com.android.oye5.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.oye5.globals.GlobalConstant;

public class AppPreference {
	public static final String SHARED_PREF_KEY = "com.android.oye5";
	private static final String PREF_KEY_USERINFO = "key_user_info";
	private static final String KEY_LATITUDE = "key_latitude";
	private static final String KEY_LONGITUDE = "key_longitude";
	private static final String KEY_GCMID = "key_gcm_id";

	public static SharedPreferences getPreferences(Context ctx) {
		return ctx.getSharedPreferences(SHARED_PREF_KEY, Activity.MODE_PRIVATE);
	}

	public static void setSharedPrefValue(Context ctx, String key, String value) {
		SharedPreferences.Editor editor = getPreferences(ctx).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStringPrefValue(Context ctx, String key) {
		return getPreferences(ctx).getString(key, "");
	}

	public static String getStringPrefValue(Context ctx, String key,
			String defval) {
		return getPreferences(ctx).getString(key, defval);
	}

	public static void setSharedPrefValue(Context ctx, String key, int value) {
		SharedPreferences.Editor editor = getPreferences(ctx).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getIntPrefValue(Context ctx, String key) {
		return getPreferences(ctx).getInt(key, 0);
	}

	public static int getIntPrefValue(Context ctx, String key, int defval) {
		return getPreferences(ctx).getInt(key, defval);
	}

	public static long getLongPrefValue(Context ctx, String key) {
		return getPreferences(ctx).getLong(key, 0L);
	}

	public static long getLongPrefValue(Context ctx, String key, long defval) {
		return getPreferences(ctx).getLong(key, defval);
	}

	public static void setSharedPrefValue(Context ctx, String key, long value) {
		SharedPreferences.Editor editor = getPreferences(ctx).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void setSharedPrefValue(Context ctx, String key, float value) {
		SharedPreferences.Editor editor = getPreferences(ctx).edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public static Float getFloatPrefValue(Context ctx, String key) {
		return getPreferences(ctx).getFloat(key, 0f);
	}

	public static void setSharedPrefValue(Context ctx, String key, boolean value) {
		SharedPreferences.Editor editor = getPreferences(ctx).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBooleanPrefValue(Context ctx, String key) {
		return getPreferences(ctx).getBoolean(key, false);
	}

	public static boolean getBooleanPrefValue(Context ctx, String key,
			boolean defval) {
		return getPreferences(ctx).getBoolean(key, defval);
	}

	public static void clearPrefValue(Context ctx, String key) {
		SharedPreferences.Editor editor = getPreferences(ctx).edit();
		editor.remove(key);
		editor.commit();
	}

	public static void setUserInfo(Context ctx, String userInfo){
		setSharedPrefValue(ctx, PREF_KEY_USERINFO, userInfo);
	}

	public static String getUserInfo(Context ctx){
		return getStringPrefValue(ctx, PREF_KEY_USERINFO);
	}

	public static void setGcmID(Context ctx, String gcmid){
		setSharedPrefValue(ctx, KEY_GCMID, gcmid);
	}

	public static String getGcmID(Context ctx){
		return getStringPrefValue(ctx, KEY_GCMID);
	}

	public static void clearUserInfo(Context ctx){
		clearPrefValue(ctx, PREF_KEY_USERINFO);
	}

    public static void setLocation(Context ctx, double lat, double lng) {
        setSharedPrefValue(ctx, KEY_LATITUDE, (float) lat);
        setSharedPrefValue(ctx, KEY_LONGITUDE, (float) lng);
    }

    public static double getLatitude(Context ctx) {
        //return (double) getFloatPrefValue(ctx, KEY_LATITUDE);
		return GlobalConstant.isDebug? 79.989:(double) getFloatPrefValue(ctx, KEY_LATITUDE);
    }

    public static double getLongitude(Context ctx) {
        //return (double) getFloatPrefValue(ctx, KEY_LONGITUDE);
		return GlobalConstant.isDebug? 134.9:(double) getFloatPrefValue(ctx, KEY_LONGITUDE);
    }
}
