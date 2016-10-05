package com.android.oye5.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.adapters.ProfileProductsGridAdapter;
import com.android.oye5.models.ProductData;

import java.util.ArrayList;
import java.util.List;

/**
 * Products Grid View fragment, that is for profile page
 */
public class ProductsGridFragment extends BaseFragment implements View.OnClickListener{

    private GridView gridView;
    private ProfileProductsGridAdapter gridAdapter;

    private List<ProductData> dataList = new ArrayList<>();

    public static ProductsGridFragment newInstance() {
        ProductsGridFragment fragment = new ProductsGridFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_products_grid, container, false);

        initView(parent, inflater);
        loadData();

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        gridView = (GridView) parent.findViewById(R.id.gridView);

        //Configuring the Grid Views with Adapter
        float fMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
        int nCols = getResources().getInteger(R.integer.profile_product_grid_col_count);
        float fSellSize = (displayWidth - fMargin * (nCols + 1)) / nCols;
        gridView.setVerticalSpacing((int) fMargin);
        gridView.setHorizontalSpacing((int) fMargin);
        gridAdapter = new ProfileProductsGridAdapter(getActivity(), inflater, (int) fSellSize, this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showToast("Item Clicked!", Toast.LENGTH_SHORT);
            }
        });
    }

    private void loadData(){
        for (int i = 0; i < 6; i++){
            ProductData data = new ProductData();
            data.setName("Product Name " + (i + 1));
            data.setPrice(50 + i);
            data.setFavorites(1200 + i);
            dataList.add(data);
        }

        gridAdapter.setData(dataList);
    }

    @Override
    public void onClick(View view) {

    }
}
