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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oaksmuth.pittayaaec.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Oak on 14/2/2559.
 * This will play the Text that read from the realm database
 * 4rd Page of the Application
 */
public class Player extends AppCompatActivity{
    private boolean isPlaying = false;
    private boolean isFinished = false;
    private int row = 0;
    private int playingAt = 0;
    private boolean isStart = false;
    private boolean isQuestion = true;
    private TextToSpeech tts;
    private Context context;
    private LinearLayout textLayout;
    private ScrollView scrollView;
    private PlayTTSTask ttsTask;
    private DecimalFormat df;
    private ImageButton playButton;
    private TextView speedTextView;
    private TextView pitchTextView;
    private TextView initialTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        context = this;
        ttsTask = new PlayTTSTask();
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
        Toast.makeText(this,"Amount: " + String.valueOf(row),Toast.LENGTH_LONG).show();
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

        //Initiate and Force Scroll Layout to be at bottom
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        //Rename ActionBar
        setTitle(topic + " : " + subTopic);

        //Initiate Topics TextView
/*
        TextView topicTextView = (TextView) findViewById(R.id.topicTextView);
        topicTextView.setText(topic);
        TextView subTopicTextView = (TextView) findViewById(R.id.subTopicTextView);
        subTopicTextView.setText(subTopic);*/

        initialTextView = (TextView) findViewById(R.id.initialTag);
        //Initiate TTS
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.ENGLISH);
                    tts.speak("Category is " + topic + "and Topic is " + subTopic, TextToSpeech.QUEUE_FLUSH, null);
                    while(!tts.isSpeaking()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    initialTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
        //Initiate SeekBars
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        pitchTextView = (TextView) findViewById(R.id.pitchTextView);
        df = new DecimalFormat("0.00");
        SeekBar speedSeekBar = (SeekBar) findViewById(R.id.speedSeekBar);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speedValue = Float.parseFloat(df.format((float) (Math.pow(2, (double) progress/50)/2)));
                speedTextView.setText(String.valueOf(speedValue));
                tts.setSpeechRate(speedValue);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar pitchSeekBar = (SeekBar) findViewById(R.id.pitchSeekBar);
        pitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float pitchValue = Float.parseFloat(df.format((float) (Math.pow(2, (double) progress/50)/2)));
                pitchTextView.setText(String.valueOf(pitchValue));
                tts.setPitch(pitchValue);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        //Initiate Buttons
        playButton = (ImageButton) findViewById(R.id.playImageButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFinished){
                    onBackPressed();
                }
                if(!isStart){
                    ttsTask.execute(qas);
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
                if(playingAt > 0)
                    playingAt--;
                else
                    onBackPressed();
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
            Log.i("Do In Background", "Initiated");
            while(true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isPlaying) {
                    if (!tts.isSpeaking()) {
                        if(playingAt == row) break;
                        if(isQuestion) {
                            Log.i("Do In Background", "isPlaying & !tts.isSpeaking & isQuestion");
                            tts.speak(params[playingAt].Question, TextToSpeech.QUEUE_FLUSH, null);
                            publishProgress(params[playingAt].Question);
                        }
                        else {
                            Log.i("Do In Background", "isPlaying & !tts.isSpeaking & !isQuestion");
                            tts.speak(params[playingAt].Answer, TextToSpeech.QUEUE_FLUSH, null);
                            publishProgress(params[playingAt].Answer);
                        }
                    }
                }
                if(isCancelled())
                {
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final String... values) {
            super.onProgressUpdate(values);
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
                        playingAt++;
                    }
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    textTextView.setTextColor(Color.BLACK);
                    textLayout.addView(textTextView);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isFinished = true;
            playButton.setBackgroundResource(R.drawable.play);
            isPlaying = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ttsTask.cancel(true);
        Intent intent = new Intent(context, TopicSelect.class);
        startActivity(intent);
        finish();
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
