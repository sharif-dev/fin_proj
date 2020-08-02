package com.example.mobile99_final_project.DataModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoryList implements Serializable {


    public ArrayList<CategoryData> list;
    public HashMap<Integer, String> hashMap;
    public int[] IDArray;
    public String[] NameArray;

    public CategoryList(){
        hashMap = new HashMap<>();
    }

    public CategoryList(ArrayList<CategoryData> finalList){
        hashMap = new HashMap<>();
        setFinalList(finalList);
        computeIdNameArrays(hashMap);
    }

    public void setFinalList(ArrayList<CategoryData> finalList) {
        this.list = finalList;

        for (int i = 0; i < list.size(); i++){
            computeHashMap(list.get(i));
        }

    }

    private void computeHashMap(CategoryData cat){
        hashMap.put(cat.id, cat.name);
        for (int i = 0; i < cat.subCategories.size(); i++){
            computeHashMap(cat.subCategories.get(i));
        }
    }

    private void computeIdNameArrays(HashMap<Integer, String> hashMap){
        IDArray = new int[hashMap.size()];
        NameArray = new String[hashMap.size()];
        int i = 0;
        for(Integer key: hashMap.keySet()){
            IDArray[i] = key;
            NameArray[i] = hashMap.get(key);
            i += 1;
        }
    }

    public ArrayList<CategoryData> getData(){return list;}

    public HashMap<Integer, String> getCategoryHashMap(){return hashMap;}

    public int[] getCatIDArray(){
        return IDArray;
    }

    public String[] getCatNameArray(){
        return NameArray;
    }
}
