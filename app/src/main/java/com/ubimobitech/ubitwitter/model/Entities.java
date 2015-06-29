/**
 * FILE: Entities.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 27/06/15
 */

package com.ubimobitech.ubitwitter.model;

import java.util.List;

/**
 * Created by benakiva on 27/06/15.
 */
public class Entities {
    private List<UserMention> user_mentions;
    private List<Hashtag> hashtags;
    private List<Url> urls;

    public List<UserMention> getUserMentions() {
        return user_mentions;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public List<Url> getUrls() {
        return urls;
    }
}
