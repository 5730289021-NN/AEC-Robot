package com.oaksmuth.pittayaaec.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Oak on 14/2/2559.
 * A class of SplashScreen
 * 1st Page of the Application
 */
public class Splash extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ModeSelect.class);
        startActivity(intent);
        finish();
    }
}

/*
package com.oaksmuth.pittayaaecrobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;

public class Splash extends AppCompatActivity {
    public static DataProvider data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            data = new DataProvider(this);
        } catch (IOException e) {
            Toast.makeText(this, "Text File Not Found", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Intent intent = new Intent(this, ModeSelectActivity.class);
        startActivity(intent);
        finish();
    }
}

 */