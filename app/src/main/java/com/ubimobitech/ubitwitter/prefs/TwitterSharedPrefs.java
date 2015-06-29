/**
 * FILE: TwitterSharedPrefs.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubimobitech.ubitwitter.auth.TwitterSession;

/**
 * Created by benakiva on 27/06/15.
 */
public class TwitterSharedPrefs {
    private static final String PREFERENCE_KEY = "UbiTwitterPrefs";

    private static final String TWITTER_OAUTH_TOKEN_KEY = "oauth_token";
    private static final String TWITTER_OAUTH_SECRET_KEY = "oauth_token_secret";
    private static final String TWITTER_IS_LOGGED_IN_KEY = "isTwitterLoggedIn";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    public static void saveTwitterSession(Context context, TwitterSession session) {
        SharedPreferences.Editor editor = getPrefs(context).edit();

        if (editor != null && session != null) {
            editor.putString(TWITTER_OAUTH_TOKEN_KEY, session.getOAuthToken());
            editor.putString(TWITTER_OAUTH_SECRET_KEY, session.getOAuthSecret());
            editor.putBoolean(TWITTER_IS_LOGGED_IN_KEY, session.isLoggedIn());
            editor.apply();
        }
    }

    public static boolean isTwitterUserLoggedIn(Context context) {
        return getPrefs(context).getBoolean(TWITTER_IS_LOGGED_IN_KEY, false);
    }

    public static TwitterSession getTwitterSession(Context context) {
        TwitterSession session = new TwitterSession();

        if (session != null) {
            session.setOAuthToken(getPrefs(context).getString(TWITTER_OAUTH_TOKEN_KEY, ""));
            session.setOAuthSecret(getPrefs(context).getString(TWITTER_OAUTH_SECRET_KEY, ""));
            session.setLoggedIn(getPrefs(context).getBoolean(TWITTER_IS_LOGGED_IN_KEY, false));
        }

        return session;
    }
}
