package com.sumasoft.findcoffeeshop.di.component;

import com.sumasoft.findcoffeeshop.data.remote.PlacesApi;
import com.sumasoft.findcoffeeshop.di.module.ApiModule;
import com.sumasoft.findcoffeeshop.di.module.ApplicationModule;
import com.sumasoft.findcoffeeshop.ui.maps.MapsActivity;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by sumasoft on 05/10/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {
    PlacesApi providePlacesApi();

    void inject(MapsActivity target);

}