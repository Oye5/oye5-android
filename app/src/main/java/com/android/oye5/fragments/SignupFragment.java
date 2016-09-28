package com.android.oye5.fragments;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.utils.Utils;

public class SignupFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout layoutEmailSigninPopup;
    private EditText edtEmail, edtPassword;
    private Button btnShowPassword;

    public static SignupFragment newInstance(){
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_signup, container, false);

        initView(parent);

        return parent;
    }

    private void initView(View parent){
        layoutEmailSigninPopup = (LinearLayout) parent.findViewById(R.id.layoutEmailSigninPopup);
        edtEmail = (EditText) parent.findViewById(R.id.edtEmail);
        edtPassword = (EditText) parent.findViewById(R.id.edtPassword);
        btnShowPassword = (Button)parent.findViewById(R.id.btnShowPassword);

        parent.findViewById(R.id.txtContactUs).setOnClickListener(this);
        parent.findViewById(R.id.btnFacebookSignIn).setOnClickListener(this);
        parent.findViewById(R.id.btnEmailSignin).setOnClickListener(this);
        parent.findViewById(R.id.btnSignup).setOnClickListener(this);

        parent.findViewById(R.id.btnClosePopup).setOnClickListener(this);
        parent.findViewById(R.id.btnShowPassword).setOnClickListener(this);
        parent.findViewById(R.id.btnSignin).setOnClickListener(this);
    }

    private void showSigninEmailPopup(){
        edtEmail.setText("");
        edtPassword.setText("");

        int nPopupHeight = getResources().getDimensionPixelSize(R.dimen.signin_email_popup_height);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, (-1) * nPopupHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(300);
        layoutEmailSigninPopup.startAnimation(anim);

        layoutEmailSigninPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutEmailSigninPopup.getLayoutParams();
                params.bottomMargin = 0;
                layoutEmailSigninPopup.setLayoutParams(params);
            }
        }, 300);

    }

    private void hideSigninEmailPopup(){
        final int nPopupHeight = getResources().getDimensionPixelSize(R.dimen.signin_email_popup_height);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, nPopupHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(300);
        layoutEmailSigninPopup.startAnimation(anim);

        layoutEmailSigninPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutEmailSigninPopup.getLayoutParams();
                params.bottomMargin = (-1) * nPopupHeight;
                layoutEmailSigninPopup.setLayoutParams(params);
            }
        }, 300);

        Utils.hideKeyboard(getActivity());
    }

    private void setPasswordVisibility(){
        String sTitle = btnShowPassword.getText().toString();
        if (sTitle.equalsIgnoreCase(getString(R.string.signin_password_show))){
            btnShowPassword.setText(getString(R.string.signin_password_hidden));
            edtPassword.setTransformationMethod(null);
        }else{
            btnShowPassword.setText(getString(R.string.signin_password_show));
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtContactUs:
                showToast("Contact Us", Toast.LENGTH_SHORT);
                break;
            case R.id.btnFacebookSignIn:
                showToast("Facebook Signin", Toast.LENGTH_SHORT);
                break;
            case R.id.btnEmailSignin:
                showSigninEmailPopup();
                break;
            case R.id.btnSignup:
                showToast("Signup", Toast.LENGTH_SHORT);
                break;
            case R.id.btnSignin:
                showToast("Signin", Toast.LENGTH_SHORT);
                break;
            case R.id.btnShowPassword:
                setPasswordVisibility();
                break;
            case R.id.btnClosePopup:
                hideSigninEmailPopup();
                break;
        }
    }
}
