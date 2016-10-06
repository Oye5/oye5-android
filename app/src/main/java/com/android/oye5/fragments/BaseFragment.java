package com.android.oye5.fragments;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.dialogs.CustomProgressDialog;

public class BaseFragment extends Fragment {
	protected int displayWidth = 0;
	protected int displayHeight = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager wm = getActivity().getWindowManager();
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

	public void showAlertDialog(String title, String sMsg){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(sMsg);
		// Set up the buttons
		builder.setTitle(title);
		builder.setPositiveButton(getResources().getString(R.string.btn_label_ok), null);
		builder.show();
	}

    protected void showToast(String text, int duration) {
        FragmentActivity activity = getActivity();
        if( activity == null || (activity instanceof BaseActivity
                && ((BaseActivity)activity).isActivityStopped() ) ) {
            return;
        }

        Toast.makeText(activity, text, duration).show();
    }

	protected CustomProgressDialog showProgressDialog(CustomProgressDialog dialog, String text) {
		FragmentActivity activity = getActivity();
		if( activity == null || (activity instanceof BaseActivity
				&& ((BaseActivity)activity).isActivityStopped() ) ) {
			return null;
		}

		if( dialog == null ) {
			dialog = new CustomProgressDialog(getActivity(), text);
		}else{
			dialog.setMessage(text);
		}

		if( !dialog.isShowing() ) {
			dialog.show();
		}
		return dialog;
	}

	protected void dismissProgressDialog(ProgressDialog dialog) {
		FragmentActivity activity = getActivity();
		if( dialog == null || activity == null || (activity instanceof BaseActivity
				&& ((BaseActivity)activity).isActivityStopped() ) ) {
			return;
		}
		dialog.dismiss();
	}

	protected boolean isActivityActive() {
		FragmentActivity activity = getActivity();
		if(activity == null || (activity instanceof BaseActivity
				&& ((BaseActivity)activity).isActivityStopped() ) ) {
			return false;
		}
		return true;
	}

	protected void setToolbarTitle(Toolbar toolbar, String title){
		((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(title);
	}
}
