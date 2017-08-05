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

public class TWNewsRecyclerAdapter extends RecyclerView.Adapter<TWNewsRecyclerAdapter.ViewHolder> {


    private int[] mImages={R.mipmap.news15,R.mipmap.news1,R.mipmap.news2,R.mipmap.news3,R.mipmap.news4,R.mipmap.news5,R.mipmap.news6,R.mipmap.news7
            ,R.mipmap.news8,R.mipmap.news9,R.mipmap.news10,R.mipmap.news11,R.mipmap.news12,R.mipmap.news13,R.mipmap.news14};
    private Context mContext;

    public TWNewsRecyclerAdapter(Context context){
        mContext=context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivImage;
        private TextView tvTitle,tvDate;
        private CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            ivImage=(ImageView)itemView.findViewById(R.id.ivImage);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
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
                .inflate(R.layout.card_tw_news_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.ivImage.setImageResource(mImages[position]);
        viewHolder.tvTitle.setText(SplashActivity.twNewsList.get(position).getTitle());
        viewHolder.tvDate.setText(SplashActivity.twNewsList.get(position).getDate());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SplashActivity.twNewsList.get(position).getUrl())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return SplashActivity.twNewsList.size();
    }

}