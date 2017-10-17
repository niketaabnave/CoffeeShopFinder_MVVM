package com.sumasoft.findcoffeeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.sumasoft.findcoffeeshop.R;

import javax.inject.Inject;

/**
 * Created by sumasoft on 06/10/17.
 */

public class DialogUtils {
    @Inject
    public DialogUtils(){

    }

    public Dialog getDialog(Context mContext,int resourceId,String title){
// Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.setContentView(resourceId);

        dialog.setCancelable(true);
        // Set dialog title
        dialog.setTitle(title);

        return dialog;
    }

    public AlertDialog showMessageOK(String message, DialogInterface.OnClickListener okListener, Context mContext) {
        return new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton(mContext.getResources().getString(R.string.ok), okListener)
                .setCancelable(false)
                .create();

    }

    public AlertDialog showMessageYesNo(String message, DialogInterface.OnClickListener btnListener, Context mContext){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.yes), btnListener)
                .setNegativeButton(mContext.getResources().getString(R.string.no), btnListener);
        final AlertDialog alert = builder.create();
        return  alert;
    }

    public  boolean isInternetPresent(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

}
