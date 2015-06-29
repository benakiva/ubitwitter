/**
 * FILE: HomeTimelineFetcher.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.usertimeline;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ubimobitech.ubitwitter.AppConstants;
import com.ubimobitech.ubitwitter.comm.JsonFetcher;
import com.ubimobitech.ubitwitter.model.TimelineTweet;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by benakiva on 27/06/15.
 */
public class HomeTimelineFetcher extends JsonFetcher<TimelineTweet> {
    private static final TimelineDateDeserialiser mDateDeserializer = new TimelineDateDeserialiser();

    public HomeTimelineFetcher(Context context) {
        super(context);
    }

    public List<TimelineTweet> fetch(long sinceId) {
        String url = AppConstants.TWITTER_HOME_TIMELINE_URL + "?include_entities=true";

        // Twitter has a limit of 200 tweets per call
        url += "&count=" + Math.min(AppConstants.TWITTER_MAX_TWEETS_PER_SEARCH_TERM, 200);

        if (sinceId > -1) {
            url += "&since_id=" + sinceId;
        }

        Type type = new TypeToken<List<TimelineTweet>>() {}.getType();
        List<TimelineTweet> results = super.fetch(url, type);

        return results;
    }

    @Override
    protected void addBuilderAdapters(GsonBuilder gsonb) {
        gsonb.registerTypeAdapter(Date.class, mDateDeserializer);
    }
}
