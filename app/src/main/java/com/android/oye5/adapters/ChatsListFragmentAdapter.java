package com.android.oye5.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.oye5.fragments.ChatsListFragment;
import com.android.oye5.fragments.ProductsGridFragment;

import java.util.ArrayList;

public class ChatsListFragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments = new ArrayList<>();
    FragmentManager mFragmentManager;

    public ChatsListFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentManager = fragmentManager;

        mFragments.add(ChatsListFragment.newInstance(0));
        mFragments.add(ChatsListFragment.newInstance(1));
        mFragments.add(ChatsListFragment.newInstance(2));
        mFragments.add(ChatsListFragment.newInstance(3));
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
