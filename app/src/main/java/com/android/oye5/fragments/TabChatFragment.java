package com.android.oye5.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.oye5.R;
import com.android.oye5.adapters.ChatsListFragmentAdapter;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Chat tab fragment that has several fragments by it's child fragment manager
 */
public class TabChatFragment extends BaseFragment implements PageSelectedListener {

    private Toolbar mToolbar;
    private ViewPager pager;
    private ChatsListFragmentAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_chat, container, false);

        initView(parent);

        return parent;
    }

    private void initView(View parent){
        mToolbar = (Toolbar) parent.findViewById(R.id.toolbar);
        setToolbarTitle(mToolbar, getString(R.string.chats));

        pager = (ViewPager) parent.findViewById(R.id.pager);
        pagerAdapter = new ChatsListFragmentAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);

        initSmartTabs(parent);
    }

    private void initSmartTabs(View parent){
        SmartTabLayout tabLayout = (SmartTabLayout) parent.findViewById(R.id.viewpagertab);
        final LinearLayout lyTabs = (LinearLayout) tabLayout.getChildAt(0);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabsTitleTypeFace(lyTabs, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final LayoutInflater inflater = LayoutInflater.from(tabLayout.getContext());
        final String chatTabTitleAry[] = getResources().getStringArray(R.array.chats_tab_title_ary);
        tabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
                View viewTab = inflater.inflate(R.layout.view_custom_chat_tab, viewGroup, false);
                TextView txtTabName = (TextView) viewTab.findViewById(R.id.txtTabName);
                ImageView imgTabIcon = (ImageView) viewTab.findViewById(R.id.imgTabIcon);
                txtTabName.setText(chatTabTitleAry[i]);

                if (i == 1){
                    imgTabIcon.setVisibility(View.VISIBLE);
                }else{
                    imgTabIcon.setVisibility(View.GONE);
                }
                return viewTab;
            }
        });
        tabLayout.setViewPager(pager);
        changeTabsTitleTypeFace(lyTabs, 0);
    }

    public void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            LinearLayout layoutChild = (LinearLayout) ly.getChildAt(j);
            TextView tvTabTitle = (TextView) ((ViewGroup)layoutChild.getChildAt(0)).getChildAt(0);
            tvTabTitle.setTypeface(null, Typeface.NORMAL);
            if (j == position) tvTabTitle.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentAttached(3, this);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentAttached(3, this);
        }
    }

    @Override
    public void onDetach() {
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentDetached(3);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentDetached(3);
        }
        super.onDetach();
    }

    @Override
    public void onPageSelected() {
        // TODO Auto-generated method stub
        Log.d(getClass().getName(), "OnPageSelected");
    }
}
