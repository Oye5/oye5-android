package com.android.oye5.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.Oye5App;
import com.android.oye5.R;
import com.android.oye5.activities.MainActivity;
import com.android.oye5.activities.SignupActivity;
import com.android.oye5.adapters.ProfileProductsFragmentAdapter;
import com.android.oye5.dialogs.CustomProgressDialog;
import com.android.oye5.globals.GlobalConstant;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;
import com.android.oye5.listeners.PhotoCropCompleteListener;
import com.android.oye5.models.UserData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.RestClientUtils;
import com.android.oye5.utils.Utils;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Profile tab fragment that has several fragments by it's child fragment manager
 */
public class TabProfileFragment extends BaseFragment implements PageSelectedListener, View.OnClickListener, PhotoCropCompleteListener {

    private CustomProgressDialog progressDialog;

    private ImageView imgProfile;
    private TextView txtName;
    private TextView txtAddress;
    private TextView txtLikedCount;
    private LinearLayout layoutStars;

    private ViewPager pager;
    private ProfileProductsFragmentAdapter pagerAdapter;

    private UserData user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_profile, container, false);

        user = Oye5App.getInstance().getUser(false);

        initView(parent);
        updateUIs();

        return parent;
    }

    private void initView(View parent){
        imgProfile = (ImageView) parent.findViewById(R.id.imgProfile);
        txtName = (TextView) parent.findViewById(R.id.txtName);
        txtAddress = (TextView) parent.findViewById(R.id.txtAddress);
        txtLikedCount = (TextView) parent.findViewById(R.id.txtLikedCount);
        layoutStars = (LinearLayout) parent.findViewById(R.id.layoutStars);

        pager = (ViewPager) parent.findViewById(R.id.pager);
        pagerAdapter = new ProfileProductsFragmentAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);

        initSmartTabs(parent);

        parent.findViewById(R.id.btnSettings).setOnClickListener(this);
        parent.findViewById(R.id.btnLogout).setOnClickListener(this);
        parent.findViewById(R.id.btnModify).setOnClickListener(this);
    }

    private void initSmartTabs(View parent){
        SmartTabLayout tabLayout = (SmartTabLayout) parent.findViewById(R.id.viewpagertab);
        final LinearLayout lyTabs = (LinearLayout) tabLayout.getChildAt(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabsTitleTypeFace(lyTabs, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final LayoutInflater inflater = LayoutInflater.from(tabLayout.getContext());
        final String productsType[] = getResources().getStringArray(R.array.user_products_type_ary);
        tabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
                View viewTab = inflater.inflate(R.layout.view_custom_tab_icon, viewGroup, false);
                ImageView imgTabIcon = (ImageView) viewTab.findViewById(R.id.imgTabIcon);
                imgTabIcon.setVisibility(View.GONE);
                TextView txtTabName = (TextView) viewTab.findViewById(R.id.txtTabName);
                txtTabName.setText(productsType[i]);
                return viewTab;
            }
        });
        tabLayout.setViewPager(pager);
        changeTabsTitleTypeFace(lyTabs, 0);
    }

    private void updateUIs(){
        txtName.setText(user.getName());
        txtAddress.setText("SAN FRANCISCO, CALIFORNIA");
        txtLikedCount.setText(user.getUserRating() + "");

        int nStarCount = (int)user.getUserRating();

        layoutStars.removeAllViews();
        for (int i = 0; i < 5; i++){
            ImageView imgStar = new ImageView(getActivity());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dp_5);
            imgStar.setLayoutParams(params);

            if (i < nStarCount){
                imgStar.setImageResource(R.drawable.ic_star_selected);
            }else{
                imgStar.setImageResource(R.drawable.ic_star_normal);
            }

            layoutStars.addView(imgStar);
        }

        Log.d(getClass().getName(), user.getProfilePicFullURL());
        Glide.with(getActivity())
                .load(user.getProfilePicFullURL())
                .placeholder(R.drawable.bg_loader_default)
                .error(R.drawable.bg_loader_default)
                .into(imgProfile);
    }

    public void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            LinearLayout layoutChild = (LinearLayout) ly.getChildAt(j);
            TextView tvTabTitle = (TextView) ((ViewGroup)layoutChild.getChildAt(0)).getChildAt(1);
            tvTabTitle.setTypeface(null, Typeface.NORMAL);
            if (j == position) tvTabTitle.setTypeface(null, Typeface.BOLD);
        }
    }

    /*private void doOpenMenu(View v){
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.inflate(R.menu.menu_profile);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.action_photo:
                        showToast("Photo", Toast.LENGTH_SHORT);
                        break;
                    case R.id.action_logout:
                        showToast("Logout", Toast.LENGTH_SHORT);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }*/

    private void doLogout(){
        final AlertDialog dlg = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_desc)
                .setPositiveButton(R.string.btn_label_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                ((MainActivity) getActivity()).doLogout();
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

    /** Select user photo to be added from either Gallery or Camera **/
    private void selectPhoto(){
        String lblFromGallery = getString(R.string.choose_from_gallery);
        String lblTakePhoto = getString(R.string.take_a_photo);

        String items[] = {
                lblFromGallery,
                lblTakePhoto};

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle(R.string.photo_change);
        dlg.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ((MainActivity) getActivity()).doChoosePhoto();
                } else if (which == 1) {
                    ((MainActivity) getActivity()).doTakePhoto();
                }
            }
        });
        dlg.show();
    }

    @Override
    public void onCropCompleted() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                doUpdateUser();
            }
        });
    }

    private void doUpdateUser(){
        try {
            RequestParams params = new RequestParams();
            //params.put("firstName", user.getFirstName());
            //params.put("lastName", user.getLastName());
            params.put("image", new File(GlobalConstant.getCropTempFilePath()));

            progressDialog = showProgressDialog(progressDialog, getString(R.string.please_wait));
            RestClientUtils.post(getActivity(), getString(R.string.PATH_USER_UPDATE) + user.getId(), params, true, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i(getClass().getName(), "Update User response:" + response.toString());
                    if (isActivityActive()) {
                        dismissProgressDialog(progressDialog);
                        imgProfile.setImageBitmap(Utils.getSafeDecodeBitmap(GlobalConstant.getCropTempFilePath(), 0));
                        showToast("Updated user successfully!", Toast.LENGTH_SHORT);
                    }
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
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                    }
                }
            });
        }catch(Exception e){}
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSettings:
                selectPhoto();
                break;
            case R.id.btnModify:
                showToast("Modify", Toast.LENGTH_SHORT);
                break;
            case R.id.btnLogout:
                doLogout();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentAttached(4, this);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentAttached(4, this);
        }
    }

    @Override
    public void onDetach() {
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentDetached(4);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentDetached(4);
        }
        super.onDetach();
    }

    @Override
    public void onPageSelected() {
        // TODO Auto-generated method stub
        Log.d(getClass().getName(), "OnPageSelected");
    }
}
