package com.oaksmuth.pittayaaec.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oaksmuth.pittayaaec.R;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oak on 14/2/2559.
 * Before playing the Text, we need to select the topic of the application
 * 3rd Page of the Application
 */


public class TopicSelect extends AppCompatActivity {
    public class Topic extends SugarRecord {
        String Topic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        List<Topic> topics = Topic.findWithQuery(Topic.class, "SELECT DISTINCT Topic FROM alldata");
        ArrayList<String> stringTopics = new ArrayList<>();
        for(Topic topic : topics)
        {
            stringTopics.add(topic.Topic);
        }
        TextView textView = (TextView) findViewById(R.id.CatalogtextView);
        ListView catalogList = (ListView) findViewById(R.id.CataloglistView);
        textView.setText("Select Topic");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringTopics);
        catalogList.setAdapter(adapter);
    }

}

/*
*package com.oaksmuth.pittayaaecrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CatalogActivity extends AppCompatActivity {
    private ArrayList<String> basicList;
    private ArrayList<Advanced> advancedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        basicList = Splash.data.basicList;
        advancedList = Splash.data.advancedList;
        TextView textView = (TextView) findViewById(R.id.CatalogtextView);
        ListView catalogList = (ListView) findViewById(R.id.CataloglistView);
        Intent intent = getIntent();
        if(intent.getStringExtra("Level").equals("0"))//Basic
        {
            textView.setText("The Basic Level");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, basicList);
            catalogList.setAdapter(adapter);
        }
        else if(intent.getStringExtra("Level").equals("1"))//Advanced
        {
            textView.setText("The Advanced Level");
            TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getApplicationContext(), advancedList);
            catalogList.setAdapter(adapter);
        }

    }
}
 */
