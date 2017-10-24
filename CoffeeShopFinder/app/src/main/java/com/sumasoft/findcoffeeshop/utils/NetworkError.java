package com.sumasoft.findcoffeeshop.utils;

/**
 * Created by sumasoft on 17/10/17.
 */

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sumasoft.findcoffeeshop.R;
import com.sumasoft.findcoffeeshop.model.CoffeeShopResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;


public class NetworkError extends Throwable {
    private final Throwable error;
    Context mContext;
    public NetworkError(Throwable e, Context context) {
        super(e);
        this.error = e;
        mContext = context;
    }

    public String getMessage() {
        return error.getMessage();
    }

    public boolean isAuthFailure() {
        return error instanceof HttpException &&
                ((HttpException) error).code() == HTTP_UNAUTHORIZED;
    }

    public boolean isResponseNull() {
        return error instanceof HttpException && ((HttpException) error).response() == null;
    }

    public String getAppErrorMessage() {
        if (this.error instanceof IOException) return mContext.getResources().getString(R.string.no_internet);
        if (!(this.error instanceof HttpException)) return mContext.getResources().getString(R.string.something_went_wrong);
        retrofit2.Response<?> response = ((HttpException) this.error).response();
        if (response != null) {
            String status = getJsonStringFromResponse(response);
            if (!TextUtils.isEmpty(status)) return status;

            Map<String, List<String>> headers = response.headers().toMultimap();
            if (headers.containsKey(mContext.getResources().getString(R.string.error_message)))
                return headers.get(mContext.getResources().getString(R.string.error_message)).get(0);
        }

        return mContext.getResources().getString(R.string.something_went_wrong);
    }

    protected String getJsonStringFromResponse(final retrofit2.Response<?> response) {
        try {
            String jsonString = response.errorBody().string();
            CoffeeShopResponse errorResponse = new Gson().fromJson(jsonString, CoffeeShopResponse.class);
            return errorResponse.status;
        } catch (Exception e) {
            return null;
        }
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkError that = (NetworkError) o;

        return error != null ? error.equals(that.error) : that.error == null;

    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }
}
