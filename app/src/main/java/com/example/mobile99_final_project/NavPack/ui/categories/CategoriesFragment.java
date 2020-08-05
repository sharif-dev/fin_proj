package com.example.mobile99_final_project.NavPack.ui.categories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobile99_final_project.Adapters.CategoryListAdapter;
import com.example.mobile99_final_project.CategoriesActivity;
import com.example.mobile99_final_project.DataHolders;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryList;
import com.example.mobile99_final_project.Enums.HandlerMassages;
import com.example.mobile99_final_project.FirstPageActivity;
import com.example.mobile99_final_project.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesFragment extends Fragment {

    public static class ActionHandler extends Handler {
        private final WeakReference<CategoriesFragment> categoriesWeakReference;

        public ActionHandler(CategoriesFragment categoriesActivity) {
            categoriesWeakReference = new WeakReference<>(categoriesActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final CategoriesFragment categoriesActivity = categoriesWeakReference.get();
            if (categoriesActivity != null) {
                switch (msg.what) {
                    case HandlerMassages.SHOW_FETCHED_CATS:
                        CategoryList categoryList = (CategoryList) msg.obj;
                        categoriesActivity.progressBar.setVisibility(View.INVISIBLE);
                        categoriesActivity.recyclerView.setAdapter(new CategoryListAdapter(categoryList.getData(), this));

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
    CategoriesFragment.ActionHandler actionHandler;

    ProgressBar progressBar;
    ArrayList<CategoryData> list;
    int top_cat_id;
    String cat_name;

    Button filterButton;
    TextView catTitle;

    String token;
    String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.category_list_recyclerView);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        progressBar = view.findViewById(R.id.categories_progressBar);
        filterButton = view.findViewById(R.id.filter_button);
        catTitle = view.findViewById(R.id.cat_title);

        executorService = Executors.newFixedThreadPool(2);

        actionHandler = new CategoriesFragment.ActionHandler(this);

        token = DataHolders.getInstance().token;
        username = DataHolders.getInstance().username;


        int count = 0;//getIntent().getIntExtra("count", 0);
        list = DataHolders.getInstance().categoryDataList;
        top_cat_id = -1;//getIntent().getIntExtra("top_cat_id", -1);
        cat_name = null;//getIntent().getStringExtra("cat_name");


        if (getArguments() != null) {
            count = getArguments().getInt("count");
            list = (ArrayList<CategoryData>) getArguments().getSerializable("categoryDataList");
            top_cat_id = getArguments().getInt("top_cat_id");
            cat_name = getArguments().getString("cat_name");
        }


        System.out.println("topcatid: " + top_cat_id);

        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(new CategoryListAdapter(list, actionHandler));

        System.out.println("hahahasdasdafsdf");

        filterButton.setVisibility(View.INVISIBLE);

        catTitle.setText("Inside Of : All Categories");

        if (top_cat_id > 0) {
            filterButton.setVisibility(View.VISIBLE);
            //setTitle(cat_name);
            catTitle.setText("Inside Of : " + cat_name);
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToFirstPageActivityShowByCategory(top_cat_id);
                }
            });
        }


        return view;
    }


    public void showSubCats(ArrayList<CategoryData> list, int top_cat_id, String cat_name) {
        Bundle bundle = new Bundle();
        bundle.putInt("count", list.size());
        bundle.putSerializable("categoryDataList", list);
        bundle.putInt("top_cat_id", top_cat_id);
        bundle.putString("cat_name", cat_name);
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_categories_self, bundle);
//        Intent intent = new Intent(getContext(), CategoriesActivity.class);
//        intent.putExtra("token", token);
//        intent.putExtra("username", username);
//        intent.putExtra("count", list.size());
//        intent.putExtra("categoryDataList", list);
//        intent.putExtra("top_cat_id", top_cat_id);
//        intent.putExtra("cat_name", cat_name);
//        startActivity(intent);

    }

    private void goToFirstPageActivityShowByCategory(int top_cat_id) {
        DataHolders.getInstance().currentPage = "http://142.93.151.73:8000/api/search-by-category?category_id=" + top_cat_id;
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_categories_to_mobile_navigation);
    }
}