package com.android.oye5;

import android.app.Application;

public class Oye5App extends Application{
	private static final String TAG = "Oye5App";

    private static Oye5App instance;

	//public static final String DEFAULT_FONT = "fonts/OpenSans-Regular.ttf";

	@Override
	public void onCreate() {		
		super.onCreate();
		instance = this;

        //init default font
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(DEFAULT_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build());*/
	}

    public static Oye5App getInstance () {
        return instance;
    }

}