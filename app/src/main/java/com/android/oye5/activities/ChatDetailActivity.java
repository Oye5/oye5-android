package com.android.oye5.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.oye5.R;
import com.android.oye5.fragments.ChatDetailFragment;

public class ChatDetailActivity extends BaseActivity{
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            currentFragment = ChatDetailFragment.newInstance();
            currentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.fragmentContent, currentFragment, "chat_details").commit();
        } else {
            currentFragment = getSupportFragmentManager().findFragmentByTag("chat_details");
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
