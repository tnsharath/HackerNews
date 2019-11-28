package com.vintile.hackernews.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vintile.hackernews.R;
import com.vintile.hackernews.util.AppConstants;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private TextInputEditText inputEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEditText = findViewById(R.id.idSearch);
    }

    public void Search(View view) {
        String searchString = Objects.requireNonNull(inputEditText.getText()).toString();
        if (searchString.length() == 0){
            Toast.makeText(this, getResources().getString(R.string.empty_string_error_msg), Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, NewsHeadlineActivity.class);
        intent.putExtra(AppConstants.SEARCH_STRING, searchString);
        startActivity(intent);
    }
}
