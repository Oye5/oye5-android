package com.android.oye5.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.android.oye5.fragments.TabBuyFragment;
import com.android.oye5.fragments.TabCategoriesFragment;
import com.android.oye5.fragments.TabChatFragment;
import com.android.oye5.fragments.TabProfileFragment;
import com.android.oye5.fragments.TabSellFragment;
import com.android.oye5.listeners.FragmentLifecycleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Pager Adapter for View Pager
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
	
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		
		fragmentList.add(new TabBuyFragment());
		fragmentList.add(new TabCategoriesFragment());
		fragmentList.add(new TabSellFragment());
		fragmentList.add(new TabChatFragment());
		fragmentList.add(new TabProfileFragment());
	}
	
	private FragmentLifecycleListener listener;

	public void setFragmentLifecycleListener(FragmentLifecycleListener listener) {
		this.listener = listener;
	}
	
	@Override
	public Fragment getItem(int pos) {
		return fragmentList.get(pos);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		if( listener != null ) {
			listener.onFragmentResumed(position);
		}
		return fragment;
	}
}
