package com.tae.asos.persistence;

import android.provider.BaseColumns;

/**
 * Created by Eduardo on 21/12/2015.
 */
public final class AsosContract {

    public static abstract class AsosEntry implements BaseColumns{
        public static final String TABLE_ASOS_WISHLIST = "data_base_asos_wishlist";
        public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
    }

    public static final String TEXT = " TEXT";
    public static final String INTEGER = " INTEGER";
    public static final String CREATE = "CREATE TABLE ";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    public static final String PK = " INTEGER PRIMARY KEY" ;
    public static final String COMA = ", ";

    public static final String SQL_CREATE_ASOS_WISHLIST_TABLE =
            CREATE + AsosEntry.TABLE_ASOS_WISHLIST + " (" +
                    AsosEntry._ID + PK + COMA +
                    AsosEntry.COLUMN_NAME_PRODUCT_ID + INTEGER + COMA +
                    AsosEntry.COLUMN_NAME_TITLE + TEXT + COMA +
                    AsosEntry.COLUMN_NAME_IMAGE_URL + TEXT + ")";

    public static final String SQL_DELETE_IMMIGRANT_TABLE =
            DROP_TABLE + AsosEntry.TABLE_ASOS_WISHLIST;
}
