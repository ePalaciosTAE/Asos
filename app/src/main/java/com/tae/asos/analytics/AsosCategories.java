package com.tae.asos.analytics;

/**
 * Created by Eduardo on 05/01/2016.
 */
public enum AsosCategories {
    MAN_CATALOGUE ("Man catalogue"),
    WOMAN_CATALOGUE ("Man woman"),
    CATALOGUE_ITEM ("Catalogue item"),
    PRODUCT_DETAIL ("Product detail"),
    WHISLIST_ITEM ("Whislist"),
    CART_ITEM ("Cart item");

    private String value;

    private AsosCategories(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
//
//    @Override
//    public String toString() {
//        return value;
//    }
}
