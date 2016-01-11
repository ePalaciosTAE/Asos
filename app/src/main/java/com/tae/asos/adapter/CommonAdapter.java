package com.tae.asos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tae.asos.R;
import com.tae.asos.listener.Common;
import com.tae.asos.listener.ItemClickListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eduardo on 19/12/2015.
 * TODO: IMPORTANT
 * This adapter works with a Common interface that we will use as the container of the data to
 * display in the adapter. To pass in the constructor the Common object, you need to create it from the list of
 * Wish or Listing and pass it into the params.
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    private Context context;
    private List<Common> commonList;

    public CommonAdapter(Context context, List<Common> commonList) {
        this.context = context;
        this.commonList = commonList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
//        view = inflater.inflate(R.layout.fragment_catalogue_item, parent, false);
        if (commonList.get(0).isListing()) { // inflate catalogue xml
            view = inflater.inflate(R.layout.fragment_catalogue_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.fragment_wish_list_item, parent, false);
        }
        return new ViewHolder(view, commonList.get(0).isListing());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context)
                .load(commonList.get(position).getCommonImageUrl().get(0))
                .resize(75, 75)
                .into(holder.circleImageView);
        holder.tvPrice.setText(commonList.get(position).getCommonTitle());
    }

    @Override
    public int getItemCount() {
        return commonList.size();
    }

//    public int getProductId(int position) {
//        return commonList.get(position).getProductId();
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView circleImageView;
        private TextView tvPrice;
//        private ItemClickListener itemClickListener;


        public ViewHolder(View itemView, boolean isListing) {
            super(itemView);
//            itemView.setOnClickListener(this);
//            itemClickListener = (ItemClickListener) context;

//            itemClickListener = (ItemClickListener) itemView.getContext();
//            circleImageView = (CircleImageView) itemView.findViewById(R.id.circle_image_view);
//            tvPrice = (TextView) itemView.findViewById(R.id.tv_catalogue_price);
            if (isListing) { // TODO send the mayout from contructor
                circleImageView = (CircleImageView) itemView.findViewById(R.id.circle_image_view);
                tvPrice = (TextView) itemView.findViewById(R.id.tv_catalogue_price);
            } else {
                circleImageView = (CircleImageView) itemView.findViewById(R.id.circle_image_view);
                tvPrice = (TextView) itemView.findViewById(R.id.tv_wish_list);
            }
        }

        @Override
        public void onClick(View v) {
//            itemClickListener.onItemClick(v, getAdapterPosition(), true);
        }

        public void setOnItemClickListener(ItemClickListener itemClickListener) {
//            this.itemClickListener = itemClickListener;
        }

    }
}
