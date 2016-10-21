package com.android.oye5.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.oye5.R;
import com.android.oye5.models.ImageData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductPhotosPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ImageData> dataList = new ArrayList<>();

    public ProductPhotosPagerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void setData(List<ImageData> _dataList) {
        dataList.clear();
        dataList.addAll(_dataList);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public ImageData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ViewGroup) object);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ViewGroup) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout viewGroup = new RelativeLayout(mContext);
        ImageView imgPhoto = new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imgPhoto.setLayoutParams(params);
        imgPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(mContext)
                .load(dataList.get(position).getUrl())
                .placeholder(R.drawable.bg_loader_default)
                .error(R.drawable.bg_loader_default)
                .into(imgPhoto);

        viewGroup.addView(imgPhoto);

        container.addView(viewGroup, 0);

        return viewGroup;
    }
}