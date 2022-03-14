package com.example.starsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        invalidateOptionsMenu();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(3000);
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(intent);

                    MainActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }

}