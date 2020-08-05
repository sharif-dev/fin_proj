package com.example.mobile99_final_project;

public class DataHolders {
    private static final DataHolders ourInstance = new DataHolders();

    public static DataHolders getInstance() {
        return ourInstance;
    }

    private DataHolders() {
    }

    public String token = "";
    public String username = "";
    public String currentPage = null;
}
