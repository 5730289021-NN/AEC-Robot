package com.oaksmuth.pittayaaec.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.oaksmuth.pittayaaec.R;
import com.oaksmuth.pittayaaec.data.DatabaseHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by Oak on 14/2/2559.
 * A class of SplashScreen
 * 1st Page of the Application
 */
public class Splash extends AppCompatActivity{
    private static final String DB_NAME  = "AEC.db";
    private static String DB_PATH = null;
    public static DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        helper = new DatabaseHelper(this);
        if(!helper.checkDataBase()) {
            try {
                helper.copyDataBase();
            } catch (IOException e) {
                Log.i("Database", "Cannot copy Database");
                Toast.makeText(this, "Cannot copy Database", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        try {
            helper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("Database", "Cannot open Database");
            Toast.makeText(this, "Cannot open Database", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, ModeSelect.class);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(intent);
        finish();
    }
}
