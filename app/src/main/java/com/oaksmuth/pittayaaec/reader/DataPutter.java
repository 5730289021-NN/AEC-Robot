package com.oaksmuth.pittayaaec.reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.oaksmuth.pittayaaec.database.Basic;
import com.oaksmuth.pittayaaec.database.Remark;
import com.oaksmuth.pittayaaec.tester.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import io.realm.Realm;

/**
 * Created by Oak on 14/2/2559.
 */
public class DataPutter extends AsyncTask<Context, Integer, String> {
    private static final String BASIC_DATA_NAME = "basicdata.dat";
    private static final String ADVANCED_DATA_NAME = "";
    public static int progress = 0;

    protected String doInBackground(Context... context) {
        int id = 0;
        Realm realm = Realm.getInstance(context[0]);
        Scanner in = null;
        try {
            in = new Scanner(context[0].getAssets().open(BASIC_DATA_NAME)).useDelimiter("\t");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("66",e.getMessage());
            return "failed";
        }
        while (in.hasNextLine()) {
            //basicData.add(new Data(in.next(),Integer.parseInt(in.next()),in.next(),in.next(),in.next(),in.next()));
            realm.beginTransaction();
            Basic basic = realm.createObject(Basic.class);
            basic.setId(++id);
            basic.setTopic(in.next().trim());
            basic.setNo(Integer.parseInt(in.next()));
            basic.setQuestion(fixedString(in.next()));
            basic.setAnswer(fixedString(in.next()));
            basic.setSubQuestion(fixedString(in.next()));
            basic.setSubAnswer(fixedString(in.next()));
            boolean isEnglish = true;
            while (true) {
                String word = in.next();
                Remark remark = realm.createObject(Remark.class);
                if (!word.isEmpty()) {
                    if (isEnglish) {
                        remark.setEnglish(word);
                        //basic.getRemarks().addEnglish(word);
                    } else {
                        remark.setThai(word);
                        basic.getRemarks().add(remark);
                        //basicData.get(basicData.size() - 1).addThai(word);
                    }
                    isEnglish = !isEnglish;
                } else {
                    in.skip("[ \t]*");
                    break;
                }
            }
            realm.commitTransaction();
            publishProgress(id);
        }
        return "Success";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progress = values[0];
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public String fixedString(String string)
    {
        if(string.startsWith("\"") && string.endsWith("\""))
            return string.substring(1, string.length()-1);
        else
            return string;
    }

}
