package com.tae.asos.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tae.asos.R;
import com.tae.asos.adapter.CommonAdapter;
import com.tae.asos.constants.Constants;
import com.tae.asos.listener.Common;
import com.tae.asos.model.wishlist.Wish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 22/12/2015.
 */
public class WishListFragment extends Fragment {

    public static final String TAG = "WishListFragment";
    private ArrayList<Wish> wishes;

    public static WishListFragment newInstance (ArrayList<Wish> wishs) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ARGS_WISHLIST, wishs);
        WishListFragment fragment = new WishListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wishes = getArguments().getParcelableArrayList(Constants.ARGS_WISHLIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_wish_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Common> commons = new ArrayList<>(wishes.size());
        for (Wish wish : wishes) {
//            Common common = wish;
            commons.add(wish);
        }
        CommonAdapter commonAdapter = new CommonAdapter(getActivity(), commons);
        recyclerView.setAdapter(commonAdapter);
        return view;
    }

}
