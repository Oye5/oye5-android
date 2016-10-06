package com.android.oye5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.adapters.MainPagerAdapter;
import com.android.oye5.globals.GlobalConstant;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;

public class MainActivity extends BaseActivity implements View.OnClickListener, FragmentLifecycleListener {

    private LinearLayout layoutBuySelected, layoutCategoriesSelected, layoutSellSelected, layoutChatSelected, layoutProfileSelected;
    private LinearLayout btnBuy, btnCategories, btnSell, btnChat, btnProfile;

    private static final int BACK_PRESS_TIME_INTERVAL = 3000;
    private long mPrevBackPressed = 0;

    /** Tab Pages Viewer & Adapters **/
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private ViewPager mPager;
    private MainPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutBuySelected = (LinearLayout) findViewById(R.id.layoutBuySelected);
        layoutCategoriesSelected = (LinearLayout) findViewById(R.id.layoutCategoriesSelected);
        layoutSellSelected = (LinearLayout) findViewById(R.id.layoutSellSelected);
        layoutChatSelected = (LinearLayout) findViewById(R.id.layoutChatSelected);
        layoutProfileSelected = (LinearLayout) findViewById(R.id.layoutProfileSelected);

        btnBuy = (LinearLayout) findViewById(R.id.btnBuy);
        btnCategories = (LinearLayout) findViewById(R.id.btnCategories);
        btnSell = (LinearLayout) findViewById(R.id.btnSell);
        btnChat = (LinearLayout) findViewById(R.id.btnChat);
        btnProfile = (LinearLayout) findViewById(R.id.btnProfile);

        btnBuy.setOnClickListener(this);
        btnCategories.setOnClickListener(this);
        btnSell.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnProfile.setOnClickListener(this);

        initPager();

        selectMenu(GlobalConstant.MENU_PROFILE, true);
    }

    private void selectMenu(int nIndex, boolean bPageChange){
        layoutBuySelected.setVisibility(View.INVISIBLE);
        layoutCategoriesSelected.setVisibility(View.INVISIBLE);
        layoutSellSelected.setVisibility(View.INVISIBLE);
        layoutChatSelected.setVisibility(View.INVISIBLE);
        layoutProfileSelected.setVisibility(View.INVISIBLE);

        btnBuy.setClickable(true);
        btnCategories.setClickable(true);
        btnSell.setClickable(true);
        btnChat.setClickable(true);
        btnProfile.setClickable(true);

        if (nIndex == GlobalConstant.MENU_BUY){
            layoutBuySelected.setVisibility(View.VISIBLE);
            btnBuy.setClickable(false);
        }else if (nIndex == GlobalConstant.MENU_CATEGORIES){
            layoutCategoriesSelected.setVisibility(View.VISIBLE);
            btnCategories.setClickable(false);
        }else if (nIndex == GlobalConstant.MENU_SELL){
            layoutSellSelected.setVisibility(View.VISIBLE);
            btnSell.setClickable(false);
        }else if (nIndex == GlobalConstant.MENU_CHAT){
            layoutChatSelected.setVisibility(View.VISIBLE);
            btnChat.setClickable(false);
        }else if (nIndex == GlobalConstant.MENU_PROFILE){
            layoutProfileSelected.setVisibility(View.VISIBLE);
            btnProfile.setClickable(false);
        }

        if (bPageChange){
            mPager.setCurrentItem(nIndex - 1);
        }
    }

    /** Initialize **/
    private void initPager(){
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(5);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                Fragment fragment = getRegisteredFragment(position);
                if (fragment instanceof PageSelectedListener) {
                    ((PageSelectedListener) fragment).onPageSelected();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub
            }
        });
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void onFragmentAttached(int idx, Fragment fragment) {
        // TODO Auto-generated method stub
        registeredFragments.append(idx, fragment);
    }

    @Override
    public void onFragmentDetached(int idx) {
        // TODO Auto-generated method stub
        registeredFragments.remove(idx);
    }

    @Override
    public void onFragmentResumed(int idx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getRegisteredFragment(mPager.getCurrentItem());
        Log.i(getClass().getName(), "onBackPressed:" + fragment);

        if ((System.currentTimeMillis() - mPrevBackPressed) < BACK_PRESS_TIME_INTERVAL) {
            finish();
        } else {
            showToast(getString(R.string.press_back_again), Toast.LENGTH_SHORT);
            mPrevBackPressed = System.currentTimeMillis();
        }
    }

    public void goToChatDetailsScreen(){
        Intent intent = new Intent(this, ChatDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBuy:
                selectMenu(GlobalConstant.MENU_BUY, true);
                break;
            case R.id.btnCategories:
                selectMenu(GlobalConstant.MENU_CATEGORIES, true);
                break;
            case R.id.btnSell:
                selectMenu(GlobalConstant.MENU_SELL, true);
                break;
            case R.id.btnChat:
                selectMenu(GlobalConstant.MENU_CHAT, true);
                break;
            case R.id.btnProfile:
                selectMenu(GlobalConstant.MENU_PROFILE, true);
                break;
        }
    }
}
