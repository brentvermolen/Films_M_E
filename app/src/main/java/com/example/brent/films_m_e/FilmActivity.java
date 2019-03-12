package com.example.brent.films_m_e;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.films_m_e.Class.ActorsFilmGridView;
import com.example.brent.films_m_e.Class.DAC;
import com.example.brent.films_m_e.Class.FavorietenDAO;
import com.example.brent.films_m_e.Class.FilmsDb;
import com.example.brent.films_m_e.Class.Methodes;
import com.example.brent.films_m_e.Model.ActeurFilm;
import com.example.brent.films_m_e.Model.Film;
import com.example.brent.films_m_e.Model.Tag;

import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;

public class FilmActivity extends AppCompatActivity {
    Film film;

    FavorietenDAO dao;

    Toolbar mToolbar;

    ImageView imgFilmHeader;
    TextView lblTagline;
    TextView lblTitel;
    TextView lblJaartal;
    TextView lblDuur;

    Button btnOmschrijving;
    Button btnExtraInfo;

    ScrollView llOmschrijving;
    TextView lblOmschrijving;

    LinearLayout llExtraInfo;
    LinearLayout llGenres;

    LinearLayout llCollectie;
    TextView lblCollectie;
    LinearLayout llCollectieInhoud;

    LinearLayout llTrailer;
    ImageButton btnTrailer;

    GridView grdActors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        setTitle("");
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        film = Methodes.FindFilmById(DAC.Films, getIntent().getIntExtra("film_id", 0));
        if (film == null){
            finish();
            Toast.makeText(this, "De film werd niet gevonden", Toast.LENGTH_SHORT).show();
        }

        dao = FilmsDb.getDatabase(this).favorietenDAO();

