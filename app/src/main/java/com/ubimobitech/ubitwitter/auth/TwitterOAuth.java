/**
 * FILE: TwitterOAuth.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.auth;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.util.Log;

import com.ubimobitech.ubitwitter.AppConstants;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 * Created by benakiva on 27/06/15.
 */
public class TwitterOAuth {
    private static final String TAG = TwitterOAuth.class.getSimpleName();

    public interface TwitterOAuthCallback {
        void onAuthorized(String token, String secret);
        void onCancel();
        void onError();
    }

    private final OAuthProvider mProvider;
    private final CommonsHttpOAuthConsumer mConsumer;

    private TwitterOAuthCallback mCallback;
    private Activity mActivity;

    public TwitterOAuth() {
        mProvider = new CommonsHttpOAuthProvider(
                AppConstants.TWITTER_REQUEST_TOKEN_URL,
                AppConstants.TWITTER_ACCESS_TOKEN_URL,
                AppConstants.TWITTER_AUTHORISE_URL);

        mConsumer = new CommonsHttpOAuthConsumer(
                AppConstants.TWITTER_CONSUMER_KEY,
                AppConstants.TWITTER_CONSUMER_SECRET);
    }

    public void authorize(Activity activity, TwitterOAuthCallback callback) {
        mCallback = callback;
        mActivity = activity;

        new RequestTokenTask().execute();
    }

    private void onGotRequestTokenUrl(String requestTokenUrl) {
        if (requestTokenUrl != null) {
            TwitterDialog twitterDialog = new TwitterDialog();
            twitterDialog.setRequestUrl(requestTokenUrl);

            twitterDialog.setOnDialogListener(new TwitterDialog.OnDialogListener() {
                @Override
                public void onComplete(String verifier) {
                    verify(verifier);
                }

                @Override
                public void onError() {
                    if (mCallback != null) {
                        mCallback.onError();
                    }
                }

                @Override
                public void onCancel() {
                    if (mCallback != null) {
                        mCallback.onCancel();
                    }
                }
            });

            FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
            twitterDialog.show(transaction, TwitterDialog.class.getSimpleName());
        } else {
            if (mCallback != null) {
                mCallback.onError();
            }
        }
    }

    private void verify(String verifier) {
        new AccessTokenTask().execute(verifier);
    }

    private void onGotAccessToken() {
        if (mCallback != null) {
            if (mConsumer.getToken() != null
                    && mConsumer.getTokenSecret() != null) {
                mCallback.onAuthorized(mConsumer.getToken(),
                        mConsumer.getTokenSecret());
            } else {
                mCallback.onError();
            }
        }
    }

    private void onError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    private class RequestTokenTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                return mProvider.retrieveRequestToken(mConsumer,
                        AppConstants.TWITTER_CALLBACK_URL);
            } catch (OAuthMessageSignerException e) {
                Log.e(TAG, "Auth exception", e);
                return null;
            } catch (OAuthNotAuthorizedException e) {
                Log.e(TAG, "Auth exception", e);
                return null;
            } catch (OAuthExpectationFailedException e) {
                Log.e(TAG, "Auth exception", e);
                return null;
            } catch (OAuthCommunicationException e) {
                Log.e(TAG, "Auth exception", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String requestTokenUrl) {
            if (requestTokenUrl != null) {
                onGotRequestTokenUrl(requestTokenUrl);
            } else {
                onError();
            }
        }
    }

    private class AccessTokenTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                mProvider.retrieveAccessToken(mConsumer, params[0]);
                return true;
            } catch (OAuthMessageSignerException e) {
                Log.e(TAG, "Auth exception", e);
                return false;
            } catch (OAuthNotAuthorizedException e) {
                Log.e(TAG, "Auth exception", e);
                return false;
            } catch (OAuthExpectationFailedException e) {
                Log.e(TAG, "Auth exception", e);
                return false;
            } catch (OAuthCommunicationException e) {
                Log.e(TAG, "Auth exception", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result != null && result) {
                onGotAccessToken();
            } else {
                onError();
            }
        }
    }
}
