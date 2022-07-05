package com.example.myschedule;

import static com.google.firebase.auth.FirebaseAuth.*;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myschedule.databinding.ActivityMain2Binding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {
    FloatingActionButton buttonf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        buttonf = (FloatingActionButton) findViewById(R.id.fab);
        Intent intent = new Intent(this, MainActivity3.class);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intenbht =  new Intent(this, MainActivity.class);
            startActivity(intenbht);
            finish();
        }
        buttonf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });
    }


}