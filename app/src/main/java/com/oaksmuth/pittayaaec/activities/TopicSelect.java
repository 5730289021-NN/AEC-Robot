package com.oaksmuth.pittayaaec.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.oaksmuth.pittayaaec.R;
import com.oaksmuth.pittayaaec.data.Advanced;
import com.oaksmuth.pittayaaec.data.TwoTextArrayAdapter;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oak on 14/2/2559.
 * Before playing the Text, we need to select the topic of the application
 * 3rd Page of the Application
 */


public class TopicSelect extends AppCompatActivity {
    private static final String tb_name = "sqlite_sequence";

    public class TopicHeader extends SugarRecord {
        String Topic;
        String SubTopics;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);
        setContentView(R.layout.activity_catalog);
        List<TopicHeader> topics = TopicHeader.findWithQuery(TopicHeader.class, "SELECT * FROM " + tb_name);
        ArrayList<Advanced> advancedTopics = new ArrayList<>();
        boolean newTopic = false;
        String header = "";
        for(int i = 0;i < topics.size(); i++)
        {
            if(advancedTopics.isEmpty() || !topics.get(i).Topic.equals(topics.get(i-1).Topic)) {
                header = topics.get(i).Topic;
                advancedTopics.add(new Advanced(Advanced.HEADER, topics.get(i).Topic));
                advancedTopics.add(new Advanced(Advanced.TOPIC, topics.get(i).SubTopics));
            }
            else
            {
                advancedTopics.add(new Advanced(Advanced.TOPIC, topics.get(i).SubTopics));
            }
        }
        final Intent intent = new Intent(this, Player.class);
        TextView textView = (TextView) findViewById(R.id.CatalogtextView);
        final ListView catalogList = (ListView) findViewById(R.id.CataloglistView);
        textView.setText("Select Topic");
        TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getApplicationContext(), advancedTopics);
        catalogList.setAdapter(adapter);
        catalogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Advanced advanced = (Advanced) catalogList.getItemAtPosition(position);
                if(advanced.type == Advanced.TOPIC)
                {
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}