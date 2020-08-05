package com.example.mobile99_final_project.NavPack.ui.create_advertisement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobile99_final_project.DataHolders;
import com.example.mobile99_final_project.DataModels.CategoryData;
import com.example.mobile99_final_project.DataModels.CategoryList;
import com.example.mobile99_final_project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AdvertisementCreationFragment extends Fragment {


    String token;
    String username;

    ArrayList<CategoryData> categoryDataList;
    HashMap<Integer, String> categoryHashMap;

    Spinner catSpinner;
    EditText descEditText;
    EditText titleEditText;

    Button createAdButton;

    int[] catIDArray;

    int selectedID;
    String title;
    String description;

    ExecutorService executorService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advertisement_creation, container, false);

        token = DataHolders.getInstance().token;
        username = DataHolders.getInstance().username;
        categoryDataList = DataHolders.getInstance().categoryDataList;
        CategoryList cl = new CategoryList(categoryDataList);
        categoryHashMap = cl.getCategoryHashMap();

        catSpinner = view.findViewById(R.id.ad_cat_cr_spinner);
        descEditText = view.findViewById(R.id.ad_desc_cr_editText);
        titleEditText = view.findViewById(R.id.ad_title_cr_editText);
        createAdButton = view.findViewById(R.id.ad_create_Button);

        executorService = Executors.newFixedThreadPool(1);

        catIDArray = cl.getCatIDArray();
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, cl.getCatNameArray());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(aa);

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedID = catIDArray[position];
                System.out.println(selectedID);
                System.out.println(token);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleEditText.getText().toString();
                description = descEditText.getText().toString();

                System.out.println(title);

                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Title textbox cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description textbox cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        createAd();
                    }
                });


            }
        });

        return view;
    }


    private void createAd() {

        String url = "http://142.93.151.73:8000/api/advertisements/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Advertisement created successfully", Toast.LENGTH_LONG).show();
                NavHostFragment.findNavController(AdvertisementCreationFragment.this).navigate(R.id.action_nav_create_advertisement_to_mobile_navigation);
                //finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "token " + token);
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject params = new JSONObject();
                try {
                    params.put("category", selectedID);
                    params.put("title", title);
                    params.put("description", description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}