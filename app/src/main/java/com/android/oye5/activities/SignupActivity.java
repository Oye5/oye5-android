package com.android.oye5.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.oye5.R;
import com.android.oye5.fragments.SignupFragment;
import com.android.oye5.globals.GlobalConstant;
import com.android.oye5.utils.Utils;

public class SignupActivity extends BaseActivity {

    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            currentFragment = SignupFragment.newInstance();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.fragmentContent, currentFragment, "signup").commit();
        } else {
            currentFragment = getSupportFragmentManager().findFragmentByTag("signup");
        }

        InitStorage();
    }

    /** Initializing local storage folder **/
    private void InitStorage() {
        Utils.CreateWorkDirectories(GlobalConstant.getHomeDirPath(), false);
        Utils.CreateWorkDirectories(GlobalConstant.getTempDirpath(), false);
    }

    public void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.appear_from_right, R.anim.disappear_to_left);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        FragmentManager fm = getSupportFragmentManager();

        int count = fm.getBackStackEntryCount();
        if (count <= 1) {
            finish();
        } else {
            fm.popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (currentFragment != null) currentFragment.onActivityResult(requestCode, resultCode, data);
    }
}
