package com.ubimobitech.ubitwitter;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ubimobitech.ubitwitter.auth.TwitterOAuth;
import com.ubimobitech.ubitwitter.auth.TwitterSession;
import com.ubimobitech.ubitwitter.comm.CommUtils;
import com.ubimobitech.ubitwitter.dialogs.ErrorDialogFragment;
import com.ubimobitech.ubitwitter.prefs.TwitterSharedPrefs;
import com.ubimobitech.ubitwitter.usertimeline.UserTimelineFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CommUtils.hasInternetConnection(this)) {
            fetchTimeline();
        } else {
            ErrorDialogFragment fragment = ErrorDialogFragment
                                    .newInstance(getString(R.string.no_connection));
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            fragment.show(ft, ErrorDialogFragment.class.getSimpleName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            fetchTimeline();
            return true;
        } else if (id == R.id.action_tweet) {
            Intent intent = new Intent(this, TweetUpdateActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            fetchTimeline();
        }
    }

    private void fetchTimeline() {
        if (!TwitterSharedPrefs.isTwitterUserLoggedIn(this)) {
            TwitterOAuth twitterOAuth = new TwitterOAuth();

            twitterOAuth.authorize(this, new TwitterOAuth.TwitterOAuthCallback() {
                @Override
                public void onAuthorized(String token, String secret) {
                    TwitterSession session = new TwitterSession(token, secret, true);
                    TwitterSharedPrefs.saveTwitterSession(getApplicationContext(), session);

                    UserTimelineFragment fragment = new UserTimelineFragment();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError() {
                    ErrorDialogFragment fragment = ErrorDialogFragment
                            .newInstance(getString(R.string.oauth_error));
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    fragment.show(ft, ErrorDialogFragment.class.getSimpleName());
                }
            });
        } else {
            UserTimelineFragment fragment = new UserTimelineFragment();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }
    }
}
