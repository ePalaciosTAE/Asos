package com.tae.asos.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tae.asos.R;
import com.tae.asos.adapter.CommonAdapter;
import com.tae.asos.constants.Constants;
import com.tae.asos.dialog.AddToWishListFragmentDialog;
import com.tae.asos.listener.Common;
import com.tae.asos.listener.RecyclerItemClickListener;
import com.tae.asos.model.api.catalogue.Catalogue;
import com.tae.asos.model.api.catalogue.Listing;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.service.AsosAPIService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 19/12/2015.
 * This fragment displays a catalogue in a grid view
 */
public class CatalogueFragment extends Fragment  {

    public static final String TAG = "CatalogueFragment";
    private List<Listing> catalogueListing;


    public static CatalogueFragment newInstance(ArrayList<Listing> catalogueId) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ARGS_CATALOGUE_LISTING, catalogueId);
        CatalogueFragment fragment = new CatalogueFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        catalogueListing = (List<Listing>) getArguments().get(Constants.ARGS_CATALOGUE_LISTING);
    }



//    private Object actuallyT;
//
//    public <T> List<T> magicalListGetter(Class<T> klazz) {
//        List<T> list = new ArrayList<>();
//        list.add(klazz.cast(actuallyT));
//        try {
//            list.add(klazz.getConstructor().newInstance()); // If default constructor
//        } ...
//        return list;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_catalogue);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,1));
        List<Common> commons = convertListToCommon();
        final CommonAdapter commonAdapter = new CommonAdapter(getActivity(), commons);
        recyclerView.setAdapter(commonAdapter);
        recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(getActivity(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int productId = catalogueListing.get(position).getProductId();
                Log.i("CatalogueFragemnt", "product id " + productId);
                getActivity().startService(AsosAPIService.makeIntentWithProductId(
                        getActivity(), Constants.API_TYPE_PRODUCT_DETAIL, productId));
            }

            @Override
            public void OnLongItemClick(View view, int position) {
                int productId = catalogueListing.get(position).getProductId();
                String title = catalogueListing.get(position).getTitle();
                String imageUrl = catalogueListing.get(position).getProductImageUrl().get(0);
                Log.i("CatalogueFragemnt", "Long Click product id " + productId);
                // TODO show dialog fragment
                AddToWishListFragmentDialog.newInstance(new Wish(productId, title, imageUrl, false)) // is wish
                .show(getFragmentManager(),AddToWishListFragmentDialog.TAG);
            }
        }));


        return view;
    }

    /**
     * TODO: Convert list<Wish or Listing> to Common
     *
     */
    @NonNull
    private List<Common> convertListToCommon() {
        List<Common> commons = new ArrayList<>(catalogueListing.size());
        for (int i = 0; i < catalogueListing.size(); i++) {
            Common d =(Common) catalogueListing.get(i);
            commons.add(d);
        }
        return commons;
    }


//
//    @Override
//    public void onItemClick(View view, int position, boolean isClicked) {
//       Log.i("CatalogueFragemnt", "product id " +catalogueAdapter.getProductId(position));
//    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }

}
