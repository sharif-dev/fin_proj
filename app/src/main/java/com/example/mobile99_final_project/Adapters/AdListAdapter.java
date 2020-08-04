package com.example.mobile99_final_project.Adapters;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile99_final_project.DataModels.AdData;
import com.example.mobile99_final_project.Enums.HandlerMassages;
import com.example.mobile99_final_project.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AdListAdapter extends RecyclerView.Adapter<AdListAdapter.AdViewHolder> {


    public static class AdViewHolder extends RecyclerView.ViewHolder{

        ImageView adImageView;
        TextView titleTextView;
        TextView ownerTextView;
        TextView categoryTextView;
        CardView cardView;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            adImageView = itemView.findViewById(R.id.ad_imageView);
            titleTextView = itemView.findViewById(R.id.ad_title_textView);
            ownerTextView = itemView.findViewById(R.id.ad_owner_textView);
            categoryTextView = itemView.findViewById(R.id.ad_category_textView);
            cardView = itemView.findViewById(R.id.ad_cardView);
        }
    }

    ArrayList<AdData> adList;
    Handler actionHandler;
    HashMap<Integer, String> categoryHashMap;
    boolean shouldShowAdActivity;


    public AdListAdapter(ArrayList<AdData> adList, Handler actionHandler, HashMap<Integer, String> categoryHashMap, boolean shouldShowAdActivity){
        this.adList = adList;
        this.actionHandler = actionHandler;
        this.categoryHashMap = categoryHashMap;
        this.shouldShowAdActivity = shouldShowAdActivity;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_cardview, parent, false);

        AdViewHolder avh = new AdViewHolder(v);

        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        final AdData adData = adList.get(position);

        holder.titleTextView.setText(adData.title);
        holder.ownerTextView.setText("");
        holder.categoryTextView.setText(categoryHashMap.get(adData.category));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = HandlerMassages.SHOW_AD_DETAILS;
                msg.obj = adData;
                actionHandler.sendMessage(msg);
            }
        });

        if(shouldShowAdActivity){
            if (adData.hasBeenAccepted){
                holder.cardView.setBackgroundColor(Color.parseColor("#00AF72"));
            } else {
                holder.cardView.setBackgroundColor(Color.parseColor("#E67272"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

}
