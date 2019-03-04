package com.example.brent.films.Class;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.films.Model.Film;

import java.util.List;

@Dao
public interface FavorietenDAO {

    @Query("Delete From Film")
    void deleteAll();

    @Query("Select * From Film")
    List<Film> getAll();

    @Query("Select * From Film Where ID=(:id)")
    Film getById(int id);

    @Insert
    void insert(Film film);

    @Query("Delete From Film Where ID = (:id)")
    void deleteById(int id);
}
