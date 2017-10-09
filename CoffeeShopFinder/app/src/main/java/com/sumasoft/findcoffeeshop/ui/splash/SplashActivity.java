package com.sumasoft.findcoffeeshop.ui.splash;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.sumasoft.findcoffeeshop.CoffeeShopFinderApplication;
import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.di.component.ActivityComponent;
import com.sumasoft.findcoffeeshop.di.component.DaggerActivityComponent;
import com.sumasoft.findcoffeeshop.di.module.ActivityModule;
import com.sumasoft.findcoffeeshop.ui.maps.MapsActivity;

import javax.inject.Inject;


public class SplashActivity extends AppCompatActivity implements SplashNavigator {

    @Inject
    SplashViewModel mViewModel;

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
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        //SplashNavigator will be set as navigator between activity and viewModel
        mViewModel.setNavigator(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //check for permission and decide next activity call
        mViewModel.checkPermission(SplashActivity.this);
    }

    @Override
    public void openMapActivity() {
        //get intent  of MapsActivity and start the activity
        Intent intent = MapsActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermissionFineLocation(String perm) {
        //check for app has given permissions or not
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    @Override
    public void showPermissionDialog(AlertDialog alertDialog) {
        //show dialog to forcefully provide permissions by navigating user to settings screen of app
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void requestLocationPermissions(final int initialPermsLocation, final String[] initialRequestLocation) {
        //request permission to allow
        requestPermissions(initialRequestLocation, initialPermsLocation);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean shouldShowRequestPermissionRationaleLocation(String permission) {
        //check for if user has denied permissions permanently
        boolean isShow = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
        return isShow;
    }

    @Override
    public void forceRequestLocationPermissions() {
        try {
            //Open the this App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        } catch ( ActivityNotFoundException e ) {
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        mViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //after permission is denied or allowed perform operation depend on that
        mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults,SplashActivity.this);
    }


}
