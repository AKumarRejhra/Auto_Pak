package com.project.autopak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Button btnshopkeeper = findViewById(R.id.shopkeeper);
        btnshopkeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,LoginActivity.class);
                i.putExtra("choose","Shopkeeper");
                startActivity(i);
            }
        });
        Button btnwholeseller = findViewById(R.id.wholeseller);
        btnwholeseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,LoginActivity.class);
                i.putExtra("choose","Wholeseller");
                startActivity(i);
            }
        });
        Button btnadmin = findViewById(R.id.admin);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,LoginActivity.class);
                i.putExtra("choose","Admin");
                startActivity(i);
            }
        });

    }
}