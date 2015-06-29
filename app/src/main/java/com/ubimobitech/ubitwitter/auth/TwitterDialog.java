/**
 * FILE: TwitterDialog.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.auth;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.ubimobitech.ubitwitter.AppConstants;
import com.ubimobitech.ubitwitter.R;

/**
 * Created by benakiva on 27/06/15.
 */
public class TwitterDialog extends DialogFragment {

    public static interface OnDialogListener {
        /**
         * Called when a dialog completes.
         *
         * Executed by the thread that initiated the dialog.
         *
         * @param verifier
         *            The oauth verifier.
         */
        public void onComplete(String verifier);

        /**
         * Called when a dialog has an error.
         *
         * Executed by the thread that initiated the dialog.
         *
         */
        public void onError();

        /**
         * Called when a dialog is canceled by the user.
         *
         * Executed by the thread that initiated the dialog.
         *
         */
        public void onCancel();
    }

    private String mUrl;
    private OnDialogListener mListener;
    private WebView mWebView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(createDialogView());

        return builder.create();
    }

    private View createDialogView() {
        View view = View.inflate(getActivity(), R.layout.twitter_dialog_fragment, null);

        mWebView = (WebView) view.findViewById(R.id.oauthView);
        mWebView.requestFocus(View.FOCUS_DOWN);
        EditText updates = (EditText)view.findViewById(R.id.txtUpdateStatus);
        updates.setFocusable(true);
        updates.requestFocus();

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new TwitterWebViewClient());
        mWebView.loadUrl(mUrl);

        return view;
    }


    public void setRequestUrl(final String url) {
        mUrl = url;
    }

    public void setOnDialogListener(OnDialogListener listener) {
        mListener = listener;
    }

    private class TwitterWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("Twitter-WebView", "Redirect URL: " + url);
            if (url.startsWith(AppConstants.TWITTER_CALLBACK_URL)) {

                Uri uri = Uri.parse(url);
                String verifier = uri.getQueryParameter("oauth_verifier");

                if (verifier != null) {
                    mListener.onComplete(verifier);
                } else {
                    mListener.onError();
                }

                TwitterDialog.this.dismiss();
                return true;
            }

            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError();
            TwitterDialog.this.dismiss();
        }
    }
}
