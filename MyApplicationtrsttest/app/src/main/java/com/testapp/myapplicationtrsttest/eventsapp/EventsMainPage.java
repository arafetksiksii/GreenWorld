package com.testapp.myapplicationtrsttest.eventsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.testapp.myapplicationtrsttest.R;

public class EventsMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_main_page);

        getWindow().setStatusBarColor(Color.GRAY);
    }
}