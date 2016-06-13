package com.oaksmuth.pittayaaec.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by noraw on 10/3/2559.
 * Last Edited 13 June 2016
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    //private static final String ENCODING = "UTF-8";
    String DB_PATH = null;
    String targetPath = null;

    private SQLiteDatabase db;

    private final Context context;

    private static String DB_NAME = "AEC2.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        //DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        targetPath = DB_PATH + DB_NAME;
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try{
            checkDB = SQLiteDatabase.openDatabase(targetPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e)
        {
            Log.i("Database", "No db is copied, performing copy db");
            Toast.makeText(context, "No db, Copying db", Toast.LENGTH_LONG).show();
        }
        if(checkDB != null){
            Log.i("Database", "DB is copied");
            Toast.makeText(context, "DB is copied", Toast.LENGTH_LONG).show();
            checkDB.close();
        }
        return checkDB != null;
    }

    public void copyDataBase() throws IOException{
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
        InputStream in = context.getAssets().open(DB_NAME);
        OutputStream out = new FileOutputStream(targetPath);
        byte[] buffer = new byte[1024];
        int length;


        while((length = in.read(buffer))>0){
            out.write(buffer, 0, length);
        }
        out.flush();
        out.close();
        in.close();
        Log.i("Database", "New database has been copied to device!");
        Toast.makeText(context, "New database has been copied to device!", Toast.LENGTH_LONG).show();
    }

    public void openDataBase() throws SQLException{
        db = SQLiteDatabase.openDatabase(targetPath, null, SQLiteDatabase.OPEN_READONLY);
        db.execSQL( "PRAGMA encoding = \"UTF-8\"" );
    }

    @Override
    public void close()
    {
        if(db != null)
            db.close();
        super.close();
    }

    public Cursor rawQuery(String sql,String[] args)
    {
        return db.rawQuery(sql,args);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
