package com.example.mobile99_final_project.NavPack.ui.my_advertisement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobile99_final_project.Adapters.AdListAdapter;
import com.example.mobile99_final_project.DataHolders;
import com.example.mobile99_final_project.DataModels.AdData;
import com.example.mobile99_final_project.DataModels.AdListGenerator;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryList;
import com.example.mobile99_final_project.Enums.HandlerMassages;
import com.example.mobile99_final_project.R;


import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserAdvertisementsFragment extends Fragment {

    public static class ActionHandler extends Handler {
        private final WeakReference<UserAdvertisementsFragment> userAdvertisementsActivityWeakReference;

        public ActionHandler(UserAdvertisementsFragment userAdvertisementsActivity){
            this.userAdvertisementsActivityWeakReference = new WeakReference<>(userAdvertisementsActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final UserAdvertisementsFragment userAdvertisementsActivity = userAdvertisementsActivityWeakReference.get();

            if (userAdvertisementsActivity != null){
                switch (msg.what){
                    case HandlerMassages.SHOW_AD_DETAILS:
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
    UserAdvertisementsFragment.ActionHandler actionHandler;
    SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_advertisements, container, false);

        actionHandler = new UserAdvertisementsFragment.ActionHandler(this);

        token = DataHolders.getInstance().token;
        username = DataHolders.getInstance().username;
        categoryDataList = DataHolders.getInstance().categoryDataList;
        CategoryList cl = new CategoryList(categoryDataList);
        categoryHashMap = cl.getCategoryHashMap();

        userAdsRecyclerView = view.findViewById(R.id.user_ads_recyclerView);
        userAdsProgressBar = view.findViewById(R.id.user_ads_progressBar);
        refreshLayout = view.findViewById(R.id.user_swipe);

        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        userAdsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        userAdsRecyclerView.setVisibility(View.INVISIBLE);

        executorService = Executors.newFixedThreadPool(1);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                getUserAdvertisements();
            }
        });

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    getUserAdvertisements();
                }
            });
        });

        return view;
    }


    private void getUserAdvertisements(){
        String url = "http://142.93.151.73:8000/api/user-ads/?username="+username;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                try {
                    final AdListGenerator adListGenerator = new AdListGenerator(response);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showUserAds(adListGenerator.getList());
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    refreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void showUserAds(ArrayList<AdData> adDataArrayList){
        userAdsProgressBar.setVisibility(View.INVISIBLE);
        userAdsRecyclerView.setAdapter(new AdListAdapter(adDataArrayList, actionHandler, categoryHashMap, true));
        userAdsRecyclerView.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(false);
    }
}