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

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mobile99_final_project.Enums.HandlerMassages;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button signUpButton;
    EditText usernameText;
    EditText passwordText;

    private static class ActionHandler extends Handler {
        private final WeakReference<MainActivity> mainActivityWeakReference;
        public ActionHandler(MainActivity mainActivity){
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            final MainActivity mainAcitvity = mainActivityWeakReference.get();
            if (mainAcitvity != null){
                switch (msg.what){
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

        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.signup_button);

        usernameText = findViewById(R.id.username_edittext);
        passwordText = findViewById(R.id.password_edittext);

        loginCredentials = new String[2];

        executorService = Executors.newSingleThreadExecutor();
        actionHandler = new ActionHandler(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginCredentials[0] = usernameText.getText().toString();
                loginCredentials[1] = passwordText.getText().toString();
                Message msg = new Message();
                msg.what = HandlerMassages.LOGIN_MESSAGE;
                actionHandler.sendMessage(msg);
            }
        });
        
        
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToSignUpActivity();
            }
        });
    }

    private void goToFirstPageActivity(String token){

    }

    private void gotToSignUpActivity(){

    }

    private void getLoginToken(){

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
