package com.android.oye5.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.oye5.R;
import com.android.oye5.fragments.PhotoSelectFragment;
import com.android.oye5.fragments.ProductPostFragment;
import com.android.oye5.fragments.ProductsByCategoryFragment;

public class ProductByCategoryActivity extends BaseActivity{
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            currentFragment = ProductsByCategoryFragment.newInstance();
            currentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.fragmentContent, currentFragment, "product_by_category").commit();
        } else {
            currentFragment = getSupportFragmentManager().findFragmentByTag("product_by_category");
        }

    }

    /*
    public void goToPhotoSelectScreen(){
        currentFragment = PhotoSelectFragment.newInstance();
        currentFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.fragmentContent, currentFragment, "photo_select").commit();
    }*/

    private void doGoBack(){
        FragmentManager fm = getSupportFragmentManager();

        int count = fm.getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            fm.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        doGoBack();
    }

}
