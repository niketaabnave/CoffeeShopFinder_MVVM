package com.sumasoft.findcoffeeshop.ui.maps;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.data.remote.PlacesApi;
import com.sumasoft.findcoffeeshop.model.CoffeeShopResponse;
import com.sumasoft.findcoffeeshop.ui.base.BaseViewModel;
import com.google.android.gms.location.LocationServices;
import com.sumasoft.findcoffeeshop.utils.Constants;
import com.sumasoft.findcoffeeshop.utils.DialogUtils;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumasoft on 05/10/17.
 */

public class MapViewModel extends BaseViewModel<MapNavigator> {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private BitmapDescriptor mCurrentbitmapDescriptor;
    private Marker mCurrLocationMarker;
    GoogleMap mGoogleMap;
    private Response<CoffeeShopResponse> mResponse;

    //inject DialogUtils to access its property
    @Inject
    DialogUtils mDialogUtils;

    //inject PlacesApi to access its property
    @Inject
    PlacesApi placesApi;


    //check for gps is enabled or not. If not enabled then redirect to settings of device to enable location manually
    public void isGpsEnabled(Context mContext, final Activity activity) {
        try {
            final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog alertDialog = mDialogUtils.showMessageYesNo(mContext.getResources().getString(R.string.enable_gps), btnClickListener,mContext);
                getNavigator().showEnableGPS(alertDialog);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    DialogInterface.OnClickListener btnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int id) {
            if(id == -1){
                //'Yes' button is clicked then open settings of device
                getNavigator().openSettingForLocationEnable();
            }else{
                //'No' button is clicked close  MapsActivity
                getNavigator().closeActivity();
            }
        }
    };


    //check for google play service is available on device
    public boolean isGooglePlayServicesAvailable(Context mContext, Activity activity) {
        try {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(mContext);
            if (result != ConnectionResult.SUCCESS) {
                if (googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog(activity, result,
                            0).show();
                }
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }



    public void onMapReady(GoogleMap googleMap, Context mContext) {
        //used fused location API to get location(latitude and longitude) of user

        mGoogleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient(mContext);
                    googleMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient(mContext);
                googleMap.setMyLocationEnabled(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Inject
    public MapViewModel() {
    }

    protected synchronized void buildGoogleApiClient(Context mContext) {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(getNavigator())
                .addOnConnectionFailedListener(getNavigator())
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    public void onConnected(Bundle bundle,Context mContext) {
        //request location updated for given interval
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.LOCATION_API_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.LOCATION_API_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, getNavigator());
        }
    }

    public void onConnectionSuspended(int i) {

    }

    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void onLocationChanged(Location location,Context mContext) {
        mLastLocation = location;

        //current latitude and longitude
        LatLng latLng = getCurrentLatLong(location);

        //get Bitmapdescriptor from vector resource
        mCurrentbitmapDescriptor = bitmapDescriptorFromVector(mContext, R.drawable.ic_person_pin_circle_blue_24dp);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //add current location marker
        mCurrLocationMarker = addMarker(latLng,mCurrentbitmapDescriptor,"Current Location");

        //find coffee shops of near by location
        getCoffeeShopsNearMe(latLng,mContext);

        //listener for marker clicked is set
        mGoogleMap.setOnMarkerClickListener(getNavigator());
    }

    private void getCoffeeShopsNearMe(LatLng latLng, final Context mContext) {
        //call to google's places API for getting nearby coffee shops within provided radius
        placesApi.getPlaces(Constants.TYPES, latLng.latitude + "," + latLng.longitude, Constants.RADIUS)
                .enqueue(new Callback<CoffeeShopResponse>() {
                    @Override
                    public void onResponse(Call<CoffeeShopResponse> call, Response<CoffeeShopResponse> response) {
                        //handle the response got from API
                        handleCoffeeShopResponse(response,mContext);
                    }

                    @Override
                    public void onFailure(Call<CoffeeShopResponse> call, Throwable t) {

                    }
                });
    }

    private LatLng getCurrentLatLong(Location location) {
        //get current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        return latLng;
    }


    public boolean onMarkerClick(Marker marker,Context mContext) {
        //show required information on click of any marker
        try {
            String tag = "";
            if (marker.getTag() != null) {
                tag = marker.getTag().toString();
            }
            if (mResponse != null) {
                for (int i = 0; i < mResponse.body().getResults().size(); i++) {
                    if (mResponse.body().getResults().get(i).getId().equals(tag) || mResponse.body().getResults().get(i).getId().equals(i)) {
                        Dialog dialog = mDialogUtils.getDialog(mContext,R.layout.popup_map_info_window,mContext.getResources().getString(R.string.info));
                        getNavigator().showInfoWindow(mResponse.body().getResults().get(i),dialog);
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private Marker addMarker(LatLng latLng, BitmapDescriptor bitmapDescriptor,String title) {
        Marker marker = null;
        try {
            MarkerOptions markerOptions = new MarkerOptions();
            //position of marker on map
            markerOptions.position(latLng);
            //title to marker
            markerOptions.title(title);

            // Adding icon to the marker
            markerOptions.icon(bitmapDescriptor);

            // Adding Marker to the Map
            marker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            return marker;
        }catch (Exception e){
            e.printStackTrace();
        }
        return marker;
    }

    //create bitmap descriptor from vector resource
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void handleCoffeeShopResponse(Response<CoffeeShopResponse> response, Context mContext) {
        try {
            mGoogleMap.clear();

            if(mCurrLocationMarker != null) {
                //current latitude and longitude
                LatLng mlatLng = getCurrentLatLong(mLastLocation);
                //add current location marker
                mCurrLocationMarker = addMarker(mlatLng, mCurrentbitmapDescriptor, mContext.getResources().getString(R.string.current_location));
            }

            mResponse = response;
            // This loop will go through all the results and add marker on each location.
            for (int i = 0; i < response.body().getResults().size(); i++) {
                Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                LatLng latLng = new LatLng(lat, lng);

                //get bitmap descriptor from vector resource
                BitmapDescriptor bitmapDescriptor  = bitmapDescriptorFromVector(mContext, R.drawable.ic_map_cafe_brown_24dp);
                Marker marker = addMarker(latLng,bitmapDescriptor,response.body().getResults().get(i).getName());
                try {
                    if(marker != null) {
                        //set tag to marker to identify in onclick listener
                        if (response.body().getResults().get(i).getId() == null) {
                            marker.setTag(i);
                        } else {
                            marker.setTag(response.body().getResults().get(i).getId());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
