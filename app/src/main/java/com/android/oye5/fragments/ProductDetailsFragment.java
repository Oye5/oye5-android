package com.android.oye5.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.Oye5App;
import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.adapters.BuyProductsGridAdapter;
import com.android.oye5.adapters.ProductPhotosPagerAdapter;
import com.android.oye5.dialogs.CustomProgressDialog;
import com.android.oye5.globals.GlobalConstant;
import com.android.oye5.libs.CircleImageView;
import com.android.oye5.models.ProductData;
import com.android.oye5.preferences.AppPreference;
import com.android.oye5.utils.RestClientUtils;
import com.bumptech.glide.Glide;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Buy tab fragment that has several fragments by it's child fragment manager
 */
public class ProductDetailsFragment extends BaseFragment implements View.OnClickListener {

    private CustomProgressDialog progressDialog;

    private ProductData product;

    private StaggeredGridView grdProducts;
    private BuyProductsGridAdapter grdAdapter;
    private List<ProductData> dataList = new ArrayList<>();

    private RelativeLayout layoutPhoto;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private ProductPhotosPagerAdapter mPagerAdapter;

    private CircleImageView imgUserPhoto;
    private TextView txtUserName;
    private LinearLayout layoutStars;
    private TextView txtLikedCount;
    private Button btnFavorite;
    private TextView txtProductName;
    private TextView txtPrice;
    private TextView txtCondition;
    private Button btnMakeOffer;
    private TextView txtAddedDate;
    private TextView txtCategory;
    private TextView txtDescription;

    public static ProductDetailsFragment newInstance(){
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_product_details, container, false);

        product = (ProductData) getArguments().get("product");

        initView(parent, inflater);
        updateUIs();
        loadData();

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        grdProducts = (StaggeredGridView) parent.findViewById(R.id.grdProducts);
        View headerView = inflater.inflate(R.layout.view_product_details_header, null);
        grdProducts.addHeaderView(headerView);

        grdAdapter = new BuyProductsGridAdapter(getActivity(), inflater, this);
        grdProducts.setAdapter(grdAdapter);

        /** Initialize user photo pager views **/
        layoutPhoto = (RelativeLayout) headerView.findViewById(R.id.layoutPhoto);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutPhoto.getLayoutParams();
        params.width = displayWidth;
        params.height = getResources().getDimensionPixelSize(R.dimen.dp_250);
        layoutPhoto.setLayoutParams(params);

        mPager = (ViewPager) headerView.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        setupViewPager();

        imgUserPhoto = (CircleImageView) headerView.findViewById(R.id.imgUserPhoto);
        txtUserName = (TextView) headerView.findViewById(R.id.txtUserName);
        layoutStars = (LinearLayout) headerView.findViewById(R.id.layoutStars);
        txtLikedCount = (TextView) headerView.findViewById(R.id.txtLikedCount);
        btnFavorite = (Button) headerView.findViewById(R.id.btnFavorite);
        txtProductName = (TextView) headerView.findViewById(R.id.txtProductName);
        txtPrice = (TextView) headerView.findViewById(R.id.txtPrice);
        txtCondition = (TextView) headerView.findViewById(R.id.txtCondition);
        btnMakeOffer = (Button) headerView.findViewById(R.id.btnMakeOffer);
        txtAddedDate = (TextView) headerView.findViewById(R.id.txtAddedDate);
        txtCategory = (TextView) headerView.findViewById(R.id.txtCategory);
        txtDescription = (TextView) headerView.findViewById(R.id.txtDescription);

        btnFavorite.setOnClickListener(this);
        headerView.findViewById(R.id.btnBack).setOnClickListener(this);
        headerView.findViewById(R.id.btnShare).setOnClickListener(this);
        headerView.findViewById(R.id.btnMenu).setOnClickListener(this);
        headerView.findViewById(R.id.btnMakeOffer).setOnClickListener(this);
        headerView.findViewById(R.id.btnFavorite).setOnClickListener(this);
    }

    private void updateUIs(){
        // Showing product images in viewPager
        mPagerAdapter.setData(product.getImagesList());

        /*Glide.with(getActivity())
                .load(product.getSeller().getProfilePicFullURL())
                .placeholder(R.drawable.bg_loader_default)
                .error(R.drawable.bg_loader_default)
                .into(imgUserPhoto);*/
        ImageLoader.getInstance().displayImage(product.getSeller().getProfilePicFullURL(), imgUserPhoto, Oye5App.getInstance().getDisplayOptions());

        txtUserName.setText(product.getSeller().getName());
        txtLikedCount.setText(product.getSeller().getUserRating() + "");

        int nStarCount = (int)product.getSeller().getUserRating();

        layoutStars.removeAllViews();
        for (int i = 0; i < 5; i++){
            ImageView imgStar = new ImageView(getActivity());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dp_5);
            imgStar.setLayoutParams(params);

            if (i < nStarCount){
                imgStar.setImageResource(R.drawable.ic_star_selected);
            }else{
                imgStar.setImageResource(R.drawable.ic_star_normal);
            }

            layoutStars.addView(imgStar);
        }

        txtProductName.setText(product.getDisplayName());
        txtPrice.setText(product.getCurrency() + product.getPrice());
        txtCondition.setText(getString(R.string.condition, product.getCondition()));
        String[] categoryAry = getResources().getStringArray(R.array.category_ary);
        txtCategory.setText(getString(R.string.category, categoryAry[product.getCategoryId() - 1]));
        txtAddedDate.setText(getString(R.string.added, product.getAddedDateString()));
        txtDescription.setText(product.getDescription());
    }

    /** Setup user photo's view pager **/
    private void setupViewPager() {
        mPagerAdapter = new ProductPhotosPagerAdapter(getActivity());
        mPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mPager);
    }

    private void loadData(){


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

    private void goToProductDetails(View view){
        ProductData data = (ProductData) view.getTag();
        ((BaseActivity) getActivity()).goToProductDetailsScreen(data);
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
            case R.id.btnFavorite:
                showToast("Message Offer", Toast.LENGTH_SHORT);
                break;
        }
    }
}
