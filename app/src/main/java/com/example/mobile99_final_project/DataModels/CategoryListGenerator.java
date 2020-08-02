package com.example.mobile99_final_project.DataModels;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryListGenerator {


    ArrayList<Integer> tier1_id;
    ArrayList<Integer> tier2_id;

    public CategoryList categoryList;

    public CategoryListGenerator(JSONObject objectList){


        tier1_id = new ArrayList<>();
        tier2_id = new ArrayList<>();
        categoryList = new CategoryList();


        try {

            categoryList.setFinalList(getCategoryList(objectList));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CategoryList getCategoryList(){return categoryList;}

    private void addSubCategories(CategoryData cat_data, JSONArray subCategories) throws JSONException {
        if (subCategories.length() == 0)
            return;

        for (int i = 0; i < subCategories.length(); i++){
            JSONObject cat = subCategories.getJSONObject(i);

            int id = cat.getInt("id");
            String name = cat.getString("name");

            tier2_id.add(id);

            CategoryData data = new CategoryData(id, name);

            cat_data.subCategories.add(data);

            JSONArray sub = cat.getJSONArray("subcategories");
            addSubCategories(data, sub);
        }
    }

    private ArrayList<CategoryData> getCategoryList(JSONObject objectList) throws JSONException{
        JSONArray resultArray = objectList.getJSONArray("results");

        ArrayList<CategoryData> tier1 = new ArrayList<>();
        ArrayList<CategoryData> finalList = new ArrayList<>();

        for (int i = 0; i < resultArray.length(); i++){

            JSONObject cat = resultArray.getJSONObject(i);
            int cat_id = cat.getInt("id");
            String cat_name = cat.getString("name");
            System.out.println(cat_name);

            if (!tier2_id.contains(cat_id)){
                JSONArray subCategories = cat.getJSONArray("subcategories");

                CategoryData catData = new CategoryData(cat_id, cat_name);
                addSubCategories(catData, subCategories);

                tier1.add(catData);
                tier1_id.add(cat_id);

            }
        }

        tier1_id.removeAll(tier2_id);

        for (int i = 0; i < tier1.size(); i++){
            CategoryData catData = tier1.get(i);
            if(tier1_id.contains(catData.id))
                finalList.add(catData);
        }

        return finalList;
    }
}
