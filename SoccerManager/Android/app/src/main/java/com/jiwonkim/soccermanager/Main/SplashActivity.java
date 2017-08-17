package com.jiwonkim.soccermanager.Main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.jiwonkim.soccermanager.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 3000);// 3 초
    }
}
