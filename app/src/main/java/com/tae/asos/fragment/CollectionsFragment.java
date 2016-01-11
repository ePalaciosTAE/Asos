package com.tae.asos.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.tae.asos.R;
import com.tae.asos.adapter.CollectionsPagerAdapter;
import com.tae.asos.constants.Constants;
import com.tae.asos.model.api.person.Person;
import com.tae.asos.service.AsosAPIService;

/**
 * Created by Eduardo on 21/12/2015.
 * This fragment show a view pager that will load a collection depending on the position
 */
public class CollectionsFragment extends Fragment implements ViewPager.OnPageChangeListener {

    public static final String TAG = "CollectionsFragment";
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private CollectionsPagerAdapter pagerAdapter;
//    private ArrayList<Listing> listingList;
    private SparseArray<Person> collectionCloths;

//    public static CollectionsFragment newInstance(Bundle bundle) {
////        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(Constants.ARGS_DRAWER_LISTING, listingList);
//        CollectionsFragment fragment = new CollectionsFragment();
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    public static CollectionsFragment newInstance(Bundle bundle) {
        Log.i(TAG, "newInstance()");
        Bundle bundleTemp = new Bundle();
        bundleTemp.putBundle(Constants.ARGS_BUNDLE, bundle);
        CollectionsFragment fragment = new CollectionsFragment();
        fragment.setArguments(bundleTemp);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() - getting bundle");
//        listingList = getArguments().getParcelableArrayList(Constants.ARGS_DRAWER_LISTING);
        Bundle bundle = getArguments().getBundle(Constants.ARGS_BUNDLE);
        collectionCloths =  bundle.getSparseParcelableArray(Constants.EXTRA_MAN_AND_WOMEN_CLOTH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager_collection);
        tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_collection);
        pagerAdapter = new CollectionsPagerAdapter(getActivity().getSupportFragmentManager(), collectionCloths);
        viewPager.setAdapter(pagerAdapter);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(this);
        Log.i(TAG, "onCreateView() - loading view pager");
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Intent intent;
        if (position == Constants.API_TYPE_MAN_CLOTH) {
            Log.i(TAG, "man service start");
            intent = AsosAPIService.makeIntent(getActivity(), Constants.API_TYPE_MAN_CLOTH);

        } else {
            Log.i(TAG, "woman service start");
            intent = AsosAPIService.makeIntent(getActivity(), Constants.API_TYPE_WOMAN_CLOTH);
        }
        getActivity().startService(intent);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
