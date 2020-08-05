package com.example.mobile99_final_project.NavPack.ui.home;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobile99_final_project.Adapters.AdListAdapter;
import com.example.mobile99_final_project.AdvertisementViewActivity;
import com.example.mobile99_final_project.DataHolders;
import com.example.mobile99_final_project.DataModels.AdData;
import com.example.mobile99_final_project.DataModels.AdListGenerator;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryListGenerator;
import com.example.mobile99_final_project.Enums.HandlerMassages;
import com.example.mobile99_final_project.FirstPageActivity;
import com.example.mobile99_final_project.MenuActivity;
import com.example.mobile99_final_project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    LocationManager locationManager;
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 0;
    public boolean SHOULD_FETCH_LOCATION = false;
    public boolean SHOULD_FETCH_ADS = true;
    public boolean SHOULD_FETCH_CATS = true;

    public String currentPageURL = "http://142.93.151.73:8000/api/advertisements/";
    public String nextPageURL = "http://142.93.151.73:8000/api/advertisements/";
    public String previousPageURL = "http://142.93.151.73:8000/api/advertisements/";


    public static class ActionHandler extends Handler {
        private final WeakReference<HomeFragment> firstPageActivityWeakReference;

        public ActionHandler(HomeFragment firstPageActivity) {
            this.firstPageActivityWeakReference = new WeakReference<>(firstPageActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final HomeFragment firstPageActivity = firstPageActivityWeakReference.get();

            if (firstPageActivity != null) {
                switch (msg.what) {
                    case HandlerMassages.GET_COORDS:
                        firstPageActivity.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                firstPageActivity.requestCurrentLocation();
                            }
                        });

                        break;
//                    case HandlerMassages.GET_CITY:
//                        final String coords = (String) msg.obj;
//                        firstPageActivity.executorService.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                firstPageActivity.getCityByCoords(coords);
//                            }
//                        });
//                        break;
//                    case HandlerMassages.SET_CITY:
//                        final String cityName = (String) msg.obj;
//                        firstPageActivity.cityTextView.setText(cityName);
//                        firstPageActivity.cityNameProgressBar.setVisibility(View.INVISIBLE);
//                        break;
//                    case HandlerMassages.FETCH_ADS:
//                        firstPageActivity.executorService.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                firstPageActivity.getAllAdvertisements();
//                            }
//                        });
//                        break;
                    case HandlerMassages.SHOW_ADS:
                        firstPageActivity.adListProgressBar.setVisibility(View.INVISIBLE);
                        firstPageActivity.refreshLayout.setRefreshing(false);
                        ArrayList<AdData> adData = (ArrayList<AdData>) msg.obj;
                        firstPageActivity.recyclerView.setAdapter(new AdListAdapter(adData, this, firstPageActivity.categoryHashMap, false));
                        firstPageActivity.recyclerView.setVisibility(View.VISIBLE);
                        break;
                    case HandlerMassages.SHOW_AD_DETAILS:
                        firstPageActivity.showAdDetails((AdData) msg.obj);
                        break;
                    case HandlerMassages.GET_CATS:
                        firstPageActivity.executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                firstPageActivity.getCategories();
                            }
                        });
                        break;
                    case HandlerMassages.CATS_FETCHED:
                        firstPageActivity.executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                firstPageActivity.getAllAdvertisements();
                                firstPageActivity.bindOnClickForReloadButton();
                            }
                        });
                        break;
                }
            }
        }
    }

    HashMap<Integer, String> categoryHashMap;

    HomeFragment.ActionHandler actionHandler;
    ExecutorService executorService;

    //TextView cityTextView;
    //Button menuButton;
    //ProgressBar cityNameProgressBar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ProgressBar adListProgressBar;

    Button nextButton;
    Button previousButton;
    Button clearFilter;
    //Button reloadButton;

    String token;
    String username;

    SwipeRefreshLayout refreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        token = DataHolders.getInstance().token;
        username = DataHolders.getInstance().username;
        clearFilter = view.findViewById(R.id.reset_filters);

        if ((DataHolders.getInstance().currentPage != null) && !(getArguments() != null && getArguments().getString("reset") != null)) {
            clearFilter.setVisibility(View.VISIBLE);
            currentPageURL = DataHolders.getInstance().currentPage;
            previousPageURL = String.copyValueOf(currentPageURL.toCharArray());
            nextPageURL = String.copyValueOf(currentPageURL.toCharArray());
        } else {
            clearFilter.setVisibility(View.GONE);
            DataHolders.getInstance().currentPage = null;
        }


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        refreshLayout = view.findViewById(R.id.swipe_container);

        recyclerView = view.findViewById(R.id.ad_list_recyclerView);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        adListProgressBar = view.findViewById(R.id.ad_list_progressBar);

        recyclerView.setVisibility(View.INVISIBLE);

        //cityTextView = findViewById(R.id.city_textview);
        //menuButton = view.findViewById(R.id.ads_next_button);
        //cityNameProgressBar = findViewById(R.id.city_name_progressBar);

        nextButton = view.findViewById(R.id.ads_next_button);
        previousButton = view.findViewById(R.id.ads_previous_button);

        //reloadButton = view.findViewById(R.id.reload_button);

        executorService = Executors.newFixedThreadPool(10);

        actionHandler = new HomeFragment.ActionHandler(this);

        if (SHOULD_FETCH_LOCATION) {
            Message msg = actionHandler.obtainMessage(HandlerMassages.GET_COORDS);
//            msg.what = HandlerMassages.GET_COORDS;
            msg.sendToTarget();
        }

        if (SHOULD_FETCH_CATS) {
            Message msg = new Message();
            msg.what = HandlerMassages.GET_CATS;
            actionHandler.sendMessage(msg);
        }


