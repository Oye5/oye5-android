package com.android.oye5.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.oye5.R;
import com.android.oye5.listeners.ICustomCallback;
import com.android.oye5.models.ChatHistoryData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressLint("InflateParams")
public class ChatsHistoryListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<ChatHistoryData> dataList;
    private OnClickListener mClickListener;

    public ChatsHistoryListAdapter(Context ctx, LayoutInflater inflater, OnClickListener onClickListener) {
        this.ctx = ctx;
        this.inflater = inflater;
        this.dataList = new ArrayList<>();
        this.mClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ChatHistoryData getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public List<ChatHistoryData> getListData() {
        return this.dataList;
    }

    public void setData(List<ChatHistoryData> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void appendData(Collection<ChatHistoryData> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void appendData(ChatHistoryData data) {
        this.dataList.add(data);
        notifyDataSetChanged();
    }

    public void insertData(ChatHistoryData note, int index) {
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
            convertView = inflater.inflate(R.layout.list_chat_history_item, parent, false);

            holder.layoutChatIn = (LinearLayout) convertView.findViewById(R.id.layoutChatIn);
            holder.layoutChatOut = (LinearLayout) convertView.findViewById(R.id.layoutChatOut);
            holder.txtInMessage = (TextView) convertView.findViewById(R.id.txtInMessage);
            holder.txtOutMessage = (TextView) convertView.findViewById(R.id.txtOutMessage);
            holder.txtInTime = (TextView) convertView.findViewById(R.id.txtInTime);
            holder.txtOutTime = (TextView) convertView.findViewById(R.id.txtOutTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatHistoryData data = dataList.get(position);

        if (data.getMessageType() == ChatHistoryData.CHAT_TYPE_IN) {
            holder.layoutChatIn.setVisibility(View.VISIBLE);
            holder.layoutChatOut.setVisibility(View.GONE);
            holder.txtInMessage.setText(data.getMessageContent());
            holder.txtInTime.setText(data.getMessageTime());
        } else if (data.getMessageType() == ChatHistoryData.CHAT_TYPE_OUT) {
            holder.layoutChatOut.setVisibility(View.VISIBLE);
            holder.layoutChatIn.setVisibility(View.GONE);
            holder.txtOutMessage.setText(data.getMessageContent());
            holder.txtOutTime.setText(data.getMessageTime());
        }


        return convertView;
    }

    /**
     * View holder for efficiency.
     */
    static class ViewHolder {
        LinearLayout layoutChatIn;
        LinearLayout layoutChatOut;
        TextView txtInMessage;
        TextView txtInTime;
        TextView txtOutMessage;
        TextView txtOutTime;
    }
}