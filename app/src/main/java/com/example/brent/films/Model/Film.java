package com.example.brent.films.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.brent.films.Class.TimeStampConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
public class Film {
    @Ignore
    private String ns = null;

    public Film(){}

    public Film(int ID, String Naam, Date releaseDate, String tagline, int duur, String omschrijving, String trailerId, int CollectieID) {
        this.ID = ID;
        this.Naam = Naam;
        this.ReleaseDate = releaseDate;
        this.Tagline = tagline;
        this.Duur = duur;
        this.Omschrijving = omschrijving;
        this.TrailerId = trailerId;
        this.CollectieID = CollectieID;

        Acteurs = new ArrayList<>();
        Genres = new ArrayList<>();
    }

    @PrimaryKey
    private int ID;
    private String Naam;
    @TypeConverters(TimeStampConverter.class)
    private Date ReleaseDate;
    private String Tagline;
    private int Duur;
    private String Omschrijving;
    private String TrailerId;
    private int CollectieID;

    @Ignore
    private List<ActeurFilm> Acteurs;
    @Ignore
    private List<FilmTags> Genres;

    @Ignore
    private Collectie Collectie;

    public int getID() {
        return ID;
    }

    public String getNaam() {
        return Naam;
    }

    public Date getReleaseDate() {
        return ReleaseDate;
    }

    public String getTagline() {
        return Tagline;
    }

    public int getDuur() {
        return Duur;
    }

    public String getOmschrijving() {
        return Omschrijving;
    }

    public String getTrailerId() {
        return TrailerId;
    }

    public int getCollectieID() {
        return CollectieID;
    }

    public List<ActeurFilm> getActeurs() {
        return (Acteurs == null) ? new ArrayList<ActeurFilm>() : Acteurs;
    }

    public List<FilmTags> getFilmTags() {
        return (Genres == null) ? new ArrayList<FilmTags>() : Genres;
    }

    public Collectie getCollectie() {
        return Collectie;
    }

    public void setActeurs(List<ActeurFilm> acteurs) {
        Acteurs = acteurs;
    }

    public void setGenres(List<FilmTags> genres) {
        Genres = genres;
    }

    public void setCollectie(com.example.brent.films.Model.Collectie collectie) {
        Collectie = collectie;
    }

    public List<Tag> getGenres(){
        if (Genres == null){
            return new ArrayList<>();
        }

        List<Tag> genres = new ArrayList<>();
        for (FilmTags ft : Genres){
            genres.add(ft.getTag());
        }

        genres.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getNaam().compareTo(o2.getNaam());
            }
        });

        return genres;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }

    public void setReleaseDate(Date releaseDate) {
        ReleaseDate = releaseDate;
    }

    public void setTagline(String tagline) {
        Tagline = tagline;
    }

    public void setDuur(int duur) {
        Duur = duur;
    }

    public void setOmschrijving(String omschrijving) {
        Omschrijving = omschrijving;
    }

    public void setTrailerId(String trailerId) {
        TrailerId = trailerId;
    }

    public void setCollectieID(int collectieID) {
        CollectieID = collectieID;
    }
}
