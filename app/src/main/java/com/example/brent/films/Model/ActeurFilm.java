package com.example.brent.films.Model;

public class ActeurFilm {
    private int ActeurID;
    private int FilmID;
    private String Karakter;
    private int Sort;

    private Acteur Acteur;
    private Film Film;

    public ActeurFilm(int acteurID, int filmID, String karakter, int sort) {
        ActeurID = acteurID;
        FilmID = filmID;
        Karakter = karakter;
        Sort = sort;
    }

    public int getActeurID() {
        return ActeurID;
    }

    public int getFilmID() {
        return FilmID;
    }

    public String getKarakter() {
        return Karakter;
    }

    public int getSort() {
        return Sort;
    }

    public Acteur getActeur() {
        return Acteur;
    }

    public Film getFilm() {
        return Film;
    }

    public void setActeur(Acteur acteur) {
        Acteur = acteur;
    }

    public void setFilm(Film film) {
        Film = film;
    }
}
