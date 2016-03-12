package com.oaksmuth.pittayaaec.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oaksmuth.pittayaaec.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Oak on 14/2/2559.
 * This will play the Text that read from the realm database
 * 4rd Page of the Application
 */
public class Player extends AppCompatActivity{
    boolean isPlaying = false;
    boolean ttsReady = false;
    int row = 0;
    int playingAt = 0;
    boolean isStart = false;
    boolean isQuestion = true;
    TextToSpeech tts;
    Context context;
    LinearLayout textLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context = this;
        //Get all data from Intent
        Intent intent = getIntent();
        final String topic = intent.getStringExtra("Topic");
        final String subTopic = intent.getStringExtra("SubTopic");

        //Query all Data from Topic
        String[] params = new String[2];
        params[0] = topic;
        params[1] = subTopic;
        Cursor cursor = Splash.helper.rawQuery("SELECT Question, Answer from Data WHERE Topic = ? and SubTopic = ?", params);
        //Retrieve data from the query
        row = cursor.getCount();
        Toast.makeText(this,String.valueOf(row),Toast.LENGTH_LONG).show();
        Log.i("Database","Collected " + row + " rows");
        final QA[] qas = new QA[row];//QA is Question and Answer
        if (cursor != null && cursor.moveToFirst()){ //make sure you got results, and move to first row
            int i = 0;
            do{
                qas[i++] = (new QA(cursor.getString(0),cursor.getString(1)));
            } while (cursor.moveToNext()); //move to next row in the query result
        }
        //Initiate Layout for TextView
        textLayout = (LinearLayout) findViewById(R.id.textLayout);


        //Initiate Topics TextView
        TextView topicTextView = (TextView) findViewById(R.id.topicTextView);
        topicTextView.setText(topic);
        TextView subTopicTextView = (TextView) findViewById(R.id.subTopicTextView);
        subTopicTextView.setText(subTopic);

        //Initiate TTS
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.ENGLISH);
                tts.speak("Category is " + topic + "and Topic is " + subTopic, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        //Initiate Buttons
        final ImageButton playButton = (ImageButton) findViewById(R.id.playImageButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStart){
                    new PlayTTSTask().execute(qas);
                    isStart = true;
                }
                if(isPlaying)
                {
                    playButton.setBackgroundResource(R.drawable.play);
                    tts.stop();
                    if(!isQuestion)//Not question -> Question
                    {
                        isQuestion = true;
                    }else//Question(Next Question) -> Go Back to Last Answer
                    {
                        isQuestion = false;
                        playingAt--;
                    }
                    isPlaying = false;
                }else{
                    playButton.setBackgroundResource(R.drawable.pause);
                    isPlaying = true;
                }
            }
        });

        ImageButton backwardButton = (ImageButton) findViewById(R.id.backwardImageButton);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.stop();
                playingAt--;
            }
        });

        ImageButton forwardButton = (ImageButton) findViewById(R.id.forwardImageButton);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.stop();
            }
        });
    }
    private class QA{
        public String Question;
        public String Answer;
        public QA(String Question, String Answer)
        {
            this.Question = Question;
            this.Answer = Answer;
        }
    }

    private class PlayTTSTask extends AsyncTask<QA, String, Void>
    {
        @Override
        protected Void doInBackground(QA... params) {
            Log.i("Do In Background","Initiated");
            while(true) {
                try {
                    Thread.sleep(500);
                    Log.i("Do In Background", "Sleeping");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isPlaying) {
                    if (!tts.isSpeaking()) {
                        if(isQuestion) {
                            Log.i("Do In Background", "isPlaying & !tts.isSpeaking & isQuestion");
                            tts.speak(params[playingAt].Question, TextToSpeech.QUEUE_FLUSH, null);
                            publishProgress(params[playingAt].Question);
                        }
                        else {
                            Log.i("Do In Background", "isPlaying & !tts.isSpeaking & !isQuestion");
                            tts.speak(params[playingAt].Answer, TextToSpeech.QUEUE_FLUSH, null);
                            publishProgress(params[playingAt].Answer);
                            if(playingAt == row - 1)
                            {
                                break;
                            }
                            playingAt++;
                        }
                    }
                }
            }
            finish();
            return null;
        }

        @Override
        protected void onProgressUpdate(final String... values) {
            super.onProgressUpdate(values);
            Log.i("on Progress Update", "Initiated");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textTextView = new TextView(context);
                    if (isQuestion) {
                        textTextView.setText("Question\t" + (playingAt + 1) + "\t:\t" + values[0] + "\n");
                        isQuestion = false;
                    } else {
                        textTextView.setText("Answer\t" + (playingAt + 1) + "\t:\t" + values[0] + "\n");
                        isQuestion = true;
                    }
                    textTextView.setTextColor(Color.BLACK);
                    textLayout.addView(textTextView);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent();
        }
    }
    @Override
    public void onStop() {
        if (tts != null) {
            tts.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        super.onDestroy();
    }
}

