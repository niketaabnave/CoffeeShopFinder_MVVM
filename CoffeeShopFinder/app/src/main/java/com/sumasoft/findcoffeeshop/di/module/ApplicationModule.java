package com.sumasoft.findcoffeeshop.di.module;

import android.app.Application;
import android.content.Context;

import com.sumasoft.findcoffeeshop.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumasoft on 05/10/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }


}
