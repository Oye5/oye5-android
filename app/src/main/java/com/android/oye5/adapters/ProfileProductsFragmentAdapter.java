package com.android.oye5.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.oye5.fragments.ProductsGridFragment;

import java.util.ArrayList;

public class ProfileProductsFragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments = new ArrayList<>();
    FragmentManager mFragmentManager;

    public ProfileProductsFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;

        mFragments.add(ProductsGridFragment.newInstance());
        mFragments.add(ProductsGridFragment.newInstance());
        mFragments.add(ProductsGridFragment.newInstance());
        mFragments.add(ProductsGridFragment.newInstance());
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
