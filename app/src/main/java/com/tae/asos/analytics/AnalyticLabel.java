package com.tae.asos.analytics;

/**
 * Created by Eduardo on 08/01/2016.
 */
public enum AnalyticLabel {

    DOWNLOADING_MAN_COLLECTION ("Event: Downloading man's collection"),
    DOWNLOADING_WOMAN_COLLECTION ("Event: Downloading woman's collection"),
    DOWNLOADING_CATALOGUE_ITEM ("Event: Downloading Catalogue item"),
    DOWNLOADING_PRODUCT_DETAIL ("Event: Downloading Product detail"),
    ADDING_WHISLIST_ITEM ("Event: Adding Whislist item"),
    ADDING_CART_ITEM ("Event: Adding Cart item");

    private String value;

    private AnalyticLabel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
