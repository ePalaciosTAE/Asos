package com.tae.asos.analytics;

/**
 * Created by Eduardo on 08/01/2016.
 */
public enum AnalyticsActions {

    DOWNLOAD ("Download"),
    ADD_TO_CART ("Add to cart"),
    ADD_TO_WHISLIST("Add to wish list");

    private String value;

    private AnalyticsActions (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
