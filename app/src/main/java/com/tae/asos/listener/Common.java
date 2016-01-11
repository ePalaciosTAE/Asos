package com.tae.asos.listener;

import java.util.List;

/**
 * Created by Eduardo on 22/12/2015.
 * TODO: explanation
 * This interface will have the needed methods to populate the CommonAdapter. For that the CommonAdapter will recive a List<Common>.
 * And the passed list of Common in the adapter's constructor will be casted from a List<Listing> or List<Wish>.
 * To make the cast happens we need to implement the Common interface in each of the models (Listing and Wish)
 */
public interface Common {

    public List<String> getCommonImageUrl();

    public String getCommonTitle();

    public boolean isListing ();
}
