package com.feeleuterio.filmo.view;

import android.app.Application;
import com.feeleuterio.filmo.api.ApiModule;
import com.feeleuterio.filmo.api.ApiService;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ApiModule.class
        }
)
public interface AppComponent {

    Application application();

    ApiService apiService();

    void inject(App app);

}
