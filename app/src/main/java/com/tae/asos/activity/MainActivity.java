package com.tae.asos.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
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
import com.tae.asos.listener.OnPaymentRequestListener;
import com.tae.asos.model.api.catalogue.Catalogue;
import com.tae.asos.model.api.catalogue.Listing;
import com.tae.asos.model.api.person.Person;
import com.tae.asos.model.api.product.Product;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.service.AsosAPIService;
import com.tae.asos.service.WishService;

import cz.msebera.android.httpclient.Header;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


// The WhislistFragment and the CartFragment has been created and has its funtionallity,
// but i didn't implemented in the project. I just test it, i saw it was working but i did'nt do
// anything else.
// Whislist will load the items selected in the GridView with onLongClickListener
// CartList will load the items added to the cartCounter

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ICartCount, OnPaymentRequestListener {

    public static final int BRAINTREE_REQUEST_CODE = 123;
    private static final String BRAIN_BASE_URL = "http://192.168.1.201:8080";
    private static final String BRAIN_TOKEN_ENDPOINT = "/token";
    private static final String BRAIN_PAYMENT_METHODS = "/payment-methods";

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
    private String mToken;



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


//        startService(AsosAPIService.makeIntent(this, 0));
//        startService(AsosAPIService.makeIntent(this, Constants.API_TYPE_MAN_AND_WOMEN));

        progressDialog.show();
        menSeparator.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        mixPanelDummyExample();
        // brain tree token
        generateBrainToken();


//        displayEmptyFragment();


    }

    /**
     * BrainTree get token method
     */
    private void generateBrainToken() {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.i("MAIN", "onCreate: BRain shit request token");
        client.get(BRAIN_BASE_URL + BRAIN_TOKEN_ENDPOINT, new TextHttpResponseHandler() { // "http://192.168.1.201:8080/token"
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String clientToken) {
                Log.i("MAIN", "onSuccess: token: "  + clientToken);
                mToken = clientToken;
                displayEmptyFragment();
                progressDialog.dismiss();
            }
        });
    }

//    public void onBraintreeSubmit(View v) {
////        PaymentRequest paymentRequest = new PaymentRequest()
////                .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJmZjFmOWM2ZDc5ZTExMDgxOWQ4NDRjNGE5MjdkNDMyZDg4NTNlOTc2NGEyNzEyZWE5YjFhODhmYjk0OGFjNDQ0fGNyZWF0ZWRfYXQ9MjAxNi0wMS0xNFQxNDo1NDo0Mi4zMDE4OTQ1MzUrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOnRydWUsInRocmVlRFNlY3VyZSI6eyJsb29rdXBVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi90aHJlZV9kX3NlY3VyZS9sb29rdXAifSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
//        PaymentRequest paymentRequest = new PaymentRequest()
//                .clientToken(mToken)
//                .amount("$10.00")
//                .primaryDescription("Awesome payment")
//                .secondaryDescription("Using the Client SDK")
//                .submitButtonText("Pay");
//        startActivityForResult(paymentRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
//    }

    /**
     * When your user provides payment information, your app code receives a paymentMethodNonce
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                Log.i("MAIN", "onActivityResult: post Nonce to server with nonce = " + nonce);
                // Send the nonce to your server.
                postNonceToServer(nonce);
            }
        }
    }

    /**
     * BrainTree Post Nonce method:
     * The payment method nonce is a string returned by the client SDK to represent a payment method.
     * This string is a reference to the customer payment method details that were provided in your
     * payment form and should be sent to your server where it can be used with the server SDKs to
     * create a new transaction request.
     *
     * Send the resulting payment method nonce to your server
     * @param nonce
     */
    private void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);
        client.post(BRAIN_BASE_URL + BRAIN_PAYMENT_METHODS, params, //"http://your-server/payment-methods"
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("MAIN", "onSuccess: statusCode: " + statusCode);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                    // Your implementation here
                }
        );
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
                                    EmptyFragment.newInstance(mToken),
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
        Intent intent = null;
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

    @Override
    public void getPaymentRequest(PaymentRequest paymentRequest) {
        Log.i("MAIN", "getPaymentRequest: start activity to make payment");
        startActivityForResult(paymentRequest.getIntent(this), MainActivity.BRAINTREE_REQUEST_CODE);
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
