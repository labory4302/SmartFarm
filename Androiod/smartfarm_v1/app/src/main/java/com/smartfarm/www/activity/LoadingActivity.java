package com.smartfarm.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.smartfarm.www.R;

public class LoadingActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);
        startLoading();
    }

    private void autoLogin(){
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }

}
