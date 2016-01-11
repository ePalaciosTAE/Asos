package com.tae.asos.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;


import com.tae.asos.listener.DatabaseDAO;
import com.tae.asos.model.wishlist.Wish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduardo on 21/12/2015.
 */
public class AsosDAO implements DatabaseDAO {

    private static AsosDAO instance;
//    private Context context;
    private AsosBdHelper asosBdHelper;

    private AsosDAO (Context context) {
//        this.context = context;
        openDataBase(context);
    }

    public static AsosDAO getInstance(Context context) {
        if (instance == null) {
            instance = new AsosDAO(context);
        }
        return instance;
    }

    private void openDataBase(Context context) {
        asosBdHelper = new AsosBdHelper(context);
    }

    @Override
    public boolean insert(Wish wish) {
        SQLiteDatabase db = asosBdHelper.getWritableDatabase();
        ContentValues values = getContentValues(wish);
        long rowId = 0;
        try {
            rowId = db.insert(
                    AsosContract.AsosEntry.TABLE_ASOS_WISHLIST,
                    AsosContract.AsosEntry.COLUMN_NAME_PRODUCT_ID,
                    values);
        } catch (SQLiteException e) {
            Log.e("AsosDAO", "Error inserting row in Asos DB " + e + "\n rowId value = " + rowId);
        } finally {
            db.close();
        }
        return rowId > -1;
    }

    @NonNull
    private ContentValues getContentValues(Wish wish) {
        ContentValues values = new ContentValues();
        values.put(AsosContract.AsosEntry.COLUMN_NAME_PRODUCT_ID, wish.getProductId());
        values.put(AsosContract.AsosEntry.COLUMN_NAME_IMAGE_URL, wish.getImageUrl());
        values.put(AsosContract.AsosEntry.COLUMN_NAME_TITLE, wish.getTitle());
        return values;
    }

    @Override
    public ArrayList<Wish> getWishes() {
//        Cursor cursor = null;
        SQLiteDatabase db = asosBdHelper.getReadableDatabase();
        String[] projection = {
                AsosContract.AsosEntry._ID,
                AsosContract.AsosEntry.COLUMN_NAME_PRODUCT_ID,
                AsosContract.AsosEntry.COLUMN_NAME_TITLE,
                AsosContract.AsosEntry.COLUMN_NAME_IMAGE_URL
        };
        String sortOrder =
                AsosContract.AsosEntry.COLUMN_NAME_TITLE + " DESC";
        ArrayList<Wish> wishs = null;
        try {
            Cursor cursor = db.query(
                    AsosContract.AsosEntry.TABLE_ASOS_WISHLIST,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder);
            cursor.moveToFirst();
            wishs = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                long productId= cursor.getLong(cursor.getColumnIndex(AsosContract.AsosEntry.COLUMN_NAME_PRODUCT_ID));
                String imageUrl = cursor.getString(cursor.getColumnIndex(AsosContract.AsosEntry.COLUMN_NAME_IMAGE_URL));
                String title = cursor.getString(cursor.getColumnIndex(AsosContract.AsosEntry.COLUMN_NAME_TITLE));
                wishs.add(new Wish(productId,title, imageUrl, false));
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e("AsosDAO", "Error reading rows in Asos DB " + e);
        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
            db.close();
        }

        return wishs;
    }
}
