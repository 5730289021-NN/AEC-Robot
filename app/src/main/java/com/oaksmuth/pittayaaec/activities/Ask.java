package com.oaksmuth.pittayaaec.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
 * Created by Oak on 13/3/2559.
 * Last Edited 6 May 2016
 */
public class Ask extends AppCompatActivity{
    private final int SPEECH_RECOGNITION_CODE = 1;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private String said;
    private String fromDB;
    private Context context;
    private TextToSpeech tts;
    private TextView speedTextView;
    private TextView pitchTextView;
    private TextView initialTextView;
    private DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_ask);
        linearLayout = (LinearLayout) findViewById(R.id.ask_textLayout);
        scrollView = (ScrollView) findViewById(R.id.ask_scrollView);
        Button askButton = (Button) findViewById(R.id.ask_button);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Speak something...");
                try {
                    startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Speech recognition is not supported in this device.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        initialTextView = (TextView) findViewById(R.id.ask_initialTag);//initial branch commit

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    tts.speak("Hello, ask me whatever you want to know", TextToSpeech.QUEUE_FLUSH, null);
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
        speedTextView = (TextView) findViewById(R.id.ask_speedTextView);
        pitchTextView = (TextView) findViewById(R.id.ask_pitchTextView);
        df = new DecimalFormat("#.##");
        SeekBar speedSeekBar = (SeekBar) findViewById(R.id.ask_speedSeekBar);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float speedValue = Float.parseFloat(df.format((float) (Math.pow(2, (double) progress / 50) / 2)));
                speedTextView.setText(String.valueOf(speedValue));
                tts.setSpeechRate(speedValue);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        SeekBar pitchSeekBar = (SeekBar) findViewById(R.id.ask_pitchSeekBar);
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    final ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    said = result.get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = new TextView(context);
                            String simp = simplifyText(said)[0] + "\n";
                            textView.setText(simp);
                            textView.setTextColor(Color.BLACK);
                            linearLayout.addView(textView);
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    Cursor cursor = Splash.helper.rawQuery("SELECT Answer FROM Data WHERE Question = ? COLLATE NOCASE", simplifyText(said));
                    if(cursor.getCount() == 0)
                    {
                        fromDB = "Sorry, I don't know";
                    }else
                    {
                        cursor.moveToFirst();
                        fromDB = cursor.getString(0);
                    }
                    tts.speak(fromDB,TextToSpeech.QUEUE_FLUSH,null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = new TextView(context);
                            String dbData = fromDB + "\n";
                            textView.setText(dbData);
                            linearLayout.addView(textView);
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                break;
            }
        }
    }
    private String[] simplifyText(String text){
        String[] ans = new String[1];
        text = text.trim();
        if(text.endsWith("?"))
        {
            text = text.substring(0,text.length() - 1);
        }
        String cap = String.valueOf(text.charAt(0)).toUpperCase();
        text = text.substring(1,text.length());
        text = cap + text + " ?";
        ans[0] = text;
        return ans;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, ModeSelect.class);
        startActivity(intent);
        finish();
    }
}
