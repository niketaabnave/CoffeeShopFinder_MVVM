package com.sumasoft.findcoffeeshop.ui.maps;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sumasoft.findcoffeeshop.model.CoffeeShopResponse;
import com.sumasoft.findcoffeeshop.model.Result;

import retrofit2.Call;

/**
 * Created by sumasoft on 05/10/17.
 */

//this interface is used to provide navigation between MapsActivity and MapViewModel

public interface MapNavigator extends OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,GoogleMap.OnMarkerClickListener  {
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    void onMapReady(GoogleMap googleMap);

    @Override
    void onConnected(@Nullable Bundle bundle);

    @Override
    void onConnectionSuspended(int i);

    @Override
    void onConnectionFailed(@NonNull ConnectionResult connectionResult);

    @Override
    void onLocationChanged(Location location);

    @Override
    boolean onMarkerClick(Marker marker);


    void showInfoWindow(Result result,Dialog dialog);

    void showEnableGPS(AlertDialog alertDialog);

    void openSettingForLocationEnable();

    void closeActivity();

    void onPlacesApiFailure(Throwable t);

    void showInternetError(AlertDialog alertDialog);
}
