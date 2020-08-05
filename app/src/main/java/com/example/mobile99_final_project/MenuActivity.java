package com.example.mobile99_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mobile99_final_project.DataModels.CategoryData;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    CardView myProfileBt;
    CardView categoriesBt;
    CardView logOutBt;
    TextView usernameTextView;

    CardView createAdBt;
    CardView myAdsBt;

    CardView searchBt;


    String token;
    String username;


    ArrayList<CategoryData> categoryDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        token = getIntent().getStringExtra("token");
        username = getIntent().getStringExtra("username");
        categoryDataList = (ArrayList<CategoryData>) getIntent().getSerializableExtra("categoryDataList");

        myProfileBt = findViewById(R.id.my_profile_bt);
        categoriesBt = findViewById(R.id.categories_bt);
        logOutBt = findViewById(R.id.log_out_bt);
        createAdBt = findViewById(R.id.create_ad_bt);
        myAdsBt = findViewById(R.id.my_ads_bt);
        searchBt = findViewById(R.id.search_bt);

        usernameTextView = findViewById(R.id.username_textView);

        usernameTextView.setText(username);

        categoriesBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCategories();
            }
        });

        myAdsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), UserAdvertisementsActivity.class);
                startActivity(intent);
            }
        });

        logOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        createAdBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdCreation();
            }
        });

        myAdsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyAds();
            }
        });

        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearchActivity();
            }
        });
    }

    private void goToCategories(){
        Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("categoryDataList", categoryDataList);
        intent.putExtra("top_cat_id", -1);
        Log.i("tagtagtag", token + "  ,,  " + username + "  ,,  " + categoryDataList.size());
        startActivity(intent);
    }

    private void goToAdCreation(){
        Intent intent = new Intent(getBaseContext(),AdvertisementCreationActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("categoryDataList", categoryDataList);
        startActivity(intent);
    }

    private void goToMyAds(){
        Intent intent = new Intent(getBaseContext(),UserAdvertisementsActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("categoryDataList", categoryDataList);
        startActivity(intent);
    }

    private void goToSearchActivity(){
        Intent intent = new Intent(getBaseContext(),SearchActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
