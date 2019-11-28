package com.vintile.hackernews.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.vintile.hackernews.R;
import com.vintile.hackernews.util.AppConstants;

public class NewsHeadlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_headline);
        String searchString = getIntent().getStringExtra(AppConstants.SEARCH_STRING);
        Toast.makeText(this, searchString, Toast.LENGTH_LONG).show();
    }
}