/*public class PlayerActivity extends AppCompatActivity{
    private boolean speaking = true;
    private boolean isQuestion = true;
    private LinearLayout textLayout;
    private TextView topicTextView;
    private TextToSpeech tts;
    private String text;
    private ArrayList<Integer> chosen;
    private int index = 0;
    private String topic;
    private ArrayList<Data> data;
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        textLayout = (LinearLayout) findViewById(R.id.textLayout);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("Mode");
        topic = intent.getStringExtra("Topic");//Means Subtopic if advanced
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit (int status){

            }
        });
        topicTextView = (TextView) findViewById(R.id.topicTextView);
        TextView subTopicTextView = (TextView) findViewById(R.id.subTopicTextView);
        if(mode.equals("0"))//Basic
        {
            topicTextView.setText(topic);
            subTopicTextView.setText("");
            data = Splash.data.basicData;
            chosen = Splash.data.findIndexBasicTopic(topic);
        }
        else if(mode.equals("1"))//Advanced
        {
            topicTextView.setText(topic);
            //TODO implement later
            //subtopic
            //data = Splash.data.advancedData;
        }
        ImageButton backwardButton = (ImageButton) findViewById(R.id.backwardImageButton);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        playButton = (ImageButton) findViewById(R.id.playImageButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(speaking) {
                    playButton.setBackgroundResource(R.drawable.play);
                    speaking = false;
                }else
                {
                    playButton.setBackgroundResource(R.drawable.pause);
                    speaking = true;
                }
            }
        });
        ImageButton forwardImageButton = (ImageButton) findViewById(R.id.forwardImageButton);
        forwardImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaking = false;
                playNextTopic();
                speaking = true;
                return;
            }
        });



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(true)
                {
                    try {
                        Thread.sleep(100);
                        if(speaking)
                        {
                            if(!tts.isSpeaking())
                            {
                                boolean speakingQuestion = isQuestion;
                                String toSpeak = nextSentence();
                                if(toSpeak == "null") {
                                    if(!playNextTopic())
                                    {
                                        onBackPressed();
                                        break;
                                    }
                                    Thread.sleep(2000);
                                }
                                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                if(speakingQuestion)
                                    text = "Q : ";
                                else
                                    text = "A : ";
                                text += toSpeak;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView textTextView = new TextView(getApplicationContext());
                                        textTextView.setText(text);
                                        textTextView.setTextColor(Color.BLACK);
                                        textLayout.addView(textTextView);
                                    }
                                });
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public synchronized String nextSentence(){
        if(chosen.size() == index)//not topic
        {
            return "null";
        }
        else//the same topic
        {
            if(isQuestion)
            {
                isQuestion = false;
                if(data.get(chosen.get(index)).sQuestion.trim().isEmpty())
                {
                    return data.get(chosen.get(index)).question.trim();
                }
                else
                {
                    return (data.get(chosen.get(index)).question + ", " + data.get(chosen.get(index)).sQuestion).trim();
                }
            }
            else
            {
                isQuestion = true;
                if(data.get(chosen.get(index)).sAnswer.trim().isEmpty())
                {
                    index++;;
                    return data.get(chosen.get(index - 1)).answer.trim();
                }
                else
                {
                    index++;;
                    return (data.get(chosen.get(index - 1)).answer + ", " + data.get(chosen.get(index - 1)).sAnswer).trim();
                }
            }
        }
    }

    public void onBackPressed(){
        speaking = false;
        tts.stop();
        tts.shutdown();
        super.onBackPressed();
    }

    public synchronized boolean playNextTopic(){
        if(Splash.data.basicList.get(Splash.data.basicList.size() - 1).equals(topic))
        {
            return false;
        }
        else
        {
            for(int i = 0;i<Splash.data.basicList.size();i++)
            {
                if(Splash.data.basicList.get(i).equals(topic))
                {
                    isQuestion = true;
                    topic = Splash.data.basicList.get(i+1);
                    index = 0;
                    chosen = Splash.data.findIndexBasicTopic(topic);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            topicTextView.setText(topic);
                        }
                    });
                    return true;
                }
            }
            return false;
        }

    }
}

* */
