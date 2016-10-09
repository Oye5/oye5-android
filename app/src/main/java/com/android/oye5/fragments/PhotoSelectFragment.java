package com.android.oye5.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.oye5.R;
import com.android.oye5.activities.BaseActivity;
import com.android.oye5.adapters.PhotosSelectGridAdapter;
import com.android.oye5.adapters.ProfileProductsGridAdapter;
import com.android.oye5.models.PhotoData;

import java.util.ArrayList;
import java.util.List;

public class PhotoSelectFragment extends BaseFragment implements View.OnClickListener {

    private ImageView imgSelectedPhoto;
    private GridView grdPhotos;
    private PhotosSelectGridAdapter grdAdapter;

    private List<PhotoData> photosList = new ArrayList<>();

    public static PhotoSelectFragment newInstance(){
        PhotoSelectFragment fragment = new PhotoSelectFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_photo_select, container, false);

        initView(parent, inflater);
        loadData();

        return parent;
    }

    private void initView(View parent, LayoutInflater inflater){
        imgSelectedPhoto = (ImageView) parent.findViewById(R.id.imgSelectedPhoto);

        imgSelectedPhoto = (ImageView) parent.findViewById(R.id.imgSelectedPhoto);
        imgSelectedPhoto.setImageResource(R.drawable.img_product_sample_6);

        grdPhotos = (GridView) parent.findViewById(R.id.grdPhotos);
        //Configuring the Grid Views with Adapter
        float fMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
        int nCols = getResources().getInteger(R.integer.photo_grid_col_count);
        float fSellSize = (displayWidth - fMargin * (nCols + 1)) / nCols;
        grdPhotos.setVerticalSpacing((int) fMargin);
        grdPhotos.setHorizontalSpacing((int) fMargin);
        grdAdapter = new PhotosSelectGridAdapter(getActivity(), inflater, (int) fSellSize, this);
        grdPhotos.setAdapter(grdAdapter);
        grdPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PhotoData photo = grdAdapter.getItem(i);
                imgSelectedPhoto.setImageResource(photo.getResId());
            }
        });

        grdPhotos.setSelection(0);

        parent.findViewById(R.id.btnBack).setOnClickListener(this);
        parent.findViewById(R.id.btnUsePhoto).setOnClickListener(this);
    }

    private void loadData(){
        PhotoData photo1 = new PhotoData();
        photo1.setResId(R.drawable.img_product_sample_6);
        PhotoData photo2 = new PhotoData();
        photo2.setResId(R.drawable.img_product_sample_7);
        PhotoData photo3 = new PhotoData();
        photo3.setResId(R.drawable.img_product_sample_8);
        PhotoData photo4 = new PhotoData();
        photo4.setResId(R.drawable.img_product_sample_9);
        PhotoData photo5 = new PhotoData();
        photo5.setResId(R.drawable.img_product_sample_10);
        PhotoData photo6 = new PhotoData();
        photo6.setResId(R.drawable.img_product_sample_11);
        PhotoData photo7 = new PhotoData();
        photo7.setResId(R.drawable.img_product_sample_12);
        PhotoData photo8 = new PhotoData();
        photo8.setResId(R.drawable.img_product_sample_13);

        photosList.add(photo1);
        photosList.add(photo2);
        photosList.add(photo3);
        photosList.add(photo4);
        photosList.add(photo5);
        photosList.add(photo6);
        photosList.add(photo7);
        photosList.add(photo8);

        grdAdapter.setData(photosList);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnReset:
                showToast("Reset", Toast.LENGTH_SHORT);
                break;
            case R.id.btnUsePhoto:
                showToast("Use Photo", Toast.LENGTH_SHORT);
                break;
            case R.id.btnGallery:
                showToast("Gallery", Toast.LENGTH_SHORT);
                break;
            case R.id.btnPhoto:
                showToast("Photo", Toast.LENGTH_SHORT);
                break;
        }
    }
}
