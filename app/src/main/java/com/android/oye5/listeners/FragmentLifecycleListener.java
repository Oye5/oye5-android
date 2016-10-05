package com.android.oye5.listeners;

import android.support.v4.app.Fragment;

/**
 * Tab fragment life cycle manager
 */
public interface FragmentLifecycleListener {

	public void onFragmentAttached(int idx, Fragment fragment);
	
	public void onFragmentDetached(int idx);
	
	public void onFragmentResumed(int idx);
}
