package com.android.oye5;

import android.app.Application;
import android.content.Context;

import com.android.oye5.models.UserData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.MyLocation;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;
import org.json.JSONObject;

public class Oye5App extends Application{
	private static final String TAG = "Oye5App";

    private static Oye5App instance;
    private UserData user = null;

	//public static final String DEFAULT_FONT = "fonts/OpenSans-Regular.ttf";

    private MyLocation mLocation;
    private DisplayImageOptions displayOptions;

	@Override
	public void onCreate() {		
		super.onCreate();
		instance = this;

        mLocation = new MyLocation(getApplicationContext());

        displayOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.bg_loader_default)
                .build();

        //init default font
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(DEFAULT_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build());*/
        Oye5App.initImageLoader(getApplicationContext());
	}

    public static Oye5App getInstance () {
        return instance;
    }

    public String getToken() {
        return getUser(false) == null? "":getUser(false).getProviderToken();
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

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public DisplayImageOptions getDisplayOptions(){
        return this.displayOptions;
    }
}