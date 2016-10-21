package com.android.oye5.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.oye5.fragments.ProfileProductsGridFragment;

import java.util.ArrayList;

public class ProfileProductsFragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments = new ArrayList<>();
    FragmentManager mFragmentManager;

    public ProfileProductsFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;

        mFragments.add(ProfileProductsGridFragment.newInstance(ProfileProductsGridFragment.TYPE_BUYING));
        mFragments.add(ProfileProductsGridFragment.newInstance(ProfileProductsGridFragment.TYPE_SELLING));
        //mFragments.add(ProfileProductsGridFragment.newInstance());
        mFragments.add(ProfileProductsGridFragment.newInstance(ProfileProductsGridFragment.TYPE_FAVORITES));
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
