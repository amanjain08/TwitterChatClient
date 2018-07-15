package com.juggad.twitterchatclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.internal.Util;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class Utils {

    private static final String TAG = Util.class.getSimpleName();

    public static String toCamelCase(String s) {
        if (s.length() == 0) {
            return s;
        }
        String[] parts = s.split(" ");
        StringBuilder camelCaseString = new StringBuilder();

        for (String part : parts) {
            if (!TextUtils.isEmpty(part)) {
                camelCaseString.append(toProperCase(part)).append(" ");
            }
        }
        return camelCaseString.toString();
    }

    public static String toProperCase(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    public static boolean isInternetConnected(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e(TAG, "isInternetConnected: ", e);
            e.printStackTrace();
            return false;
        }
    }

}
