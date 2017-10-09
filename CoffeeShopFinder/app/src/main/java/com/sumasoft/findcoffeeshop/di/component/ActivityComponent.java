package com.sumasoft.findcoffeeshop.di.component;

import com.sumasoft.findcoffeeshop.di.PerActivity;
import com.sumasoft.findcoffeeshop.di.module.ActivityModule;
import com.sumasoft.findcoffeeshop.ui.maps.MapsActivity;
import com.sumasoft.findcoffeeshop.ui.splash.SplashActivity;

import dagger.Component;

/**
 * Created by sumasoft on 05/10/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);
    void inject(MapsActivity mapsActivity);

}
