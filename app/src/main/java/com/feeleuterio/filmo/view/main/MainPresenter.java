package com.feeleuterio.filmo.view.main;

import android.annotation.SuppressLint;
import android.support.annotation.VisibleForTesting;
import com.feeleuterio.filmo.api.ApiService;
import com.feeleuterio.filmo.api.model.Configuration;
import com.feeleuterio.filmo.api.model.Movies;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
        Observable<Configuration> observable = apiService.getConfiguration();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Configuration>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Configuration configuration) {
                        view.onConfigurationSet(configuration.images);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

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
        Observable<Movies> observable = apiService.getMovies(page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Movies movies) {
                        view.showContent(movies.movies, isRefresh);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getSearch(final boolean isRefresh, final String query) {
        Observable<Movies> observable = apiService.getSearch(query, ApiService.Adult.INCLUDE_ADULT, page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movies>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Movies movies) {
                        view.showContent(movies.movies, isRefresh);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError();
                    }

                    @Override
                    public void onComplete() {

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
