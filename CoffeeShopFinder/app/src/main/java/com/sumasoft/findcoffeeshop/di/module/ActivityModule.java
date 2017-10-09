package com.sumasoft.findcoffeeshop.di.module;

import android.app.Activity;
import android.content.Context;

import com.sumasoft.findcoffeeshop.di.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumasoft on 05/10/17.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }
}
