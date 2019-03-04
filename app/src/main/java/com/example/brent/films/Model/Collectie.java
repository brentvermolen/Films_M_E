package com.example.brent.films.Model;

import java.util.ArrayList;
import java.util.List;

public class Collectie {
    private int ID;
    private String Naam;

    private List<Film> Films;

    public Collectie(int ID, String naam) {
        this.ID = ID;
        Naam = naam;

        Films = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public String getNaam() {
        return Naam;
    }

    public List<Film> getFilms() {
        return (Films == null) ? new ArrayList<Film>() : Films;
    }

    public void setFilms(List<Film> films) {
        Films = films;
    }
}
