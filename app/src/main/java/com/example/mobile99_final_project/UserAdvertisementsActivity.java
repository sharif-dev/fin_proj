package com.example.mobile99_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mobile99_final_project.Adapters.AdListAdapter;
import com.example.mobile99_final_project.DataModels.AdData;
import com.example.mobile99_final_project.DataModels.AdListGenerator;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryList;
import com.example.mobile99_final_project.Enums.HandlerMessages;



public class UserAdvertisementsActivity extends AppCompatActivity {

    public static class ActionHandler extends Handler {
        private final WeakReference<UserAdvertisementsActivity> userAdvertisementsActivityWeakReference;

        public ActionHandler(UserAdvertisementsActivity userAdvertisementsActivity){
            this.userAdvertisementsActivityWeakReference = new WeakReference<>(userAdvertisementsActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final UserAdvertisementsActivity userAdvertisementsActivity = userAdvertisementsActivityWeakReference.get();

            if (userAdvertisementsActivity != null){
                switch (msg.what){
                    case HandlerMessages.SHOW_AD_DETAILS:
                        System.out.println("clicked");
                        break;
                }
            }
        }
    }

    String token;
    String username;

    ArrayList<CategoryData> categoryDataList;
    HashMap<Integer, String> categoryHashMap;


    ExecutorService executorService;

    RecyclerView userAdsRecyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ProgressBar userAdsProgressBar;
    UserAdvertisementsActivity.ActionHandler actionHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_advertisements);

        actionHandler = new ActionHandler(this);

        token = getIntent().getStringExtra("token");
        username = getIntent().getStringExtra("username");
        categoryDataList = (ArrayList<CategoryData>) getIntent().getSerializableExtra("categoryDataList");
        CategoryList cl = new CategoryList(categoryDataList);
        categoryHashMap = cl.getCategoryHashMap();

        userAdsRecyclerView = findViewById(R.id.user_ads_recyclerView);
        userAdsProgressBar = findViewById(R.id.user_ads_progressBar);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        userAdsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        userAdsRecyclerView.setVisibility(View.INVISIBLE);

        executorService = Executors.newFixedThreadPool(1);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                getUserAdvertisements();
            }
        });

    }

    private void getUserAdvertisements(){
        String url = "http://142.93.151.73:8000/api/user-ads/?username="+username;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                try {
                    final AdListGenerator adListGenerator = new AdListGenerator(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showUserAds(adListGenerator.getList());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void showUserAds(ArrayList<AdData> adDataArrayList){
        userAdsProgressBar.setVisibility(View.INVISIBLE);
        userAdsRecyclerView.setAdapter(new AdListAdapter(adDataArrayList, actionHandler, categoryHashMap, true));
        userAdsRecyclerView.setVisibility(View.VISIBLE);
    }

}
