package com.example.brent.films.Class;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;

import java.util.List;

@Dao
public interface GenresDAO {

    @Query("Delete From Tag")
    void deleteAll();

    @Query("Select * From Tag")
    List<Tag> getAll();

    @Query("Select * From Tag Where ID = (:id)")
    Tag getById(int id);

    @Query("Select * From Tag Where ID >= 100")
    List<Tag> getOwnTags();

    @Query("Select * From Tag Where ID < 100")
    List<Tag> getExistingTags();

    @Insert
    void insert(Tag tag);

    @Query("Delete From Tag Where ID = (:id)")
    void deleteById(int id);
}
