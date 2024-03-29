package com.android.oye5.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.oye5.Oye5App;
import com.android.oye5.R;
import com.android.oye5.dialogs.CustomProgressDialog;
import com.android.oye5.models.ProductData;
import com.android.oye5.utils.Utils;

public class BaseActivity extends AppCompatActivity {

    protected boolean isActivityActive;

    protected int displayWidth = 0;
    protected int displayHeight = 0;

    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        isActivityActive = true;

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        if( Build.VERSION.SDK_INT >= 13 ) {
            Point size = new Point();
            display.getSize(size);
            displayWidth = size.x;
            displayHeight = size.y;
        } else {
            displayWidth = display.getWidth();
            displayHeight = display.getHeight();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityActive = false;
    }

    public void onStop() {
        super.onStop();
        isActivityActive = false;
        System.gc();
    }

    public boolean isActivityStopped() {
        return !isActivityActive;
    }

    protected void showToast(String text, int duration) {
        if (isActivityStopped()) {
            return;
        }

        Toast.makeText(this, text, duration).show();
    }

    protected CustomProgressDialog showProgressDialog(CustomProgressDialog dialog, String text) {
        if (dialog == null) {
            dialog = new CustomProgressDialog(this, text);
        } else {
            dialog.setMessage(text);
        }

        if (!dialog.isShowing()) dialog.show();

        return dialog;
    }

    protected void dismissProgressDialog(CustomProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showAlertDialog(String title, String sMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(sMsg);
        // Set up the buttons
        builder.setTitle(title);
        builder.setPositiveButton(getResources().getString(R.string.btn_label_ok), null); builder.show();
    }

    public void showErrorAlertDialog(String title, String sMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(sMsg);
        // Set up the buttons
        builder.setTitle(title);
        builder.setPositiveButton(getResources().getString(R.string.btn_label_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/


    public void goToProductDetailsScreen(ProductData product){
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    public void goToFilterScreen(){
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    public void goToProductPostScreen(){
        Intent intent = new Intent(this, ProductPostActivity.class);
        startActivity(intent);
    }

    public void initLocationManager() {
        // Check GPS is enabled or not.
        if (!Utils.CheckGPSOpen(this)) {
            final AlertDialog dlg = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                    .setTitle(R.string.gps_enable_title)
                    .setMessage(R.string.gps_enable_confirm)
                    .setPositiveButton(R.string.btn_label_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton(R.string.btn_label_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).create();
            dlg.show();
        }

        Oye5App.getInstance().getMyLocation().setLocationListners();
    }

    /*private Context context;
    private GoogleCloudMessaging gcm;
    private String regid = "";
    public void checkGCMRegistration(){
        context = getApplicationContext();
        if( checkPlayServices() ){
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.equals("")) {
                registerInBackground();
            } else {
                Log.i(getClass().getName(), "Registration is found:" + regid);
            }
        }
    }

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(getClass().getName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    /*private String getRegistrationId(Context context) {
        String registrationId = AppPreference.getGCMRegId(context);
        if (registrationId.equals("")) {
            Log.i(getClass().getName(), "Registration not found.");
            return "";
        }

        return registrationId;
    }*/


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    /*private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(GlobalConstant.SENDER_ID);
                    return regid;
                } catch (IOException ex) {
                    Log.e(getClass().getName(), "IOException:" + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String id) {
                if( id != null ) {
                    AppPreference.setGCMRegId(context, id);
                }
            }
        }.execute(null, null, null);
    }*/
}