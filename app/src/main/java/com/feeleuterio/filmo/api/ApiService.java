package com.feeleuterio.filmo.api;

import com.feeleuterio.filmo.api.model.Configuration;
import com.feeleuterio.filmo.api.model.Movie;
import com.feeleuterio.filmo.api.model.Movies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/3/movie/upcoming")
    Call<Movies> getMovies(@Query("page") int page);

    @GET("/3/movie/{id}")
    Call<Movie> getMovie(@Path("id") int id);


    @Headers("Cache-Control: public, max-stale=2419200") // 4 weeks
    @GET("/3/configuration")
    Call<Configuration> getConfiguration();

}
