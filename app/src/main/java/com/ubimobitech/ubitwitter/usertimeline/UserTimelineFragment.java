package com.ubimobitech.ubitwitter.usertimeline;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ubimobitech.ubitwitter.adapter.HomeTimelineAdapter;
import com.ubimobitech.ubitwitter.R;
import com.ubimobitech.ubitwitter.model.TimelineTweet;

import java.util.List;

public class UserTimelineFragment extends Fragment {
    private List<TimelineTweet> mTimelineTweets;
    private HomeTimelineFetcher mTimelineFetcher;
    private ListView mListView;
    private HomeTimelineAdapter mAdapter;

    public UserTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTimelineFetcher = new HomeTimelineFetcher(getActivity());

        TimelineFetcherTask timelineFetcherTask = new TimelineFetcherTask();
        timelineFetcherTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_timeline, container, false);

        mListView = (ListView) view.findViewById(R.id.listview_tweets);

        return view;
    }

    private class TimelineFetcherTask extends AsyncTask<Void, Void, List<TimelineTweet>> {
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
        protected List<TimelineTweet> doInBackground(Void... params) {
            return mTimelineFetcher.fetch(-1);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param timelineTweets The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<TimelineTweet> timelineTweets) {
            if (timelineTweets != null && timelineTweets.size() > 0) {
                mAdapter = new HomeTimelineAdapter(getActivity(), timelineTweets);
                mListView.setAdapter(mAdapter);
            }
        }
    }
}
