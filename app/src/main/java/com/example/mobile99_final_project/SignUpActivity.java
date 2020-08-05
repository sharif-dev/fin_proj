package com.example.mobile99_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobile99_final_project.Enums.HandlerMassages;
import com.example.mobile99_final_project.NavPack.MainNavActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {

    String Token;
    Button createAccountButton;
    TextInputEditText usernameText;
    TextInputEditText passwordText;
    TextInputEditText c_passwordText;
    TextInputEditText phoneText;

    Handler actionHandler;

    String[] signUpCredentials = new String[4];

    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        Token = getIntent().getStringExtra("token");

        executorService = Executors.newSingleThreadExecutor();

        createAccountButton = findViewById(R.id.signup_button);
        usernameText = findViewById(R.id.username_reg_edittext);
        passwordText = findViewById(R.id.password_reg_edittext);
        c_passwordText = findViewById(R.id.cpassword_reg_edittext);
        phoneText = findViewById(R.id.phone_reg_edittext);

        actionHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case HandlerMassages.SIGNUP_START:
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                createUser();
                            }
                        });
                        break;

                    case HandlerMassages.SIGNUP_SUCCESS:
                        String token = (String) msg.obj;
                        goToFirstPageActivity(token);
                        break;

                    case HandlerMassages.SIGNUP_FAIL:

                        break;
                }
            }
        };

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputsValidity()){

                    signUpCredentials[0] = usernameText.getText().toString();
                    signUpCredentials[1] = passwordText.getText().toString();
                    signUpCredentials[2] = phoneText.getText().toString();
                    Message msg = new Message();
                    msg.what = HandlerMassages.SIGNUP_START;
                    actionHandler.sendMessage(msg);

                }
            }
        });


    }

    private boolean checkInputsValidity(){
        String username = usernameText.getText().toString();
        String pass = passwordText.getText().toString();
        String c_pass = c_passwordText.getText().toString();
        String phone = phoneText.getText().toString();

        if (username.contains(" ")){
            Toast.makeText(this,"username should not contain white-space", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.contains(" ")){
            Toast.makeText(this,"password should not contain white-space", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pass.equals(c_pass)){
            Toast.makeText(this,"make sure confirm password and password are the same", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() != 11 || !phone.substring(0,2).equals("09")){
            Toast.makeText(this,"make sure the phone number is entered according to the example template", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

    private void createUser(){

        String url = "http://142.93.151.73:8000/api/users/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Message msg = new Message();
                    msg.what = HandlerMassages.SIGNUP_SUCCESS;
                    msg.obj = jsonObject.getString("token");
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
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject params = new JSONObject();
                JSONObject profile = new JSONObject();

                try {
//                    System.out.println("hghghg");
                    System.out.println(signUpCredentials[1]);
                    params.put("username", signUpCredentials[0]);
                    params.put("password", signUpCredentials[1]);

                    profile.put("phone_number", signUpCredentials[2]);

                    params.put("user_profile", profile);
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


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void goToFirstPageActivity(String token){
        Intent intent = new Intent(getBaseContext(), MainNavActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", signUpCredentials[0]);
        DataHolders.getInstance().token = token;
        DataHolders.getInstance().username = signUpCredentials[0];
        DataHolders.getInstance().currentPage = null;
        DataHolders.getInstance().categoryDataList = null;
        startActivity(intent);
        finish();
    }
}
