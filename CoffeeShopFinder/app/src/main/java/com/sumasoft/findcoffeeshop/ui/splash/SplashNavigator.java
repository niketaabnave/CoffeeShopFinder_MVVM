package com.sumasoft.findcoffeeshop.ui.splash;

import android.support.v7.app.AlertDialog;

/**
 * Created by sumasoft on 05/10/17.
 */

//this interface is used to provide navigation between splashActivty and SplashViewModel

interface SplashNavigator {
    void openMapActivity();
    boolean hasPermissionFineLocation(String perm);
    void showPermissionDialog(AlertDialog alertDialog);

    void requestLocationPermissions(int initialPermsLocation, String[] initialRequestLocation);

    boolean shouldShowRequestPermissionRationaleLocation(String permission);

    void forceRequestLocationPermissions();
}
