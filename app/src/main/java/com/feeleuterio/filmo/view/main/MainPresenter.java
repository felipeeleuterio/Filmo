package com.feeleuterio.filmo.view.main;

import android.annotation.SuppressLint;
import android.support.annotation.VisibleForTesting;
import com.feeleuterio.filmo.api.ApiService;
import com.feeleuterio.filmo.api.model.Configuration;
import com.feeleuterio.filmo.api.model.Movies;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private ApiService apiService;

    private int page = 1;
    private String query = "";
    private Configuration configuration;

    @Inject
    public MainPresenter(MainContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void start(String query) {
        view.showLoading(false);
        if(query.isEmpty())
            getMovies(true);
        else {
            page = 1;
            getSearch(true, query);
        }
        getConfiguration();
    }

    private void getConfiguration() {
        Call<Configuration> call = apiService.getConfiguration();
        call.enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if (response.isSuccessful()) {
                    view.onConfigurationSet(response.body().images);
                }
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPullToRefresh(String query) {
        page = 1;
        query = query;
        view.showLoading(true);
        if(query.isEmpty())
            getMovies(true);
        else
            getSearch(true, query);
    }

    @Override
    public void onScrollToBottom(String query) {
        page++;
        view.showLoading(true);
        if(query.isEmpty())
            getMovies(false);
        else
            getSearch(false, query);
    }

    private void getMovies(final boolean isRefresh) {
        Call<Movies> call = apiService.getMovies(page);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    view.showContent(response.body().movies, isRefresh);
                } else {
                    view.showError();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                view.showError();
            }
        });
    }

    private void getSearch(final boolean isRefresh, final String query) {
        Call<Movies> call = apiService.getSearch(query, ApiService.Adult.INCLUDE_ADULT, page);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    view.showContent(response.body().movies, isRefresh);
                } else {
                    view.showError();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                view.showError();
            }
        });
    }

    @VisibleForTesting
    public String getReleaseDate() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(cal.getTime());
    }

    @Override
    public void finish() {

    }

}