//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToMenuActivity();
//            }
//        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(nextPageURL);

                if (!nextPageURL.equals(currentPageURL)) {
                    previousPageURL = currentPageURL;
                    currentPageURL = nextPageURL;

                    recyclerView.setVisibility(View.INVISIBLE);
                    adListProgressBar.setVisibility(View.VISIBLE);

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            getAllAdvertisements();
                        }
                    });
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!previousPageURL.equals(currentPageURL)) {
                    nextPageURL = currentPageURL;
                    currentPageURL = previousPageURL;

                    recyclerView.setVisibility(View.INVISIBLE);
                    adListProgressBar.setVisibility(View.VISIBLE);

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            getAllAdvertisements();
                        }
                    });
                }
            }
        });

        clearFilter.setOnClickListener((view1) -> {
            Bundle bundle = new Bundle();
            bundle.putString("reset", "true");
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_self, bundle);
        });

        Log.i("tagtagtag", "oncreate");

        return view;
    }


    private void goToMenuActivity() {
        Intent intent = new Intent(getContext(), MenuActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.putExtra("categoryDataList", DataHolders.getInstance().categoryDataList);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_ACCESS_COARSE_LOCATION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                requestCurrentLocation();
//            } else {
//                SHOULD_FETCH_LOCATION = false;
//            }
//        }
    }

    private void requestCurrentLocation() {

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
//        } else {
//
//            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (lastKnownLocation == null) {
//
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        System.out.println("locchang");
//                        Message msg = new Message();
//                        msg.what = HandlerMassages.GET_CITY;
//                        msg.obj = "lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
//                        actionHandler.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                    }
//
//                    @Override
//                    public void onProviderEnabled(String provider) {
//
//                    }
//
//                    @Override
//                    public void onProviderDisabled(String provider) {
//
//                    }
//                });
//            } else {
//                Message msg = new Message();
//                msg.what = HandlerMassages.GET_CITY;
//                msg.obj = "lat=" + lastKnownLocation.getLatitude() + "&lon=" + lastKnownLocation.getLongitude();
//                actionHandler.sendMessage(msg);
//            }
//        }


    }

    private void getCityByCoords(String coords) {
//        String url = "https://eu1.locationiq.com/v1/reverse.php?key=fc584252e587ed&" + coords + "&format=json&accept-language=native";
//        System.out.println(url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    System.out.println(response);
//                    JSONObject jsonObject = new JSONObject(response);
//                    String cityName = jsonObject.getJSONObject("address").getString("state");
//                    Message msg = new Message();
//                    msg.what = HandlerMassages.SET_CITY;
//                    msg.obj = cityName;
//                    actionHandler.sendMessage(msg);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }

    private void getAllAdvertisements() {
        String url = currentPageURL;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray a = jsonObject.getJSONArray("results");
//                    JSONObject b = a.getJSONObject(0);

                    String next = jsonObject.getString("next");
                    String previous = jsonObject.getString("previous");

                    AdListGenerator adListGenerator = new AdListGenerator(jsonObject);

                    nextPageURL = (!next.equals("null")) ? next : currentPageURL;
                    previousPageURL = (!previous.equals("null")) ? previous : currentPageURL;

                    System.out.println(nextPageURL);
                    System.out.println(previousPageURL);


                    Message msg = new Message();
                    msg.what = HandlerMassages.SHOW_ADS;
                    msg.obj = adListGenerator.getList();

                    actionHandler.sendMessage(msg);


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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getCategories() {

        String url = "http://142.93.151.73:8000/api/categories/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    CategoryListGenerator categoryListGenerator = new CategoryListGenerator(jsonObject);

                    DataHolders.getInstance().categoryDataList = categoryListGenerator.getCategoryList().getData();
                    categoryHashMap = categoryListGenerator.getCategoryList().getCategoryHashMap();

                    Message msg = new Message();
                    msg.what = HandlerMassages.CATS_FETCHED;
                    actionHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void bindOnClickForReloadButton() {
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    getAllAdvertisements();
                }
            });
        });
//        reloadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recyclerView.setVisibility(View.INVISIBLE);
//                adListProgressBar.setVisibility(View.VISIBLE);
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        getAllAdvertisements();
//                    }
//                });
//            }
//        });
    }

    public void showAdDetails(AdData adData) {

        Intent intent = new Intent(getContext(), AdvertisementViewActivity.class);
        intent.putExtra("AdData", adData);
        intent.putExtra("categoryDataList", DataHolders.getInstance().categoryDataList);
        startActivity(intent);

    }
}