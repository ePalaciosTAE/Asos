package com.tae.asos.model.wishlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tae.asos.listener.Common;
import com.tae.asos.model.api.catalogue.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 21/12/2015.
 */
public class Wish implements Parcelable, Common{
    private long productId;
    private String imageUrl;
    private String title;
    private boolean cart;

    public Wish() {
    }

    public Wish(long productId, String imageUrl, String title, boolean cart) {
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.cart = cart;
    }

    protected Wish(Parcel in) {
        productId = in.readLong();
        imageUrl = in.readString();
        title = in.readString();
        cart = in.readByte() != 0;
    }

    public static final Creator<Wish> CREATOR = new Creator<Wish>() {
        @Override
        public Wish createFromParcel(Parcel in) {
            return new Wish(in);
        }

        @Override
        public Wish[] newArray(int size) {
            return new Wish[size];
        }
    };

    public boolean isCart() {
        return cart;
    }

    public void setCart(boolean cart) {
        this.cart = cart;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getCommonImageUrl() {
        List<String> list = new ArrayList<>();
        list.add(getImageUrl());
        return list;
    }
    @Override
    public String getCommonTitle() {
        return getTitle();
    }

    @Override
    public boolean isListing() {
        return false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(productId);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeByte((byte) (cart ? 1 : 0));
    }
}
