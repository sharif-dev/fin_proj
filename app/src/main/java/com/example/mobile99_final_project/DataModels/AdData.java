package com.example.mobile99_final_project.DataModels;

import java.io.Serializable;

public class AdData implements Serializable {

    public String title;
    public String description;
    public String owner;
    public int category;
    public String imageURL;
    public boolean hasBeenAccepted;

    public AdData(String title, String description, String owner, int category, String imageURL, boolean hasBeenAccepted){
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.category = category;
        this.imageURL = imageURL;
        this.hasBeenAccepted = hasBeenAccepted;
    }
}
