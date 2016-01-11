package com.tae.asos.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tae.asos.R;
import com.tae.asos.adapter.DrawerAdapter;
import com.tae.asos.constants.Constants;
import com.tae.asos.model.api.person.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 20/12/2015.
 * This fragment is displayed from the Collections pager adapter
 */
public class SingleCollectionFragment extends Fragment {

    public static final String TAG = "DrawerClothsFragment";
    private List<Listing> listings;

    public static SingleCollectionFragment newInstance(ArrayList<Listing> listings) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ARGS_DRAWER_LISTING, listings);
        SingleCollectionFragment fragment = new SingleCollectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listings = (ArrayList<Listing>) getArguments().get(Constants.ARGS_DRAWER_LISTING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer_cloths, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.drawer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DrawerAdapter drawerAdapter = new DrawerAdapter(getActivity(), listings);
        recyclerView.setAdapter(drawerAdapter);
        return view;
    }
}
