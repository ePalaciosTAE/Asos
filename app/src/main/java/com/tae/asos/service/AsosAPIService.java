package com.tae.asos.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.tae.asos.api.AsosRestAdapter;
import com.tae.asos.application.AsosApplication;
import com.tae.asos.constants.Constants;
import com.tae.asos.analytics.AnalyticLabel;
import com.tae.asos.analytics.AnalyticsActions;
import com.tae.asos.analytics.AsosCategories;
import com.tae.asos.analytics.MixPanelUtils;

/**
 * Created by Eduardo on 19/12/2015.
 */
public class AsosAPIService extends IntentService {


    private static final String TAG = "ClothService";

    public AsosAPIService() {
        super(TAG);
    }

    public static Intent makeIntent(Context context, int apiId) {
        return new Intent(context, AsosAPIService.class).putExtra(Constants.EXTRA_API_ID, apiId);
    }

    public static Intent makeIntentWithCatalogueId(Context context, int apiId, String catalogueId) {
        return new Intent(context, AsosAPIService.class)
                .putExtra(Constants.EXTRA_API_ID, apiId)
                .putExtra(Constants.EXTRA_CATALOGUE_ID, catalogueId);
    }

    public static Intent makeIntentWithProductId(Context context, int apiId, int productId) {
        return new Intent(context, AsosAPIService.class)
                .putExtra(Constants.EXTRA_API_ID, apiId)
                .putExtra(Constants.EXTRA_PRODUCT_DETAIL, productId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AsosRestAdapter restAdapter = new AsosRestAdapter(getApplicationContext());

        switch (intent.getIntExtra(Constants.EXTRA_API_ID, 0)) {
            case Constants.API_TYPE_MAN_CLOTH:
                MixPanelUtils.sendTrackToMixPanel(getApplicationContext(),
                        MixPanelUtils.mixPanelTrackFactory(AsosCategories.MAN_CATALOGUE, ""), Constants.DRAWER_SCREEN);

                AsosApplication.getInstance().trackEvent(
                        AsosCategories.MAN_CATALOGUE.getValue(),
                        AnalyticsActions.DOWNLOAD.getValue(),
                        AnalyticLabel.DOWNLOADING_MAN_COLLECTION.getValue());

                restAdapter.getManCloths();
                break;
            case Constants.API_TYPE_WOMAN_CLOTH:
                MixPanelUtils.sendTrackToMixPanel(getApplicationContext(),
                        MixPanelUtils.mixPanelTrackFactory(AsosCategories.WOMAN_CATALOGUE, ""), Constants.DRAWER_SCREEN);

                AsosApplication.getInstance().trackEvent(
                        AsosCategories.WOMAN_CATALOGUE.getValue(),
                        AnalyticsActions.DOWNLOAD.getValue(),
                        AnalyticLabel.DOWNLOADING_WOMAN_COLLECTION.getValue());

                restAdapter.getWomenCloth();
                break;
            case Constants.API_TYPE_CATALOGUE:
                String catalogueId = intent.getStringExtra(Constants.EXTRA_CATALOGUE_ID);
                MixPanelUtils.sendTrackToMixPanel(getApplicationContext(),
                        MixPanelUtils.mixPanelTrackFactory(AsosCategories.CATALOGUE_ITEM, catalogueId), Constants.CATALOGUE_GRID_SCREEN);

                AsosApplication.getInstance().trackEvent(
                        AsosCategories.CATALOGUE_ITEM.getValue(),
                        AnalyticsActions.DOWNLOAD.getValue(),
                        AnalyticLabel.DOWNLOADING_CATALOGUE_ITEM.getValue());

                // TODO get catalogue id and pass it into the paramenter
                restAdapter.getCatalogue(catalogueId);
                break;
            case Constants.API_TYPE_PRODUCT_DETAIL:
                int productDetail = intent.getIntExtra(Constants.EXTRA_PRODUCT_DETAIL, 0);
                MixPanelUtils.sendTrackToMixPanel(getApplicationContext(),
                        MixPanelUtils.mixPanelTrackFactory(AsosCategories.PRODUCT_DETAIL, String.valueOf(productDetail)), Constants.PRODUCT_DETAIL_SCREEN);

                AsosApplication.getInstance().trackEvent(
                        AsosCategories.PRODUCT_DETAIL.getValue(),
                        AnalyticsActions.DOWNLOAD.getValue(),
                        AnalyticLabel.DOWNLOADING_PRODUCT_DETAIL.getValue());

                restAdapter.getProductDetail(productDetail);
                break;
            case Constants.API_TYPE_MAN_AND_WOMEN :
                restAdapter.getManAndWomenCollections();
                break;

        }


    }
}
