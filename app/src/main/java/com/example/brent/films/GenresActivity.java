package com.example.brent.films;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.brent.films.Class.GenresAdapter;

public class GenresActivity extends AppCompatActivity {

    ListView lstGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        initViews();
        handleEvents();
    }

    private void initViews() {
        lstGenres = (ListView) findViewById(R.id.lstGenres);
        lstGenres.setAdapter(new GenresAdapter(this));
    }

    private void handleEvents() {

    }
}
