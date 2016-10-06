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
import android.widget.TextView;

import com.android.oye5.R;
import com.android.oye5.listeners.ICustomCallback;
import com.android.oye5.models.ChatData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressLint("InflateParams")
public class ChatsListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<ChatData> dataList;
    private OnClickListener mClickListener;

    private ICustomCallback mListener = null;
    private int mRightWidth = 0;
    private int mCenterWidth = 0;

    public ChatsListAdapter(Context ctx, LayoutInflater inflater, int centerWidth, int rightWidth, ICustomCallback l, OnClickListener onClickListener) {
        this.ctx = ctx;
        this.inflater = inflater;
        this.dataList = new ArrayList<>();
        this.mClickListener = onClickListener;
        mCenterWidth = centerWidth;
        mRightWidth = rightWidth;
        mListener = l;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ChatData getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public List<ChatData> getListData() {
        return this.dataList;
    }

    public void setData(List<ChatData> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void appendData(Collection<ChatData> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void insertData(ChatData note, int index) {
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
            convertView = inflater.inflate(R.layout.list_chat_item, parent, false);

            holder.imgProduct = (ImageView) convertView.findViewById(R.id.imgProduct);
            holder.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            holder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.imgUserPhoto = (ImageView) convertView.findViewById(R.id.imgUserPhoto);
            holder.btnDelete = (LinearLayout) convertView.findViewById(R.id.btnDelete);
            holder.item_center = (View)convertView.findViewById(R.id.item_center);
            holder.item_right = (View)convertView.findViewById(R.id.item_right);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) holder.item_center.getLayoutParams();
        lp1.width = mCenterWidth;
        holder.item_center.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        ChatData data = dataList.get(position);

        holder.imgProduct.setImageResource(R.drawable.img_product_sample);
        holder.txtUserName.setText(data.getUserName());
        holder.txtProductName.setText(data.getProductName());
        holder.txtTime.setText(data.getChatTime());
        holder.imgUserPhoto.setImageResource(R.drawable.img_sample_user);

        final int thisPosition = position;
        final View view = convertView;
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.callOnClick(thisPosition, 0, view);
            }
        });

        return convertView;
    }

    /**
     * View holder for efficiency.
     */
    static class ViewHolder {
        ImageView imgProduct;
        TextView txtProductName;
        TextView txtUserName;
        TextView txtTime;
        ImageView imgUserPhoto;
        LinearLayout btnDelete;
        View item_center;
        View item_right;
    }
}