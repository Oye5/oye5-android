package com.android.oye5.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.activities.MainActivity;
import com.android.oye5.adapters.BuyProductsGridAdapter;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;
import com.android.oye5.models.ProductData;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Buy tab fragment that has several fragments by it's child fragment manager
 */
public class ProductDetailsFragment extends BaseFragment implements View.OnClickListener {

    private StaggeredGridView grdProducts;
    private BuyProductsGridAdapter grdAdapter;
    private List<ProductData> dataList = new ArrayList<>();

    public static ProductDetailsFragment newInstance(){
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_product_details, container, false);

        initView(parent, inflater);
        loadData();

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        grdProducts = (StaggeredGridView) parent.findViewById(R.id.grdProducts);
        View headerView = inflater.inflate(R.layout.view_product_details_header, null);
        grdProducts.addHeaderView(headerView);

        grdAdapter = new BuyProductsGridAdapter(getActivity(), inflater, this);
        grdProducts.setAdapter(grdAdapter);

        headerView.findViewById(R.id.btnBack).setOnClickListener(this);
        headerView.findViewById(R.id.btnShare).setOnClickListener(this);
        headerView.findViewById(R.id.btnMenu).setOnClickListener(this);
        headerView.findViewById(R.id.btnMakeOffer).setOnClickListener(this);
        headerView.findViewById(R.id.btnMessage).setOnClickListener(this);
    }

    private void loadData(){
        ProductData data1 = new ProductData();
        data1.setImgResId(R.drawable.img_product_sample_1);
        data1.setName("Longboard - Board Zone Brand New!");
        data1.setPrice(510);
        data1.setDistance(20);

        ProductData data2 = new ProductData();
        data2.setImgResId(R.drawable.img_product_sample_2);
        data2.setName("Bike New!");
        data2.setPrice(1000);
        data2.setDistance(12);

        ProductData data3 = new ProductData();
        data3.setImgResId(R.drawable.img_product_sample_3);
        data3.setName("Garmin Approach x 40 Smart Golf Band");
        data3.setPrice(555);
        data3.setDistance(10);

        ProductData data4 = new ProductData();
        data4.setImgResId(R.drawable.img_product_sample_4);
        data4.setName("Product Sample");
        data4.setPrice(10);
        data4.setDistance(5);

        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        dataList.add(data4);

        grdAdapter.setData(dataList);
    }

    private void goToProductDetails(View view){
        ProductData data = (ProductData) view.getTag();
        ((BaseActivity) getActivity()).goToProductDetailsScreen();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.layoutProductItem:
                goToProductDetails(view);
                break;
            case R.id.btnMenu:
                showToast("Menu Clicked!", Toast.LENGTH_SHORT);
                break;
            case R.id.btnShare:
                showToast("Share Clicked!", Toast.LENGTH_SHORT);
                break;
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnMakeOffer:
                showToast("Make Offer", Toast.LENGTH_SHORT);
                break;
            case R.id.btnMessage:
                showToast("Message Offer", Toast.LENGTH_SHORT);
                break;
        }
    }
}
