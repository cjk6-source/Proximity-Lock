package com.cjk6.proximitylock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Button on_button;
    Button off_button;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        on_button = findViewById(R.id.on_button);
        off_button = findViewById(R.id.off_button);
        title = findViewById(R.id.title);
    }


    public void startService(View view) {
        Intent serviceIntent = new Intent(this, ScreenService.class);
        startService(serviceIntent);
    }

    public void stopService(View view) {
        Intent serviceIntent = new Intent(this, ScreenService.class);
        stopService(serviceIntent);
    }

}
