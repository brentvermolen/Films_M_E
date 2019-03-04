package com.example.brent.films.Model;

public class FilmTags {
    private int Film_ID;
    private int Tag_ID;

    private Film Film;
    private Tag Tag;

    public FilmTags(int film_ID, int tag_ID) {
        Film_ID = film_ID;
        Tag_ID = tag_ID;
    }

    public int getFilm_ID() {
        return Film_ID;
    }

    public int getTag_ID() {
        return Tag_ID;
    }

    public Film getFilm() {
        return Film;
    }

    public Tag getTag() {
        return Tag;
    }

    public void setFilm(Film film) {
        Film = film;
    }

    public void setTag(Tag tag) {
        Tag = tag;
    }
}
