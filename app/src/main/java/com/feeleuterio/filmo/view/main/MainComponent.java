package com.feeleuterio.filmo.view.main;

import com.feeleuterio.filmo.view.ActivityScope;
import com.feeleuterio.filmo.view.AppComponent;
import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = MainModule.class
)
interface MainComponent {

    void inject (MainActivity mainActivity);

}
