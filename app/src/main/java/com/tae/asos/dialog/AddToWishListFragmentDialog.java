package com.tae.asos.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.tae.asos.R;
import com.tae.asos.constants.Constants;
import com.tae.asos.manager.WishManager;
import com.tae.asos.model.wishlist.Wish;
import com.tae.asos.service.SaveWishListTask;
import com.tae.asos.service.WishService;

/**
 * Created by Eduardo on 21/12/2015.
 */
public class AddToWishListFragmentDialog extends DialogFragment {

    public static String TAG = "AddToWishListFragmentDialog";
    private Wish wish;

    public static AddToWishListFragmentDialog newInstance(Wish wish) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARGS_WISHLIST, wish);
        AddToWishListFragmentDialog fragmentDialog = new AddToWishListFragmentDialog();
        fragmentDialog.setArguments(bundle);
        return fragmentDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wish = (Wish) getArguments().get(Constants.ARGS_WISHLIST);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.wishlist_dialog_fragment_title);
        builder.setMessage(R.string.wishlist_dialog_fragment_message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO start service to save data in data base passing the wish
//                WishManager.getInstance(getActivity()).saveWish(wish);
                getActivity().startService(WishService.makeIntent(getActivity(), wish));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
