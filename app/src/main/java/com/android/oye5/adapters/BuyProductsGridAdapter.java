package com.android.oye5.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.oye5.R;
import com.android.oye5.models.ProductData;
import com.etsy.android.grid.util.DynamicHeightImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@SuppressLint("InflateParams")
public class BuyProductsGridAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<ProductData> dataList;
    private OnClickListener mClickListener;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final Random mRandom;

    public BuyProductsGridAdapter(Context ctx, LayoutInflater inflater, OnClickListener onClickListener) {
        this.ctx = ctx;
        this.inflater = inflater;
        this.dataList = new ArrayList<>();
        this.mClickListener = onClickListener;
        mRandom = new Random();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ProductData getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public List<ProductData> getListData() {
        return this.dataList;
    }

    public void setData(List<ProductData> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void appendData(Collection<ProductData> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void insertData(ProductData note, int index) {
        this.dataList.add(index, note);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (this.dataList.isEmpty()) {
            return;
        }
        this.dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_stag_grid_product_item, parent, false);

            holder.layoutProductItem = (LinearLayout) convertView.findViewById(R.id.layoutProductItem);
            holder.imgPhoto = (DynamicHeightImageView) convertView.findViewById(R.id.imgPhoto);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txtDistance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        holder.imgPhoto.setHeightRatio(positionHeight);

        ProductData data = dataList.get(position);

        holder.imgPhoto.setImageResource(data.getImgResId());
        holder.txtName.setText(data.getName());
        holder.txtDistance.setText(data.getDistance() + "km");
        holder.txtPrice.setText("$" + data.getPrice());

        holder.layoutProductItem.setTag(data);
        holder.layoutProductItem.setOnClickListener(mClickListener);

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    /**
     * View holder for efficiency.
     */
    static class ViewHolder {
        LinearLayout layoutProductItem;
        DynamicHeightImageView imgPhoto;
        TextView txtName;
        TextView txtPrice;
        TextView txtDistance;
    }
}