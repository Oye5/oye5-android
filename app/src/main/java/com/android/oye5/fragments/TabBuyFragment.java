package com.android.oye5.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.Oye5App;
import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.activities.MainActivity;
import com.android.oye5.activities.SignupActivity;
import com.android.oye5.adapters.BuyProductsGridAdapter;
import com.android.oye5.globals.GlobalConstant;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;
import com.android.oye5.models.ProductData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.RestClientUtils;
import com.android.oye5.utils.Utils;
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
public class TabBuyFragment extends BaseFragment implements View.OnClickListener, PageSelectedListener {
    private EditText edtSearch;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridView grdProducts;
    private BuyProductsGridAdapter grdAdapter;
    private List<ProductData> dataList = new ArrayList<>();
    private View viewNotFound;
    private TextView txtNotFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_buy, container, false);

        initView(parent, inflater);
        loadData(false);

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        edtSearch = (EditText) parent.findViewById(R.id.edtSearch);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }

                return false;
            }
        });

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

        parent.findViewById(R.id.btnFilter).setOnClickListener(this);
        parent.findViewById(R.id.btnSearch).setOnClickListener(this);
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

    private void doSearch(){
        Utils.showKeyboard(getActivity(), false, edtSearch);
        swipeRefreshLayout.setRefreshing(true);
        loadData(true);
    }

    private void loadData(final boolean swipeRefresh){
        try {
            if (!swipeRefresh) showProgressBar(true);
            String url = getString(R.string.PATH_PRODUCT_SERVICE) + "?lattitude=" + AppPreference.getLatitude(getActivity()) + "&longitude=" +
                    AppPreference.getLongitude(getActivity()) + "&start=0&num_results=100";
            String query = edtSearch.getText().toString();
            if (!query.equals("")) url += "&q=" + query;
            RestClientUtils.get(getActivity(), url, null, false, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(getClass().getName(), "Product List response:" + response.toString());
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
                ((BaseActivity) getActivity()).goToProductDetailsScreen((ProductData) view.getTag());
                break;
            case R.id.btnFilter:
                ((BaseActivity) getActivity()).goToFilterScreen();
                break;
            case R.id.btnSearch:
                doSearch();
                break;
        }
    }
}
