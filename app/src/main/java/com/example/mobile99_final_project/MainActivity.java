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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mobile99_final_project.Enums.HandlerMassages;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobile99_final_project.NavPack.MainNavActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    //Button signUpButton;
    //EditText usernameText;
    //EditText passwordText;

    TextInputEditText usernameET;
    TextInputEditText passwordET;
    TextView signuptv1;
    TextView signuptv2;

    boolean debug = true;

    private static class ActionHandler extends Handler {
        private final WeakReference<MainActivity> mainActivityWeakReference;

        public ActionHandler(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final MainActivity mainAcitvity = mainActivityWeakReference.get();
            if (mainAcitvity != null) {
                switch (msg.what) {
                    case HandlerMassages.LOGIN_MESSAGE:
                        mainAcitvity.executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                mainAcitvity.getLoginToken();
                            }
                        });
                        break;

                    case HandlerMassages.SUCCESSFUL_LOGIN_MESSAGE:
                        mainAcitvity.goToFirstPageActivity((String) msg.obj);
                        break;

                    case HandlerMassages.TOKEN_CHECK_SUCCESS:
                        mainAcitvity.goToFirstPageActivity((String) msg.obj);
                        break;

                }
            }
        }
    }


    private ActionHandler actionHandler;

    private String[] loginCredentials;
    final String serverAddress = "";

    final String tokenFilename = "token_file.txt";

    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        loginButton = findViewById(R.id.login_button);
        //signUpButton = findViewById(R.id.signup_button);

        usernameET = findViewById(R.id.username_edittext);
        passwordET = findViewById(R.id.password_edittext);
        signuptv1 = findViewById(R.id.signupfirst);
        signuptv2 = findViewById(R.id.signupsecond);

        loginCredentials = new String[2];

        executorService = Executors.newSingleThreadExecutor();
        actionHandler = new ActionHandler(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginCredentials[0] = usernameET.getText().toString();
                loginCredentials[1] = passwordET.getText().toString();
                if (!loginCredentials[0].equals("") && !loginCredentials[1].equals("")) {
                    Message msg = new Message();
                    msg.what = HandlerMassages.LOGIN_MESSAGE;
                    actionHandler.sendMessage(msg);
                }
            }
        });


        signuptv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToSignUpActivity();
            }
        });
        signuptv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToSignUpActivity();
            }
        });

        if (debug) {
            loginCredentials[0] = "salehsagharchi";
            loginCredentials[1] = "1234";
            if (!loginCredentials[0].equals("") && !loginCredentials[1].equals("")) {
                Message msg = new Message();
                msg.what = HandlerMassages.LOGIN_MESSAGE;
                actionHandler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        usernameET.setText("");
        passwordET.setText("");
    }

    private void goToFirstPageActivity(String token) {
        Intent intent = new Intent(getBaseContext(), MainNavActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("username", loginCredentials[0]);
        DataHolders.getInstance().token = token;
        DataHolders.getInstance().username = loginCredentials[0];
        DataHolders.getInstance().currentPage = null;
        DataHolders.getInstance().categoryDataList = null;
        startActivity(intent);
    }

    private void gotToSignUpActivity() {
        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
        startActivity(intent);
    }

    private void getLoginToken() {
        String url = "http://142.93.151.73:8000/api-auth/";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    final String token = jsonObject.getString("token");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = HandlerMassages.SUCCESSFUL_LOGIN_MESSAGE;
                            msg.obj = token;
                            actionHandler.sendMessage(msg);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof com.android.volley.NetworkError) {
                    Toast.makeText(getApplicationContext(), "there was a network error, check your connection or try again later", Toast.LENGTH_LONG).show();

                } else if (error instanceof com.android.volley.ServerError) {
                    Toast.makeText(getApplicationContext(), "make sure username and password are correct", Toast.LENGTH_LONG).show();
                }
                error.printStackTrace();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject params = new JSONObject();
                try {
                    params.put("username", loginCredentials[0]);
                    params.put("password", loginCredentials[1]);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
