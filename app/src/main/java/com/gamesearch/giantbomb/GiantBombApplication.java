package com.gamesearch.giantbomb;


import android.app.Application;

import dagger.APIComponent;
import dagger.APIModule;
import dagger.DaggerAPIComponent;

public class GiantBombApplication extends Application {

    private APIComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        apiComponent = createMyComponent();
    }

    APIComponent getMyComponent() {
        return apiComponent;
    }

    private APIComponent createMyComponent() {
        return DaggerAPIComponent.builder().aPIModule(new APIModule()).build();
    }

}
