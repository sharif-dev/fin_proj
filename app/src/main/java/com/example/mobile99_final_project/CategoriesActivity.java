package com.example.mobile99_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.mobile99_final_project.Adapters.CategoryListAdapter;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryList;
import com.example.mobile99_final_project.Enums.HandlerMassages;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesActivity extends AppCompatActivity {


    public static class ActionHandler extends Handler {
        private final WeakReference<CategoriesActivity> categoriesWeakReference;

        public ActionHandler(CategoriesActivity categoriesActivity){
            categoriesWeakReference = new WeakReference<>(categoriesActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final CategoriesActivity categoriesActivity = categoriesWeakReference.get();
            if (categoriesActivity != null){
                switch (msg.what){
                    case HandlerMassages.SHOW_FETCHED_CATS:
//                        CategoryList categoryList = (CategoryList) msg.obj;
//                        categoriesActivity.progressBar.setVisibility(View.INVISIBLE);
//                        categoriesActivity.recyclerView.setAdapter(new CategoryListAdapter(categoryList.getData(), this));

                        break;

                    case HandlerMassages.SHOW_SUB_CATS:
                        CategoryData categoryData = (CategoryData) msg.obj;
                        categoriesActivity.showSubCats(categoryData.subCategories, categoryData.id, categoryData.name);
                        break;

                    case HandlerMassages.FILTER_BY_CAT:
                        CategoryData categoryData1 = (CategoryData) msg.obj;
                        categoriesActivity.goToFirstPageActivityShowByCategory(categoryData1.id);
                        break;
                }
            }
        }
    }

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    ExecutorService executorService;
    ActionHandler actionHandler;

    ProgressBar progressBar;
    ArrayList<CategoryData> list;
    int top_cat_id;
    String cat_name;

    Button filterButton;

    String token;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
//
//        recyclerView = findViewById(R.id.category_list_recyclerView);
//        recyclerViewLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(recyclerViewLayoutManager);
//
//        progressBar = findViewById(R.id.categories_progressBar);
//        filterButton = findViewById(R.id.filter_button);
//
//        executorService = Executors.newFixedThreadPool(2);
//
//        actionHandler = new ActionHandler(this);
//
//        token = getIntent().getStringExtra("token");
//        username = getIntent().getStringExtra("username");
//
//        int count = getIntent().getIntExtra("count",0);
//        list = (ArrayList<CategoryData>) getIntent().getSerializableExtra("categoryDataList");
//        top_cat_id = getIntent().getIntExtra("top_cat_id", -1);
//        cat_name = getIntent().getStringExtra("cat_name");
//        System.out.println("topcatid: "+ top_cat_id);
//
//        progressBar.setVisibility(View.INVISIBLE);
//        recyclerView.setAdapter(new CategoryListAdapter(list, actionHandler));
//
//        System.out.println("hahahasdasdafsdf");
//
//        filterButton.setVisibility(View.INVISIBLE);
//
//        setTitle("All Categories");
//
//        if (top_cat_id > 0){
//            filterButton.setVisibility(View.VISIBLE);
//            setTitle(cat_name);
//            filterButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    goToFirstPageActivityShowByCategory(top_cat_id);
//                }
//            });
//        }


    }

    public void showSubCats(ArrayList<CategoryData> list, int top_cat_id, String cat_name){
        Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("count", list.size());
        intent.putExtra("categoryDataList", list);
        intent.putExtra("top_cat_id", top_cat_id);
        intent.putExtra("cat_name", cat_name);
        startActivity(intent);

    }

    private void goToFirstPageActivityShowByCategory(int top_cat_id){
        Intent intent = new Intent(getBaseContext(), FirstPageActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);

        String url = "http://142.93.151.73:8000/api/search-by-category?category_id=" + top_cat_id;
        intent.putExtra("current_page", url);

        startActivity(intent);

    }

}
