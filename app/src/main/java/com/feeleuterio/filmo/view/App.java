package com.feeleuterio.filmo.view;

import android.app.Application;

import com.feeleuterio.filmo.BuildConfig;
import com.feeleuterio.filmo.R;
import com.feeleuterio.filmo.api.ApiModule;

public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule(getString(R.string.base_url), BuildConfig.TMDB_API_KEY))
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent(Application app) {
        return ((App) app).getAppComponent();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
