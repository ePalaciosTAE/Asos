package com.tae.asos.analytics;

import android.content.Context;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.tae.asos.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eduardo on 05/01/2016.
 */
public class MixPanelUtils {

    public static final String TAG = "MixPanelUtils";

    public static void sendTrackToMixPanel(Context context, JSONObject properties, String screen) {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, Constants.MIX_PANEL_PROJECT_TOKEN);
        mixpanel.track(screen, properties);
        mixpanel.getPeople().set(properties); // TODO is this need it?¿?¿¿
    }

    public static JSONObject mixPanelTrackFactory (AsosCategories asosCategories, String id) {
        JSONObject properties = null;
        try {
            properties = new JSONObject();
            switch (asosCategories) {
                case MAN_CATALOGUE :
                    properties.put(Constants.MIX_PANEL_MAN_CATALOGUE, asosCategories.getValue());
                    break;
                case WOMAN_CATALOGUE :
                    properties.put(Constants.MIX_PANEL_WOMAN_CATALOGUE, asosCategories.getValue());
                    break;
                case CATALOGUE_ITEM :
                    properties.put(Constants.MIX_PANEL_CATALOGUE_ITEM, asosCategories.getValue());
                    properties.put(Constants.EXTRA_CATALOGUE_ID, id);
                    Log.i(TAG, "mixPanelTrackFactory: catalogue item id: " + id);
                    break;
                case PRODUCT_DETAIL :
                    properties.put(Constants.MIX_PANEL_PRODUCT_DETAIL, asosCategories.getValue());
                    properties.put(Constants.EXTRA_PRODUCT_DETAIL, id);
                    Log.i(TAG, "mixPanelTrackFactory: product detail item id: " + id);
                    break;
                case WHISLIST_ITEM :
                    properties.put(Constants.MIX_PANEL_WHISLIST_ITEM, asosCategories.getValue());
                    properties.put(Constants.EXTRA_WISH, id);
                    Log.i(TAG, "mixPanelTrackFactory: wish item id: " + id);
                    break;
                case CART_ITEM :
                    properties.put(Constants.MIX_PANEL_CART_ITEM, asosCategories.getValue());
                    properties.put(Constants.EXTRA_PRODUCT_DETAIL, id);
                    Log.i(TAG, "mixPanelTrackFactory: cart item id: " + id);
                    break;
            }
        } catch (JSONException e) {
            Log.e("MixPanelUtils", "Unable to add properties to JSONObject", e);
        }
        return properties;
    }
}
