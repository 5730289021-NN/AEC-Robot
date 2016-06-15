package com.oaksmuth.pittayaaec.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.oaksmuth.pittayaaec.R;
import com.oaksmuth.pittayaaec.data.Advanced;
import com.oaksmuth.pittayaaec.data.TwoTextArrayAdapter;


import java.util.ArrayList;

/**
 * Created by Oak on 14/2/2559.
 * Before playing the Text, we need to select the topic of the application
 * 3rd Page of the Application
 */


public class TopicSelect extends AppCompatActivity {
    private static final String tb_name = "Data";
    private SearchView sv;
    private TwoTextArrayAdapter adapter;
    private ListView catalogList;
    private Context context;
    private ArrayList<TopicHeader> topics;


    public class TopicHeader {
        String Topic;
        String SubTopic;
        public TopicHeader(String Topic, String SubTopic){
            this.Topic = Topic;
            this.SubTopic = SubTopic;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        context = this;
        final Cursor cursor = Splash.helper.rawQuery("SELECT DISTINCT Topic, SubTopic FROM " + tb_name,null);

        topics = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()){ //make sure you got results, and move to first row
            do{
                topics.add(new TopicHeader(cursor.getString(0),cursor.getString(1)));
            } while (cursor.moveToNext()); //move to next row in the query result
        }

        final ArrayList<Advanced> advancedTopics = new ArrayList<>();
        for(int i = 0;i < topics.size(); i++)
        {
            if(advancedTopics.isEmpty() || !topics.get(i).Topic.equals(topics.get(i-1).Topic)) {
                advancedTopics.add(new Advanced(Advanced.HEADER, topics.get(i).Topic));
                advancedTopics.add(new Advanced(Advanced.TOPIC, topics.get(i).SubTopic));
            }
            else
            {
                advancedTopics.add(new Advanced(Advanced.TOPIC, topics.get(i).SubTopic));
            }
        }
        final Intent intent = new Intent(this, Player.class);
        catalogList = (ListView) findViewById(R.id.CataloglistView);
        adapter = new TwoTextArrayAdapter(getApplicationContext(), advancedTopics);
        catalogList.setAdapter(adapter);
        catalogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Advanced advanced = (Advanced) catalogList.getItemAtPosition(position);
                if(advanced.type == Advanced.TOPIC)//Make sure it's not header
                {
                    //find its header
                    String header = null;
                    Log.i("Database","i = " + position);
                    for(int i = position;i>=0;i--)
                    {
                        if(advancedTopics.get(i).type == Advanced.HEADER)
                        {
                            Log.i("Database", (advancedTopics.get(i).sentence + " is " + advancedTopics.get(i).type));
                            header = advancedTopics.get(i).sentence;
                            break;
                        }
                    }
                    intent.putExtra("Topic", header);
                    intent.putExtra("SubTopic", advanced.sentence);
                    //Toast.makeText(context,"Topic: " + header + " SubTopic: " + advanced.sentence,Toast.LENGTH_LONG).show();
                    Log.i("Database", "Topic: " + header + " SubTopic: " + advanced.sentence);
                    startActivity(intent);
                    finish();
                }
            }
        });
        sv = (SearchView) findViewById(R.id.searchView);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(context,query,Toast.LENGTH_SHORT).show();
                ArrayList<TopicHeader> th = new ArrayList<>();
                ArrayList<Advanced> advanced2 = new ArrayList<>();
                for(TopicHeader s:topics)//u
                {
                    if(s.SubTopic.toLowerCase().contains(query.toLowerCase()))
                    {
                        th.add(s);
                    }
                }
                for(int i = 0;i < th.size(); i++)
                {
                    if(advanced2.isEmpty() || !th.get(i).Topic.equals(th.get(i-1).Topic)) {
                        advanced2.add(new Advanced(Advanced.HEADER, th.get(i).Topic));
                        advanced2.add(new Advanced(Advanced.TOPIC, th.get(i).SubTopic));
                    }
                    else
                    {
                        advanced2.add(new Advanced(Advanced.TOPIC, th.get(i).SubTopic));
                    }
                }
                adapter = new TwoTextArrayAdapter(getApplicationContext(), advanced2);
                catalogList.setAdapter(adapter);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                {
                    adapter = new TwoTextArrayAdapter(getApplicationContext(), advancedTopics);
                    catalogList.setAdapter(adapter);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, ModeSelect.class);
        startActivity(intent);
        finish();
    }

}