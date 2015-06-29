/**
 * FILE: TwitterSession.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.auth;

/**
 * Created by benakiva on 27/06/15.
 */
public class TwitterSession {
    private String mOAuthToken;
    private String mOAuthSecret;
    private boolean isLoggedIn;

    public TwitterSession(final String oauthToken, final String oauthSecret, boolean isLoggedIn) {
        mOAuthToken = oauthToken;
        mOAuthSecret = oauthSecret;
        this.isLoggedIn = isLoggedIn;
    }

    /**
     *
     */
    public TwitterSession() {
        mOAuthToken = "";
        mOAuthSecret = "";
        isLoggedIn = false;
    }

    public void setOAuthToken(String oauthToken) {
        this.mOAuthToken = oauthToken;
    }

    public void setOAuthSecret(String oauthSecret) {
        this.mOAuthSecret = oauthSecret;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getOAuthToken() {
        return mOAuthToken;
    }

    public String getOAuthSecret() {
        return mOAuthSecret;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
