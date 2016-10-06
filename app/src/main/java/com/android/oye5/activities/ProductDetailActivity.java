package com.android.oye5.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.oye5.R;
import com.android.oye5.fragments.ChatDetailFragment;
import com.android.oye5.fragments.ProductDetailsFragment;

public class ProductDetailActivity extends BaseActivity{
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            currentFragment = ProductDetailsFragment.newInstance();
            currentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.fragmentContent, currentFragment, "product_details").commit();
        } else {
            currentFragment = getSupportFragmentManager().findFragmentByTag("product_details");
        }

    }

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
