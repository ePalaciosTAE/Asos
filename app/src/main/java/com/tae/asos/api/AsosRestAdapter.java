package com.tae.asos.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;

import com.tae.asos.constants.Constants;
import com.tae.asos.model.api.catalogue.Catalogue;
import com.tae.asos.model.api.catalogue.Listing;
import com.tae.asos.model.api.person.Person;
import com.tae.asos.model.api.product.Product;
import com.tae.asos.utils.Utils;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Eduardo on 19/12/2015.
 */
public class AsosRestAdapter {

    private Context context;
    private IAsos iAsos;

    public AsosRestAdapter (Context context) {
        this.context = context;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        iAsos = restAdapter.create(IAsos.class);
    }

    public void getManCloths() {
        iAsos.getManClothes(new Callback<Person>() {
            @Override
            public void success(Person manCloths, Response response) {
                Log.i("MAIN", "SUcces downloading man cloth: ");
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS)
                                .putExtra(Constants.EXTRA_MEN_CLOTH, manCloths)
                                .putExtra(Constants.EXTRA_TYPE, Constants.API_TYPE_MAN_CLOTH));
            }

            @Override
            public void failure(RetrofitError error) {
                handleErrorResponse(error);
            }
        });
    }

    public void getWomenCloth() {
        iAsos.getWomanClothes(new Callback<Person>() {
            @Override
            public void success(Person womenCloths, Response response) {
                Log.i("MAIN", "Succes downloading women cloth: ");
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS)
                                .putExtra(Constants.EXTRA_WOMEN_CLOTH, womenCloths)
                                .putExtra(Constants.EXTRA_TYPE, Constants.API_TYPE_WOMAN_CLOTH));
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.HTTP)) {
                    Utils.showToastErrorInRetrofit(context, "Http error: ", error);
                } else if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    Utils.showToastErrorInRetrofit(context, "Network error: ", error);
                } else if (error.getKind().equals(RetrofitError.Kind.CONVERSION)) {
                    Utils.showToastErrorInRetrofit(context, "Conversion error: ", error);
                } else {
                    Utils.showToastErrorInRetrofit(context, "Unknown error: ", error);
                }
            }
        });
    }

    public void getCatalogue(String catalogueId) {
        iAsos.getCatalogue(catalogueId, new Callback<Catalogue>() {
            @Override
            public void success(Catalogue catalogue, Response response) {
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS)
                                .putExtra(Constants.EXTRA_CATALOGUE, catalogue)
                                .putExtra(Constants.EXTRA_TYPE, Constants.API_TYPE_CATALOGUE));
            }

            @Override
            public void failure(RetrofitError error) {
                handleErrorResponse(error);
            }
        });
    }

    public void getProductDetail(int productId) {
        iAsos.getProductDetail(productId, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS)
                                .putExtra(Constants.EXTRA_PRODUCT_DETAIL, product)
                                .putExtra(Constants.EXTRA_TYPE, Constants.API_TYPE_PRODUCT_DETAIL));
            }

            @Override
            public void failure(RetrofitError error) {
                handleErrorResponse(error);
            }
        });
    }

    public void getManAndWomenCollections() {
        final SparseArray<Person> clothCollections = new SparseArray<>(2);
        iAsos.getManClothes(new Callback<Person>() {
            @Override
            public void success(Person person, Response response) { // get data from man collections
                Log.i("RestAdapter", "download man collextions download ok");
                clothCollections.put(Constants.SPARSE_KEY_MAN, person);
                iAsos.getWomanClothes(new Callback<Person>() { // after loading the men data, load the  women data
                    @Override
                    public void success(Person person, Response response) {
                        Log.i("RestAdapter", "download woman collextions download ok");
                        clothCollections.put(Constants.SPARSE_KEY_WOMEN, person);
                        Log.i("RestAdapter", "send sparse array with both collections to main activity");
                        //send man an women data to main activity
                        Bundle bundle = new Bundle();
                        bundle.putSparseParcelableArray(Constants.EXTRA_MAN_AND_WOMEN_CLOTH, clothCollections);
                        LocalBroadcastManager.getInstance(context)
                                .sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_SUCCESS)
                                        .putExtra(Constants.EXTRA_BUNDLE, bundle)
                                        .putExtra(Constants.EXTRA_TYPE, Constants.API_TYPE_MAN_AND_WOMEN));
                    }


                    @Override
                    public void failure(RetrofitError error) {
                        handleErrorResponse(error);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                handleErrorResponse(error);
            }
        });
    }

    private void handleErrorResponse(RetrofitError error) {
        if (error.getKind().equals(RetrofitError.Kind.HTTP)) {
            Utils.showToastErrorInRetrofit(context, "Http error: ", error);
        } else if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
            Utils.showToastErrorInRetrofit(context, "Network error: ", error);
        } else if (error.getKind().equals(RetrofitError.Kind.CONVERSION)) {
            Utils.showToastErrorInRetrofit(context, "Conversion error: ", error);
        } else {
            Utils.showToastErrorInRetrofit(context, "Unknown error: ", error);
        }
    }


}
