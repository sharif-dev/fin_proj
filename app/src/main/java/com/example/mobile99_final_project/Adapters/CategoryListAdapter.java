package com.example.mobile99_final_project.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile99_final_project.CategoriesActivity;
import com.example.mobile99_final_project.DataModels.CategoryData;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
