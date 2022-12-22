package com.antonenko.mine_safety;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), authorization.class);
                startActivity(intent);
                finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(r, 1000);
    }
}