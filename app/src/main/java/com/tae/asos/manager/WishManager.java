package com.tae.asos.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.tae.asos.R;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.persistence.AsosDAO;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eduardo on 22/12/2015.
 */
public class WishManager {

    private static WishManager instance;
    private Context context;

    private WishManager(Context context) {
        this.context = context;
    }




    public static WishManager getInstance(Context context) {
        if (instance == null) {
            instance = new WishManager(context);
        }
        return instance;
    }


    public boolean saveWish(Wish wish) {
        if (!wish.isCart()) {
            return AsosDAO.getInstance(context).insert(wish);
        } else {
            // TODO implent save Caart/basket here
            return false;
        }
//        SaveWishListTask saveWishListTask = new SaveWishListTask(context);
//        saveWishListTask.execute(wish);
    }

    public ArrayList<Wish> getWishes() {
        return AsosDAO.getInstance(context).getWishes();
    }

    private void saveCartCounter(int count) {
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_chart_counter), Context.MODE_PRIVATE);
        sp.edit().putInt(context.getString(R.string.sp_counter_key), count).apply(); // save total count
    }

    public void saveCart(final Wish wis, final int count) {
        saveCartCounter(count);
//        Set<String> wishValues = new HashSet<>(1);
        StringBuilder bulBuilder = new StringBuilder();
        bulBuilder.append(wis.getProductId());
        bulBuilder.append(",");
        bulBuilder.append(wis.getImageUrl());
        bulBuilder.append(",");
        bulBuilder.append(wis.getTitle());
//        wishValues.add(String.valueOf(wis.getProductId()));
//        wishValues.add(wis.getImageUrl());
//        wishValues.add(wis.getTitle());
//        wishValues.add("sdfsdfsdf");
//        wishValues.add("asddsfsdfsdfsdfsddsfsdfsd");
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_set_cart), Context.MODE_PRIVATE);
//        sp.edit().putInt(context.getString(R.string.sp_counter_key), count) // save total count
//                        .putInt(String.valueOf(productId), count) // save key productId - val: count
//        sp.edit().putStringSet(String.valueOf(count), wishValues).apply();
        sp.edit().putString(String.valueOf(count), bulBuilder.toString()).apply();



//        Collection<?> col = keys.values();
////        Set<?> set = (Set<?>) col;
//        List<Object> list = Arrays.asList(col.toArray());
//        List<String> listString = new ArrayList<>();
//        for (Object o : list) {
//            HashSet<String> hs = (HashSet<String>) o;
////            Log.i("Manager", "list element: " + hs.toString());
//            Iterator<String> iterator = hs.iterator();
//            while (iterator.hasNext()) {
//                String value = iterator.next();
////                Log.i("Manager", "key(map.Entry) into while in hatshset: " + key);
////                Log.i("Manager", "hashet value int while in hashset: " + value);
//            }
//        }
////////////////////////////////////////////////////////////////////////////



//        Map<String, String> keys = (Map<String, String>) sp.getAll();
//        SparseArray<String> sparseArrayCart = new SparseArray<>(keys.size());
//        for (Map.Entry<String, String> mapEntry : keys.entrySet()) {
//            String key  = mapEntry.getKey();
//            String value = mapEntry.getValue();
//            Log.i("Manager", "key(map.Entry): " + key);
//            Log.i("Manager", "mapEntry(getValue): " + mapEntry.getValue());
//            sparseArrayCart.put(Integer.parseInt(key), value);
//        }
//        sparseArrayCart.size();
//        String[] array = null;
//        List<Wish> carts = new ArrayList<>();
//        for (int i = 0; i < sparseArrayCart.size(); i++) {
//            String s = sparseArrayCart.get(i);
//            if (s != null) {
//                array = s.split(",");
//                    Log.i("Manager","sparse value: " +  array[0]);
//                    Wish wish = new Wish(Long.parseLong(array[0]), array[1], array[2], true);
//                    carts.add(wish);
//
//            }
//        }
//        carts.size();

    }


}
