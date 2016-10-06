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
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;

/**
 * Categories tab fragment that has several fragments by it's child fragment manager
 */
public class TabCategoriesFragment extends BaseFragment implements View.OnClickListener, PageSelectedListener {

    private EditText edtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_categories, container, false);

        initView(parent);

        return parent;
    }

    private void initView(View parent){
        edtSearch = (EditText) parent.findViewById(R.id.edtSearch);

        parent.findViewById(R.id.btnCategory1).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory2).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory3).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory4).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory5).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory6).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory7).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory8).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory9).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory10).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory11).setOnClickListener(this);
        parent.findViewById(R.id.btnCategory12).setOnClickListener(this);

        parent.findViewById(R.id.btnSearch).setOnClickListener(this);
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
            case R.id.btnCategory9:
            case R.id.btnCategory10:
            case R.id.btnCategory11:
            case R.id.btnCategory12:
                showToast("Category Clicked!", Toast.LENGTH_SHORT);
                break;
            case R.id.btnSearch:
                showToast("Search!", Toast.LENGTH_SHORT);
                break;
        }
    }
}
