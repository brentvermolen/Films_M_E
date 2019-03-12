package com.example.brent.films_m_e;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.films_m_e.Class.DAC;
import com.example.brent.films_m_e.Class.Methodes;
import com.example.brent.films_m_e.Class.MoviesGridView;
import com.example.brent.films_m_e.Model.Acteur;
import com.example.brent.films_m_e.Model.ActeurFilm;
import com.example.brent.films_m_e.Model.Film;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActeurActivity extends AppCompatActivity {

    Acteur acteur;

    Toolbar mToolbar;

    ImageView imgPoster;
    TextView lblNaam;

    GridView grdMovies;
    List<Film> films;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acteur);

        setTitle("");

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.argb(50,255,255,255));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        acteur = Methodes.FindActeurById(DAC.Acteurs, getIntent().getIntExtra("acteur_id", 0));

        if (acteur == null){
            Toast.makeText(this, "acteur niet gevonden", Toast.LENGTH_SHORT).show();
            finish();
        }

        initViews();
        handleEvents();
    }

    private void initViews() {
        imgPoster = (ImageView) findViewById(R.id.imgPoster);

        try {
            imgPoster.setImageBitmap(Methodes.getBitmapFromAsset(this, "acteurs/" + acteur.getID() + ".jpg"));
        } catch (IOException e) {
            imgPoster.setImageResource(R.drawable.no_image);
            e.printStackTrace();
        }

        lblNaam = (TextView) findViewById(R.id.lblNaam);
        lblNaam.setText(acteur.getNaam());

        grdMovies = (GridView) findViewById(R.id.grdMovies);
        films = new ArrayList<>();

        for(ActeurFilm af : acteur.getFilms()){
            films.add(af.getFilm());
        }

        films = Methodes.SortFilmsByNameAndCollection(films);

        grdMovies.setAdapter(new MoviesGridView(this, films));

        grdMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Intent intent = new Intent(ActeurActivity.this, FilmActivity.class);
                intent.putExtra("film_id", films.get(position).getID());
                startActivity(intent);
            }
        });
    }

    private void handleEvents() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case 16908332:
                onBackPressed();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }
}
