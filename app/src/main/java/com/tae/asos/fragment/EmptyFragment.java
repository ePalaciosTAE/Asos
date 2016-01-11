package com.tae.asos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.tae.asos.R;
import com.tae.asos.adapter.CollectionsPagerAdapter;

/**
 * Created by Eduardo on 20/12/2015.
 */
public class EmptyFragment extends Fragment {

    public static final String TAG = "EmptyFragment";
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private CollectionsPagerAdapter pagerAdapter;

    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty_view, container, false);
        return view;
    }
}
