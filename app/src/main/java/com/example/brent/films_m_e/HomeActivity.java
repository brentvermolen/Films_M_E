package com.example.brent.films_m_e;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.brent.films_m_e.Class.DAC;
import com.example.brent.films_m_e.Class.DialogSorterenOp;
import com.example.brent.films_m_e.Class.FavorietenDAO;
import com.example.brent.films_m_e.Class.FilmsDb;
import com.example.brent.films_m_e.Class.GenresDAO;
import com.example.brent.films_m_e.Class.MoviesGridView;
import com.example.brent.films_m_e.Class.Methodes;
import com.example.brent.films_m_e.Model.Collectie;
import com.example.brent.films_m_e.Model.Film;
import com.example.brent.films_m_e.Model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    Toolbar mToolbar;

    FavorietenDAO favorietenDAO;
    GenresDAO genresDAO;

    ImageView imgRandHeader;

    LinearLayout llGenres;
    ImageButton btnFavorieten;
    Button btnAlleFilms;

    GridView grdMovies;

    List<Film> currentlyShown;

    private int currentSort;
    private boolean currentSortDesc;

    private List<Button> btns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("");

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(Color.argb(100,255,255,255));
        setSupportActionBar(mToolbar);

        currentlyShown = DAC.Films;

        favorietenDAO = FilmsDb.getDatabase(this).favorietenDAO();
        genresDAO = FilmsDb.getDatabase(this).genresDAO();

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

        llGenres = (LinearLayout) findViewById(R.id.llGenres);

        DAC.Collecties.sort(new Comparator<Collectie>() {
            @Override
            public int compare(Collectie o1, Collectie o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });

        setGenreBar();

        btnAlleFilms = (Button) findViewById(R.id.btnTagAlle);
        btnAlleFilms.setEnabled(false);
        btnAlleFilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Button btn : btns){
                btn.setEnabled(true);
            }
            btnFavorieten.setEnabled(true);
                btnAlleFilms.setEnabled(false);
                showFilms(DAC.Films);
            }
        });

        btnFavorieten = (ImageButton) findViewById(R.id.btnTagFav);
        btnFavorieten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Button btn : btns){
                    btn.setEnabled(true);
                }
                btnAlleFilms.setEnabled(true);
                btnFavorieten.setEnabled(false);

                List<Film> favo = favorietenDAO.getAll();
                favo.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        return o1.getNaam().compareTo(o2.getNaam());
                    }
                });

                showFilms(favo);
            }
        });

        grdMovies = (GridView) findViewById(R.id.grdMovies);
        showFilms(currentlyShown);
    }

    private void setGenreBar() {
        llGenres.removeAllViews();
        btns.clear();

        List<Tag> tags = new ArrayList<>(DAC.Tags);
        tags.removeAll(genresDAO.getExistingTags());

        for(final Tag tag : tags){
            final Button btnGenre = new Button(this, null, 0, R.style.btnTag);
            btnGenre.setId(tag.getID());
            btns.add(btnGenre);
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(7, 0, 7, 0);
            btnGenre.setLayoutParams(buttonLayoutParams);
            //btnGenre.setTextColor(ContextCompat.getColor(this, R.color.textcolor_btn_tag));
            try {
                @SuppressLint("ResourceType") XmlResourceParser parser = getResources().getXml(R.color.textcolor_btn_tag);
                ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
                btnGenre.setTextColor(colors);
            } catch (Exception e) {
                // handle exceptions
            }
            btnGenre.setText(tag.getNaam());

            btnGenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Button btn : btns){
                        btn.setEnabled(true);
                    }
                    btnFavorieten.setEnabled(true);
                    btnAlleFilms.setEnabled(true);
                    btnGenre.setEnabled(false);

                    showFilms(tag.getFilms());
                }
            });

            llGenres.addView(btnGenre);
        }
    }

    private void handleEvents() {

    }

    private void showFilms(List<Film> films){
        setTitle(films.size() + " Films");

        currentlyShown = films;

        sortCurrentlyShown();
        grdMovies.setAdapter(new MoviesGridView(this, films));

        grdMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, FilmActivity.class);
                intent.putExtra("film_id", currentlyShown.get(position).getID());
                startActivity(intent);
            }
        });
    }

    private void sortCurrentlyShown() {
        switch (currentSort){
            case 0:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getNaam().compareTo(o1.getNaam());
                        }else{
                            return o1.getNaam().compareTo(o2.getNaam());
                        }
                    }
                });
                break;
            case 1:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getReleaseDate().compareTo(o1.getReleaseDate());
                        }else{
                            return o1.getReleaseDate().compareTo(o2.getReleaseDate());
                        }
                    }
                });
                break;
            case 2:
                currentlyShown.sort(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (currentSortDesc){
                            return o2.getDuur() - o1.getDuur();
                        }else {
                            return o1.getDuur() - o2.getDuur();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                Intent intent = new Intent(HomeActivity.this, ZoekenActivity.class);
                startActivity(intent);
                break;
            case R.id.action_genres:
                Intent intent1 = new Intent(HomeActivity.this, GenresActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.action_sort:
                final DialogSorterenOp dialogSorterenOp = new DialogSorterenOp(HomeActivity.this, currentSort, currentSortDesc);

                dialogSorterenOp.setPositiveButton("Sorteer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentSort = dialogSorterenOp.getSpinnerValue();
                        currentSortDesc = dialogSorterenOp.getDescending();

                        showFilms(currentlyShown);
                    }
                }).setNegativeButton("Stop", null);

                dialogSorterenOp.show();


                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                genresDAO = FilmsDb.getDatabase(this).genresDAO();
                setGenreBar();
        }
    }
}
