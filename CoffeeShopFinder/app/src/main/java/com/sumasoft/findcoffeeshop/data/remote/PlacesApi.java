package com.sumasoft.findcoffeeshop.data.remote;

import com.sumasoft.findcoffeeshop.utils.Constants;
import com.sumasoft.findcoffeeshop.model.CoffeeShopResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sumasoft on 06/10/17.
 */

public interface PlacesApi {

    @GET("api/place/nearbysearch/"+Constants.RESPONSE_FORMAT+"?sensor=true&key="+Constants.PLACES_API_KEY)
    Call<CoffeeShopResponse> getPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
