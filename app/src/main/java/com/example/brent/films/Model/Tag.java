package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {
    @PrimaryKey
    private int ID;
    private String Naam;
    private int Count;

    @Ignore
    private List<FilmTags> Films;

    public Tag(){}

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

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public boolean equals(Object obj) {
        try{
            Tag t = (Tag)obj;

            if (t.getID() == this.getID()){
                return true;
            }
        }catch (Exception e){}

        return false;
    }
}
