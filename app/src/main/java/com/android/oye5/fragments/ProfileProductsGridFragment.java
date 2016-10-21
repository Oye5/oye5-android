package com.android.oye5.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.Oye5App;
import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.adapters.ProfileProductsGridAdapter;
import com.android.oye5.models.ProductData;
import com.android.oye5.models.UserData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.RestClientUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * Products Grid View fragment, that is for profile page
 */
public class ProfileProductsGridFragment extends BaseFragment implements View.OnClickListener{

    public static final int TYPE_BUYING = 1;
    public static final int TYPE_SELLING = 2;
    public static final int TYPE_SOLD = 3;
    public static final int TYPE_FAVORITES = 4;

    private int productType = 0;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridViewWithHeaderAndFooter gridView;
    private ProfileProductsGridAdapter gridAdapter;
    private View viewNotFound;
    private TextView txtNotFound;

    private List<ProductData> dataList = new ArrayList<>();

    private UserData user;

    public static ProfileProductsGridFragment newInstance(int type) {
        ProfileProductsGridFragment fragment = new ProfileProductsGridFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_profile_products_grid, container, false);

        productType = getArguments().getInt("type");
        user = Oye5App.getInstance().getUser(false);

        initView(parent, inflater);
        loadData(false);

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
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
        gridView = (GridViewWithHeaderAndFooter) parent.findViewById(R.id.gridView);
        viewNotFound = inflater.inflate(R.layout.view_product_not_found, null);
        txtNotFound = (TextView) viewNotFound.findViewById(R.id.txtNotFound);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.dp_200));
        txtNotFound.setLayoutParams(params);
        gridView.addHeaderView(viewNotFound);
        txtNotFound.setVisibility(View.GONE);

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
                //((BaseActivity) getActivity()).goToProductDetailsScreen();
            }
        });
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
        if (gridAdapter.getCount() <= 0){
            txtNotFound.setVisibility(View.VISIBLE);
        }else{
            txtNotFound.setVisibility(View.GONE);
        }
    }

    private void loadData(final boolean swipeRefresh){
        try {
            if (!swipeRefresh) showProgressBar(true);

            String url = "";
            if (productType == TYPE_BUYING){
                url = getString(R.string.PATH_USER_GET_BUYING, user.getId()) + "?num_results=100";
                //url = getString(R.string.PATH_PRODUCT_SERVICE) + "?lattitude=" + AppPreference.getLatitude(getActivity()) + "&longitude=" +
                        //AppPreference.getLongitude(getActivity()) + "&start=0&num_results=100";
            }else if (productType == TYPE_SELLING){
                url = getString(R.string.PATH_USER_GET_SELLING) + user.getId();
            }else if (productType == TYPE_FAVORITES){
                url = getString(R.string.PATH_USER_GET_FAVORITE, user.getId()) + "?num_results=100";
            }

            RestClientUtils.get(getActivity(), url, null, true, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(getClass().getName(), "User Product List response:" + response.toString());
                    if (isActivityActive()) {
                        if (swipeRefresh) swipeRefreshLayout.setRefreshing(false);
                        if (!swipeRefresh) showProgressBar(false);

                        dataList.clear();
                        dataList.addAll(ProductData.buildDataListFromJSONArray(response));

                        gridAdapter.setData(dataList);

                        showNotFoundLabel();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
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

    }
}
