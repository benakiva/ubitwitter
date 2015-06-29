/**
 * FILE: RequestManager.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.comm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.provider.SyncStateContract;

import com.ubimobitech.ubitwitter.AppConstants;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * Created by benakiva on 27/06/15.
 */
public class RequestManager {
    private static SchemeRegistry mRegistry;
    private static HttpClient mGlobalClient;

    /**
     * Creates the connection scheme.
     * @return SchemeRegistry
     */
    private static SchemeRegistry getRegistry() {
        if (mRegistry == null) {
            mRegistry = new SchemeRegistry();
            mRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            mRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        }

        return mRegistry;
    }

    /**
     * Default constructor. Instantiates a new HttpClient if one doesn't already exist.
     * @return HttpClient
     */
    public static HttpClient getNewHttpClient() {
        if (mGlobalClient == null) {
            mGlobalClient = getNewHttpClient(AppConstants.DEFAULT_HTTP_CONNECTION_TIMEOUT,
                    AppConstants.DEFAULT_HTTP_SOCKET_TIMEOUT);
        }

        return mGlobalClient;
    }

    /**
     * Class constructor. Instantiates a thread safe HttpClient.
     * @param connectionTimeout
     * @param socketTimeout
     * @return HttpClient
     */
    public static HttpClient getNewHttpClient(int connectionTimeout, int socketTimeout) {
        HttpParams params = new BasicHttpParams();
        ClientConnectionManager mgr = new ThreadSafeClientConnManager(params, getRegistry());

        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);

        return new DefaultHttpClient(mgr, params);
    }

    /**
     * Adds pre-defined header to HttpRequest
     * @param request, HttpRequest
     */
    public static void addHeaders(HttpRequest request, Context context) {
        request.addHeader("Accept", "application/json");
        request.addHeader("Accept-Encoding", "gzip");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("X-Connected-By", getConnectedBy(context));
    }

    /**
     * Creates an inputstream from the HttpResponse.
     * @param response, HttpResponse received from remote server
     * @return InputStreamReader
     * @throws IllegalStateException
     * @throws IOException
     */
    public static InputStreamReader getContentStreamReader(HttpResponse response)
            throws IllegalStateException, IOException {
        Header encoding = response.getLastHeader("Content-Encoding");

        if (encoding != null && encoding.getValue() != null
                && encoding.getValue().equals("gzip")) {
            return new InputStreamReader(new GZIPInputStream(response.getEntity().getContent()), "UTF-8");
        } else {
            return new InputStreamReader(response.getEntity().getContent(), "UTF-8");
        }
    }

    /**
     * Determines the connection type used to connect to the server
     * @return String, containing the connection type such as WiFi, 3G etc
     */
    private static String getConnectedBy(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null) {
            return cm.getActiveNetworkInfo().getTypeName();
        } else {
            return "Unknown";
        }
    }
}
