package com.tae.asos.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tae.asos.R;
import com.tae.asos.adapter.CommonAdapter;
import com.tae.asos.listener.Common;
import com.tae.asos.model.wishlist.Wish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Eduardo on 21/12/2015.
 */
public class CartFragment extends Fragment {

    public static final String TAG = "CartFragment";

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    private List<Wish> carts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.sp_set_cart), Context.MODE_PRIVATE);
        Map<String, String> keys = (Map<String, String>) sp.getAll();
        SparseArray<String> sparseArrayCart = new SparseArray<>(keys.size());
        for (Map.Entry<String, String> mapEntry : keys.entrySet()) {
            String key  = mapEntry.getKey();
            String value = mapEntry.getValue();
            Log.i("Manager", "key(map.Entry): " + key);
            Log.i("Manager", "mapEntry(getValue): " + mapEntry.getValue());
            sparseArrayCart.put(Integer.parseInt(key), value);
        }
        sparseArrayCart.size();
        String[] array = null;
        carts = new ArrayList<>();
        for (int i = 0; i < sparseArrayCart.size(); i++) {
            String s = sparseArrayCart.get(i);
            if (s != null) {
                array = s.split(",");
                Log.i("Manager","sparse value: " +  array[0]);
                Wish wish = new Wish(Long.parseLong(array[0]), array[1], array[2], true);
                carts.add(wish);

            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_wish_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        List<Common> commons = new ArrayList<>(carts.size());
        for (Wish wish : carts) {
            Common common = wish;
            commons.add(common);
        }
        CommonAdapter adapter = new CommonAdapter(getActivity(), commons);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
