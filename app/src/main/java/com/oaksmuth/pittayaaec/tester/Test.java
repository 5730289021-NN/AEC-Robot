package com.oaksmuth.pittayaaec.tester;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oaksmuth.pittayaaec.R;
import com.oaksmuth.pittayaaec.reader.DataPutter;

/**
 * Created by Oak on 14/2/2559.
 * A tester
 */
public class Test extends AppCompatActivity{
    public LinearLayout rootLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();
        DataPutter dp = new DataPutter();
        dp.execute(this);
        TextView tv = new TextView(this);
        tv.setText(DataPutter.progress);
        rootLayout.addView(tv);
    }
}
