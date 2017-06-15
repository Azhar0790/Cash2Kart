package com.example.azhar_sarps.cash2kart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.azhar_sarps.cash2kart.pojo.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by azhar-sarps on 31-May-17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<Message> list;
    String category;
    Animation animation, animation2;
    View itemView;
    private static final String Myntra_url = "https://linksredirect.com/?pub_id=12078CL10975&subid=sub_id&source=linkkit&url=https%3A%2F%2Fwww.myntra.com%2F";
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_category,ll_deals;
        public TextView tv_category,tv_link,tv_url_img;
        public ImageView iv_category,iv_deals;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_category = (LinearLayout) itemView.findViewById(R.id.ll_category);
            ll_deals = (LinearLayout) itemView.findViewById(R.id.ll_deals);
            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            tv_link = (TextView) itemView.findViewById(R.id.tv_link);
            tv_url_img = (TextView) itemView.findViewById(R.id.tv_url_img);
            iv_category = (ImageView) itemView.findViewById(R.id.iv_category);
            iv_deals = (ImageView) itemView.findViewById(R.id.iv_deals);
        }
    }

    public CategoryAdapter(Context context, List<Message> list,String category) {
        this.context = context;
        this.list = list;
        this.category = category;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(category.equals("deals")){
            holder.ll_deals.setVisibility(View.VISIBLE);
            holder.ll_category.setVisibility(View.GONE);
        }else {
            holder.ll_deals.setVisibility(View.GONE);
            holder.ll_category.setVisibility(View.VISIBLE);
        }


        holder.tv_category.setText(list.get(position).getStTitle());
        holder.tv_link.setText(list.get(position).getStLink());
        holder.tv_url_img.setText(list.get(position).getStImg());
        holder.tv_category.setVisibility(View.GONE);
        animation = AnimationUtils.loadAnimation(context, R.anim.grid_anim);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.animation_fade);
        Picasso.with(context).load("https://cash2kart.com/upload/store/" + list.get(position).getStImg()).into(holder.iv_category);
        Picasso.with(context).load("https://cash2kart.com/upload/store/" + list.get(position).getStImg()).into(holder.iv_deals);
        holder.iv_category.startAnimation(animation);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("url :- " + list.get(position).getStLink());
                System.out.println("image_url :- " + holder.tv_url_img.getText().toString());

//                if(holder.tv_link.getText().toString().equals("https://linksredirect.com/?pub_id=12078CL10975&subid=sub_id&source=linkkit&url=https%3A%2F%2Fwww.myntra.com%2F")){
//                    Intent i = new Intent(context, WebviewActivity.class);
//                    i.putExtra("url", holder.tv_link.getText().toString());
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(i);
//                }else {
                    Intent i = new Intent(context, WebviewActivity.class);
                    i.putExtra("url", holder.tv_link.getText().toString());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
//                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}