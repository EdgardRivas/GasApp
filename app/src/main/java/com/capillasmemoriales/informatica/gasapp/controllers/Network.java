package com.capillasmemoriales.informatica.gasapp.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static ConnectivityManager manager;
    public static final int DURATION = 2000;
    private static final String IP = "https://sisadmov.laauxiliadora.com/";
    public static final String LOGIN = IP + "loginGas.php";
    public static final String GET_PERIOD = IP + "getPeriod.php";
    public static final String GET_DETAILS = IP + "getDetails.php";
    public static final String ADD_DETAIL = IP + "addDetail.php";
    public static final String EDIT_DETAIL = IP + "editDetail.php";

    /** Alternative URL's **/
    /*
    public static final String GET_DETAILS =  "http://14.14.1.7:8080/crm_android/getDetails.php";
    public static final String GET_PERIOD = "http://14.14.1.7:8080/crm_android/getPeriod.php";
    public static final String EDIT_DETAIL = "http://14.14.1.7:8080/crm_android/editDetail.php";
    */

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
/*
    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isConnectedMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
*/
}
