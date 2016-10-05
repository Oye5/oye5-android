package com.android.oye5.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.oye5.R;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;

/**
 * Buy tab fragment that has several fragments by it's child fragment manager
 */
public class TabBuyFragment extends BaseFragment implements PageSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_buy, container, false);
        return parent;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentAttached(0, this);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentAttached(0, this);
        }
    }

    @Override
    public void onDetach() {
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentDetached(0);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentDetached(0);
        }
        super.onDetach();
    }

    @Override
    public void onPageSelected() {
        // TODO Auto-generated method stub
        Log.d(getClass().getName(), "OnPageSelected");
    }
}
