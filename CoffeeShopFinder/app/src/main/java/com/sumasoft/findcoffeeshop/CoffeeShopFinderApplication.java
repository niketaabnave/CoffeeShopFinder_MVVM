package com.sumasoft.findcoffeeshop;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.sumasoft.findcoffeeshop.di.component.ApplicationComponent;
import com.sumasoft.findcoffeeshop.di.component.DaggerApplicationComponent;
import com.sumasoft.findcoffeeshop.di.module.ApplicationModule;


/**
 * Created by sumasoft on 04/10/17.
 */

public class CoffeeShopFinderApplication extends MultiDexApplication{


    ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }

    public ApplicationComponent getAppComponent(){
        return appComponent;
    }

    protected ApplicationComponent initDagger(CoffeeShopFinderApplication application){
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }
}