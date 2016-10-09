package com.android.oye5.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.activities.ProductPostActivity;
import com.android.oye5.adapters.BuyProductsGridAdapter;
import com.android.oye5.models.ProductData;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.List;

public class ProductPostFragment extends BaseFragment implements View.OnClickListener {

    public static ProductPostFragment newInstance(){
        ProductPostFragment fragment = new ProductPostFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_product_post, container, false);

        initView(parent, inflater);

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        parent.findViewById(R.id.btnBack).setOnClickListener(this);
        parent.findViewById(R.id.btnReset).setOnClickListener(this);

        parent.findViewById(R.id.btnAddPhoto1).setOnClickListener(this);
        parent.findViewById(R.id.btnAddPhoto2).setOnClickListener(this);
        parent.findViewById(R.id.btnAddPhoto3).setOnClickListener(this);

        parent.findViewById(R.id.btnSaveAsDraft).setOnClickListener(this);
        parent.findViewById(R.id.btnPublish).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnAddPhoto1:case R.id.btnAddPhoto2:case R.id.btnAddPhoto3:
                ((ProductPostActivity) getActivity()).goToPhotoSelectScreen();
                break;
            case R.id.btnReset:
                showToast("Reset", Toast.LENGTH_SHORT);
                break;
            case R.id.btnSaveAsDraft:
                showToast("Save As Draft", Toast.LENGTH_SHORT);
                break;
            case R.id.btnPublish:
                showToast("Publish", Toast.LENGTH_SHORT);
                break;
        }
    }
}
