package com.android.oye5.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class TabBuyFragment extends BaseFragment implements View.OnClickListener, PageSelectedListener {

    private StaggeredGridView grdProducts;
    private BuyProductsGridAdapter grdAdapter;
    private List<ProductData> dataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_buy, container, false);

        initView(parent, inflater);
        loadData();

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        grdProducts = (StaggeredGridView) parent.findViewById(R.id.grdProducts);
        grdAdapter = new BuyProductsGridAdapter(getActivity(), inflater, this);
        grdProducts.setAdapter(grdAdapter);

        parent.findViewById(R.id.btnFilter).setOnClickListener(this);
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
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        dataList.add(data4);
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        dataList.add(data4);

        grdAdapter.setData(dataList);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.layoutProductItem:
                ((BaseActivity) getActivity()).goToProductDetailsScreen();
                break;
            case R.id.btnFilter:
                ((BaseActivity) getActivity()).goToFilterScreen();
                break;
        }
    }
}
