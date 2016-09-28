package com.android.oye5.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.oye5.R;

public class CustomProgressDialog extends ProgressDialog {
	public static final String DEBUG_TAG = "CustomProgressDialog";
	String mMessage;
	private Context mContext;
	private TextView progressMessage;
	public CustomProgressDialog(Context context, String message) {
		super(context, R.style.CustomAlertDialogStyle);
		this.mMessage = message;
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_custom_progressbar);

		setCancelable(false);

		progressMessage = (TextView) findViewById(R.id.progressMessage);
		progressMessage.setText(mMessage);
	}

	public void setMessage(String message){
		mMessage = message;
		progressMessage.setText(mMessage);
	}
}
