package com.sumasoft.findcoffeeshop.ui.base;


/**
 * Created by sumasoft on 05/10/17.
 */

public class BaseViewModel<N> {

    private N mNavigator;

    public void setNavigator(N navigator) {
        this.mNavigator = navigator;
    }

    public N getNavigator() {
        //will return reference of interface
        return mNavigator;
    }

    public void onActivityDestroyed() {
        mNavigator = null;
    }

}
