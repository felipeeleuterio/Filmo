package com.feeleuterio.filmo.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.feeleuterio.filmo.api.ApiService;
import com.feeleuterio.filmo.api.model.Movie;
import com.feeleuterio.filmo.dto.TMDbResponse;
import com.feeleuterio.filmo.dto.Resource;
import com.feeleuterio.filmo.room.MovieDao;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import retrofit2.Response;

public class MovieRepository {
    final private ApiService api;
    final private MovieDao dao;
    final private Executor executor;

    @Inject
    public MovieRepository(ApiService api, MovieDao dao, Executor executor) {
        this.api = api;
        this.dao = dao;
        this.executor = executor;
    }

    public LiveData<Resource<TMDbResponse>> browseRepo(final int page, final int limit) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                refresh(page, limit);
            }
        });
        int offset = (page - 1) * limit;
        final LiveData<List<Movie>> source = dao.get(offset, limit);

        final MediatorLiveData mediator = new MediatorLiveData();
        mediator.addSource(source, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d("DATA", "Observed: " + page + " , " + limit);
                TMDbResponse resp = new TMDbResponse(page, movies);
                Resource<TMDbResponse> success = Resource.<TMDbResponse>success(resp);
                mediator.setValue(success);
            }
        });

        return mediator;
    }

    @WorkerThread
    private void refresh(final int page, final int limit) {

        try {
            int offset = (page - 1) * limit;
            List<Movie> list = dao.hasData(offset, limit);
            if (list != null && !list.isEmpty()) {
                Log.d("DATA", "From cache");
                return;
            }
            Log.d("DATA", "Fetching from server: " + page + " , " + limit);
            Response<Movie> response = api.getMovie(page).execute();
            List<Movie> body = (List<Movie>) response.body();
            if (body != null) {
                Movie[] arr = new Movie[body.size()];
                body.toArray(arr);

                long[] ids = dao.insertAll(arr);
                if (ids == null || ids.length != arr.length) {
                    Log.e("API", "Unable to insert");
                } else {
                    Log.d("DATA", "Data inserted");
                }
            }
        } catch (IOException e) {
            Log.e("API", "" + e.getMessage());
        }
    }

    public void clearCache() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }
}
