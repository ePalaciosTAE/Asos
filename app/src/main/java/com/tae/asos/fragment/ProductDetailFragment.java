package com.tae.asos.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tae.asos.R;
import com.tae.asos.adapter.ProductPagerAdapter;
import com.tae.asos.constants.Constants;
import com.tae.asos.listener.ICartCount;
import com.tae.asos.model.api.product.Product;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.service.WishService;

/**
 * Created by Eduardo on 19/12/2015.
 */
public class ProductDetailFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "ProductDetailFragment";
    private Product product;
    private int itemCounter;
    private TextView tvCounter;
    private ICartCount iCartCount;


    public static ProductDetailFragment newInstance (Product product) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARGS_PRODUCT, product);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = (Product) getArguments().get(Constants.ARGS_PRODUCT);
        Log.i(TAG, "producnt " + product.getTitle());
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.sp_chart_counter), Context.MODE_PRIVATE);
        itemCounter = sp.getInt(getString(R.string.sp_counter_key), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        iCartCount = (ICartCount) getActivity();
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_product);
        ProductPagerAdapter productPagerAdapter
                = new ProductPagerAdapter(getActivity().getSupportFragmentManager(), product.getProductImageUrls());
        viewPager.setAdapter(productPagerAdapter);
        tvCounter = (TextView) view.findViewById(R.id.tv_toolbar_counter);
        setTextViewCounter();
        if (itemCounter != 0) {
            tvCounter.setVisibility(View.VISIBLE);
        }
        setToolbar(view);

        Button btnAdd = (Button) view.findViewById(R.id.btn_product_add);
        btnAdd.setOnClickListener(this);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_product_title);
        TextView tvAvailable = (TextView) view.findViewById(R.id.tv_product_available);
        TextView tvSize = (TextView) view.findViewById(R.id.tv_product_size);
        tvTitle.setText(product.getTitle());
        if (product.getInStock()) {
            tvAvailable.setText("In Stock");
        } else {
            tvAvailable.setText("Not available");
        }
        tvSize.setText(product.getCurrentPrice());

        return view;
    }

    private void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToCatalogueFragment();
            }

            private void backToCatalogueFragment() {
                CatalogueFragment catalogueFragment = (CatalogueFragment) getActivity()
                        .getSupportFragmentManager().findFragmentByTag(CatalogueFragment.TAG);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.container, catalogueFragment, CatalogueFragment.TAG).commit();
            }
        });
    }

    private void setTextViewCounter() {
        tvCounter.setText(String.valueOf(itemCounter));
    }

    @Override
    public void onClick(View v) {
        itemCounter++;
        tvCounter.setVisibility(View.VISIBLE);
        setTextViewCounter();
        iCartCount.getCartCount(itemCounter, new Wish(
                product.getProductId(),
                product.getProductImageUrls().get(0),
                product.getTitle(),
                true // is cart
        ));
        FragmentManager fm = getActivity().getSupportFragmentManager();
//        fm.beginTransaction().replace(R.id.container, CartFragment.newInstance(), CartFragment.TAG).commit(); //display cart from SP
        getActivity().startService(WishService.makeIntent(getActivity(), null)); // this will show wish
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


}
