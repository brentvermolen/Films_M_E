package com.example.brent.films.Class;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.brent.films.Model.Film;
import com.example.brent.films.Model.Tag;

import java.util.concurrent.Executors;

@Database(entities =  {Film.class, Tag.class }, version = 1)
public abstract class FilmsDb extends RoomDatabase {
    private static FilmsDb INSTANCE;
    private static final String DB_NAME = "Films.db";

    public abstract FavorietenDAO favorietenDAO();
    public abstract GenresDAO genresDAO();

    public static FilmsDb getDatabase (final Context context){
        if (INSTANCE == null) {
            synchronized (FilmsDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FilmsDb.class, DB_NAME)
                            .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            new EmptyAndFillDbAsync(INSTANCE).insertData();
                                        }
                                    });

                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    /*public void clearDb(){
        if (INSTANCE != null){
            new EmptyAndFillDbAsync(INSTANCE).execute();
        }
    }*/

    private static class EmptyAndFillDbAsync {
        private final FavorietenDAO favorietenDao;
        private final GenresDAO genresDAO;

        public EmptyAndFillDbAsync(FilmsDb instance) {
            favorietenDao = instance.favorietenDAO();
            genresDAO = instance.genresDAO();
        }

        protected Void insertData() {
            favorietenDao.deleteAll();
            genresDAO.deleteAll();

            return null;
        }
    }
}
