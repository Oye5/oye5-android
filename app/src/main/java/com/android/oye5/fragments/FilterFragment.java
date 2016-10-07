package com.android.oye5.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.oye5.R;

/**
 * Filter fragment
 */
public class FilterFragment extends BaseFragment implements View.OnClickListener {

    public static FilterFragment newInstance(){
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_filter, container, false);

        initView(parent, inflater);

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        parent.findViewById(R.id.btnBack).setOnClickListener(this);
        parent.findViewById(R.id.btnReset).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnReset:
                showToast("Reset", Toast.LENGTH_SHORT);
                break;
        }
    }
}
