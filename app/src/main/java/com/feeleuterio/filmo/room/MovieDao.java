package com.feeleuterio.filmo.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.feeleuterio.filmo.api.model.Movie;
import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Movie... movies);

    @Query("SELECT * FROM movie LIMIT :limit OFFSET :offset")
    LiveData<List<Movie>> get(int offset, int limit);

    @Query("DELETE FROM movie WHERE id=:id")
    void delete(int id);

    @Query("SELECT * FROM movie LIMIT :limit OFFSET :offset")
    List<Movie> hasData(int offset, int limit);

    @Query("DELETE FROM movie")
    void deleteAll();

}
