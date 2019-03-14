package com.feeleuterio.filmo.view.detail;

import com.feeleuterio.filmo.view.ActivityScope;
import com.feeleuterio.filmo.view.AppComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = DetailModule.class
)
interface DetailComponent {

    void inject(DetailActivity DetailActivity);

}