        initViews();
        handleEvents();
    }

    private void initViews() {
        imgFilmHeader = (ImageView) findViewById(R.id.imgFilmHeader);
        lblTitel = (TextView) findViewById(R.id.lblTitel);
        lblTagline = (TextView) findViewById(R.id.lblTagline);
        lblJaartal = (TextView) findViewById(R.id.lblJaartal);
        lblDuur = (TextView) findViewById(R.id.lblDuur);

        try {
            imgFilmHeader.setImageBitmap(Methodes.getBitmapFromAsset(this, "films/" + film.getID() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        lblTitel.setText(film.getNaam());
        if (film.getTagline() == null){
            film.setTagline("");
        }
        if (!(film.getTagline().equals(""))){
            lblTagline.setText(film.getTagline());
        }else{
            lblTagline.setVisibility(View.GONE);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(film.getReleaseDate());
        lblJaartal.setText(String.valueOf(c.get(Calendar.YEAR)));
        int uren = film.getDuur() / 60;
        int min = film.getDuur() - (uren * 60);
        lblDuur.setText(" " + uren + "uur " + min + "min.");

        btnOmschrijving = (Button) findViewById(R.id.btnOmschrijving);
        btnExtraInfo = (Button) findViewById(R.id.btnExtraInfo);

        llOmschrijving = (ScrollView) findViewById(R.id.llOmschrijving);
        llExtraInfo = (LinearLayout) findViewById(R.id.llExtraInfo);

        lblOmschrijving = (TextView) findViewById(R.id.lblOmschrijving);
        lblOmschrijving.setText(film.getOmschrijving());

        llGenres = (LinearLayout) findViewById(R.id.llGenres);
        for(final Tag tag : film.getGenres()){
            final Button btnGenre = new Button(this, null, 0, R.style.btnTagFilmDetail);
            btnGenre.setId(tag.getID());
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(7, 0, 7, 0);
            btnGenre.setLayoutParams(buttonLayoutParams);
            try {
                @SuppressLint("ResourceType") XmlResourceParser parser = getResources().getXml(R.color.textcolor_btn_tag_film_detail);
                ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
                btnGenre.setTextColor(colors);
            } catch (Exception e) {
                // handle exceptions
            }
            btnGenre.setText(tag.getNaam());

            llGenres.addView(btnGenre);
        }

        llCollectie = (LinearLayout) findViewById(R.id.llCollectie);
        lblCollectie = (TextView) findViewById(R.id.lblCollectie);
        llCollectieInhoud = (LinearLayout) findViewById(R.id.llCollectieInhoud);

        if (film.getCollectieID() == 0){
            llCollectie.setVisibility(View.GONE);
        }else{
            lblCollectie.setText(film.getCollectie().getNaam());

            film.getCollectie().getFilms().sort(new Comparator<Film>() {
                @Override
                public int compare(Film o1, Film o2) {
                    return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                }
            });
            for (final Film f : film.getCollectie().getFilms()){
                ImageView img = new ImageView(this);
                img.setId(f.getID());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                img.setLayoutParams(params);
                img.setAdjustViewBounds(true);

                Bitmap bm;
                try {
                    bm = Methodes.getBitmapFromAsset(this, "films/" + f.getID() + ".jpg");
                    bm = Methodes.getRoundedCornerBitmap(bm, 15);
                    img.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (f.getID() == film.getID()){
                    img.setEnabled(false);
                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FilmActivity.this, FilmActivity.class);
                        intent.putExtra("film_id", f.getID());
                        startActivity(intent);
                        finish();
                    }
                });

                llCollectieInhoud.addView(img);
            }
        }

        llTrailer = (LinearLayout) findViewById(R.id.llTrailer);
        btnTrailer = (ImageButton) findViewById(R.id.btnTrailer);

        grdActors = (GridView) findViewById(R.id.grdActors);
        film.getActeurs().sort(new Comparator<ActeurFilm>() {
            @Override
            public int compare(ActeurFilm o1, ActeurFilm o2) {
                return o1.getSort() - o2.getSort();
            }
        });

        grdActors.setAdapter(new ActorsFilmGridView(this, film.getActeurs()));

        grdActors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Intent intent = new Intent(FilmActivity.this, ActeurActivity.class);
                intent.putExtra("acteur_id", film.getActeurs().get(position).getActeurID());
                startActivity(intent);
            }
        });
    }

    private void handleEvents() {
        btnOmschrijving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOmschrijving.setEnabled(false);
                btnExtraInfo.setEnabled(true);

                llExtraInfo.setVisibility(View.GONE);
                llOmschrijving.setVisibility(View.VISIBLE);
            }
        });
        btnExtraInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOmschrijving.setEnabled(true);
                btnExtraInfo.setEnabled(false);

                llOmschrijving.setVisibility(View.GONE);
                llExtraInfo.setVisibility(View.VISIBLE);
            }
        });

        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appIntent;
                Intent webIntent;

                String ytId = film.getTrailerId();
                if (ytId == null){
                    ytId = "";
                }

                if (ytId.equals("")){
                    Calendar c = Calendar.getInstance();
                    c.setTime(film.getReleaseDate());

                    appIntent = new Intent(Intent.ACTION_SEARCH);
                    appIntent.setPackage("com.google.android.youtube");
                    appIntent.putExtra("query", film.getNaam() + " (" + c.get(Calendar.YEAR) + ")");
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/results?search_query=" + film.getNaam() + " (" + c.get(Calendar.YEAR) + ")"));
                }else{
                    appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + ytId));
                    webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + ytId));
                }
                try {
                    FilmActivity.this.startActivity(appIntent);
                } catch (Exception ex) {
                    FilmActivity.this.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_film_acteur, menu);

        MenuItem item = menu.findItem(R.id.action_favorite);

        if (dao.getById(film.getID()) != null){
            item.setIcon(R.drawable.ic_favorite);
        }else {
            item.setIcon(R.drawable.ic_not_favorite);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_favorite:
                Film f = dao.getById(film.getID());
                if (f == null){
                    item.setIcon(R.drawable.ic_favorite);
                    dao.insert(film);
                }else {
                    item.setIcon(R.drawable.ic_not_favorite);
                    dao.deleteById(film.getID());
                }

                dao = FilmsDb.getDatabase(FilmActivity.this).favorietenDAO();

                break;
            case 16908332:
                onBackPressed();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }
}
