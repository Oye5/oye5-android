package com.android.oye5.activities;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.dialogs.CustomProgressDialog;

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
}