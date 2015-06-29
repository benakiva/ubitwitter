/**
 * FILE: AppConstants.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter;

/**
 * Created by benakiva on 27/06/15.
 */
public class AppConstants {
    public static final String TWITTER_CONSUMER_KEY = "P7EsZPfcipUqkge24QkaheL5B";
    public static final String TWITTER_CONSUMER_SECRET =
            "hGqtsr6ZKiE7RwOkiRXdSKrsCEXztxEwaVItJVxyPewV9TnWrz";

    public static final int TWITTER_MAX_TWEETS_PER_SEARCH_TERM = 20;

    /**
     * Twitter authorisation endpoints
     */
    public static final String TWITTER_REQUEST_TOKEN_URL =
                            "https://api.twitter.com/oauth/request_token";
    public static final String TWITTER_ACCESS_TOKEN_URL =
                            "https://api.twitter.com/oauth/access_token";
    public static final String TWITTER_AUTHORISE_URL = "https://api.twitter.com/oauth/authorize";

    public static final String TWITTER_CALLBACK_URL = "http://twcallback.ubimobitech.com";

    /* Twitter post status endpoint */
    public static final String TWITTER_POSTSTATUS_URL = "https://api.twitter.com/1.1/statuses/update.json";

    /* Twitter Home timeline endpoint */
    public static final String TWITTER_HOME_TIMELINE_URL = "https://api.twitter.com/1.1/statuses/home_timeline.json";

    public static final int DEFAULT_HTTP_CONNECTION_TIMEOUT = 20000;
    public static final int DEFAULT_HTTP_SOCKET_TIMEOUT = 20000;

    public static final int NO_INTERNET_CONNECTION = -22;

    public static final int TWITTER_MAX_CHAR = 140;
}
