package com.android.oye5.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.oye5.R;
import com.android.oye5.models.PhotoData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressLint("InflateParams")
public class PhotosSelectGridAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<PhotoData> dataList;
    private OnClickListener mClickListener;
    private int mItemSize;

    public PhotosSelectGridAdapter(Context ctx, LayoutInflater inflater, int nSize, OnClickListener onClickListener) {
        this.ctx = ctx;
        this.inflater = inflater;
        this.dataList = new ArrayList<>();
        this.mClickListener = onClickListener;
        this.mItemSize = nSize;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public PhotoData getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public List<PhotoData> getListData() {
        return this.dataList;
    }

    public void setData(List<PhotoData> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void appendData(Collection<PhotoData> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void insertData(PhotoData note, int index) {
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
            convertView = inflater.inflate(R.layout.view_grid_photo_item, parent, false);

            holder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.imgPhoto.getLayoutParams();
            params.width = mItemSize;
            params.height = mItemSize;
            holder.imgPhoto.setLayoutParams(params);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoData data = dataList.get(position);

        holder.imgPhoto.setImageResource(data.getResId());

        return convertView;
    }

    /**
     * View holder for efficiency.
     */
    static class ViewHolder {
        ImageView imgPhoto;
    }
}