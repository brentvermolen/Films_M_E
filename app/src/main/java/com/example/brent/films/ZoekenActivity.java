package com.example.brent.films;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.brent.films.Class.ActorsGridView;
import com.example.brent.films.Class.DAC;
import com.example.brent.films.Class.Methodes;
import com.example.brent.films.Class.MoviesGridView;
import com.example.brent.films.Model.Acteur;
import com.example.brent.films.Model.Film;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ZoekenActivity extends AppCompatActivity {

    LinearLayout llZoekenFilms;

    LinearLayout llZoekenActeurs;

    ImageView imgRandHeader;

    EditText txtZoeken;
    List<Acteur> acteurs;
    List<Film> films;

    GridView grdFilms;
    GridView grdActeurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoeken);

        initViews();
        handleEvents();
    }

    private void initViews() {
        imgRandHeader = (ImageView) findViewById(R.id.imgRandHeader);
        Random rand = new Random();
        int randId = rand.nextInt(DAC.Films.size());
        Bitmap bm = null;
        try {
            bm = Methodes.getBitmapFromAsset(this, "films/" + DAC.Films.get(randId).getID() + ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgRandHeader.setImageBitmap(bm);

        llZoekenFilms = (LinearLayout) findViewById(R.id.llZoekenFilms);
        llZoekenActeurs= (LinearLayout) findViewById(R.id.llZoekenActeurs);

        txtZoeken = (EditText) findViewById(R.id.txtZoeken);

        grdActeurs = findViewById(R.id.grdActeurs);
        grdFilms = findViewById(R.id.grdFilms);
    }

    private void handleEvents() {
        txtZoeken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                acteurs = Methodes.FilterActeursByName(s.toString());
                acteurs.sort(new Comparator<Acteur>() {
                    @Override
                    public int compare(Acteur o1, Acteur o2) {
                        return o1.getNaam().compareTo(o2.getNaam());
                    }
                });
                films = Methodes.FilterFilmsByName(s.toString());
                films = Methodes.SortFilmsByNameAndCollection(films);

                resetResultaten();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void resetResultaten() {
        if (acteurs.size() == 0){
            llZoekenActeurs.setVisibility(View.GONE);
        }else {
            llZoekenActeurs.setVisibility(View.VISIBLE);
        }

        if (films.size() == 0){
            llZoekenFilms.setVisibility(View.GONE);
        }else{
            llZoekenActeurs.setVisibility(View.VISIBLE);
        }

        grdFilms.setAdapter(new MoviesGridView(this, films));
        grdFilms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Intent intent = new Intent(ZoekenActivity.this, FilmActivity.class);
                intent.putExtra("film_id", films.get(position).getID());
                startActivity(intent);
            }
        });

        grdActeurs.setAdapter(new ActorsGridView(this, acteurs));
        grdActeurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ZoekenActivity.this, ActeurActivity.class);
                intent.putExtra("acteur_id", acteurs.get(position).getID());
                startActivity(intent);
            }
        });
    }
}
