package com.example.mobile99_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
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
    }

    private void goToFirstPageActivity(String token){

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
