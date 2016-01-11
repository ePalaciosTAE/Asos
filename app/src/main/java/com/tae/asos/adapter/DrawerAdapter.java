package com.tae.asos.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tae.asos.R;
import com.tae.asos.constants.Constants;
import com.tae.asos.listener.ICatalogeId;
import com.tae.asos.listener.ItemClickListener;
import com.tae.asos.model.api.person.Listing;
import com.tae.asos.service.AsosAPIService;

import java.util.List;

/**
 * Created by Eduardo on 18/12/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private List<Listing> titles;
    private Context context;
    private ICatalogeId iCatalogeId;
    private ProgressDialog progressDialog;

    public DrawerAdapter(Context context) {
        this.context = context;
    }


    public DrawerAdapter(Context context, List<Listing> titles) {
        this.titles = titles;
        this.context = context;
    }

    public void setListing(List<Listing> titles) {
        this.titles = titles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.drawer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(titles.get(position).getName());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isClicked) {  //here we staart a service to  display a catalogue withn the man or woman collections
                Log.i("DrawerAdapter", "id " + titles.get(position).getCategoryId() + // the category id will be use to know which catalogue do we have to display
                        "\n name: " + titles.get(position).getName());
                context.startService(AsosAPIService.makeIntentWithCatalogueId(
                                context,
                                Constants.API_TYPE_CATALOGUE,
                                titles.get(position).getCategoryId())
                );
                if (progressDialog != null) {
                    progressDialog.show();
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public Listing getListingItem(int position) {
        return titles.get(position);
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.drawer_tv_title);
            itemView.setOnClickListener(this);
//            iDrawerItemClickListener = (IDrawerItemClickListener) itemView.getContext();
        }


        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition(), true);
        }
    }
}
