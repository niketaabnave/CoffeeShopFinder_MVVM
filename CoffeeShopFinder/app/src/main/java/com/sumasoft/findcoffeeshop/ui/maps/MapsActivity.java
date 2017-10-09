package com.sumasoft.findcoffeeshop.ui.maps;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.sumasoft.findcoffeeshop.CoffeeShopFinderApplication;
import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.di.component.ActivityComponent;
import com.sumasoft.findcoffeeshop.di.component.DaggerActivityComponent;
import com.sumasoft.findcoffeeshop.di.module.ActivityModule;
import com.sumasoft.findcoffeeshop.model.Result;

import javax.inject.Inject;


public class MapsActivity extends FragmentActivity implements MapNavigator {

    //inject MapViewModel to access its properties
    @Inject
    MapViewModel mViewModel;

    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(((CoffeeShopFinderApplication) getApplication()).getAppComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getActivityComponent().inject(this);

        //MapNavigator will be set as navigator between activity and viewModel
        mViewModel.setNavigator(this);

        //show error dialog if Google Play Services not available
        mViewModel.isGooglePlayServicesAvailable(MapsActivity.this, MapsActivity.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public  void  onResume(){
        super.onResume();

        //checked for gps is enabled or not
        mViewModel.isGpsEnabled(MapsActivity.this, MapsActivity.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mViewModel.onMapReady(googleMap,MapsActivity.this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mViewModel.onConnected(bundle,MapsActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mViewModel.onConnectionSuspended(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mViewModel.onConnectionFailed(connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        mViewModel.onLocationChanged(location,MapsActivity.this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return mViewModel.onMarkerClick(marker,MapsActivity.this);
    }


    @Override
    public void showInfoWindow(Result result,Dialog dialog) {
        //name,address,shop open status will be shown in this dialog

        if(result != null) {
            try {
                boolean isOpenNow = false;
                String name = result.getName();
                String vicinity = result.getVicinity();
                if (result.getOpeningHours() != null) {
                    isOpenNow = result.getOpeningHours().getOpenNow();
                }
                double rating = result.getRating();

                TextView txtName = dialog.findViewById(R.id.txtName);
                TextView txtVicinity = dialog.findViewById(R.id.txtVicinity);
                TextView txtIsOpen = dialog.findViewById(R.id.txtIsOpen);
                TextView txtRating = dialog.findViewById(R.id.txtRating);
                RatingBar ratingbar = dialog.findViewById(R.id.rating);

                //set name of coffee shop
                if (name != null) {
                    txtName.setText(name);
                }
                //set address of coffee shop
                if (vicinity != null) {
                    txtVicinity.setText(vicinity);
                }
                //set open status of coffe shop
                if (isOpenNow) {
                    txtIsOpen.setText(getResources().getString(R.string.open_yes));
                } else {
                    txtIsOpen.setText(getResources().getString(R.string.open_no));
                }
                if (result.getOpeningHours() == null) {
                    txtIsOpen.setVisibility(View.GONE);
                }
                ratingbar.setRating((float) rating);
                txtRating.setText(String.valueOf(rating));
                //show the dialog
                dialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showEnableGPS(AlertDialog alertDialog) {
        //show enable GPS of device dialog
        alertDialog.show();
    }

    @Override
    public void openSettingForLocationEnable() {
        //call to open Location  enable setting
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void closeActivity() {
        //finish this activity
        finish();
    }


    public static Intent getStartIntent(Context context) {
        //returns  intent of this activity
        Intent intent = new Intent(context, MapsActivity.class);
        return intent;
    }
}
