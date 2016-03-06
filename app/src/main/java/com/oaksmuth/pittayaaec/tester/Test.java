package com.oaksmuth.pittayaaec.tester;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.oaksmuth.pittayaaec.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Oak on 14/2/2559.
 * A tester
 */
public class Test extends AppCompatActivity{
    private static final String DB_NAME  = "alldata_db.db";
    private static String DB_PATH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        DB_PATH = cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
        if(!new File(DB_PATH+DB_NAME).isFile()) {
            Log.i("Database", "No db is copied, performing copy db");
            copyDataBase(this);
        }else
        {
            Log.i("Database", "db is already there");
        }
    }

    private void copyDataBase(Context context)
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
    }
}
