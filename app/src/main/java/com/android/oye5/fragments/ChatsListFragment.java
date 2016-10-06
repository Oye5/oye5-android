package com.android.oye5.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.MainActivity;
import com.android.oye5.adapters.ChatsListAdapter;
import com.android.oye5.listeners.ICustomCallback;
import com.android.oye5.models.ChatData;

import java.util.ArrayList;
import java.util.List;

public class ChatsListFragment extends BaseFragment implements View.OnClickListener {

    private ListView lstChats;
    private ChatsListAdapter lstAdapter;
    private RelativeLayout layoutNoChats;

    private List<ChatData> dataList = new ArrayList<>();

    private int nChatType = 0;

    public static ChatsListFragment newInstance(int chatType) {
        ChatsListFragment fragment = new ChatsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("chat_type", chatType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_chat_list, container, false);

        nChatType = getArguments().getInt("chat_type");

        initView(parent, inflater);

        if (nChatType == 2){
            layoutNoChats.setVisibility(View.VISIBLE);
            lstChats.setVisibility(View.GONE);
        }else{
            loadData();
            layoutNoChats.setVisibility(View.GONE);
            lstChats.setVisibility(View.VISIBLE);
        }


        return parent;
    }

    private void initView(View parent, LayoutInflater inflater) {
        layoutNoChats = (RelativeLayout) parent.findViewById(R.id.layoutNoChats);
        layoutNoChats.setOnClickListener(this);

        int rightMenuWidth = getResources().getDimensionPixelSize(R.dimen.dp_80);
        lstChats = (ListView) parent.findViewById(R.id.lstChats);
        //lstChats.setDisplayWidth(displayWidth, 0, rightMenuWidth);
        lstChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) getActivity()).goToChatDetailsScreen();
            }
        });
        /*lstChats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showToast("Item Long Clicked", Toast.LENGTH_SHORT);
                return false;
            }
        });*/
        lstAdapter = new ChatsListAdapter(getActivity(), inflater, displayWidth, rightMenuWidth, callBack, this);
        lstChats.setAdapter(lstAdapter);
    }

    private void loadData() {
        dataList.clear();

        for (int i = 0; i < 10; i++) {
            ChatData data = new ChatData();
            data.setUserName("Nicholas Stewart " + (i + 1));
            data.setProductName("lbl speaker " + (i + 1));
            data.setChatTime(47 + i + " second ago");
            dataList.add(data);
        }

        lstAdapter.setData(dataList);
    }
    private ICustomCallback callBack = new ICustomCallback() {
        @Override
        public void callOnClick(int pos, int type, View view) {
            Log.d(getClass().getName(), "pos:" + pos + ", type:" + type);

            //lstChats.hiddenLeftRight(view);
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSellYourStuff:
                showToast("Sell Your Stuff!", Toast.LENGTH_SHORT);
                break;
        }
    }
}
