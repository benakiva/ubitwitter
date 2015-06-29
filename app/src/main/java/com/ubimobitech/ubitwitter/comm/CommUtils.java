/**
 * FILE: CommUtils.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.comm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by benakiva on 27/06/15.
 */
public class CommUtils {
    private static final String TAG = CommUtils.class.getSimpleName();

    /**
     * Check if the device has any network connection
     * @param context
     * @return true or false
     */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.i(TAG, "Internet Connection Unavailable");

            return false;
        }
    }
}
