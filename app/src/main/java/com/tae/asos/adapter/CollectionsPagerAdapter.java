package com.tae.asos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;

import com.tae.asos.constants.Constants;
import com.tae.asos.fragment.SingleCollectionFragment;
import com.tae.asos.model.api.person.Listing;
import com.tae.asos.model.api.person.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 20/12/2015.
 */
public class CollectionsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = {"Men", "Women"};
    private List<Listing> listingList;
    private SparseArray<Person> collectionCloths;

    public CollectionsPagerAdapter(FragmentManager fm, SparseArray<Person> collectionCloths) {
        super(fm);
        this.listingList = listingList;
        this.collectionCloths = collectionCloths;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("CollectionsPagerAdapter", "onCreate()");
        Person person;
        if (position == 0) {
            person = collectionCloths.get(Constants.SPARSE_KEY_MAN);
            return SingleCollectionFragment.newInstance((ArrayList<Listing>) person.getListing());
        } else {
            person = collectionCloths.get(Constants.SPARSE_KEY_WOMEN);
            return SingleCollectionFragment.newInstance((ArrayList<Listing>) person.getListing());
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return tabTitles[position];
        } else {
            return tabTitles[position];
        }
    }


}
