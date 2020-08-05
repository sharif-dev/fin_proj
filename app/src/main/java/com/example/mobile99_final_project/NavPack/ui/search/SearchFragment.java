package com.example.mobile99_final_project.NavPack.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile99_final_project.DataHolders;
import com.example.mobile99_final_project.R;

public class SearchFragment extends Fragment {


    String token;
    String username;

    Button searchButton;
    EditText searchQuery;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        token = DataHolders.getInstance().token;
        username = DataHolders.getInstance().username;

        searchButton = view.findViewById(R.id.search_Button);
        searchQuery = view.findViewById(R.id.search_editText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchQuery.getText().toString().equals(""))
                    goToFirstPageActivityWithQuery(searchQuery.getText().toString());
            }
        });

        return view;
    }


    private void goToFirstPageActivityWithQuery(String Query) {
        String page = "http://142.93.151.73:8000/api/search/?query=" + Query;
        DataHolders.getInstance().currentPage = page;
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_search_to_mobile_navigation);
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}