package com.example.myschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity4 extends AppCompatActivity {
Button button;
EditText ghkl,ghjk,ghj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main5);
button = findViewById(R.id.button4);
    ghkl = findViewById(R.id.event1);
    ghjk = findViewById(R.id.event2);

        Intent intent = getIntent();

        String Time = intent.getStringExtra("key");
        String NAme = intent.getStringExtra("key2");
        String Phone = intent.getStringExtra("key3");
        String C = intent.getStringExtra("key1");




    }

}