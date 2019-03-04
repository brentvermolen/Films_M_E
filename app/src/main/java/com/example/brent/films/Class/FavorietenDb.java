package com.example.brent.films.Class;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.brent.films.Model.Film;

import java.util.concurrent.Executors;

@Database(entities =  {Film.class }, version = 1)
public abstract class FavorietenDb extends RoomDatabase {
    private static FavorietenDb INSTANCE;
    private static final String DB_NAME = "favorits.db";

    public abstract FavorietenDAO favorietenDAO();

    public static FavorietenDb getDatabase (final Context context){
        if (INSTANCE == null) {
            synchronized (FavorietenDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavorietenDb.class, DB_NAME)
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

        public EmptyAndFillDbAsync(FavorietenDb instance) {
            favorietenDao = instance.favorietenDAO();
        }

        protected Void insertData() {
            favorietenDao.deleteAll();

            return null;
        }
    }
}
