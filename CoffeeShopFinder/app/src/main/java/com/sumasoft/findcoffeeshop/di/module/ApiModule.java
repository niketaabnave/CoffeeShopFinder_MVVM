package com.sumasoft.findcoffeeshop.di.module;

import com.sumasoft.findcoffeeshop.utils.Constants;
import com.sumasoft.findcoffeeshop.data.remote.PlacesApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;


import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumasoft on 06/10/17.
 */
@Module
public class ApiModule {
    @Provides
    Converter.Factory provideConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return client;
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient client, Converter.Factory converter) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.PLACES_SEARCH_URL)
                .client(client)
                .addConverterFactory(converter)
                .build();

        return retrofit;
    }

    @Provides
    PlacesApi providePlacesApi(Retrofit retrofit){
        PlacesApi placesApi = retrofit.create(PlacesApi.class);
        return placesApi;
    }
}
