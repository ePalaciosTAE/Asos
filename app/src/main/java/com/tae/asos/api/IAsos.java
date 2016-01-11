package com.tae.asos.api;

import com.tae.asos.constants.Constants;
import com.tae.asos.model.api.catalogue.Catalogue;
import com.tae.asos.model.api.person.Person;
import com.tae.asos.model.api.product.Product;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Eduardo on 19/12/2015.
 */
public interface IAsos {

    @GET(Constants.END_POINT_CLOTHS_MAN)
    public void getManClothes(Callback<Person> response);

    @GET(Constants.END_POINT_CLOTHS_WOMAN)
    public void getWomanClothes(Callback<Person> response);

    @GET(Constants.END_POINT_CATALOGUE)
    public void getCatalogue(@Query(Constants.QUERY_CAT_ID) String queryCatId, Callback<Catalogue> response);

    @GET(Constants.END_POINT_PRODUCT_DETAIL)
    public void getProductDetail(@Query(Constants.QUERY_CAT_ID)int query, Callback<Product> response);
}
