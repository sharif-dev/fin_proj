package com.example.mobile99_final_project.DataModels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdListGenerator {

    private ArrayList<AdData> list;

    public AdListGenerator(JSONObject jsonObject) throws JSONException {
        JSONArray results = jsonObject.getJSONArray("results");
        list = new ArrayList<>();

        for(int i = 0; i < results.length(); i++){
            JSONObject Ad = results.getJSONObject(i);
            list.add(new AdData(Ad.getString("title"), Ad.getString("description"),
                    Ad.getString("owner"), Ad.getInt("category"),
                    Ad.getString("image"), Ad.getBoolean("active")));

        }
    }

    public AdListGenerator(JSONArray results) throws JSONException {
        list = new ArrayList<>();

        for(int i = 0; i < results.length(); i++){
            JSONObject Ad = results.getJSONObject(i);
            list.add(new AdData(Ad.getString("title"), Ad.getString("description"),
                    Ad.getString("owner"), Ad.getInt("category"),
                    Ad.getString("image"), Ad.getBoolean("active")));

        }
    }

    public AdListGenerator(JSONObject jsonObject, boolean shouldExcludeUnAccepted) throws JSONException {
        JSONArray results = jsonObject.getJSONArray("results");
        list = new ArrayList<>();

        if (shouldExcludeUnAccepted){
            for(int i = 0; i < results.length(); i++){
                JSONObject Ad = results.getJSONObject(i);
                if (Ad.getBoolean("active")) {
                    list.add(new AdData(Ad.getString("title"), Ad.getString("description"),
                            Ad.getString("owner"), Ad.getInt("category"),
                            Ad.getString("image"), Ad.getBoolean("active")));
                }
            }
        } else {
            for(int i = 0; i < results.length(); i++){
                JSONObject Ad = results.getJSONObject(i);
                list.add(new AdData(Ad.getString("title"), Ad.getString("description"),
                        Ad.getString("owner"), Ad.getInt("category"),
                        Ad.getString("image"), Ad.getBoolean("active")));

            }
        }
    }

    public ArrayList<AdData> getList(){
        return list;
    }
}
