package com.android.oye5.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.adapters.ProfileProductsFragmentAdapter;
import com.android.oye5.listeners.FragmentLifecycleListener;
import com.android.oye5.listeners.PageSelectedListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Profile tab fragment that has several fragments by it's child fragment manager
 */
public class TabProfileFragment extends BaseFragment implements PageSelectedListener, View.OnClickListener {

    private ImageView imgProfile;
    private TextView txtName;
    private TextView txtAddress;
    private TextView txtLikedCount;
    private LinearLayout layoutStars;

    private ViewPager pager;
    private ProfileProductsFragmentAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_tab_profile, container, false);

        initView(parent);
        updateUIs();

        return parent;
    }

    private void initView(View parent){
        imgProfile = (ImageView) parent.findViewById(R.id.imgProfile);
        txtName = (TextView) parent.findViewById(R.id.txtName);
        txtAddress = (TextView) parent.findViewById(R.id.txtAddress);
        txtLikedCount = (TextView) parent.findViewById(R.id.txtLikedCount);
        layoutStars = (LinearLayout) parent.findViewById(R.id.layoutStars);

        pager = (ViewPager) parent.findViewById(R.id.pager);
        pagerAdapter = new ProfileProductsFragmentAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);

        initSmartTabs(parent);

        parent.findViewById(R.id.btnSettings).setOnClickListener(this);
        parent.findViewById(R.id.btnModify).setOnClickListener(this);
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
        final String productsType[] = getResources().getStringArray(R.array.user_products_type_ary);
        tabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
                View viewTab = inflater.inflate(R.layout.view_custom_tab_icon, viewGroup, false);
                ImageView imgTabIcon = (ImageView) viewTab.findViewById(R.id.imgTabIcon);
                imgTabIcon.setVisibility(View.GONE);
                TextView txtTabName = (TextView) viewTab.findViewById(R.id.txtTabName);
                txtTabName.setText(productsType[i]);
                return viewTab;
            }
        });
        tabLayout.setViewPager(pager);
        changeTabsTitleTypeFace(lyTabs, 0);
    }

    private void updateUIs(){
        txtName.setText("Alex Hong");
        txtAddress.setText("SAN FRANCISCO, CALIFORNIA");
        txtLikedCount.setText("(999)");
    }

    public void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            LinearLayout layoutChild = (LinearLayout) ly.getChildAt(j);
            TextView tvTabTitle = (TextView) ((ViewGroup)layoutChild.getChildAt(0)).getChildAt(1);
            tvTabTitle.setTypeface(null, Typeface.NORMAL);
            if (j == position) tvTabTitle.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSettings:
                showToast("Settings", Toast.LENGTH_SHORT);
                break;
            case R.id.btnModify:
                showToast("Modify", Toast.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentAttached(4, this);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentAttached(4, this);
        }
    }

    @Override
    public void onDetach() {
        if (getParentFragment() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getParentFragment()).onFragmentDetached(4);
        }

        if (getActivity() instanceof FragmentLifecycleListener) {
            ((FragmentLifecycleListener) getActivity()).onFragmentDetached(4);
        }
        super.onDetach();
    }

    @Override
    public void onPageSelected() {
        // TODO Auto-generated method stub
        Log.d(getClass().getName(), "OnPageSelected");
    }
}
