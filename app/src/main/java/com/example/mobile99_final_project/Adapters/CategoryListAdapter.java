package com.example.mobile99_final_project.Adapters;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile99_final_project.CategoriesActivity;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.Enums.HandlerMassages;
import com.example.mobile99_final_project.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>{


    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        TextView categorySub;
        CardView cardView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_name_textView);
            categorySub = itemView.findViewById(R.id.category_subcat_textView);
            cardView = itemView.findViewById(R.id.category_cardView);
        }
    }

    final ArrayList<CategoryData> finalList;
    CategoriesActivity.ActionHandler actionHandler;

    public CategoryListAdapter(ArrayList<CategoryData> list, CategoriesActivity.ActionHandler actionHandler) {
        this.finalList = list;
        this.actionHandler = actionHandler;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cardview_simple, parent, false);

        CategoryViewHolder cvh = new CategoryViewHolder(v);

        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        final CategoryData catData = finalList.get(position);

        holder.categoryName.setText(catData.name);
        if(catData.subCategories.size() == 0){

            holder.categorySub.setText("");

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = HandlerMassages.FILTER_BY_CAT;
//                    CategoryList categoryList = new CategoryList();
//                    categoryList.setFinalList(finalList.get(position).subCategories);
                    msg.obj =finalList.get(position);
                    actionHandler.sendMessage(msg);
                }
            });


        } else if (catData.subCategories.size() == 1){

            holder.categorySub.setText("1 subclass");

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = HandlerMassages.SHOW_SUB_CATS;
                    msg.obj =finalList.get(position);
                    actionHandler.sendMessage(msg);
                }
            });

        } else {

            holder.categorySub.setText(catData.subCategories.size() + " subclasses");

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = HandlerMassages.SHOW_SUB_CATS;
                    msg.obj = finalList.get(position);
                    actionHandler.sendMessage(msg);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return finalList.size();
    }
}
