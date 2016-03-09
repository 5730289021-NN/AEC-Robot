package com.oaksmuth.pittayaaec.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oaksmuth.pittayaaec.R;
import com.oaksmuth.pittayaaec.data.TwoTextArrayAdapter;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oak on 14/2/2559.
 * Before playing the Text, we need to select the topic of the application
 * 3rd Page of the Application
 */


public class TopicSelect extends AppCompatActivity {
    private static final String tb_name = "alldata";

    public class TopicHeader extends SugarRecord {
        String Topic;
        String SubTopics;
    }
    public class Advanced{
        public static final boolean TOPIC = true;
        public static final boolean HEADER = false;
        public String sentence;
        public boolean type;

        public Advanced(boolean type,String sentence){
            this.type = type;
            this.sentence = sentence;
        }

        @Override
        public String toString() {
            return sentence;
        }

        public int getViewType() {
            if(type){
                return TwoTextArrayAdapter.RowType.LIST_ITEM.ordinal();
            }
            else {
                return TwoTextArrayAdapter.RowType.HEADER_ITEM.ordinal();
            }
        }

        public View getView(LayoutInflater inflater, View convertView) {
            View view;
            TextView text;
            if(type) {
                if (convertView == null) {
                    view = (View) inflater.inflate(R.layout.topic, null);
                } else {
                    view = convertView;
                }
                text = (TextView) view.findViewById(R.id.topicTextView);
            }else {
                if (convertView == null) {
                    view = (View) inflater.inflate(R.layout.header, null);
                } else {
                    view = convertView;
                }
                text = (TextView) view.findViewById(R.id.separator);
            }
            text.setText(sentence);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        List<TopicHeader> topics = TopicHeader.findWithQuery(TopicHeader.class, "SELECT DISTINCT Topic,SubTopic FROM " + tb_name);
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
        TextView textView = (TextView) findViewById(R.id.CatalogtextView);
        ListView catalogList = (ListView) findViewById(R.id.CataloglistView);
        textView.setText("Select Topic");
        TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(getApplicationContext(), advancedTopics);
        catalogList.setAdapter(adapter);
    }

}