package com.example.mobile99_final_project.DataModels;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryData implements Serializable {
    public final int id;
    public final String name;

    public ArrayList<CategoryData> subCategories;

    public CategoryData(int id, String name){
        this.id = id;
        this.name = name;
        subCategories = new ArrayList<>();
    }
}