package com.tae.asos.service;

import android.content.Context;
import android.os.AsyncTask;

import com.tae.asos.R;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.persistence.AsosDAO;
import com.tae.asos.utils.Utils;

/**
 * Created by Eduardo on 21/12/2015.
 */
public class SaveWishListTask extends AsyncTask<Wish, Void, Boolean> {

    private Context context;

    public SaveWishListTask(Context context) {
        this.context = context;
    }


    @Override
    protected Boolean doInBackground(Wish... params) {
        return AsosDAO.getInstance(context).insert(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            Utils.showToast(context, context.getString(R.string.data_base_item_added));
        }
    }
}
