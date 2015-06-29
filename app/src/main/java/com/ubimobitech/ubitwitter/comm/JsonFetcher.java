/**
 * FILE: JsonFetcher.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.comm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.ubimobitech.ubitwitter.AppConstants;
import com.ubimobitech.ubitwitter.auth.TwitterSession;
import com.ubimobitech.ubitwitter.prefs.TwitterSharedPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.lang.reflect.Type;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

/**
 * Created by benakiva on 27/06/15.
 */
public class JsonFetcher<T> {
    private static final String TAG = JsonFetcher.class.getSimpleName();
    private final GsonBuilder mGsonb;
    private HttpClient mHttpClient;
    private Context mContext;
    private int mStatusCode;


    public JsonFetcher(Context context) {
        mContext = context;
        mGsonb = new GsonBuilder();
        addBuilderAdapters(mGsonb);

        mHttpClient = RequestManager.getNewHttpClient();
    }

    protected HttpUriRequest createRequest(String url) {
        return new HttpGet(url);
    }

    protected <V> V fetch(String url, Type type) {
        V result = null;

        if (CommUtils.hasInternetConnection(mContext)) {
            try {
                HttpUriRequest request = createRequest(url);
                RequestManager.addHeaders(request, mContext);

                CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                        AppConstants.TWITTER_CONSUMER_KEY,
                        AppConstants.TWITTER_CONSUMER_SECRET);

                TwitterSession session = TwitterSharedPrefs.getTwitterSession(mContext);

                if (!TextUtils.isEmpty(session.getOAuthToken())) {
                    consumer.setTokenWithSecret(session.getOAuthToken(), session.getOAuthSecret());
                    try {
                        consumer.sign(request);
                    } catch (OAuthMessageSignerException e) {
                        e.printStackTrace();
                    } catch (OAuthExpectationFailedException e) {
                        e.printStackTrace();
                    } catch (OAuthCommunicationException e) {
                        e.printStackTrace();
                    }
                }

                HttpResponse response = mHttpClient.execute(request);

                mStatusCode = response.getStatusLine().getStatusCode();

                switch (mStatusCode) {
                    case HttpStatus.SC_OK:
                        JsonReader reader = new JsonReader(
                                RequestManager.getContentStreamReader(response));
                        Gson gson = mGsonb.create();

                        result = gson.fromJson(reader, type);
                        mStatusCode = HttpStatus.SC_OK;

                        break;
                }
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalArgumentException ie) {
                Log.e(TAG, ie.getMessage());
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage() != null ? ioe.getMessage() : "Unknown IO Error");
            }
        } else {
            mStatusCode = AppConstants.NO_INTERNET_CONNECTION;
        }

        return result;
    }

    /**
     * Sub classes can override this to add different adapters
     */
    protected void addBuilderAdapters(GsonBuilder gsonb) {
    }

}
