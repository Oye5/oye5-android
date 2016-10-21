package com.android.oye5.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.adapters.BuyProductsGridAdapter;
import com.android.oye5.globals.GlobalConstant;
import com.android.oye5.models.ProductData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.RestClientUtils;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Buy tab fragment that has several fragments by it's child fragment manager
 */
public class ProductsByCategoryFragment extends BaseFragment implements View.OnClickListener{

    private int categoryId = 0;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridView grdProducts;
    private BuyProductsGridAdapter grdAdapter;
    private List<ProductData> dataList = new ArrayList<>();
    private View viewNotFound;
    private TextView txtNotFound;

    public static ProductsByCategoryFragment newInstance(){
        ProductsByCategoryFragment fragment = new ProductsByCategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_products_by_category, container, false);

        categoryId = getArguments().getInt("category_id");
        initView(parent, inflater);
        loadData(false);

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        String[] categoryAry = getResources().getStringArray(R.array.category_ary);
        TextView txtTitle = (TextView) parent.findViewById(R.id.toolbar_title);
        txtTitle.setText(categoryAry[categoryId - 1]);

        progressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);

        swipeRefreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });

        grdProducts = (StaggeredGridView) parent.findViewById(R.id.grdProducts);
        viewNotFound = inflater.inflate(R.layout.view_product_not_found, null);
        txtNotFound = (TextView) viewNotFound.findViewById(R.id.txtNotFound);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displayHeight - getResources().getDimensionPixelSize(R.dimen.dp_100));
        txtNotFound.setLayoutParams(params);
        txtNotFound.setVisibility(View.GONE);
        grdProducts.addHeaderView(viewNotFound);

        grdAdapter = new BuyProductsGridAdapter(getActivity(), inflater, this);
        grdProducts.setAdapter(grdAdapter);

        parent.findViewById(R.id.btnBack).setOnClickListener(this);
    }

    private void showProgressBar(boolean show){
        if (show){
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showNotFoundLabel(){
        if (grdAdapter.getCount() <= 0){
            txtNotFound.setVisibility(View.VISIBLE);
        }else{
            txtNotFound.setVisibility(View.GONE);
        }
    }

    private void loadData(final boolean swipeRefresh){
        try {
            if (!swipeRefresh) showProgressBar(true);
            String url = getString(R.string.PATH_PRODUCT_SERVICE) + "?lattitude=" + AppPreference.getLatitude(getActivity()) + "&longitude=" +
                    AppPreference.getLongitude(getActivity()) + "&categoryId=" + categoryId + "&start=0&num_results=100";
            RestClientUtils.get(getActivity(), url, null, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(getClass().getName(), "Product By Category List response:" + response.toString());
                    if (isActivityActive()) {
                        if (swipeRefresh) swipeRefreshLayout.setRefreshing(false);
                        if (!swipeRefresh) showProgressBar(false);

                        dataList.clear();

                        if (GlobalConstant.isDebug) {
                            dataList.addAll(ProductData.buildDataListFromJSONArray(response));
                            dataList.addAll(ProductData.buildDataListFromJSONArray(response));
                            dataList.addAll(ProductData.buildDataListFromJSONArray(response));
                        }
                        dataList.addAll(ProductData.buildDataListFromJSONArray(response));

                        grdAdapter.setData(dataList);

                        showNotFoundLabel();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response1:" + responseString);
                    if (isActivityActive()) {
                        if (swipeRefresh) swipeRefreshLayout.setRefreshing(false);
                        if (!swipeRefresh) showProgressBar(false);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject obj) {
                    Log.e(getClass().getName(), "Failed, code:" + statusCode + ", response2:" + obj);
                    if (isActivityActive()) {
                        if (swipeRefresh) swipeRefreshLayout.setRefreshing(false);
                        if (!swipeRefresh) showProgressBar(false);
                        showToast(getString(R.string.server_connection_error), Toast.LENGTH_SHORT);
                    }
                }
            });
        }catch(Exception e){}
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.layoutProductItem:
                //((BaseActivity) getActivity()).goToProductDetailsScreen();
                break;
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
        }
    }
}
