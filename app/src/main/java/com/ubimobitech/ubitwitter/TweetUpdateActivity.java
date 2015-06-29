package com.ubimobitech.ubitwitter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ubimobitech.ubitwitter.comm.CommUtils;
import com.ubimobitech.ubitwitter.comm.PostTweet;
import com.ubimobitech.ubitwitter.dialogs.ErrorDialogFragment;

import org.apache.http.HttpStatus;

public class TweetUpdateActivity extends AppCompatActivity implements View.OnClickListener,
        TextWatcher, TextView.OnEditorActionListener {
    private EditText mTweetEdit;
    private Button mSend;
    private TextView mNumChar;
    private Integer mMaxChar = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_update);

        mSend = (Button) findViewById(R.id.send_tweet);
        mSend.setOnClickListener(this);
        mSend.setEnabled(false);

        mTweetEdit = (EditText) findViewById(R.id.tweet_text);
        mTweetEdit.addTextChangedListener(this);
        mTweetEdit.setOnEditorActionListener(this);

        mNumChar = (TextView) findViewById(R.id.num_character);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == mSend) {
            postTweet();
        }
    }

    private void postTweet() {
        if (CommUtils.hasInternetConnection(this)) {
            PostStatusTask postTask = new PostStatusTask();
            postTask.execute(mTweetEdit.getText().toString());
        } else {
            ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(getString(R.string.no_connection));
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            fragment.show(ft, ErrorDialogFragment.class.getSimpleName());
        }
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!mSend.isEnabled() && count > 0 && count < AppConstants.TWITTER_MAX_CHAR) {
            mSend.setEnabled(true);
            mNumChar.setTextColor(Color.BLACK);
        } else if (mSend.isEnabled() && count == 0) {
            mSend.setEnabled(false);
            mNumChar.setTextColor(Color.BLACK);
        } else if (mSend.isEnabled() && count > AppConstants.TWITTER_MAX_CHAR) {
            mSend.setEnabled(false);
            mNumChar.setTextColor(Color.RED);
        } else if (!mSend.isEnabled() && count > AppConstants.TWITTER_MAX_CHAR) {
            mSend.setEnabled(false);
            mNumChar.setTextColor(Color.RED);
        }

        mMaxChar = AppConstants.TWITTER_MAX_CHAR - count;
        mNumChar.setText(mMaxChar.toString());
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Called when an action is being performed.
     *
     * @param v        The view that was clicked.
     * @param actionId Identifier of the action.  This will be either the
     *                 identifier you supplied, or {@link EditorInfo#IME_NULL
     *                 EditorInfo.IME_NULL} if being called due to the enter key
     *                 being pressed.
     * @param event    If triggered by an enter key, this is the event;
     *                 otherwise, this is null.
     * @return Return true if you have consumed the action, else false.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            if (!TextUtils.isEmpty(v.getText())) {
                postTweet();
            }
            return true;
        }

        return false;
    }

    private class PostStatusTask extends AsyncTask<String, Void, Integer> {
        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param statusCode The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Integer statusCode) {
            if (statusCode == HttpStatus.SC_OK) {
                Toast.makeText(getApplicationContext(), R.string.post_success, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.post_failure, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Integer doInBackground(String... params) {
            PostTweet postTweet = new PostTweet(getApplicationContext());

            return postTweet.postStatus(params[0]);
        }
    }
}
