package com.feeleuterio.filmo.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.feeleuterio.filmo.api.model.Movie;

@Database(entities = {Movie.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract MovieDao movieDao();
}
