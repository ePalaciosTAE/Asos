
package com.tae.asos.model.api.catalogue;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FacetValue implements Parcelable{

    @SerializedName("Count")
    @Expose
    private Integer Count;
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Name")
    @Expose
    private String Name;

    protected FacetValue(Parcel in) {
        Id = in.readString();
        Name = in.readString();
    }

    public static final Creator<FacetValue> CREATOR = new Creator<FacetValue>() {
        @Override
        public FacetValue createFromParcel(Parcel in) {
            return new FacetValue(in);
        }

        @Override
        public FacetValue[] newArray(int size) {
            return new FacetValue[size];
        }
    };

    /**
     * 
     * @return
     *     The Count
     */
    public Integer getCount() {
        return Count;
    }

    /**
     * 
     * @param Count
     *     The Count
     */
    public void setCount(Integer Count) {
        this.Count = Count;
    }

    /**
     * 
     * @return
     *     The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * 
     * @param Id
     *     The Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * 
     * @return
     *     The Name
     */
    public String getName() {
        return Name;
    }

    /**
     * 
     * @param Name
     *     The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
    }
}
