
package com.tae.asos.model.api.catalogue;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Facet implements Parcelable{

    @SerializedName("FacetValues")
    @Expose
    private List<FacetValue> FacetValues = new ArrayList<FacetValue>();
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Sequence")
    @Expose
    private Integer Sequence;

    protected Facet(Parcel in) {
        Id = in.readString();
        Name = in.readString();
    }

    public static final Creator<Facet> CREATOR = new Creator<Facet>() {
        @Override
        public Facet createFromParcel(Parcel in) {
            return new Facet(in);
        }

        @Override
        public Facet[] newArray(int size) {
            return new Facet[size];
        }
    };

    /**
     * 
     * @return
     *     The FacetValues
     */
    public List<FacetValue> getFacetValues() {
        return FacetValues;
    }

    /**
     * 
     * @param FacetValues
     *     The FacetValues
     */
    public void setFacetValues(List<FacetValue> FacetValues) {
        this.FacetValues = FacetValues;
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

    /**
     * 
     * @return
     *     The Sequence
     */
    public Integer getSequence() {
        return Sequence;
    }

    /**
     * 
     * @param Sequence
     *     The Sequence
     */
    public void setSequence(Integer Sequence) {
        this.Sequence = Sequence;
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
