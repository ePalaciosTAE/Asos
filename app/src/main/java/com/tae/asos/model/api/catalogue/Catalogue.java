
package com.tae.asos.model.api.catalogue;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Catalogue implements Parcelable{

    @SerializedName("AlsoSearched")
    @Expose
    private List<Object> AlsoSearched = new ArrayList<Object>();
    @SerializedName("Description")
    @Expose
    private String Description;
    @SerializedName("Facets")
    @Expose
    private List<Facet> Facets = new ArrayList<Facet>();
    @SerializedName("ItemCount")
    @Expose
    private Integer ItemCount;
    @SerializedName("Listings")
    @Expose
    private List<Listing> Listings = new ArrayList<Listing>();
    @SerializedName("RedirectUrl")
    @Expose
    private String RedirectUrl;
    @SerializedName("SortType")
    @Expose
    private String SortType;

    protected Catalogue(Parcel in) {
        Description = in.readString();
        RedirectUrl = in.readString();
        SortType = in.readString();
    }

    public static final Creator<Catalogue> CREATOR = new Creator<Catalogue>() {
        @Override
        public Catalogue createFromParcel(Parcel in) {
            return new Catalogue(in);
        }

        @Override
        public Catalogue[] newArray(int size) {
            return new Catalogue[size];
        }
    };

    /**
     * 
     * @return
     *     The AlsoSearched
     */
    public List<Object> getAlsoSearched() {
        return AlsoSearched;
    }

    /**
     * 
     * @param AlsoSearched
     *     The AlsoSearched
     */
    public void setAlsoSearched(List<Object> AlsoSearched) {
        this.AlsoSearched = AlsoSearched;
    }

    /**
     * 
     * @return
     *     The Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * 
     * @param Description
     *     The Description
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * 
     * @return
     *     The Facets
     */
    public List<Facet> getFacets() {
        return Facets;
    }

    /**
     * 
     * @param Facets
     *     The Facets
     */
    public void setFacets(List<Facet> Facets) {
        this.Facets = Facets;
    }

    /**
     * 
     * @return
     *     The ItemCount
     */
    public Integer getItemCount() {
        return ItemCount;
    }

    /**
     * 
     * @param ItemCount
     *     The ItemCount
     */
    public void setItemCount(Integer ItemCount) {
        this.ItemCount = ItemCount;
    }

    /**
     * 
     * @return
     *     The Listings
     */
    public List<Listing> getListings() {
        return Listings;
    }

    /**
     * 
     * @param Listings
     *     The Listings
     */
    public void setListings(List<Listing> Listings) {
        this.Listings = Listings;
    }

    /**
     * 
     * @return
     *     The RedirectUrl
     */
    public String getRedirectUrl() {
        return RedirectUrl;
    }

    /**
     * 
     * @param RedirectUrl
     *     The RedirectUrl
     */
    public void setRedirectUrl(String RedirectUrl) {
        this.RedirectUrl = RedirectUrl;
    }

    /**
     * 
     * @return
     *     The SortType
     */
    public String getSortType() {
        return SortType;
    }

    /**
     * 
     * @param SortType
     *     The SortType
     */
    public void setSortType(String SortType) {
        this.SortType = SortType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeString(RedirectUrl);
        dest.writeString(SortType);
    }
}
