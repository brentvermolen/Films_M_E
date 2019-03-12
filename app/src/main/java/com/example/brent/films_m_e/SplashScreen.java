package com.example.brent.films_m_e;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.brent.films_m_e.Class.DAC;
import com.example.brent.films_m_e.Class.Methodes;
import com.example.brent.films_m_e.Model.Acteur;
import com.example.brent.films_m_e.Model.ActeurFilm;
import com.example.brent.films_m_e.Model.Collectie;
import com.example.brent.films_m_e.Model.Film;
import com.example.brent.films_m_e.Model.FilmTags;
import com.example.brent.films_m_e.Model.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    TextView lblProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lblProgress = (TextView) findViewById(R.id.lblProgress);

        new AsyncTask<Void, String, Void>(){
            @Override
            protected void onProgressUpdate(String... values) {
                lblProgress.setText(values[0]);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.e("t", "start");
                Type listFilmType = new TypeToken<ArrayList<Film>>(){}.getType();
                publishProgress("Bezig met laden van films");
                DAC.Films = new Gson().fromJson(loadJSONFromAsset("films.json"), listFilmType);

                Type listCollectieType = new TypeToken<ArrayList<Collectie>>(){}.getType();
                publishProgress("Bezig met laden van collecties");
                DAC.Collecties = new Gson().fromJson(loadJSONFromAsset("collecties.json"), listCollectieType);

                for(Collectie collectie : DAC.Collecties){
                    collectie.setFilms(Methodes.GetMoviesFromCollection(DAC.Films, collectie));
                }

                for(Film film : DAC.Films){
                    film.setCollectie(Methodes.GetCollectionFromID(DAC.Collecties, film.getCollectieID()));
                }

                Type listActeurType = new TypeToken<ArrayList<Acteur>>(){}.getType();
                publishProgress("Bezig met laden van acteurs");
                DAC.Acteurs = new Gson().fromJson(loadJSONFromAsset("acteurs.json"), listActeurType);

                Type listActeurFilmType = new TypeToken<ArrayList<ActeurFilm>>(){}.getType();
                DAC.ActeurFilms = new Gson().fromJson(loadJSONFromAsset("acteurs_films.json"), listActeurFilmType);

                List<ActeurFilm> lstAf;

                for(ActeurFilm af : DAC.ActeurFilms) {
                    Acteur a = Methodes.FindActeurById(DAC.Acteurs, af.getActeurID());
                    Film f = Methodes.FindFilmById(DAC.Films, af.getFilmID());

                    try {
                        af.setActeur(a);
                        af.setFilm(f);

                        lstAf = a.getFilms();
                        lstAf.add(af);
                        a.setFilms(lstAf);
                        lstAf = f.getActeurs();
                        lstAf.add(af);
                        f.setActeurs(lstAf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Type listTagType = new TypeToken<ArrayList<Tag>>(){}.getType();
                publishProgress("Bezig met laden van genres");
                DAC.Tags = new Gson().fromJson(loadJSONFromAsset("tags.json"), listTagType);

                Type listFilmTagsType = new TypeToken<ArrayList<FilmTags>>(){}.getType();
                DAC.FilmTags = new Gson().fromJson(loadJSONFromAsset("film_tags.json"), listFilmTagsType);

                List<FilmTags> lstFt;
                for (FilmTags ft : DAC.FilmTags){
                    Film f = Methodes.FindFilmById(DAC.Films, ft.getFilm_ID());
                    Tag t = Methodes.FindTagById(DAC.Tags, ft.getTag_ID());

                    ft.setFilm(f);
                    ft.setTag(t);

                    lstFt = f.getFilmTags();
                    lstFt.add(ft);
                    f.setGenres(lstFt);

                    lstFt = t.getFilmTags();
                    lstFt.add(ft);
                    t.setFilms(lstFt);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        }.execute();
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
