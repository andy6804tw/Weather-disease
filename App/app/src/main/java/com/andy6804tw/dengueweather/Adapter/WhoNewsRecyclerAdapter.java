package com.andy6804tw.dengueweather.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy6804tw.dengueweather.LoadingSplash.SplashActivity;
import com.andy6804tw.dengueweather.R;

/**
 * Created by andy6804tw on 2017/8/3.
 */

public class WhoNewsRecyclerAdapter extends RecyclerView.Adapter<WhoNewsRecyclerAdapter.ViewHolder> {

    private int mImages=R.mipmap.news_who;
    private Context mContext;

    public WhoNewsRecyclerAdapter(Context context){
        mContext=context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivImage;
        private TextView tvTitle,tvDetail,tvDate;
        private CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            ivImage=(ImageView)itemView.findViewById(R.id.ivImage);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvDetail=(TextView)itemView.findViewById(R.id.tvDetail);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            cardView=(CardView)itemView.findViewById(R.id.cardView);
//            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
//            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
//            itemDetail =
//                    (TextView)itemView.findViewById(R.id.item_detail);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    int position = getAdapterPosition();
//
//
//                }
//            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_who_news_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.ivImage.setImageResource(mImages);
        viewHolder.tvTitle.setText(SplashActivity.whoNewsList.get(position).getTitle());
        viewHolder.tvDetail.setText(SplashActivity.whoNewsList.get(position).getDescription());
        viewHolder.tvDate.setText(SplashActivity.whoNewsList.get(position).getDate());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SplashActivity.whoNewsList.get(position).getUrl())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return SplashActivity.whoNewsList.size();
    }

}