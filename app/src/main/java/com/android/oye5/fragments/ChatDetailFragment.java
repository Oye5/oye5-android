package com.android.oye5.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.adapters.ChatsHistoryListAdapter;
import com.android.oye5.models.ChatHistoryData;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailFragment extends BaseFragment implements View.OnClickListener{

    private ListView lstChatsHistory;
    private ChatsHistoryListAdapter lstAdapter;
    private List<ChatHistoryData> dataList = new ArrayList<>();

    private EditText edtMessage;

    public static ChatDetailFragment newInstance(){
        ChatDetailFragment fragment = new ChatDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_chat_details, container, false);

        initView(parent, inflater);
        loadData();

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        lstChatsHistory = (ListView) parent.findViewById(R.id.lstChatsHistory);
        lstAdapter = new ChatsHistoryListAdapter(getActivity(), inflater, this);
        lstChatsHistory.setAdapter(lstAdapter);

        edtMessage = (EditText) parent.findViewById(R.id.edtMessage);

        parent.findViewById(R.id.btnBack).setOnClickListener(this);
        parent.findViewById(R.id.btnSend).setOnClickListener(this);
        parent.findViewById(R.id.btnInterested).setOnClickListener(this);
        parent.findViewById(R.id.btnNotInterested).setOnClickListener(this);
        parent.findViewById(R.id.btnNegotiable).setOnClickListener(this);
    }

    private void loadData(){
        ChatHistoryData data1 = new ChatHistoryData();
        data1.setMessageContent("Hi, how are you?");
        data1.setMessageTime("5 mins ago");
        data1.setMessageType(ChatHistoryData.CHAT_TYPE_IN);

        ChatHistoryData data2 = new ChatHistoryData();
        data2.setMessageContent("I am fine, thanks! Are you interested?");
        data2.setMessageTime("4 mins ago");
        data2.setMessageType(ChatHistoryData.CHAT_TYPE_OUT);

        dataList.add(data1);
        dataList.add(data2);

        lstAdapter.setData(dataList);
    }

    private void sendNewMessage(String quickMsg){
        String msg;
        if (quickMsg.equals("")) {
            msg = edtMessage.getText().toString();

            if (msg.equals("")) {
                showToast("Please input the message!", Toast.LENGTH_SHORT);
                return;
            }
        }else{
            msg = quickMsg;
        }

        ChatHistoryData data = new ChatHistoryData();
        data.setMessageContent(msg);
        data.setMessageTime("Just now");
        data.setMessageType(ChatHistoryData.CHAT_TYPE_OUT);

        lstAdapter.appendData(data);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                lstChatsHistory.setSelection(lstAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnSend:
                sendNewMessage("");
                break;
            case R.id.btnInterested:
                sendNewMessage(getString(R.string.chats_interested));
                break;
            case R.id.btnNegotiable:
                sendNewMessage(getString(R.string.chats_negotiable));
                break;
            case R.id.btnNotInterested:
                sendNewMessage(getString(R.string.chats_not_interested));
                break;
        }
    }
}
