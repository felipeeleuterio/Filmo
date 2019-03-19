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

    enum Adult {
        INCLUDE_ADULT("false");

        String value;

        Adult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    @GET("/3/movie/upcoming")
    Call<Movies> getMovies(@Query("page") int page);

    @GET("/3/movie/{id}")
    Call<Movie> getMovie(@Path("id") int id);

    @GET("/3/search/movie")
    Call<Movies> getSearch(@Query("query") String query, @Query("include_adult") Adult include,
                           @Query("page") int page);

    @Headers("Cache-Control: public, max-stale=2592000")
    @GET("/3/configuration")
    Call<Configuration> getConfiguration();

}
