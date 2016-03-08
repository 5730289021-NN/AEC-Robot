package com.oaksmuth.pittayaaec.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;
//import android.support.v7.widget.

import com.oaksmuth.pittayaaec.R;


/**
 * Created by Oak on 14/2/2559.
 * Mode Selector 1.Play
 * 2nd Page of the Application
 */
public class ModeSelect extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeselect);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        CardView catalogCard = (CardView) findViewById(R.id.catalogCard);
        catalogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment newFragment = new TypeSelectFragment();
                //newFragment.show(getSupportFragmentManager(),"Select Level");
            }
        });

        CardView askCard = (CardView) findViewById(R.id.askCard);
        askCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ask what", Toast.LENGTH_LONG).show();
            }
        });
    }
}


/*
package com.oaksmuth.pittayaaecrobot;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class ModeSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modeselect);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        CardView catalogCard = (CardView) findViewById(R.id.catalogCard);
        catalogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ModeSelectDialog();
                newFragment.show(getSupportFragmentManager(),"Select Level");
            }
        });

        CardView askCard = (CardView) findViewById(R.id.askCard);
        askCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ask what",Toast.LENGTH_LONG).show();
            }
        });
    }

}

 */