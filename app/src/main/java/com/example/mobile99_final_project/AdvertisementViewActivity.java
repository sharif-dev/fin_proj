package com.example.mobile99_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mobile99_final_project.DataModels.AdData;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryList;

import java.util.ArrayList;
import java.util.HashMap;

public class AdvertisementViewActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView ownerTextView;
    TextView catTextView;
    TextView descriptionTextView;

    ArrayList<CategoryData> categoryDataList;
    HashMap<Integer, String> categoryHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_view);

        titleTextView = findViewById(R.id.ad_details_title_textView);
        ownerTextView = findViewById(R.id.ad_details_owner_textView);
        catTextView = findViewById(R.id.ad_details_cat_textView);
        descriptionTextView = findViewById(R.id.ad_details_description);

        AdData adData = (AdData) getIntent().getSerializableExtra("AdData");
        categoryDataList = (ArrayList<CategoryData>) getIntent().getSerializableExtra("categoryDataList");
        CategoryList cl = new CategoryList(categoryDataList);
        categoryHashMap = cl.getCategoryHashMap();

        if (adData != null){
            titleTextView.setText(adData.title);
            ownerTextView.setText(adData.owner);
            System.out.println(adData.category);
            catTextView.setText(categoryHashMap.get(adData.category));
            descriptionTextView.setText(adData.description);
        }

        setTitle("Advertisement Details");
    }
}
