package com.tae.asos.listener;

import com.tae.asos.model.wishlist.Wish;

import java.util.List;

/**
 * Created by Eduardo on 22/12/2015.
 */
public interface DatabaseDAO {

    public boolean insert(Wish wish);
    public List<Wish> getWishes();
}
