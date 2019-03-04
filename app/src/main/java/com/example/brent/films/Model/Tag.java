package com.example.brent.films.Model;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    private int ID;
    private String Naam;
    private int Count;

    private List<FilmTags> Films;

    public Tag(int ID, String naam, int count) {
        this.ID = ID;
        Naam = naam;
        Count = count;

        Films = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public String getNaam() {
        return Naam;
    }

    public int getCount() {
        return Count;
    }

    public List<Film> getFilms() {
        if (Films == null){
            new ArrayList<Film>();
        }

        List<Film> films = new ArrayList<>();
        for (FilmTags ft : Films){
            films.add(ft.getFilm());
        }

        return films;
    }

    public void setFilms(List<FilmTags> films) {
        Films = films;
    }

    public List<FilmTags> getFilmTags() {
        return (Films == null) ? new ArrayList<FilmTags>() : Films;
    }
}
