package com.example.brent.films.Model;

import java.util.ArrayList;
import java.util.List;

public class Acteur {
    private int ID;
    private String Naam;
    private List<ActeurFilm> Films;

    public Acteur(int ID, String naam) {
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

    public List<ActeurFilm> getFilms() {
        return (Films == null) ? new ArrayList<ActeurFilm>() : Films;
    }

    public void setFilms(List<ActeurFilm> films) {
        Films = films;
    }
}
