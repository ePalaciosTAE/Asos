package com.tae.asos.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.tae.asos.R;
import com.tae.asos.adapter.DrawerAdapter;
import com.tae.asos.constants.Constants;
import com.tae.asos.fragment.CatalogueFragment;
import com.tae.asos.fragment.CollectionsFragment;
import com.tae.asos.fragment.EmptyFragment;
import com.tae.asos.fragment.ProductDetailFragment;
import com.tae.asos.fragment.WishListFragment;
import com.tae.asos.listener.ICartCount;
import com.tae.asos.model.api.catalogue.Catalogue;
import com.tae.asos.model.api.catalogue.Listing;
import com.tae.asos.model.api.person.Person;
import com.tae.asos.model.api.product.Product;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.service.AsosAPIService;
import com.tae.asos.service.WishService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


// The WhislistFragment and the CartFragment has been created and has its funtionallity,
// but i didn't implemented in the project. I just test it, i saw it was working but i did'nt do
// anything else.
// Whislist will load the items selected in the GridView with onLongClickListener
// CartList will load the items added to the cartCounter

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ICartCount {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private DrawerAdapter drawerAdapter;
//    private ActionBarDrawerToggle drawerToggle;
    private List<String> drawerTitles; // TODO get titles from model from API 1
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private Button btnMan, btnWoman;
    private TextView tvItemCounter;
    private View menSeparator;
    private View womenSeparator;



    private AsosReceiver asosReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAccountsPermissions();
        setHeaderButtons();
        setToolbar();
        initViews();
        setCartCounter();
        asosReceiver = new AsosReceiver();

        startService(AsosAPIService.makeIntent(this, 0));
        startService(AsosAPIService.makeIntent(this, Constants.API_TYPE_MAN_AND_WOMEN));

        progressDialog.show();
        menSeparator.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        displayEmptyFragment();
        mixPanelDummyExample();


    }

    private void mixPanelDummyExample() {
    /* To Track the data in MixPanel we have to pass a JSONObject as parameter.
        Choose like 4-5 events (actions) of the app to track
     */
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, Constants.MIX_PANEL_PROJECT_TOKEN);
        try {
            JSONObject properties = new JSONObject();
            properties.put("Gender", "Female");
            properties.put("Logged in", false);
            mixpanel.track("MainActivity - onCreate called", properties);
            mixpanel.getPeople().set(properties);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }


    private void getAccountsPermissions() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                        Constants.REQUEST_CODE_GET_ACCOUNT_PERMISSIONS);
                return;
            }
        }
    }


    private void displayEmptyFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(
                                    R.id.container,
                                    EmptyFragment.newInstance(),
                                    EmptyFragment.TAG)
                            .addToBackStack(EmptyFragment.TAG)
                            .commit();
    }

    private void initViews() {
        tvItemCounter = (TextView) findViewById(R.id.tv_toolbar_counter);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress_dialog_loading));
        menSeparator = findViewById(R.id.header_separator_men);
        womenSeparator = findViewById(R.id.header_separator_women);
    }

    private void setCartCounter() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.sp_chart_counter), MODE_PRIVATE);
        int count = sp.getInt(getString(R.string.sp_counter_key), 0);
        tvItemCounter.setText(String.valueOf(count));
        if (count > 0) {
            tvItemCounter.setVisibility(View.VISIBLE);
        }
    }

    private void setHeaderButtons() {
        btnMan = (Button) findViewById(R.id.btn_header_man);
        btnMan.setOnClickListener(this);
        btnMan.setFocusableInTouchMode(true);
        btnWoman = (Button) findViewById(R.id.btn_header_woman);
        btnWoman.setOnClickListener(this);
    }



    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MainActivity.this)
                .registerReceiver(asosReceiver, new IntentFilter(Constants.ACTION_DOWNLOAD_SUCCESS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(asosReceiver);
    }


    @Override
    public void onClick(View v) {
        // TODO start service depending on the button clicked
        Intent intent;
        if (v.getId() == R.id.btn_header_man) {
            intent = AsosAPIService.makeIntent(this, Constants.API_TYPE_MAN_CLOTH);
            menSeparator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            womenSeparator.setBackgroundColor(getResources().getColor(R.color.colorDivider));
        } else {
            intent = AsosAPIService.makeIntent(this, Constants.API_TYPE_WOMAN_CLOTH);
            menSeparator.setBackgroundColor(getResources().getColor(R.color.colorDivider));
            womenSeparator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        progressDialog.show();
        startService(intent);
    }


    private void displayCatalogueFragment(ArrayList<Listing> catalogueListing) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(
                R.id.container,
                CatalogueFragment.newInstance(catalogueListing),
                CatalogueFragment.TAG
        ).addToBackStack(CatalogueFragment.TAG).commit();
    }

    @Override
    public void getCartCount(final int count, final Wish wish) {
        tvItemCounter.setText(String.valueOf(count));
        startService(WishService.makeIntent(this, wish, count));
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SharedPreferences sp = getSharedPreferences(getString(R.string.sp_chart_counter), Context.MODE_PRIVATE);
//                sp.edit().putInt(getString(R.string.sp_counter), count) // save total count
//                        .putInt(String.valueOf(wish.getProductId()), count) // save key productId - val: count
//                        .apply();
//            }
//        });
//        thread.run();
//        if (thread.isAlive()) {
//            thread.interrupt();
//        }

    }


    private class AsosReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            DrawerAdapter drawerAdapter = null;
            dismissProgressDialog();
            FragmentManager fm = getSupportFragmentManager();
            switch (intent.getIntExtra(Constants.EXTRA_TYPE, 0)) {
                case Constants.API_TYPE_MAN_CLOTH:
                    Person person = intent.getParcelableExtra(Constants.EXTRA_MEN_CLOTH);
                    drawerAdapter = new DrawerAdapter(MainActivity.this, person.getListing());
                    break;
                case Constants.API_TYPE_WOMAN_CLOTH:
                    Person personWoman = intent.getParcelableExtra(Constants.EXTRA_WOMEN_CLOTH);
                    drawerAdapter = new DrawerAdapter(MainActivity.this, personWoman.getListing());
                    break;
                case Constants.API_TYPE_CATALOGUE:
                    dismissProgressDialog();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Catalogue catalogue = intent.getParcelableExtra(Constants.EXTRA_CATALOGUE);
                    ArrayList<Listing> arrayListing = (ArrayList<Listing>) catalogue.getListings();
                    displayCatalogueFragment(arrayListing);
                    break;
                case Constants.API_TYPE_PRODUCT_DETAIL:
                    Product product = intent.getParcelableExtra(Constants.EXTRA_PRODUCT_DETAIL);
                    displayProductDetailFragment(product);
                    break;
                case Constants.API_TYPE_MAN_AND_WOMEN :
                    Log.i("MainACtivity", "display tabs fragment with man and woman collextion");
                    Bundle bundle = intent.getBundleExtra(Constants.EXTRA_BUNDLE);

//                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(
                            R.id.container,
                            CollectionsFragment.newInstance(bundle),
                            CollectionsFragment.TAG
                    ).commit();
                    break;
                case Constants.TYPE_WISH:
                    ArrayList<Wish> wishes = intent.getParcelableArrayListExtra(Constants.EXTRA_WISH_LIST);
//                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().replace(
                            R.id.container,
                            WishListFragment.newInstance(wishes),
                            WishListFragment.TAG
                    ).addToBackStack(WishListFragment.TAG).commit();
                    break;
            }
            if (drawerAdapter != null) {
                drawerAdapter.setProgressDialog(progressDialog);
                recyclerView.setAdapter(drawerAdapter);
            }
        }

        private void displayProductDetailFragment(Product product) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(
                    R.id.container,
                    ProductDetailFragment.newInstance(product),
                    ProductDetailFragment.TAG
            ).addToBackStack(ProductDetailFragment.TAG).commit();
        }
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm; // TODO FIX THIS --> create fragmentLoader method to load and keep currentFragment
//        if (getSupportFragmentManager().findFragmentByTag(ProductDetailFragment.TAG) != null) {
//            fm = getSupportFragmentManager();
//            fm.beginTransaction().replace(R.id.container, getSupportFragmentManager().findFragmentByTag(CatalogueFragment.TAG), EmptyFragment.TAG).commit();
//        }
//        if (getSupportFragmentManager().findFragmentByTag(CatalogueFragment.TAG) != null) {
//            fm = getSupportFragmentManager();
//            fm.beginTransaction().replace(R.id.container, getSupportFragmentManager().findFragmentByTag(EmptyFragment.TAG), EmptyFragment.TAG).commit();
//        }
//        if (getSupportFragmentManager().findFragmentByTag(EmptyFragment.TAG) != null) {
//            finish();
//        }
    }

}
