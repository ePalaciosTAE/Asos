package com.tae.asos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tae.asos.R;
import com.tae.asos.constants.Constants;

/**
 * Created by Eduardo on 19/12/2015.
 */
public class ProductDetailImageFragment extends Fragment {

    public static final String TAG = "ProductDetailImageFragment";
    private String url;

    public static ProductDetailImageFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS_PRODUCT_IMAGE_URL, url);
        ProductDetailImageFragment fragment = new ProductDetailImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(Constants.ARGS_PRODUCT_IMAGE_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail_image, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_product);
        Picasso.with(getActivity()).load(url).resize(400,180).centerCrop().into(imageView);
        return view;
    }
}
