package com.oaksmuth.pittayaaec.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.oaksmuth.pittayaaec.R;

//import android.support.v7.widget.


/**
 * Created by Oak on 14/2/2559.
 * Mode Selector 1.Play
 * 2nd Page of the Application
 */
public class ModeSelect extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeselect);
        context = this;
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        CardView catalogCard = (CardView) findViewById(R.id.catalogCard);
        catalogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TopicSelect.class);
                startActivity(intent);
                finish();
            }
        });

        CardView askCard = (CardView) findViewById(R.id.askCard);
        askCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Ask.class);
                startActivity(intent);
                finish();
            }
        });
    }
}