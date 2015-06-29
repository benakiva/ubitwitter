/**
 * FILE: PostTweet.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.comm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ubimobitech.ubitwitter.AppConstants;
import com.ubimobitech.ubitwitter.auth.TwitterSession;
import com.ubimobitech.ubitwitter.prefs.TwitterSharedPrefs;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.net.URLEncoder;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

/**
 * Created by benakiva on 27/06/15.
 */
public class PostTweet {
    private static final String TAG = PostTweet.class.getSimpleName();

    private HttpClient mHttpClient;
    private Context mContext;
    private int mStatusCode;

    public PostTweet(Context context) {
        mContext = context;
        mHttpClient = RequestManager.getNewHttpClient();
    }

    public int postStatus(String message) {
        if (CommUtils.hasInternetConnection(mContext)) {
            StringBuilder url = new StringBuilder();
            url.append(AppConstants.TWITTER_POSTSTATUS_URL);
            url.append("?status=");
            url.append(URLEncoder.encode(message));

            try {
                HttpUriRequest request = createRequest(url.toString());
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

                return response.getStatusLine().getStatusCode();
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage());
            } catch (IllegalArgumentException ie) {
                Log.e(TAG, ie.getMessage());
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage() != null ? ioe.getMessage() : "Unknown IO Error");
            }
        }

        return -1;
    }

    protected HttpUriRequest createRequest(String url) {
        return new HttpPost(url);
    }
}
