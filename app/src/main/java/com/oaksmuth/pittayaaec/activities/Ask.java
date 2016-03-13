package com.oaksmuth.pittayaaec.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oaksmuth.pittayaaec.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Oak on 13/3/2559.
 */
public class Ask extends AppCompatActivity{
    private final int SPEECH_RECOGNITION_CODE = 1;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private String said;
    private Context context;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        linearLayout = (LinearLayout) findViewById(R.id.ask_textLayout);
        scrollView = (ScrollView) findViewById(R.id.ask_scrollView);
        setContentView(R.layout.activity_ask);
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
                            textView.setText(said);
                            linearLayout.addView(textView);
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    cursor = Splash.helper.rawQuery("SELECT Answer from Data WHERE Question = ?", simplifyText(said));

                }
                break;
            }
        }
    }
    private String[] simplifyText(String text){
        String[] ans = new String[1];
        if(text.endsWith("?"))
        {
            text.subSequence(0,text.length() - 1);
            text = text + " ?";
        }
        ans[0] = text;
        return ans;
    }

}
