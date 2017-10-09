package com.sumasoft.findcoffeeshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.ui.maps.MapsActivity;

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
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create();

    }

    public AlertDialog showMessageYesNo(String message, DialogInterface.OnClickListener btnListener, Context mContext){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", btnListener)
                .setNegativeButton("No", btnListener);
        final AlertDialog alert = builder.create();
        return  alert;
    }

}
