package com.example.admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

    }

    public void udatemsg(View view) {
        startActivity(new Intent(MainActivity.this, UpdateImage.class));
    }

    public void updateImg(View view) {
        startActivity(new Intent(MainActivity.this, Uploadimg.class));
    }

    public void delete(View view) {
        startActivity(new Intent(MainActivity.this, DeleteItem.class));
    }
}