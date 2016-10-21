package com.android.oye5.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.MainActivity;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;

/**
 * Categories tab fragment that has several fragments by it's child fragment manager
 */
public class TabCategoriesFragment extends BaseFragment implements View.OnClickListener, PageSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_categories, container, false);

        initView(parent);

        return parent;
    }

    private void initView(View parent){
        parent.findViewById(R.id.btnCategory1).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory2).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory3).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory4).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory5).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory6).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory7).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory8).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentAttached(1, this);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentAttached(1, this);
        }
    }

    @Override
    public void onDetach() {
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentDetached(1);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentDetached(1);
        }
        super.onDetach();
    }

    @Override
    public void onPageSelected() {
        // TODO Auto-generated method stub
        Log.d(getClass().getName(), "OnPageSelected");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCategory1:
            case R.id.btnCategory2:
            case R.id.btnCategory3:
            case R.id.btnCategory4:
            case R.id.btnCategory5:
            case R.id.btnCategory6:
            case R.id.btnCategory7:
            case R.id.btnCategory8:
                ((MainActivity) getActivity()).goToProductByCategoryScreen(Integer.parseInt(view.getTag().toString()));
                break;
        }
    }
}
