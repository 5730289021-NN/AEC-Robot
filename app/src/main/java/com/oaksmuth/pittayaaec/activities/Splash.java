package com.oaksmuth.pittayaaec.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.oaksmuth.pittayaaec.R;
import com.oaksmuth.pittayaaec.data.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

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
        setContentView(R.layout.activity_test);

        /*ContextWrapper cw = new ContextWrapper(getApplicationContext());
        DB_PATH = cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
        if(!new File(DB_PATH+DB_NAME).isFile()) {
            Log.i("Database", "No db is copied, performing copy db");
            Toast.makeText(this,"No db, Copying db",Toast.LENGTH_LONG).show();
            copyDataBase(this);
        }else {
            Log.i("Database", "db is already there");
            Toast.makeText(this,"DB is Instantiated",Toast.LENGTH_LONG).show();
        }*/
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
    /*private void copyDataBase(Context context)
    {
        Log.i("Database", "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput;
        int length;
        // Open your local db as the input stream
        InputStream myInput;
        try
        {
            myInput = context.getAssets().open(DB_NAME);
            Log.i("Database", "Found file");
            // transfer bytes from the inputfile to the
            // outputfile
            File f = new File(DB_PATH + DB_NAME);
            if(!f.exists())
            {
                f.mkdirs();
                if(!f.createNewFile())
                {
                    f.delete();
                    f.createNewFile();
                }
            }
            myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "New database has been copied to device!");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }*/
}
