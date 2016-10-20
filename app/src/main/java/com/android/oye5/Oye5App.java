package com.android.oye5;

import android.app.Application;

import com.android.oye5.models.UserData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.MyLocation;

import org.json.JSONException;
import org.json.JSONObject;

public class Oye5App extends Application{
	private static final String TAG = "Oye5App";

    private static Oye5App instance;
    private UserData user = null;

	//public static final String DEFAULT_FONT = "fonts/OpenSans-Regular.ttf";

    private MyLocation mLocation;

	@Override
	public void onCreate() {		
		super.onCreate();
		instance = this;

        mLocation = new MyLocation(getApplicationContext());

        //init default font
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(DEFAULT_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build());*/
	}

    public static Oye5App getInstance () {
        return instance;
    }

    public UserData getUser(boolean forceFetch){
        try {
            if (user == null || forceFetch) {
                String userInfo = AppPreference.getUserInfo(this);
                if (userInfo != null && !userInfo.equals("")) {
                    user = new UserData(new JSONObject(userInfo));
                }
            }
        }catch(JSONException e){}
        return this.user;
    }

    public void setUserInfo(String userInfo){
        AppPreference.setUserInfo(this, userInfo);
        this.user = getUser(true);
    }

    public void clearUser(){
        AppPreference.clearUserInfo(this);
        this.user = null;
    }

    /** Get location tracking object **/
    public MyLocation getMyLocation(){
        return this.mLocation;
    }

}