package com.example.brent.films_m_e;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.brent.films_m_e.Class.ActorsGridView;
import com.example.brent.films_m_e.Class.DAC;
import com.example.brent.films_m_e.Class.Methodes;
import com.example.brent.films_m_e.Class.MoviesGridView;
import com.example.brent.films_m_e.Model.Acteur;
import com.example.brent.films_m_e.Model.Film;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ZoekenActivity extends AppCompatActivity {

    Toolbar mToolbar;

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

        setTitle("");

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                films.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        return o1.getNaam().compareTo(o2.getNaam());
                    }
                });

                resetResultaten();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void resetResultaten() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zoeken, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_toggle:
                if (item.getTitle().equals("Zoek Acteurs")){
                    item.setTitle("Zoek Films");
                    item.setIcon(R.drawable.ic_movie);
                    llZoekenFilms.setVisibility(View.GONE);
                    llZoekenActeurs.setVisibility(View.VISIBLE);
                }else{
                    item.setTitle("Zoek Acteurs");
                    item.setIcon(R.drawable.ic_person);
                    llZoekenActeurs.setVisibility(View.GONE);
                    llZoekenFilms.setVisibility(View.VISIBLE);
                }
                break;
            case 16908332:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
