package com.android.oye5.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.Oye5App;
import com.android.oye5.R;
import com.android.oye5.activities.SignupActivity;
import com.android.oye5.dialogs.CustomProgressDialog;
import com.android.oye5.models.UserData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.RestClientUtils;
import com.android.oye5.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class SignupFragment extends BaseFragment implements View.OnClickListener{

    private CustomProgressDialog progressDialog;

    private LinearLayout layoutEmailSigninPopup;
    private EditText edtEmail, edtPassword;
    private Button btnShowPassword;

    private LinearLayout layoutEmailSignupPopup;
    private EditText edtSignupEmail, edtSignupUsername, edtSignupPassword;
    private Button btnSignupShowPassword;

    private LinearLayout layoutButtons;
    private TextView txtContactUs;

    private ImageView imgLogo;

    /**
     * Facebook Login button layout, Callback manager
     **/
    private CallbackManager callbackManager;
    private LoginButton btnFBLogin;
    private LinearLayout layoutLoginBtn;

    public static SignupFragment newInstance(){
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        View parent = inflater.inflate(R.layout.fragment_signup, container, false);

        initView(parent);
        fbLoginConfiguration(parent);

        doLogoAnimation();

        return parent;
    }

    private void initView(View parent){
        imgLogo = (ImageView) parent.findViewById(R.id.imgLogo);
        layoutButtons = (LinearLayout) parent.findViewById(R.id.layoutButtons);
        txtContactUs = (TextView) parent.findViewById(R.id.txtContactUs);

        layoutEmailSigninPopup = (LinearLayout) parent.findViewById(R.id.layoutEmailSigninPopup);
        edtEmail = (EditText) parent.findViewById(R.id.edtEmail);
        edtPassword = (EditText) parent.findViewById(R.id.edtPassword);
        btnShowPassword = (Button)parent.findViewById(R.id.btnShowPassword);

        layoutEmailSignupPopup = (LinearLayout) parent.findViewById(R.id.layoutSignupPopup);
        edtSignupUsername = (EditText) parent.findViewById(R.id.edtSignupUsername);
        edtSignupEmail = (EditText) parent.findViewById(R.id.edtSignupEmail);
        edtSignupPassword = (EditText) parent.findViewById(R.id.edtSignupPassword);
        btnSignupShowPassword = (Button)parent.findViewById(R.id.btnSignupShowPassword);

        parent.findViewById(R.id.txtContactUs).setOnClickListener(this);
        parent.findViewById(R.id.btnFacebookSignIn).setOnClickListener(this);
        parent.findViewById(R.id.btnEmailSignin).setOnClickListener(this);
        parent.findViewById(R.id.btnSignup).setOnClickListener(this);

        parent.findViewById(R.id.btnClosePopup).setOnClickListener(this);
        parent.findViewById(R.id.btnShowPassword).setOnClickListener(this);
        parent.findViewById(R.id.btnSignin).setOnClickListener(this);

        parent.findViewById(R.id.btnCloseSignupPopup).setOnClickListener(this);
        parent.findViewById(R.id.btnSignupShowPassword).setOnClickListener(this);
        parent.findViewById(R.id.btnEmailSignup).setOnClickListener(this);
    }

    private void doLogoAnimation(){
        // Start Logo Animation
        imgLogo.setVisibility(View.INVISIBLE);
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(2000);
        imgLogo.startAnimation(anim);
        imgLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgLogo.setVisibility(View.VISIBLE);
                doButtonsAnimation();
            }
        }, 2000);
    }

    private void doButtonsAnimation(){
        if (Oye5App.getInstance().getToken().equals("")){
            // Do button animation for login
            AlphaAnimation anim = new AlphaAnimation(0, 1);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(500);
            layoutButtons.startAnimation(anim);
            txtContactUs.startAnimation(anim);
            layoutButtons.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutButtons.setVisibility(View.VISIBLE);
                    txtContactUs.setVisibility(View.VISIBLE);
                }
            }, 500);
        }else{
            doGetUserInfo();
        }
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

    private void showSignupEmailPopup(){
        edtSignupEmail.setText("");
        edtSignupPassword.setText("");
        edtSignupUsername.setText("");

        int nPopupHeight = getResources().getDimensionPixelSize(R.dimen.signup_email_popup_height);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, (-1) * nPopupHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(300);
        layoutEmailSignupPopup.startAnimation(anim);

        layoutEmailSignupPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutEmailSignupPopup.getLayoutParams();
                params.bottomMargin = 0;
                layoutEmailSignupPopup.setLayoutParams(params);
            }
        }, 300);

    }

    private void hideSignupEmailPopup(){
        final int nPopupHeight = getResources().getDimensionPixelSize(R.dimen.signup_email_popup_height);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, nPopupHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(300);
        layoutEmailSignupPopup.startAnimation(anim);

        layoutEmailSignupPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutEmailSignupPopup.getLayoutParams();
                params.bottomMargin = (-1) * nPopupHeight;
                layoutEmailSignupPopup.setLayoutParams(params);
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

    private void setSignupPasswordVisibility(){
        String sTitle = btnSignupShowPassword.getText().toString();
        if (sTitle.equalsIgnoreCase(getString(R.string.signin_password_show))){
            btnSignupShowPassword.setText(getString(R.string.signin_password_hidden));
            edtSignupPassword.setTransformationMethod(null);
        }else{
            btnSignupShowPassword.setText(getString(R.string.signin_password_show));
            edtSignupPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    private void doSignin(final String _email, final String _password){
        String email = _email.equals("")? edtEmail.getText().toString():_email;
        if (email.equals("") || !Utils.isEmailValid(email)){
            showToast(getString(R.string.valid_enter_email), Toast.LENGTH_SHORT);
            return;
        }

        String password = _password.equals("")? edtPassword.getText().toString():_password;
        if (password.equals("")){
            showToast(getString(R.string.valid_enter_password), Toast.LENGTH_SHORT);
            return;
        }

        try {
            JSONObject objParams = new JSONObject();
            objParams.put("email", email);
            objParams.put("password", password);
            objParams.put("gcmId", AppPreference.getGcmID(getActivity()));
            objParams.put("lattitude", AppPreference.getLatitude(getActivity()));
            objParams.put("longitude", AppPreference.getLongitude(getActivity()));
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.post(getActivity(), getString(R.string.PATH_USER_SIGNIN), objParams, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Signin response:" + response.toString());
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);

                        String message = response.has("message")? response.optString("message"):"";

                        if (message.equals("")) {
                            Oye5App.getInstance().setUserInfo(response.toString());

                            if (_email.equals("") && _password.equals("")) {
                                hideSigninEmailPopup();
                                layoutEmailSigninPopup.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((SignupActivity) getActivity()).goToMainActivity();
                                    }
                                }, 300);
                            } else {
                                ((SignupActivity) getActivity()).goToMainActivity();
                            }
                        }else{
                            showToast(message, Toast.LENGTH_SHORT);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + responseString);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + obj);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);

                        String message = obj.optString("message");
                        showToast(message, Toast.LENGTH_SHORT);
                    }
                }
            });
        }catch (JSONException eJSON){}
        catch(Exception e){}
    }

    private void doSignup(){
        final String email = edtSignupEmail.getText().toString();
        if (email.equals("") || !Utils.isEmailValid(email)){
            showToast(getString(R.string.valid_enter_email), Toast.LENGTH_SHORT);
            return;
        }

        String username = edtSignupUsername.getText().toString();
        if (username.equals("")){
            showToast(getString(R.string.valid_enter_username), Toast.LENGTH_SHORT);
            return;
        }

        final String password = edtSignupPassword.getText().toString();
        if (password.equals("")){
            showToast(getString(R.string.valid_enter_password), Toast.LENGTH_SHORT);
            return;
        }

        try {
            JSONObject objParams = new JSONObject();
            objParams.put("email", email);
            objParams.put("username", username);
            objParams.put("password", password);
            objParams.put("gcmId", AppPreference.getGcmID(getActivity()));
            objParams.put("lattitude", AppPreference.getLatitude(getActivity()));
            objParams.put("longitude", AppPreference.getLongitude(getActivity()));
            objParams.put("appType", "android");
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.post(getActivity(), getString(R.string.PATH_USER_SIGNUP), objParams, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Signup response:" + response.toString());
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);

                        String message = response.optString("message");
                        //String code = response.optString("code");
                        if (message.equals("")/*code.equals("S001")*/) {
                            hideSignupEmailPopup();
                            layoutEmailSignupPopup.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doSignin(email, password);
                                }
                            }, 300);
                        } else {
                            showToast(message, Toast.LENGTH_SHORT);
                        }}
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response1:" + responseString);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response2:" + obj);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        //showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);

                        String code = obj.optString("code");
                        if (!code.equals("S001")) {
                            String message = obj.optString("message");
                            showToast(message, Toast.LENGTH_SHORT);
                        }
                    }
                }
            });
        }catch (JSONException eJSON){}
        catch(Exception e){}
    }

    private void doGetUserInfo(){
        try {
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.get(getActivity(), getString(R.string.PATH_USER_INFO) + Oye5App.getInstance().getUser(false).getId(), null, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "User Info response:" + response.toString());
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        try {
                            response.put("providerToken", Oye5App.getInstance().getToken());
                        }catch(Exception e){}
                        Oye5App.getInstance().setUserInfo(response.toString());
                        ((SignupActivity) getActivity()).goToMainActivity();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response1:" + responseString);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response2:" + obj);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                        getActivity().finish();
                    }
                }
            });
        }catch(Exception e){}
    }

    private void doSigninViaFacebook(String accessToken){
        try {
            JSONObject objParams = new JSONObject();
            objParams.put("fbAuthToken", accessToken);
            objParams.put("gcmId", AppPreference.getGcmID(getActivity()));
            objParams.put("lattitude", AppPreference.getLatitude(getActivity()));
            objParams.put("longitude", AppPreference.getLongitude(getActivity()));
            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.post(getActivity(), getString(R.string.PATH_USER_FB_SIGNIN), objParams, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Facebook Signin response:" + response.toString());
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);

                        String message = response.has("message") ? response.optString("message") : "";

                        if (message.equals("")) {
                            Oye5App.getInstance().setUserInfo(response.toString());
                            ((SignupActivity) getActivity()).goToMainActivity();
                        } else {
                            showToast(message, Toast.LENGTH_SHORT);
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + responseString);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response:" + obj);
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);

                        String message = obj.optString("message");
                        showToast(message, Toast.LENGTH_SHORT);
                    }
                }
            });
        }catch (JSONException eJSON){}
        catch(Exception e){}
    }

    private void fbLoginConfiguration(View parent) {
        btnFBLogin = (LoginButton) parent.findViewById(R.id.btnFBLogin);
        btnFBLogin.setFragment(this);

        // Facebook Login Button Appearance Customization
        float fbIconScale = 1.45F;
        Drawable drawable = getResources().getDrawable(com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale), (int) (drawable.getIntrinsicHeight() * fbIconScale));
        btnFBLogin.setCompoundDrawables(drawable, null, null, null);
        btnFBLogin.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        btnFBLogin.setPadding(
                getResources().getDimensionPixelSize(R.dimen.fb_margin_override_lr),
                getResources().getDimensionPixelSize(R.dimen.fb_margin_override_top),
                0,
                getResources().getDimensionPixelSize(R.dimen.fb_margin_override_bottom));
        btnFBLogin.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.fb_text_size));

        btnFBLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                //doSigninViaFacebook(loginResult.getAccessToken().getToken());
                showToast(loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG);
                /*GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v(getClass().getName(), "Facebook Profile:" + response.toString());
                                doSignupByFacebook(response.getJSONObject());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,first_name,last_name,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();*/
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtContactUs:
                showToast("Contact Us", Toast.LENGTH_SHORT);
                break;
            case R.id.btnFacebookSignIn:
                ((SignupActivity) getActivity()).goToMainActivity();
                break;
            case R.id.btnEmailSignin:
                showSigninEmailPopup();
                break;
            case R.id.btnSignup:
                showSignupEmailPopup();
                break;
            case R.id.btnSignin:
                doSignin("", "");
                break;
            case R.id.btnShowPassword:
                setPasswordVisibility();
                break;
            case R.id.btnClosePopup:
                hideSigninEmailPopup();
                break;
            case R.id.btnEmailSignup:
                doSignup();
                break;
            case R.id.btnSignupShowPassword:
                setSignupPasswordVisibility();
                break;
            case R.id.btnCloseSignupPopup:
                hideSignupEmailPopup();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
