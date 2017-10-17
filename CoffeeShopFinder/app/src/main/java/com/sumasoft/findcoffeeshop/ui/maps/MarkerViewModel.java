package com.sumasoft.findcoffeeshop.ui.maps;

import android.app.Dialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.model.CoffeeShopResponse;
import com.sumasoft.findcoffeeshop.model.Result;
import com.sumasoft.findcoffeeshop.utils.Constants;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;
/**
 * Created by sumasoft on 17/10/17.
 */

public class MarkerViewModel {
    @Inject
    public MarkerViewModel(){

    }
    public Marker addMarker(LatLng latLng, BitmapDescriptor bitmapDescriptor, String title, GoogleMap mGoogleMap) {
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
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(Constants.CAMEARA_ZOOM));
            return marker;
        }catch (Exception e){
            e.printStackTrace();
        }
        return marker;
    }
    public Result getMarkerResponse(Marker marker, Response<CoffeeShopResponse> mResponse) {
        try {
            String tag = "";
            if (marker.getTag() != null) {
                tag = marker.getTag().toString();
            }
            if (mResponse != null) {
                for (int i = 0; i < mResponse.body().getResults().size(); i++) {
                    if (mResponse.body().getResults().get(i).getId().equals(tag) || mResponse.body().getResults().get(i).getId().equals(i)) {
                        return mResponse.body().getResults().get(i);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
