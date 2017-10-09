package com.sumasoft.findcoffeeshop.ui.splash;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.ui.base.BaseViewModel;
import com.sumasoft.findcoffeeshop.utils.DialogUtils;

import javax.inject.Inject;

/**
 * Created by sumasoft on 05/10/17.
 */

class SplashViewModel extends BaseViewModel<SplashNavigator>   {

    //inject DialogUtils to access its property
    @Inject
    DialogUtils mDialog;

    private static final String[] INITIAL_REQUEST_LOCATION = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_PERMS_LOCATION = 1339;

    @Inject
    public SplashViewModel() {
    }

    public void checkPermission(Context mContext) {
        //check current android OS and if its M then request run time permissions
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (!canAccessLocation()) {
                //check for user has permanently denied permission or not
                if (getNavigator().shouldShowRequestPermissionRationaleLocation(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //show user alertdialog
                    AlertDialog alertDialog = mDialog.showMessageOK(mContext.getResources().getString(R.string.force_allow_location), okClickListener,mContext);
                    getNavigator().showPermissionDialog(alertDialog);
                    return;
                }

                getNavigator().requestLocationPermissions(INITIAL_PERMS_LOCATION, INITIAL_REQUEST_LOCATION);
            } else {
                decideNextActivity();
            }
        } else {
            //call next activity
            decideNextActivity();
        }

    }

    private boolean canAccessLocation() {
        //check for user can access location or not
        return (getNavigator().hasPermissionFineLocation(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,Context mContext) {
        switch (requestCode) {
            case INITIAL_PERMS_LOCATION:
                //if permission granted call next activity
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    decideNextActivity();
                } else {
                    //if permission denied then show dialog to force user to allow permission acess
                    AlertDialog alertDialog = mDialog.showMessageOK(mContext.getResources().getString(R.string.force_allow_location), okClickListener,mContext);
                    getNavigator().showPermissionDialog(alertDialog);
                }
                break;

        }
    }


    private void decideNextActivity() {

        getNavigator().openMapActivity();

    }



    //listener to listen on Ok button click
    DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            getNavigator().forceRequestLocationPermissions();
        }
    };
}
