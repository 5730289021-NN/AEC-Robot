package com.oaksmuth.pittayaaec.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
    private TextToSpeech tts;
    private String header;
    private Advanced advanced;

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

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.ENGLISH);
                    //tts.speak("Category is " + topic + "and Topic is " + subTopic, TextToSpeech.QUEUE_FLUSH, null);
            }
        }});



        final Cursor cursor = Splash.helper.rawQuery("SELECT DISTINCT Topic, SubTopic FROM " + tb_name,null);

        topics = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()){ //make sure you got results, and move to first row
            do{
                topics.add(new TopicHeader(cursor.getString(0),cursor.getString(1)));
            } while (cursor.moveToNext()); //move to next row in the query result
        }


        final ArrayList<Advanced> advancedTopics = new ArrayList<>();//for making listView
        for(int i = 0;i < topics.size(); i++)
        {
            //Populate ListView
                advancedTopics.add(new Advanced(Advanced.TOPIC, topics.get(i).SubTopic));
        }



        //final Intent intent = new Intent(this, Player.class);
        catalogList = (ListView) findViewById(R.id.CataloglistView);
        adapter = new TwoTextArrayAdapter(getApplicationContext(), advancedTopics);
        catalogList.setAdapter(adapter);
        //Bind populated listView


        catalogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                advanced = (Advanced) catalogList.getItemAtPosition(position);
                //find its header
                header = topics.get(position).Topic;
                Log.i("Database", "i = " + position);
                /*
                for(int i = position;i>=0;i--)
                    {
                        if(advancedTopics.get(i).type == Advanced.HEADER)
                        {

                            header = advancedTopics.get(i).sentence;
                            break;
                        }
                }*/

                Log.i("Database", topics.get(position).Topic + " and " + topics.get(position).SubTopic);

                //intent.putExtra("Topic", header);
                //intent.putExtra("SubTopic", advanced.sentence);
                //Toast.makeText(context,"Topic: " + header + " SubTopic: " + advanced.sentence,Toast.LENGTH_LONG).show();
                //Log.i("Database", "Topic: " + header + " SubTopic: " + advanced.sentence);
                /****************Special Edit Here*********************************************/
                //Toast.makeText(context, "Start Converting", Toast.LENGTH_SHORT).show();
                String[] params = new String[2];
                params[0] = topics.get(position).Topic;
                params[1] = topics.get(position).SubTopic;
                Cursor cursor = Splash.helper.rawQuery("SELECT _id, Question, Answer from Data WHERE Topic = ? and SubTopic = ?", params);
                int row = cursor.getCount();
                Log.i("Thread", "Got " + row + " rows");
                final QA[] qas = new QA[row];//QA is Question and Answer
                if (cursor != null && cursor.moveToFirst()) { //make sure you got results, and move to first row
                    int i = 0;
                    do {
                        qas[i++] = (new QA(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                    } while (cursor.moveToNext()); //move to next row in the query result
                }
                Log.i("Thread", "Copied to QAS");

                String textToConvert = "Category is " + topics.get(position).Topic + "... and Topic is " + topics.get(position).SubTopic + " ... ";

                for (int m = 0; m < qas.length; m++) {
                    textToConvert += " ... " + ordinal(Integer.parseInt(qas[m].id)) + " ... ";
                    textToConvert += " ... " + qas[m].Question + " ... ";
                    textToConvert += " ... " + qas[m].Answer + " ... ";
                }
                Log.i("Thread", "Added to text convert position file " + (position + 1) + ".wav");

                HashMap<String, String> myHashRender = new HashMap();

                String path = context.getFilesDir().getPath();
                File f = new File(path + (position + 1) + ".wav");
                if(!f.exists())
                {
                    f.mkdirs();
                    try {
                        if(!f.createNewFile())
                        {
                            f.delete();
                            f.createNewFile();
                        }
                    } catch (IOException e) {e.printStackTrace();
                    Log.i("Folder", "Error");}
                }

                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textToConvert);
                Log.i("Thread", "Before Synthesised");
                tts.synthesizeToFile(textToConvert, null, f, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
                //tts.synthesizeToFile(textToConvert, myHashRender, destinationFileName);
                Log.i("Thread", "Finished Synthesised");

                Toast.makeText(context, "Finished, file save at " + path, Toast.LENGTH_SHORT).show();
            }
                    /******************************************************************************/
                    //startActivity(intent);
                    //finish();
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
    private class QA{
        public String Question;
        public String Answer;
        public String id;
        public QA(String id, String Question, String Answer)
        {
            this.id = id;
            this.Question = Question;
            this.Answer = Answer;
        }
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

}