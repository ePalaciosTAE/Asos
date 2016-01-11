package com.tae.asos.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.tae.asos.application.AsosApplication;
import com.tae.asos.constants.Constants;
import com.tae.asos.manager.WishManager;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.analytics.AnalyticLabel;
import com.tae.asos.analytics.AnalyticsActions;
import com.tae.asos.analytics.AsosCategories;
import com.tae.asos.analytics.MixPanelUtils;

import java.util.ArrayList;

/**
 * Created by Eduardo on 22/12/2015.
 */
public class WishService extends IntentService {

    public static final String TAG = "WishService";
    private Context context;

    public WishService() {
        super(TAG);
    }


    public WishService(Context context) {
        super(TAG);
        this.context = context;
    }

    public static Intent makeIntent(Context context, Wish wish) {
        return new Intent(context, WishService.class)
                .putExtra(Constants.EXTRA_WISH, wish);
    }

    public static Intent makeIntent(Context context, Wish wish, int cartCount) {
        return new Intent(context, WishService.class)
                .putExtra(Constants.EXTRA_WISH, wish)
                .putExtra(Constants.EXTRA_CART_COUNT, cartCount);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Wish wish = intent.getParcelableExtra(Constants.EXTRA_WISH);
        if (wish != null) {
            if (wish.isCart()) { //basket (cart)
                MixPanelUtils.sendTrackToMixPanel(getApplicationContext(),
                        MixPanelUtils.mixPanelTrackFactory(AsosCategories.CART_ITEM, String.valueOf(wish.getProductId())), Constants.PRODUCT_DETAIL_SCREEN);

                AsosApplication.getInstance().trackEvent(
                        AsosCategories.CART_ITEM.getValue(),
                        AnalyticsActions.ADD_TO_CART.getValue(),
                        AnalyticLabel.ADDING_CART_ITEM.getValue());

                int cartCount = intent.getIntExtra(Constants.EXTRA_CART_COUNT, 0);
                WishManager.getInstance(getApplicationContext()).saveCart(wish, cartCount);
            } else {// is wish
                MixPanelUtils.sendTrackToMixPanel(getApplicationContext(),
                        MixPanelUtils.mixPanelTrackFactory(AsosCategories.WHISLIST_ITEM, String.valueOf(wish.getProductId())), Constants.PRODUCT_DETAIL_SCREEN);

                AsosApplication.getInstance().trackEvent(
                        AsosCategories.WHISLIST_ITEM.getValue(),
                        AnalyticsActions.ADD_TO_WHISLIST.getValue(),
                        AnalyticLabel.ADDING_WHISLIST_ITEM.getValue());

                WishManager.getInstance(getApplicationContext()).saveWish(wish);
            }
        } else {
            ArrayList<Wish> wishes =  WishManager.getInstance(getApplicationContext()).getWishes();
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS)
                            .putParcelableArrayListExtra(Constants.EXTRA_WISH_LIST, wishes)
                            .putExtra(Constants.EXTRA_TYPE, Constants.TYPE_WISH));

        }
    }
}
