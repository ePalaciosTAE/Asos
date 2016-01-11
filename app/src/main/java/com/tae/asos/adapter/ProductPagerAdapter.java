package com.tae.asos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tae.asos.fragment.ProductDetailImageFragment;

import java.util.List;

/**
 * Created by Eduardo on 19/12/2015.
 */
public class ProductPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> imagesUrls;


    public ProductPagerAdapter(FragmentManager fm, List<String> imagesUrls) {
        super(fm);
        this.imagesUrls = imagesUrls;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductDetailImageFragment.newInstance(imagesUrls.get(position));
    }

    @Override
    public int getCount() {
        return imagesUrls.size();
    }
}
